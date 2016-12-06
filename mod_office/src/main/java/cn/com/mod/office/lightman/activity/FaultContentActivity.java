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
import android.widget.ImageView;

import com.joshua.common.util.MaskUtils;
import com.joshua.common.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import cn.com.mod.office.lightman.MyApplication;
import cn.com.mod.office.lightman.R;
import cn.com.mod.office.lightman.activity.base.BaseActivity;
import cn.com.mod.office.lightman.api.ILightMgrApi;
import cn.com.mod.office.lightman.api.resp.LampsResp;
import cn.com.mod.office.lightman.entity.FaultRecord;
import cn.com.mod.office.lightman.entity.Lamps;
import cn.com.mod.office.lightman.widget.LedView;

public class FaultContentActivity extends BaseActivity {

    private ImageView ivBack;
    private AbsoluteLayout ledCanvas;
    private EditText faultDesc;
    private FaultRecord record;
    private MaskUtils  maskUtils;
    private List<LedView> ledViews;

    private String roomId;
    private FaultRecord faultRecord;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fault_content);
        init();
    }

    private void init() {
        roomId = getIntent().getStringExtra("roomId");
        faultRecord = (FaultRecord) getIntent().getSerializableExtra("faultRecord");
        record = (FaultRecord) getIntent().getSerializableExtra("record");
        ivBack = (ImageView) findViewById(R.id.ic_back);
        ledCanvas = (AbsoluteLayout) findViewById(R.id.led_canvas);
        faultDesc = (EditText) findViewById(R.id.fault_desc);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        maskUtils = new MaskUtils(this);
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
                    ToastUtils.show(FaultContentActivity.this, R.string.room_no_map);
                    finish();
                    return;
                }
                switch (code) {
                    case CODE_SUCCESS:
                        ledCanvas.setBackground(new BitmapDrawable(bitmap));
                        // 获取房间灯组
                        MyApplication.getInstance().getClient().getLampsInRoom(roomId, new ILightMgrApi.Callback<LampsResp>() {
                            @Override
                            public void callback(int code, LampsResp resp) {
                                switch (code) {
                                    case CODE_SUCCESS:
                                        if (resp.getLamps() != null)
                                            canvasLamps(resp.getLamps());
                                        break;
                                    case CODE_FAILURE:
                                        ToastUtils.show(FaultContentActivity.this,resp.getError_desc());
                                        break;
                                }
                            }
                        });
                }
            }
        });
    }
    private void canvasLamps(List<Lamps> lamps) {
        ledViews = new ArrayList<>();
        // LED图标的大小比例， 600px:30px
        float rate = 30.0f / 600;
        for (final Lamps ledInfo : lamps) {
            final LedView ledView = new LedView(FaultContentActivity.this, ledInfo);
            ledView.setClickable(false);
            ledView.setTag(ledInfo.getLamp_id());
            ledView.setNeedCallback(false);
            int x = ledInfo.getLamp_x();
            int y = ledInfo.getLamp_y();
            int w = ledCanvas.getWidth();
            int h = ledCanvas.getHeight();
            if (w > h) {
                int size = (int) (h * rate);
                int pointX = (int) (h / 600.0 * x) + (w - h) / 2;
                int pointY = (int) (h / 600.0 * y);
                ledCanvas.addView(ledView, new AbsoluteLayout.LayoutParams(size, size, pointX, pointY));
            } else {
                int size = (int) (w * rate);
                int pointX = (int) (w / 600.0 * x);
                int pointY = ((int) (w / 600.0 * y) + (h - w) / 2);
                ledCanvas.addView(ledView, new AbsoluteLayout.LayoutParams(size, size, pointX, pointY));
            }
            ledViews.add(ledView);
        }
        initLamps();
    }

    private void initLamps() {
        if(faultRecord!=null){
            String lamps_id = faultRecord.getLamp_id();
            String[] ids = lamps_id.split(",");
            if(ledViews!=null&&ledViews.size()>0){
                for (String id:ids){
                    for (LedView led:ledViews){
                        String lampId = (String) led.getTag();
                        if(id.equals(lampId)){
                            led.setLinkedChecked(true);
                            break;
                        }
                    }
                }
            }
            faultDesc.setText(faultRecord.getMsg_content());
        }
    }
}
