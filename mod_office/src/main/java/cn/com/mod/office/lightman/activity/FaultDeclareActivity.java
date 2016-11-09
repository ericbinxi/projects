package cn.com.mod.office.lightman.activity;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.joshua.common.util.MaskUtils;
import com.joshua.common.util.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.com.mod.office.lightman.MyApplication;
import cn.com.mod.office.lightman.R;
import cn.com.mod.office.lightman.activity.base.BaseActivity;
import cn.com.mod.office.lightman.api.BaseResp;
import cn.com.mod.office.lightman.api.ILightMgrApi;
import cn.com.mod.office.lightman.entity.Lamps;
import cn.com.mod.office.lightman.widget.LedView;

public class FaultDeclareActivity extends BaseActivity implements View.OnClickListener {

    private ImageView ic_back;
    private TextView record;
    private FrameLayout fl_lamp, fl_result;
    private AbsoluteLayout led_canvas;
    private TextView tv_commit;
    private EditText fault_desc;
    private MaskUtils maskUtils;

    private String roomId;
    private List<Lamps> lamps;
    private List<String> selectIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fault_declare);
        init();
        initListener();
    }

    private void init() {
        ic_back = (ImageView) findViewById(R.id.ic_back);
        record = (TextView) findViewById(R.id.ic_menu);
        fl_lamp = (FrameLayout) findViewById(R.id.fl_lamp);
        fl_result = (FrameLayout) findViewById(R.id.fl_result);
        led_canvas = (AbsoluteLayout) findViewById(R.id.led_canvas);
        tv_commit = (TextView) findViewById(R.id.tv_commit);
        fault_desc = (EditText) findViewById(R.id.fault_desc);
        maskUtils = new MaskUtils(this);
        selectIds = new ArrayList<>();

        roomId = getIntent().getStringExtra("roomId");
    }

    private void initListener() {
        ic_back.setOnClickListener(this);
        record.setOnClickListener(this);
        tv_commit.setOnClickListener(this);
    }

    @Override
    public void loadData() {
        if (TextUtils.isEmpty(roomId)) {
            finish();
            return;
        }
        // 获取房间图片
        maskUtils.show();
        MyApplication.getInstance().getClient().getRoomImg(roomId, new ILightMgrApi.Callback<Bitmap>() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void callback(int code, Bitmap bitmap) {
                maskUtils.cancel();
                if (bitmap == null) {
                    ToastUtils.show(FaultDeclareActivity.this,R.string.room_no_map);
                    finish();
                    return;
                }
                switch (code) {
                    case CODE_SUCCESS:
                        led_canvas.setBackground(new BitmapDrawable(bitmap));
                        // 获取房间灯组
                        MyApplication.getInstance().getClient().getLampsInRoom(roomId, new ILightMgrApi.Callback<List<Lamps>>() {
                            @Override
                            public void callback(int code, List<Lamps> groupInfos) {
                                switch (code) {
                                    case CODE_SUCCESS:
                                        canvasLamps(groupInfos);
                                        break;
                                }
                            }
                        });
                }
            }
        });
    }

    private void canvasLamps(List<Lamps> lamps) {
        this.lamps = lamps;
        // LED图标的大小比例， 600px:30px
        float rate = 30.0f / 600;
        for (final Lamps ledInfo : lamps) {
            final LedView ledView = new LedView(FaultDeclareActivity.this, ledInfo);
            ledView.setTag(ledInfo.getLamp_id());
            ledView.setOnCheckStateChangeListener(new LedView.OnCheckStateChangeListener() {
                @Override
                public synchronized void onCheckStateChange(Lamps ledInfo, boolean isChecked) {
                    if (isChecked) {
                        selectIds.add(ledInfo.getLamp_id());
                    } else {
                        selectIds.remove(ledInfo.getLamp_id());
                    }
                }
            });

            int x = ledInfo.getLamp_x();
            int y = ledInfo.getLamp_y();
            int w = led_canvas.getWidth();
            int h = led_canvas.getHeight();
            if (w > h) {
                int size = (int) (h * rate);
                int pointX = (int) (h / 600.0 * x) + (w - h) / 2;
                int pointY = (int) (h / 600.0 * y);
                led_canvas.addView(ledView, new AbsoluteLayout.LayoutParams(size, size, pointX, pointY));
            } else {
                int size = (int) (w * rate);
                int pointX = (int) (w / 600.0 * x);
                int pointY = ((int) (w / 600.0 * y) + (h - w) / 2);
                led_canvas.addView(ledView, new AbsoluteLayout.LayoutParams(size, size, pointX, pointY));
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ic_back:
                finish();
                break;
            case R.id.ic_menu:
                break;
            case R.id.tv_commit:
                commit();
                break;
        }
    }

    private void commit() {
        String faultDesc = fault_desc.getText().toString().trim();
        String idStr = "";
        for (String id:selectIds){
            idStr = id+",";
        }
        idStr.substring(0,idStr.length()-1);
        MyApplication.getInstance().getClient().addFaultRecord("", faultDesc, idStr, new ILightMgrApi.Callback<BaseResp>() {
            @Override
            public void callback(int code, BaseResp entity) {
                if(code==0){
                    fl_result.setVisibility(View.VISIBLE);
                    fl_lamp.setVisibility(View.GONE);
                }else {
                    fl_result.setVisibility(View.GONE);
                    fl_lamp.setVisibility(View.VISIBLE);
                    ToastUtils.show(FaultDeclareActivity.this,entity.getError_desc());
                }
            }
        });

    }
}
