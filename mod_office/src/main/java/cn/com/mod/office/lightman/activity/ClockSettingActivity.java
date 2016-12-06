package cn.com.mod.office.lightman.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.joshua.common.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import cn.com.mod.office.lightman.MyApplication;
import cn.com.mod.office.lightman.R;
import cn.com.mod.office.lightman.activity.base.BaseActivity;
import cn.com.mod.office.lightman.adapter.GridViewAdapter;
import cn.com.mod.office.lightman.adapter.SceneButtonAdapter;
import cn.com.mod.office.lightman.api.BaseResp;
import cn.com.mod.office.lightman.api.ILightMgrApi;
import cn.com.mod.office.lightman.api.resp.GetModesResp;
import cn.com.mod.office.lightman.entity.Clock;
import cn.com.mod.office.lightman.entity.DynamicMode;
import cn.com.mod.office.lightman.entity.NormalMode;
import cn.com.mod.office.lightman.entity.SceneInfo;
import cn.com.mod.office.lightman.entity.base.BaseModeEntity;
import cn.com.mod.office.lightman.widget.timepicker.adapter.ArrayWheelAdapter;
import cn.com.mod.office.lightman.widget.timepicker.widget.WheelView;

public class ClockSettingActivity extends BaseActivity implements View.OnClickListener{

    private static int[] texts = {R.string.sunday,R.string.monday, R.string.tuesday, R.string.wednesday, R.string.thursday, R.string.friday, R.string.saturday};

    private ImageView back;
    private TextView title,save,mode_name;
    private WheelView wheelHour,wheelMin;
    private LinearLayout repeatWeek;
    private GridView gridView;
    private GridViewAdapter adapter;

    private CheckedTextView[] mWeeks;

    private List<SceneInfo> mSceneInfos;
    private SceneButtonAdapter mSceneButtonAdapter;
    private SceneInfo mCurrentScene;

    private int type =1;//1:创建闹钟 2：编辑闹钟
    private Clock clock;
    private String roomId;
    private String mode_id="";

    private List<BaseModeEntity> modeEntities;
    private List<DynamicMode> dynamicModes;
    private List<NormalMode> normalModes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock_setting);
        initView();
        initScene();
    }

    private void initData() {
        type = getIntent().getIntExtra("type",1);
        roomId = getIntent().getStringExtra("roomId");
        if(type==1){
            title.setText(R.string.clock_create);
        }else{
            clock = (Clock) getIntent().getSerializableExtra("clock");
            title.setText(R.string.clock_edit);
        }
    }

    @Override
    public void loadData() {
        if(!TextUtils.isEmpty(roomId)){
            MyApplication.getInstance().getClient().getModes(roomId, new ILightMgrApi.Callback<GetModesResp>() {
                @Override
                public void callback(int code, GetModesResp resp) {
                    if (code == 0) {
                        if(resp.getNormal_modes()!=null&&resp.getNormal_modes().size()>0){
                            normalModes = resp.getNormal_modes();
                            for(NormalMode normalMode:normalModes){
                                BaseModeEntity entity = new BaseModeEntity();
                                entity.setModeType(1);
                                entity.setMode_name(normalMode.getMode_name());
                                entity.setMode_id(normalMode.getMode_id());
                                modeEntities.add(entity);
                            }
                        }
                        if(resp.getDynamic_modes()!=null&&resp.getDynamic_modes().size()>0){
                            dynamicModes = resp.getDynamic_modes();
                            for (DynamicMode dynamicMode:dynamicModes){
                                BaseModeEntity entity = new BaseModeEntity();
                                entity.setModeType(2);
                                entity.setMode_name(dynamicMode.getMode_name());
                                entity.setMode_id(dynamicMode.getMode_id());
                                modeEntities.add(entity);
                            }
                        }
                        initSceneAdapter();
                    } else {
                        ToastUtils.show(ClockSettingActivity.this, resp.getError_desc());
                    }
                }
            });
        }
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ic_back:
                finish();
                break;
            case R.id.ic_menu:
                //1:添加闹钟  2：修改闹钟
                operateClock();
                break;
        }
    }

    private void operateClock() {
        String hour = (String) wheelHour.getSelectionItem();
        String minute = (String) wheelMin.getSelectionItem();
        String clock_name = mode_name.getText().toString().trim();
        if(TextUtils.isEmpty(clock_name)||"自定义模式".equals(clock_name)){
            ToastUtils.show(this,"请选择模式");
            return;
        }
        String weekday = "";
        for(int i=0;i<mWeeks.length;i++){
            CheckedTextView textView = mWeeks[i];
            int day = i;
            if(i==0)day=7;
            if(textView.isChecked()){
                weekday = weekday+","+day;
            }
        }
        if(TextUtils.isEmpty(weekday)){
            ToastUtils.show(this,R.string.choose_weekday_please);
            return;
        }
        weekday = weekday.substring(1);
        if(TextUtils.isEmpty(mode_id)){
            ToastUtils.show(this,R.string.choose_weekday_please);
            return;
        }
        if(type==1){
            MyApplication.getInstance().getClient().addClock(roomId, clock_name, mode_id, weekday, hour, minute, new ILightMgrApi.Callback<BaseResp>() {
                @Override
                public void callback(int code, BaseResp entity) {
                    if(code==0){
                        ToastUtils.show(ClockSettingActivity.this,R.string.add_clock_success);
                        Intent data = new Intent();
                        data.putExtra("flag","success");
                        setResult(RESULT_OK,data);
                        finish();
                    }else {
                        ToastUtils.show(ClockSettingActivity.this,entity.getError_desc());
                    }
                }
            });
        }else {
            MyApplication.getInstance().getClient().updateClock(mode_id, clock.getClockId(), weekday, hour, minute, new ILightMgrApi.Callback<BaseResp>() {
                @Override
                public void callback(int code, BaseResp entity) {
                    if(code==0){
                        ToastUtils.show(ClockSettingActivity.this,R.string.edit_clock_success);
                        Intent data = new Intent();
                        data.putExtra("flag","success");
                        setResult(RESULT_OK,data);
                        finish();
                    }else {
                        ToastUtils.show(ClockSettingActivity.this,entity.getError_desc());
                    }
                }
            });
        }
    }

    private void initView() {
        back = (ImageView) findViewById(R.id.ic_back);
        title = (TextView) findViewById(R.id.title);
        save = (TextView) findViewById(R.id.ic_menu);
        mode_name = (TextView) findViewById(R.id.mode_name);
        wheelHour = (WheelView) findViewById(R.id.wheel_hour);
        wheelMin = (WheelView) findViewById(R.id.wheel_min);
        repeatWeek = (LinearLayout) findViewById(R.id.week);
        gridView = (GridView) findViewById(R.id.gridview);

        back.setOnClickListener(this);
        save.setOnClickListener(this);
        modeEntities = new ArrayList<>();
        initWheelView();

        mWeeks = new CheckedTextView[7];
        for (int i = 0; i < texts.length; i++) {
            initRepeatDay(i);
        }
        initData();
        //初始化数据
        if(type==2&&clock!=null){
            String hour = clock.getHour();
            String minute = clock.getMinute();
            wheelHour.setSelection(Integer.parseInt(hour)+1);
            wheelMin.setSelection(Integer.parseInt(minute)+1);
            //(1,2,3,4)
            String[] weeks = clock.getWeekday().split(",");
            for(int i=0;i<weeks.length;i++){
                String day = weeks[i].replace("(","").replace(")","");
                mWeeks[Integer.parseInt(day)].setChecked(true);
            }
            mode_id = clock.getMode_id();
        }
        save.setOnClickListener(this);
    }

    private void initRepeatDay(int i) {
        final CheckedTextView textView = new CheckedTextView(this);
        textView.setTag(i);
        textView.setText(texts[i]);
        textView.setTextColor(getResources().getColor(R.color.orange));
        textView.setTextSize(12);
        textView.setGravity(Gravity.CENTER);
        textView.setBackgroundResource(R.drawable.btn_week);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
        params.gravity = Gravity.CENTER_VERTICAL;
        final LinearLayout content = new LinearLayout(this);
        content.setGravity(Gravity.CENTER);
        content.addView(textView);
        repeatWeek.addView(content, params);

        mWeeks[i] = textView;
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setChecked(!textView.isChecked());
                if (textView.isChecked()) {
                    textView.setTextColor(getResources().getColor(R.color.black));
                    textView.setBackgroundResource(R.drawable.btn_week_press);
                } else {
                    textView.setTextColor(getResources().getColor(R.color.orange));
                    textView.setBackgroundResource(R.drawable.btn_week);
                }
            }
        });
    }

    private void initWheelView() {
        WheelView.WheelViewStyle style = new WheelView.WheelViewStyle();
        style.selectedTextColor = Color.parseColor("#0288ce");
        style.backgroundColor = getResources().getColor(R.color.dark);
        style.textColor = Color.GRAY;
        style.selectedTextSize = 20;

        wheelHour.setWheelSize(5);
        wheelHour.setLoop(true);
        wheelHour.setWheelAdapter(new ArrayWheelAdapter(this));
        wheelHour.setSkin(WheelView.Skin.Holo);
        wheelHour.setWheelData(createHours());
        wheelHour.setStyle(style);

        wheelMin.setWheelSize(5);
        wheelMin.setLoop(true);
        wheelMin.setWheelAdapter(new ArrayWheelAdapter(this));
        wheelMin.setSkin(WheelView.Skin.Holo);
        wheelMin.setWheelData(createMinutes());
        wheelMin.setStyle(style);


    }

    private void initScene() {
        // 默认的4个按钮 + 自定义按钮
        mSceneInfos = new ArrayList<>();
        adapter = new GridViewAdapter(this, mSceneInfos, new GridViewAdapter.SceneButtonAdapterListener() {
            @Override
            public void onSceneClick(SceneInfo sceneInfo) {
                mode_name.setText(sceneInfo.name);
                mode_id = sceneInfo.id;
            }
        });
        mSceneButtonAdapter = new SceneButtonAdapter(this, mSceneInfos, new SceneButtonAdapter.SceneButtonAdapterListener() {
            @Override
            public void onSceneClick(SceneInfo sceneInfo) {
                mode_name.setText(sceneInfo.name);
                mode_id = sceneInfo.id;
            }

            @Override
            public void onSceneClockClick(SceneInfo sceneInfo) {

            }

            @Override
            public void onSceneDeleteClick(SceneInfo sceneInfo) {

            }

            @Override
            public void onSceneEditClick(SceneInfo sceneInfo) {

            }
        });
        gridView.setAdapter(adapter);
        gridView.setVerticalSpacing(1);

    }

    private void initSceneAdapter() {
        if(modeEntities!=null&&modeEntities.size()>0){
            int checkPosion = -1;
            for (int i=0;i<modeEntities.size();i++){
                BaseModeEntity entity = modeEntities.get(i);
                SceneInfo scene;
                if(Integer.parseInt(entity.getMode_id())<=4){
                    scene = new SceneInfo(entity.getMode_id()+"", entity.getMode_name(), null, SceneInfo.TYPE_DEFAULT);
                }else{
                    scene = new SceneInfo(entity.getMode_id()+"", entity.getMode_name(), null, SceneInfo.TYPE_DIY);
                    scene.mode_type = entity.getModeType();
                }
                mSceneInfos.add(scene);
                if(entity.getMode_id().equals(mode_id)){
                    checkPosion = i;
                    mode_name.setText(entity.getMode_name());
                }
            }
//            mSceneButtonAdapter.setSceneInfo(mSceneInfos);
            adapter.setSceneInfo(checkPosion,mSceneInfos);
        }
    }
    private ArrayList<String> createHours() {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < 24; i++) {
            if (i < 10) {
                list.add("0" + i);
            } else {
                list.add("" + i);
            }
        }
        return list;
    }

    private ArrayList<String> createMinutes() {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < 60; i++) {
            if (i < 10) {
                list.add("0" + i);
            } else {
                list.add("" + i);
            }
        }
        return list;
    }

}
