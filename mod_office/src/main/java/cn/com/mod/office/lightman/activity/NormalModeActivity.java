package cn.com.mod.office.lightman.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.joshua.common.util.MaskUtils;
import com.joshua.common.util.ToastUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import cn.com.mod.office.lightman.MyApplication;
import cn.com.mod.office.lightman.R;
import cn.com.mod.office.lightman.activity.base.BaseActivity;
import cn.com.mod.office.lightman.api.BaseResp;
import cn.com.mod.office.lightman.api.ILightMgrApi;
import cn.com.mod.office.lightman.entity.Lamps;
import cn.com.mod.office.lightman.widget.LedView;

public class NormalModeActivity extends BaseActivity implements View.OnClickListener{

    private static final int REQUEST_EDIT_PARAMS = 101;

    private ImageView back;
    private TextView title,ic_menu;
    private EditText et_modename;
    private LinearLayout bottom_layout;
    private AbsoluteLayout ledCanvas;
    private MaskUtils maskUtils;
    private ToastUtils toastUtils;
    private LinearLayout select_menu,select_all,select_none,grouping,unmarshall;

    private String roomId,modeId;
    private int type;

    private List<LedView> ledViews;
    private List<Lamps> lamps;
    private List<String> mSelectedLeds = new ArrayList<>();

    private int lamp_brightness,lamp_colorTemp,lamp_h_degree,lamp_v_degree,lamp_l_degree;
    private String lamp_rgb;//灯的色温  16进制表示

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_mode);
        initView();
        initData();
    }


    private void initView() {
        back = (ImageView) findViewById(R.id.ic_back);
        title = (TextView) findViewById(R.id.title);
        ic_menu = (TextView) findViewById(R.id.ic_menu);
        et_modename = (EditText) findViewById(R.id.et_modename);
        bottom_layout = (LinearLayout) findViewById(R.id.bottom);
        ledCanvas = (AbsoluteLayout) findViewById(R.id.led_canvas);

        select_menu = (LinearLayout) findViewById(R.id.select_menu);
        select_all = (LinearLayout) findViewById(R.id.select_all);
        select_none = (LinearLayout) findViewById(R.id.select_none);
        grouping = (LinearLayout) findViewById(R.id.grouping);
        unmarshall = (LinearLayout) findViewById(R.id.unmarshall);

        back.setOnClickListener(this);
        ic_menu.setOnClickListener(this);
        bottom_layout.setOnClickListener(this);
        select_all.setOnClickListener(this);
        select_none.setOnClickListener(this);
        grouping.setOnClickListener(this);
        unmarshall.setOnClickListener(this);
        maskUtils = new MaskUtils(this);
        toastUtils = new ToastUtils(this);
        ledViews = new ArrayList<>();

    }

    private void initData() {
        roomId = getIntent().getStringExtra("roomId");
        type = getIntent().getIntExtra("mode_type",1);
        modeId = getIntent().getStringExtra("mode_id");

        if(type==1){
            title.setText(R.string.mode_create);
        }else{
            title.setText(R.string.mode_edit);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.select_all:
                handleLedViews(true);
                break;
            case R.id.select_none:
                handleLedViews(false);
                break;
            case R.id.grouping:
                break;
            case R.id.unmarshall:
                break;
            case R.id.ic_back:
                finish();
                break;
            case R.id.ic_menu://保存
                addNormalMode();
                break;
            case R.id.bottom://调节灯具
                Intent intent = new Intent(this,PatameterSettingActivity.class);
                intent.putExtra("leds", mSelectedLeds.toArray(new String[]{}));
                intent.putExtra("roomId", roomId);
                intent.putExtra("type", SceneActivity.TYPE_ADD);
                startActivityForResult(intent,REQUEST_EDIT_PARAMS);
                break;
        }
    }

    private void addNormalMode() {
        String modeName = et_modename.getText().toString().trim();
        if(TextUtils.isEmpty(modeName)){
            ToastUtils.show(this,R.string.et_mode_name);
            return;
        }
        if(lamp_l_degree==0&&lamp_v_degree==0&&lamp_h_degree==0&&lamp_brightness==0&&lamp_colorTemp==0){
            ToastUtils.show(this,R.string.setting_params_tips);
            return;
        }
        MyApplication.getInstance().getClient().createNormalMode(roomId, modeName, (String[]) mSelectedLeds.toArray(), lamp_rgb, lamp_brightness, lamp_colorTemp, lamp_h_degree, lamp_v_degree, lamp_l_degree, new ILightMgrApi.Callback<BaseResp>() {
            @Override
            public void callback(int code, BaseResp entity) {
                if(code==0){
                    //添加成功  返回模式管理界面
                    finish();
                }else{
                    ToastUtils.show(NormalModeActivity.this,entity.getError_desc());
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK&&requestCode == REQUEST_EDIT_PARAMS){
            //处理灯的颜色 亮度等
            if(data!=null){
                lamp_brightness = data.getIntExtra("lamp_brightness",0);
                lamp_colorTemp = data.getIntExtra("lamp_colorTemp",0);
                lamp_h_degree = data.getIntExtra("lamp_h_degree",0);
                lamp_v_degree = data.getIntExtra("lamp_v_degree",0);
                lamp_l_degree = data.getIntExtra("lamp_l_degree",0);
                lamp_rgb = data.getStringExtra("lamp_rgb");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void loadData() {
        if(roomId!=null){
            maskUtils.show();
            // 获取房间图片
            MyApplication.getInstance().getClient().getRoomImg(roomId, new ILightMgrApi.Callback<Bitmap>() {
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void callback(int code, Bitmap bitmap) {
                    maskUtils.cancel();
                    if(bitmap==null){
                        toastUtils.show(getString(R.string.room_no_map));
                        finish();
                        return;
                    }
                    switch (code) {
                        case CODE_SUCCESS:
                            ledCanvas.setBackground(new BitmapDrawable(bitmap));
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

    }
    private void canvasLamps(List<Lamps> lamps) {
        this.lamps = lamps;
        // LED图标的大小比例， 600px:30px
        float rate = 30.0f / 600;

        for (final Lamps ledInfo : lamps) {
            final LedView ledView = new LedView(NormalModeActivity.this, ledInfo);
            ledView.setTag(ledInfo.getLamp_id());
            ledView.setOnCheckStateChangeListener(new LedView.OnCheckStateChangeListener() {
                @Override
                public synchronized void onCheckStateChange(Lamps ledInfo, boolean isChecked) {
                    if (isChecked) {
                        mSelectedLeds.add(ledInfo.getLamp_id());
                        handleItemList(ledInfo, true);
                    } else {
                        mSelectedLeds.remove(ledInfo.getLamp_id());
                        handleItemList(ledInfo, false);
                    }
                }
            });

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
    }
    // 监听地图点选LED灯图标后，对列表的选中状态的变化
    private synchronized void handleItemList(Lamps ledInfo, boolean isSelected) {
        //显示操作面板
        if(mSelectedLeds.size()>0){
            select_menu.setVisibility(View.VISIBLE);
        }else{
            select_menu.setVisibility(View.GONE);
        }

    }
    private void handleLedViews(boolean checked) {
        for (LedView v:ledViews){
            v.setChecked(checked);
        }
        mSelectedLeds.clear();
        if(checked&&lamps!=null&&lamps.size()>0){
            for (Lamps l:lamps){
                mSelectedLeds.add(l.getLamp_id());
            }
        }
    }
}
