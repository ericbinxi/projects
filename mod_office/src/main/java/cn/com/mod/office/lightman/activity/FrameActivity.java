package cn.com.mod.office.lightman.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.joshua.common.util.MaskUtils;
import com.joshua.common.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import cn.com.mod.office.lightman.MyApplication;
import cn.com.mod.office.lightman.R;
import cn.com.mod.office.lightman.activity.base.BaseActivity;
import cn.com.mod.office.lightman.api.ILightMgrApi;
import cn.com.mod.office.lightman.api.resp.LampsResp;
import cn.com.mod.office.lightman.entity.LampParam;
import cn.com.mod.office.lightman.entity.LampStatusResp;
import cn.com.mod.office.lightman.entity.Lamps;
import cn.com.mod.office.lightman.widget.LedView;

public class FrameActivity extends BaseActivity implements View.OnClickListener {

    private static final int REQUEST_EDIT_PARAMS = 1001;
    private int index = -1;
    private ArrayList<String> mSelectedLeds = new ArrayList<>();
    private List<LedView> ledViews = new ArrayList<>();
    private ArrayList<LampParam> lampParams = new ArrayList<>();

    private AbsoluteLayout ledCanvas;
    private LinearLayout adjustLamp;
    private TextView titile;
    private ImageView ivBack;

    private String roomId;
    private MaskUtils maskUtils;
    private List<Lamps> lamps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame);
        index = getIntent().getIntExtra("index", -1);
        roomId = getIntent().getStringExtra("roomId");
        init();
    }

    private void init() {
        maskUtils = new MaskUtils(this);
        ledCanvas = (AbsoluteLayout) findViewById(R.id.led_canvas);
        adjustLamp = (LinearLayout) findViewById(R.id.bottom);
        titile = (TextView) findViewById(R.id.title);
        ivBack = (ImageView) findViewById(R.id.ic_back);
        ivBack.setOnClickListener(this);
        adjustLamp.setOnClickListener(this);
        if (index < 0) index = 1;
        titile.setText(String.format(getString(R.string.frame_index), index));
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void loadData() {
        if (roomId != null) {
            maskUtils.show();
            // 获取房间图片
            MyApplication.getInstance().getClient().getRoomImg(roomId, new ILightMgrApi.Callback<Bitmap>() {
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void callback(int code, Bitmap bitmap) {
                    maskUtils.cancel();
                    if (bitmap == null) {
                        ToastUtils.show(FrameActivity.this, getString(R.string.room_no_map));
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
                                            if (resp != null && resp.getLamps() != null) {
                                                lamps = resp.getLamps();
                                                canvasLamps(resp.getLamps());
                                            }
                                            break;
                                        case CODE_FAILURE:
                                            ToastUtils.show(FrameActivity.this, resp.getError_desc());
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
//        this.lamps = lamps;
        // LED图标的大小比例， 600px:30px
        float rate = 30.0f / 600;

        for (final Lamps ledInfo : lamps) {
            final LedView ledView = new LedView(FrameActivity.this, ledInfo);
            ledView.setTag(ledInfo.getLamp_id());
            ledView.setOnCheckStateChangeListener(new LedView.OnCheckStateChangeListener() {
                @Override
                public synchronized void onCheckStateChange(Lamps ledInfo, boolean isChecked) {
                    if (isChecked) {
                        mSelectedLeds.add(ledInfo.getLamp_id());
                    } else {
                        mSelectedLeds.remove(ledInfo.getLamp_id());
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ic_back:
                if(setResultData())
                    finish();
                break;
            case R.id.bottom:
                Intent intent = new Intent(this, PatameterSettingActivity.class);
                intent.putExtra("leds", mSelectedLeds);
                intent.putExtra("roomId", roomId);
                intent.putExtra("type", 1);
                startActivityForResult(intent, REQUEST_EDIT_PARAMS);
                break;
        }
    }

    private boolean setResultData() {
        if (mSelectedLeds.size() > 0) {
//            generateData();
//            for (String id : mSelectedLeds) {
//                LampParam param = new LampParam();
//                param.setLamp_brightness(lamp_brightness + "");
//                param.setLamp_colorTemp(lamp_colorTemp + "");
//                param.setLamp_rgb(lamp_rgb);
//                param.setLamp_h_degree(lamp_h_degree + "");
//                param.setLamp_v_degree(lamp_v_degree + "");
//                param.setLamp_l_degree(lamp_l_degree + "");
//                param.setLamp_id(id);
//                lampParams.add(param);
//            }
            if(lampParams.size()<=0){
                ToastUtils.show(this,"请选择灯具并设置参数");
                return false;
            }
            Intent data = new Intent();
            data.putExtra("lampParams", lampParams);
            setResult(RESULT_OK, data);
            return true;
        } else {
            ToastUtils.show(this,"请选择灯具并设置参数");
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        if(setResultData())
            super.onBackPressed();
    }

    private int lamp_brightness;
    private int lamp_colorTemp;
    private String lamp_rgb;
    private int lamp_v_degree;
    private int lamp_h_degree;
    private int lamp_l_degree;

    private void generateData() {
        lamp_brightness = 30;
        lamp_colorTemp = 3612;
        lamp_rgb = "f0f0f0";
        lamp_v_degree = 45;
        lamp_h_degree = -130;
        lamp_l_degree = 45;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_EDIT_PARAMS) {
            if (mSelectedLeds.size() > 0) {
//                generateData();
                LampParam param = (LampParam) data.getSerializableExtra("lampParam");
                for (String id : mSelectedLeds) {
                    param.setLamp_id(id);
                    lampParams.add(param);
                }
                ToastUtils.show(this,"当前灯具参数已设置");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
