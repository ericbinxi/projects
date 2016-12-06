package cn.com.mod.office.lightman.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
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
import java.util.List;

import cn.com.mod.office.lightman.MyApplication;
import cn.com.mod.office.lightman.R;
import cn.com.mod.office.lightman.activity.base.BaseActivity;
import cn.com.mod.office.lightman.api.BaseResp;
import cn.com.mod.office.lightman.api.ILightMgrApi;
import cn.com.mod.office.lightman.api.resp.LampsResp;
import cn.com.mod.office.lightman.entity.LampParam;
import cn.com.mod.office.lightman.entity.Lamps;
import cn.com.mod.office.lightman.entity.NormalMode;
import cn.com.mod.office.lightman.entity.TieLampsResp;
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

    private String roomId;
    private String modeId;
    private int type;

    private List<LedView> ledViews;
    private List<Lamps> mSelectedLeds = new ArrayList<>();

    private int lamp_brightness,lamp_colorTemp,lamp_h_degree,lamp_v_degree,lamp_l_degree;
    private String lamp_rgb;//灯的色温  16进制表示

    private List<Lamps> lamps;
    private NormalMode mode;
    private List<String> hasParamLampIds = new ArrayList<>();
//    private List<String> duplicateLampIds = new ArrayList<>();
    private List<LampParam> lampParams = new ArrayList<>();

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
        mode = (NormalMode) getIntent().getSerializableExtra("normalMode");

        if(type==1){
            title.setText(R.string.mode_create);
        }else{
            title.setText(R.string.mode_edit);
            if(mode!=null){
                et_modename.setText(mode.getMode_name().toCharArray(),0,mode.getMode_name().length());
            }
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
                tieGroupLamps();
                break;
            case R.id.unmarshall:
                unTieGroupLamps();
                break;
            case R.id.ic_back:
                finish();
                break;
            case R.id.ic_menu://保存
                addNormalMode();
                break;
            case R.id.bottom://调节灯具
                if(mSelectedLeds.size()>0){
                    Intent intent = new Intent(this,PatameterSettingActivity.class);
                    intent.putExtra("leds", getLedIdStringArray(mSelectedLeds));
                    intent.putExtra("roomId", roomId);
                    intent.putExtra("type", 1);
                    startActivityForResult(intent,REQUEST_EDIT_PARAMS);
                }else{
                    ToastUtils.show(this,"请选择灯具");
                }

                break;
        }
    }
    private String[] getLedIdStringArray(List<Lamps> lamps){
        String[] results = new String[lamps.size()];
        for (int i=0;i<lamps.size();i++){
            results[i] = lamps.get(i).getLamp_id();
        }
        return results;
    }

    private void unTieGroupLamps() {
        if(mSelectedLeds.size()>0){
            String groupId = mSelectedLeds.get(0).getGroup_id();
            if(!TextUtils.isEmpty(groupId)){
                for (Lamps l:mSelectedLeds){
                    if(!groupId.equals(l.getGroup_id())){
                        ToastUtils.show(this, R.string.untie_dif_group);
                        return;
                    }
                }
                maskUtils.show();
                MyApplication.getInstance().getClient().untieLampGroup(groupId, new ILightMgrApi.Callback<BaseResp>() {
                    @Override
                    public void callback(int code, BaseResp entity) {
                        maskUtils.cancel();
                        if (code == 0) {
                            ToastUtils.show(NormalModeActivity.this, R.string.untie_group_success);
                            handleLedViews(false);
                        } else {
                            ToastUtils.show(NormalModeActivity.this, entity.getError_desc());
                        }

                    }
                });
            }else {
                ToastUtils.show(this, R.string.untie_dif_group);
            }
        }else{
            ToastUtils.show(this, R.string.untie_failure_tips);
        }
    }

    private void tieGroupLamps() {
        if (mSelectedLeds != null && mSelectedLeds.size() > 0) {
            String lampIdStr = "";
            for (Lamps lamp : mSelectedLeds) {
                lampIdStr = lampIdStr + "," + lamp.getLamp_id();
            }
            lampIdStr = lampIdStr.substring(1);
            maskUtils.show();
            MyApplication.getInstance().getClient().tieLampGroup(roomId, lampIdStr, new ILightMgrApi.Callback<TieLampsResp>() {
                @Override
                public void callback(int code, TieLampsResp entity) {
                    maskUtils.cancel();
                    if (code == 0 && entity != null && !TextUtils.isEmpty(entity.getGroup_id())) {
                        ToastUtils.show(NormalModeActivity.this, R.string.tie_group_success);
                    } else {
                        ToastUtils.show(NormalModeActivity.this, entity.getError_desc());
                    }
                }
            });
        }
    }
    private void addNormalMode() {
        String modeName = et_modename.getText().toString().trim();
        if(TextUtils.isEmpty(modeName)){
            ToastUtils.show(this,R.string.et_mode_name);
            return;
        }
//        if(lamp_l_degree==0&&lamp_v_degree==0&&lamp_h_degree==0&&lamp_brightness==0&&lamp_colorTemp==0){
//            ToastUtils.show(this,R.string.setting_params_tips);
//            return;
//        }
//        if(mSelectedLeds.size()<=0){
//            ToastUtils.show(this,"请选择灯具并设置参数");
//        }
        List<String> ids = new ArrayList<>();
        for (Lamps lamp:mSelectedLeds){
            ids.add(lamp.getLamp_id());
        }
//        generateData();
        MyApplication.getInstance().getClient().createNormalMode(roomId,modeId ,modeName,ids, lampParams, new ILightMgrApi.Callback<BaseResp>() {
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
        if(resultCode==RESULT_OK&&requestCode == REQUEST_EDIT_PARAMS){
            //处理灯的颜色 亮度等
            if(data!=null&&data.hasExtra("lampParam")){
                LampParam param = (LampParam) data.getSerializableExtra("lampParam");
                if(mSelectedLeds.size()>0){
                    for (Lamps lamp:mSelectedLeds){
                        LampParam lp = (LampParam) param.clone();
                        if(lp!=null){
                            lp.setLamp_id(lamp.getLamp_id());
                            lampParams.add(lp);
                        }


                        if(!hasParamLampIds.contains(lamp.getLamp_id()))
                            hasParamLampIds.add(lamp.getLamp_id());
                    }
                    ToastUtils.show(this,"当前选中的灯参数设置成功");
                    //清除之前选择的灯
                    handleLedViews(false);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
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
                            MyApplication.getInstance().getClient().getLampsInRoom(roomId, new ILightMgrApi.Callback<LampsResp>() {
                                @Override
                                public void callback(int code, LampsResp resp) {
                                    switch (code) {
                                        case CODE_SUCCESS:
                                            if(resp!=null&&resp.getLamps()!=null){
                                                lamps = resp.getLamps();
                                                canvasLamps(resp.getLamps());
                                            }
                                            break;
                                        case CODE_FAILURE:
                                            ToastUtils.show(NormalModeActivity.this,resp.getError_desc());
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
        // LED图标的大小比例， 600px:30px
        float rate = 30.0f / 600;

        for (final Lamps ledInfo : lamps) {
            final LedView ledView = new LedView(NormalModeActivity.this, ledInfo);
            ledView.setTag(ledInfo.getLamp_id());
            ledView.setOnCheckStateChangeListener(new LedView.OnCheckStateChangeListener() {
                @Override
                public synchronized void onCheckStateChange(final Lamps ledInfo, boolean isChecked) {
                    if (isChecked) {
                        if(!mSelectedLeds.contains(ledInfo))
                            mSelectedLeds.add(ledInfo);
                        if(hasParamLampIds.contains(ledInfo.getLamp_id())){
                            AlertDialog dialog = new AlertDialog.Builder(NormalModeActivity.this).
                                    setCancelable(false).setMessage("此灯（"+ledInfo.getLamp_id()+")已设置参数，请不要重复设置参数？").
                                    setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
//                                            if(!duplicateLampIds.contains(ledInfo.getLamp_id()))
//                                                duplicateLampIds.add(ledInfo.getLamp_id());
                                            ledView.setChecked(false);
                                            dialog.dismiss();
                                        }
                                    }).create();
                            dialog.show();
                        }
                        handGroupLamps(ledInfo);
                        handleItemList(ledInfo, true);
                    } else {
                        if(mSelectedLeds.contains(ledInfo))
                            mSelectedLeds.remove(ledInfo);
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
    private void handGroupLamps(Lamps lamp) {
        if (mSelectedLeds != null && mSelectedLeds.size() == 1) {
            String groupId = lamp.getGroup_id();
            if(!TextUtils.isEmpty(groupId)){
                for (Lamps l:lamps){
                    if(groupId.equals(l.getGroup_id())){
                        handleCheckLed(l);
                    }
                }
            }
        }
    }

    private void handleCheckLed(Lamps lamp) {
        for (LedView ledview : ledViews) {
            String id = (String) ledview.getTag();
            if (id.equals(lamp.getLamp_id())) {
                ledview.setLinkedChecked(true);
                if (!mSelectedLeds.contains(lamp)) {
                    mSelectedLeds.add(lamp);
                }
                break;
            }
        }
    }
    // 监听地图点选LED灯图标后，对列表的选中状态的变化
    private void handleItemList(Lamps ledInfo, boolean isSelected) {
        //显示操作面板
        if(mSelectedLeds.size()>0){
            select_menu.setVisibility(View.VISIBLE);
        }else{
            select_menu.setVisibility(View.GONE);
        }

    }
    private void handleLedViews(boolean checked) {
        List<Lamps> temps = new ArrayList<>();
        temps.addAll(mSelectedLeds);
        mSelectedLeds.clear();
        mSelectedLeds.addAll(lamps);
        if(!checked){
            mSelectedLeds.removeAll(temps);
        }
        for (LedView led : ledViews){
            led.setLinkedChecked(false);
        }
        for (Lamps lamp:mSelectedLeds){
            for (LedView v : ledViews){
                String lampId = (String) v.getTag();
                if(lampId.equals(lamp.getLamp_id())){
                    v.setChecked(true);
                    break;
                }
            }
        }
    }
}
