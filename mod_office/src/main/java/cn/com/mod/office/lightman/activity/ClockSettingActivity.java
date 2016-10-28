package cn.com.mod.office.lightman.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.com.mod.office.lightman.MyApplication;
import cn.com.mod.office.lightman.R;
import cn.com.mod.office.lightman.activity.base.BaseActivity;
import cn.com.mod.office.lightman.adapter.GridViewAdapter;
import cn.com.mod.office.lightman.adapter.SceneButtonAdapter;
import cn.com.mod.office.lightman.api.ILightMgrApi;
import cn.com.mod.office.lightman.api.resp.GetModesResp;
import cn.com.mod.office.lightman.entity.BaseResponse;
import cn.com.mod.office.lightman.entity.Clock;
import cn.com.mod.office.lightman.entity.SceneInfo;
import cn.com.mod.office.lightman.widget.timepicker.adapter.ArrayWheelAdapter;
import cn.com.mod.office.lightman.widget.timepicker.widget.WheelView;

public class ClockSettingActivity extends BaseActivity implements View.OnClickListener{

    private static int[] texts = {R.string.sunday,R.string.monday, R.string.tuesday, R.string.wednesday, R.string.thursday, R.string.friday, R.string.saturday};

    private ImageView back;
    private TextView title,save;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock_setting);
        initData();
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
                public void callback(int code, GetModesResp entity) {

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
                break;
        }
    }
    private void initView() {
        back = (ImageView) findViewById(R.id.ic_back);
        title = (TextView) findViewById(R.id.title);
        save = (TextView) findViewById(R.id.ic_menu);
        wheelHour = (WheelView) findViewById(R.id.wheel_hour);
        wheelMin = (WheelView) findViewById(R.id.wheel_min);
        repeatWeek = (LinearLayout) findViewById(R.id.week);
        gridView = (GridView) findViewById(R.id.gridview);

        back.setOnClickListener(this);
        save.setOnClickListener(this);

        initWheelView();

        mWeeks = new CheckedTextView[7];
        for (int i = 0; i < texts.length; i++) {
            final CheckedTextView textView = new CheckedTextView(this);
            textView.setText(texts[i]);
            textView.setTextColor(getResources().getColor(R.color.orange));
            textView.setTextSize(12);
            textView.setGravity(Gravity.CENTER);
            textView.setBackgroundResource(R.drawable.btn_week);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
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
            //情景模式
            int scene = clock.getMode_id();
        }
    }

    private void initWheelView() {
        WheelView.WheelViewStyle style = new WheelView.WheelViewStyle();
        style.selectedTextColor = Color.parseColor("#0288ce");
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
        mSceneInfos = new ArrayList<SceneInfo>();
        final SceneInfo scene1 = new SceneInfo("1", getString(R.string.default_scene1), null, SceneInfo.TYPE_DEFAULT);
        SceneInfo scene2 = new SceneInfo("2", getString(R.string.default_scene2), null, SceneInfo.TYPE_DEFAULT);
        SceneInfo scene3 = new SceneInfo("3", getString(R.string.default_scene3), null, SceneInfo.TYPE_DEFAULT);
        SceneInfo scene4 = new SceneInfo("4", getString(R.string.default_scene4), null, SceneInfo.TYPE_DEFAULT);

        SceneInfo scene5 = new SceneInfo("5", getString(R.string.default_scene4), null, SceneInfo.TYPE_DEFAULT);
        SceneInfo scene6 = new SceneInfo("6", getString(R.string.default_scene4), null, SceneInfo.TYPE_DEFAULT);
        SceneInfo scene7 = new SceneInfo("7", getString(R.string.default_scene4), null, SceneInfo.TYPE_DEFAULT);
        SceneInfo scene8 = new SceneInfo("8", getString(R.string.default_scene4), null, SceneInfo.TYPE_DEFAULT);

        SceneInfo scene9 = new SceneInfo("", getString(R.string.default_add), null, SceneInfo.TYPE_NONE);
        mSceneInfos.add(scene1);
        mSceneInfos.add(scene2);
        mSceneInfos.add(scene3);
        mSceneInfos.add(scene4);

        mSceneInfos.add(scene5);
        mSceneInfos.add(scene6);
        mSceneInfos.add(scene7);
        mSceneInfos.add(scene8);

        mSceneInfos.add(scene9);
        adapter = new GridViewAdapter(this,mSceneInfos,null);
        gridView.setAdapter(adapter);
//        mSceneButtonAdapter = new SceneButtonAdapter(ClockSettingActivity.this, mSceneInfos, new SceneButtonAdapter.SceneButtonAdapterListener() {
//            @Override
//            public void onSceneClick(SceneInfo sceneInfo) {
//                if (sceneInfo.type == SceneInfo.TYPE_NONE) {
//                    Intent intent = new Intent(ClockSettingActivity.this, SceneActivity.class);
////                    intent.putExtra("leds", mSelectedLeds.toArray(new String[]{}));
////                    intent.putExtra("roomId", id);
////                    intent.putExtra("type", SceneActivity.TYPE_ADD);
////                    startActivityForResult(intent, REQUEST_CODE_ADD_SCENE);
//                    return;
//                }
//
//                ILightMgrApi.Callback<BaseResponse> responseCallback = new ILightMgrApi.Callback<BaseResponse>() {
//                    @Override
//                    public void callback(int code, BaseResponse response) {
//                        switch (code) {
//                            case CODE_SUCCESS:
//                                break;
//                        }
//                    }
//                };
//                // 没有选中任何LED灯，则以房间为单位发送
////                if (mSelectedLeds.size() == 0) {
////                    if (sceneInfo.type == SceneInfo.TYPE_DEFAULT) {
////                        MyApplication.getInstance().getClient().applyDefaultScene(sceneInfo.id, null, new String[]{id}, null, null, responseCallback);
////                    } else if (sceneInfo.type == SceneInfo.TYPE_DIY) {
////                        MyApplication.getInstance().getClient().applyDiyScene(sceneInfo.id, id, null, null, responseCallback);
////                    }
////                    return;
////                }
//                // 如果有选中LED灯，则优先以灯组发送，剩余的按灯发送
////                if (mSelectedLeds.size() != 0) {
////                    if (mSelectedGroups.size() != 0) {
////                        if (sceneInfo.type == SceneInfo.TYPE_DEFAULT) {
////                            MyApplication.getInstance().getClient().applyDefaultScene(sceneInfo.id, null, null, mSelectedGroups.toArray(new String[]{}), null, responseCallback);
////                        } else if (sceneInfo.type == SceneInfo.TYPE_DIY) {
////                            MyApplication.getInstance().getClient().applyDiyScene(sceneInfo.id, null, mSelectedGroups.toArray(new String[]{}), null, responseCallback);
////                        }
////                    }
////                    ArrayList<String> otherLeds = new ArrayList<String>();
////                    for (String ledId : mSelectedLeds) {
////                        if (!mSelectedGroups.contains(getGroup(ledId))) {
////                            otherLeds.add(ledId);
////                        }
////                    }
////                    if (otherLeds.size() != 0) {
////                        if (sceneInfo.type == SceneInfo.TYPE_DEFAULT) {
////                            MyApplication.getInstance().getClient().applyDefaultScene(sceneInfo.id, null, null, null, otherLeds.toArray(new String[]{}), responseCallback);
////                        } else if (sceneInfo.type == SceneInfo.TYPE_DIY) {
////                            MyApplication.getInstance().getClient().applyDiyScene(sceneInfo.id, null, null, otherLeds.toArray(new String[]{}), responseCallback);
////                        }
////                    }
////                }
//            }
//
//            @Override
//            public void onSceneClockClick(SceneInfo sceneInfo) {
//                mCurrentScene = sceneInfo;
//            }
//
//            @Override
//            public void onSceneDeleteClick(final SceneInfo sceneInfo) {
//                new AlertDialog.Builder(ClockSettingActivity.this)
//                        .setTitle(getString(R.string.delete_scene_dialog_title))
//                        .setMessage(getString(R.string.delete_scene_dialog_msg))
//                        .setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
////                                mMaskUtils.show();
//                                MyApplication.getInstance().getClient().deleteDiyScene(sceneInfo.id, new ILightMgrApi.Callback<BaseResponse>() {
//                                    @Override
//                                    public void callback(int code, BaseResponse response) {
////                                        mMaskUtils.cancel();
////                                        switch (code) {
////                                            case CODE_TIMEOUT:
////                                                mToastUtils.show(getString(R.string.tip_timeout));
////                                                break;
////                                            case CODE_NETWORK_ERROR:
////                                                mToastUtils.show(getString(R.string.tip_network_connect_faild));
////                                                break;
////                                            case CODE_SUCCESS:
////                                                if (response.success) {
////                                                    mSceneInfos.remove(sceneInfo);
////                                                    mSceneButtonAdapter.notifyDataSetChanged();
////                                                } else {
////                                                    mToastUtils.show(response.msg);
////                                                }
////                                                break;
////                                        }
//                                    }
//                                });
//                            }
//                        }).setNegativeButton("取消", null).show();
//            }
//
//            @Override
//            public void onSceneEditClick(SceneInfo sceneInfo) {
////                Intent intent = new Intent(RoomActivity.this, SceneActivity.class);
////                intent.putExtra("sceneId", sceneInfo.id);
////                intent.putExtra("name", sceneInfo.name);
////                intent.putExtra("type", SceneActivity.TYPE_EDIT);
////                intent.putExtra("leds", mSelectedLeds.toArray(new String[]{}));
////                startActivityForResult(intent, REQUEST_CODE_EDIT_SCENE);
//            }
//        });
//        mSceneButtonAdapter.setDevideVisible(false);
//        gridView.setAdapter(mSceneButtonAdapter);

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
