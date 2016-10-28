package cn.com.mod.office.lightman.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.joshua.common.util.MaskUtils;
import com.joshua.common.util.ToastUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.com.mod.office.lightman.MyApplication;
import cn.com.mod.office.lightman.R;
import cn.com.mod.office.lightman.activity.base.BaseActivity;
import cn.com.mod.office.lightman.adapter.SceneButtonAdapter;
import cn.com.mod.office.lightman.api.ILightMgrApi;
import cn.com.mod.office.lightman.entity.BaseResponse;
import cn.com.mod.office.lightman.entity.GroupInfo;
import cn.com.mod.office.lightman.entity.Lamps;
import cn.com.mod.office.lightman.entity.SceneInfo;
import cn.com.mod.office.lightman.widget.HorizontalListView;
import cn.com.mod.office.lightman.widget.ItemListView;
import cn.com.mod.office.lightman.widget.LedView;
import cn.com.mod.office.lightman.widget.ScrollBar;

/**
 * 房间界面
 * Created by CAT on 2014/11/10.
 */
public class RoomActivity extends BaseActivity implements OnClickListener{
    public static final String TAG = "RoomActivity";

    public static final int REQUEST_CODE_CLOCK = 1;
    public static final int REQUEST_CODE_ADD_SCENE = 2;
    public static final int REQUEST_CODE_EDIT_SCENE = 3;

    private ToastUtils mToastUtils;
    private MaskUtils mMaskUtils;
    private TextView mRoomName;
    private ImageView mGoBack;
    private AbsoluteLayout mLedCanvas;
    private ItemListView mList;
    private ImageView mMenu;
    private HorizontalListView mSceneList;
    private ScrollBar mScrollBar;
    private SceneButtonAdapter mSceneButtonAdapter;
    private List<GroupInfo> mGroupInfos;
    private List<SceneInfo> mSceneInfos;
    private Map<String, List<LedView>> mLedViews;
    private Set<String> mSelectedGroups = new HashSet<String>();
    private Set<String> mSelectedLeds = new HashSet<String>();
    private SceneInfo mCurrentScene;
    private boolean isSelecting = false;

    private LinearLayout select_menu,select_all,select_none,grouping,unmarshall;
    private LinearLayout ll_adjust_lamp;

    private String id;
    private String name;

    private LinearLayout ll_scene;
    private TextView adjust_lamp;
    private ArrayList<LedView> ledViews;
    private List<Lamps> lamps;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        initView();
        initListener();
    }
    // 初始化
    public void initView() {
        // 初始化系统工具
        mMaskUtils = new MaskUtils(this);
        mToastUtils = new ToastUtils(this);

        // 初始化组件
        ll_scene = (LinearLayout) findViewById(R.id.ll_scene);
        adjust_lamp = (TextView) findViewById(R.id.adjust_lamp);

        mRoomName = (TextView) findViewById(R.id.room_name);
        mGoBack = (ImageView) findViewById(R.id.ic_back);
        mList = (ItemListView) findViewById(R.id.menu_list);
        mLedCanvas = (AbsoluteLayout) findViewById(R.id.led_canvas);
        mMenu = (ImageView) findViewById(R.id.ic_menu);
        mSceneList = (HorizontalListView) findViewById(R.id.control_panel);
        mScrollBar = (ScrollBar) findViewById(R.id.scrollbar);

        select_menu = (LinearLayout) findViewById(R.id.select_menu);
        ll_adjust_lamp = (LinearLayout) findViewById(R.id.ll_adjust_lamp);
        select_all = (LinearLayout) findViewById(R.id.select_all);
        select_none = (LinearLayout) findViewById(R.id.select_none);
        grouping = (LinearLayout) findViewById(R.id.grouping);
        unmarshall = (LinearLayout) findViewById(R.id.unmarshall);

//        mScrollBar.setScrollbarWidth(mSceneList.getChildCount(), 4);
        mSceneList.setScrollBar(mScrollBar);
        mScrollBar.setColor(getResources().getColor(R.color.orange));
        mScrollBar.setVisibility(View.VISIBLE);

        id = getIntent().getStringExtra("roomId");
        name = getIntent().getStringExtra("roomName");

        initScene();
    }
    private void initListener() {
        mScrollBar.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

        mLedCanvas.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mList.getVisibility() == View.VISIBLE) {
                    mList.setVisibility(View.INVISIBLE);
                }
            }
        });
        mList.setRoomMode(new ItemListView.OnChooseRoomMode() {
            @Override
            public void onChooseRoomMode(int index) {
                //index  1:模式管理  2：闹钟设置  3：故障申报
                if(index==1){
                    Intent intent = new Intent(RoomActivity.this,ModeManagerActivity.class);
                    intent.putExtra("roomId",id);
                    startActivity(intent);
                }else if(index==2){
                    Intent intent1 = new Intent(RoomActivity.this,ClockSettingActivity.class);
                    intent1.putExtra("roomId",id);
                    startActivity(intent1);
                }

            }
        });



        mGoBack.setOnClickListener(new OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           finish();
                                       }
                                   }

        );
        select_all.setOnClickListener(this);
        select_none.setOnClickListener(this);
        grouping.setOnClickListener(this);
        unmarshall.setOnClickListener(this);
        adjust_lamp.setOnClickListener(this);

        mMenu.setOnClickListener(new OnClickListener() {
                                     @Override
                                     public void onClick(View v) {
                                         if (mList.getVisibility() == View.VISIBLE) {
                                             mList.setVisibility(View.INVISIBLE);
                                         } else {
                                             mList.setVisibility(View.VISIBLE);
                                         }
                                     }
                                 }
        );
    }
    private void initScene() {
        // 默认的4个按钮 + 自定义按钮
        mSceneInfos = new ArrayList<SceneInfo>();
        final SceneInfo scene1 = new SceneInfo("1", getString(R.string.default_scene1), null, SceneInfo.TYPE_DEFAULT);
        SceneInfo scene2 = new SceneInfo("2", getString(R.string.default_scene2), null, SceneInfo.TYPE_DEFAULT);
        SceneInfo scene3 = new SceneInfo("3", getString(R.string.default_scene3), null, SceneInfo.TYPE_DEFAULT);
        SceneInfo scene4 = new SceneInfo("4", getString(R.string.default_scene4), null, SceneInfo.TYPE_DEFAULT);
        SceneInfo scene5 = new SceneInfo("", getString(R.string.default_add), null, SceneInfo.TYPE_NONE);
        mSceneInfos.add(scene1);
        mSceneInfos.add(scene2);
        mSceneInfos.add(scene3);
        mSceneInfos.add(scene4);
        mSceneInfos.add(scene5);
        mSceneButtonAdapter = new SceneButtonAdapter(RoomActivity.this, mSceneInfos, new SceneButtonAdapter.SceneButtonAdapterListener() {
            @Override
            public void onSceneClick(SceneInfo sceneInfo) {
                if (sceneInfo.type == SceneInfo.TYPE_NONE) {
                    Intent intent = new Intent(RoomActivity.this, SceneActivity.class);
                    intent.putExtra("leds", mSelectedLeds.toArray(new String[]{}));
                    intent.putExtra("roomId", id);
                    intent.putExtra("type", SceneActivity.TYPE_ADD);
                    startActivityForResult(intent, REQUEST_CODE_ADD_SCENE);
                    return;
                }

                ILightMgrApi.Callback<BaseResponse> responseCallback = new ILightMgrApi.Callback<BaseResponse>() {
                    @Override
                    public void callback(int code, BaseResponse response) {
                        switch (code) {
                            case CODE_SUCCESS:
                                break;
                        }
                    }
                };
                // 没有选中任何LED灯，则以房间为单位发送
                if (mSelectedLeds.size() == 0) {
                    if (sceneInfo.type == SceneInfo.TYPE_DEFAULT) {
                        MyApplication.getInstance().getClient().applyDefaultScene(sceneInfo.id, null, new String[]{id}, null, null, responseCallback);
                    } else if (sceneInfo.type == SceneInfo.TYPE_DIY) {
                        MyApplication.getInstance().getClient().applyDiyScene(sceneInfo.id, id, null, null, responseCallback);
                    }
                    return;
                }
                // 如果有选中LED灯，则优先以灯组发送，剩余的按灯发送
                if (mSelectedLeds.size() != 0) {
                    if (mSelectedGroups.size() != 0) {
                        if (sceneInfo.type == SceneInfo.TYPE_DEFAULT) {
                            MyApplication.getInstance().getClient().applyDefaultScene(sceneInfo.id, null, null, mSelectedGroups.toArray(new String[]{}), null, responseCallback);
                        } else if (sceneInfo.type == SceneInfo.TYPE_DIY) {
                            MyApplication.getInstance().getClient().applyDiyScene(sceneInfo.id, null, mSelectedGroups.toArray(new String[]{}), null, responseCallback);
                        }
                    }
                    ArrayList<String> otherLeds = new ArrayList<String>();
                    for (String ledId : mSelectedLeds) {
                        if (!mSelectedGroups.contains(getGroup(ledId))) {
                            otherLeds.add(ledId);
                        }
                    }
                    if (otherLeds.size() != 0) {
                        if (sceneInfo.type == SceneInfo.TYPE_DEFAULT) {
                            MyApplication.getInstance().getClient().applyDefaultScene(sceneInfo.id, null, null, null, otherLeds.toArray(new String[]{}), responseCallback);
                        } else if (sceneInfo.type == SceneInfo.TYPE_DIY) {
                            MyApplication.getInstance().getClient().applyDiyScene(sceneInfo.id, null, null, otherLeds.toArray(new String[]{}), responseCallback);
                        }
                    }
                }
            }

            @Override
            public void onSceneClockClick(SceneInfo sceneInfo) {
                mCurrentScene = sceneInfo;
            }

            @Override
            public void onSceneDeleteClick(final SceneInfo sceneInfo) {
                new AlertDialog.Builder(RoomActivity.this)
                        .setTitle(getString(R.string.delete_scene_dialog_title))
                        .setMessage(getString(R.string.delete_scene_dialog_msg))
                        .setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mMaskUtils.show();
                                MyApplication.getInstance().getClient().deleteDiyScene(sceneInfo.id, new ILightMgrApi.Callback<BaseResponse>() {
                                    @Override
                                    public void callback(int code, BaseResponse response) {
                                        mMaskUtils.cancel();
                                        switch (code) {
                                            case CODE_TIMEOUT:
                                                mToastUtils.show(getString(R.string.tip_timeout));
                                                break;
                                            case CODE_NETWORK_ERROR:
                                                mToastUtils.show(getString(R.string.tip_network_connect_faild));
                                                break;
                                            case CODE_SUCCESS:
                                                if (response.success) {
                                                    mSceneInfos.remove(sceneInfo);
                                                    mSceneButtonAdapter.notifyDataSetChanged();
                                                } else {
                                                    mToastUtils.show(response.msg);
                                                }
                                                break;
                                        }
                                    }
                                });
                            }
                        }).setNegativeButton("取消", null).show();
            }

            @Override
            public void onSceneEditClick(SceneInfo sceneInfo) {
                Intent intent = new Intent(RoomActivity.this, SceneActivity.class);
                intent.putExtra("sceneId", sceneInfo.id);
                intent.putExtra("name", sceneInfo.name);
                intent.putExtra("type", SceneActivity.TYPE_EDIT);
                intent.putExtra("leds", mSelectedLeds.toArray(new String[]{}));
                startActivityForResult(intent, REQUEST_CODE_EDIT_SCENE);
            }
        });

        mSceneList.setAdapter(mSceneButtonAdapter);

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
            case R.id.adjust_lamp:
                Intent intent = new Intent(RoomActivity.this,PatameterSettingActivity.class);
                intent.putExtra("leds", mSelectedLeds.toArray(new String[]{}));
                intent.putExtra("roomId", id);
                intent.putExtra("type", SceneActivity.TYPE_ADD);
                startActivity(intent);
                break;
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

    // 获取group目前选中的led灯的数目
    private int getSelectedLedCount(String groupId) {
        int count = 0;
        List<LedView> ledViews = mLedViews.get(groupId);
        for (LedView ledView : ledViews) {
            if (ledView.isChecked()) {
                count++;
            }
        }
        return count;
    }

    // 获取led所在的group
    private String getGroup(String ledId) {
        String groupId = null;
        for (String group : mLedViews.keySet()) {
            List<LedView> ledViews = mLedViews.get(group);
            for (LedView ledView : ledViews) {
                if (ledView.getLedInfo().getLamp_id().equals(ledId)) {
                    groupId = group;
                    break;
                }
            }
            if (groupId != null) {
                break;
            }
        }
        return groupId;
    }

    @Override
    public void loadData() {
        if (id != null && name != null) {
            mRoomName.setText(name);
            mMaskUtils.show();
            getScenes();

            // 获取房间图片
            MyApplication.getInstance().getClient().getRoomImg(id, new ILightMgrApi.Callback<Bitmap>() {
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void callback(int code, Bitmap bitmap) {
                    mMaskUtils.cancel();
                    if(bitmap==null){
                        mToastUtils.show(getString(R.string.room_no_map));
                        finish();
                        return;
                    }
                    switch (code) {
                        case CODE_SUCCESS:
                            mLedCanvas.setBackground(new BitmapDrawable(bitmap));
                            // 获取房间灯组
                            MyApplication.getInstance().getClient().getLampsInRoom(id, new ILightMgrApi.Callback<List<Lamps>>() {
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
        ledViews = new ArrayList<LedView>();
        for (final Lamps ledInfo : lamps) {
            final LedView ledView = new LedView(RoomActivity.this, ledInfo);
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
            int w = mLedCanvas.getWidth();
            int h = mLedCanvas.getHeight();
            if (w > h) {
                int size = (int) (h * rate);
                int pointX = (int) (h / 600.0 * x) + (w - h) / 2;
                int pointY = (int) (h / 600.0 * y);
                mLedCanvas.addView(ledView, new AbsoluteLayout.LayoutParams(size, size, pointX, pointY));
            } else {
                int size = (int) (w * rate);
                int pointX = (int) (w / 600.0 * x);
                int pointY = ((int) (w / 600.0 * y) + (h - w) / 2);
                mLedCanvas.addView(ledView, new AbsoluteLayout.LayoutParams(size, size, pointX, pointY));
            }
            ledViews.add(ledView);
        }
        if (mLedViews == null) {
            mLedViews = new HashMap<String, List<LedView>>();
        }
        mLedViews.put(id, ledViews);
        // 获取各个灯组的LED灯
//                                                MyApplication.getInstance().getClient().listLeds(groupInfo.id, new ILightMgrApi.Callback<List<LedInfo>>() {
//                                                    private String groupId = groupInfo.id;
//
//                                                    @Override
//                                                    public void callback(int code, List<LedInfo> ledInfos) {
//                                                        switch (code) {
//                                                            case CODE_SUCCESS:
//
//                                                                break;
//                                                        }
//                                                    }
//                                                });
    }

    // 监听地图点选LED灯图标后，对列表的选中状态的变化
    private synchronized void handleItemList(Lamps ledInfo, boolean isSelected) {
        //显示操作面板
        if(mSelectedLeds.size()>0){
            mMenu.setVisibility(View.INVISIBLE);
            select_menu.setVisibility(View.VISIBLE);
            ll_adjust_lamp.setVisibility(View.VISIBLE);
            ll_scene.setVisibility(View.GONE);
        }else{
            mMenu.setVisibility(View.VISIBLE);
            select_menu.setVisibility(View.GONE);
            ll_adjust_lamp.setVisibility(View.GONE);
            ll_scene.setVisibility(View.VISIBLE);
        }

    }

    private void getScenes() {

        // 获取房间默认情景
        MyApplication.getInstance().getClient().getDefaultSceneName(id, new ILightMgrApi.Callback<List<SceneInfo>>() {
            @Override
            public void callback(int code, List<SceneInfo> sceneInfos) {
                switch (code) {
                    case CODE_SUCCESS:
                        for (int i = 0; i < sceneInfos.size(); i++) {
                            mSceneInfos.get(i).name = sceneInfos.get(i).name;
                            mSceneButtonAdapter.notifyDataSetChanged();
                        }
                        break;
                }
            }
        });

        // 获取房间自定义情景
        MyApplication.getInstance().getClient().listDiyScene(id, new ILightMgrApi.Callback<List<SceneInfo>>() {
            @Override
            public void callback(int code, List<SceneInfo> sceneInfos) {
                switch (code) {
                    case CODE_SUCCESS:
                        for (SceneInfo sceneInfo : sceneInfos) {
                            sceneInfo.type = SceneInfo.TYPE_DIY;
                            sceneInfo.icon = getResources().getDrawable(R.drawable.default_diy2);
                        }
                        mSceneInfos.addAll(mSceneInfos.size() - 1, sceneInfos);
                        mSceneButtonAdapter.notifyDataSetChanged();

                        for (final SceneInfo sceneInfo : mSceneInfos) {
                            if (sceneInfo.type == SceneInfo.TYPE_DIY) {
                                MyApplication.getInstance().getClient().getDiySceneImg(sceneInfo.id, new ILightMgrApi.Callback<Bitmap>() {
                                    @Override
                                    public void callback(int code, Bitmap entity) {
                                        switch (code) {
                                            case CODE_SUCCESS:
                                                sceneInfo.icon = new BitmapDrawable(getResources(), entity);
                                                mSceneButtonAdapter.notifyDataSetChanged();
                                                break;
                                        }
                                    }
                                });
                            }
                        }
                        break;
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mList.getVisibility() == View.VISIBLE) {
            mList.setVisibility(View.INVISIBLE);
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            if (mList.getVisibility() == View.VISIBLE) {
                mList.setVisibility(View.INVISIBLE);
            } else {
                mList.setVisibility(View.VISIBLE);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_CLOCK:
                if (resultCode == RESULT_OK) {
                    String id = data.getStringExtra("id");
                    if (id != null) {
                        for (int i = 0; i < mGroupInfos.size(); i++) {
                            if (mGroupInfos.get(i).id.equals(id)) {
                                mGroupInfos.get(i).hasClock = false;
//                                mList.getAdapter().notifyDataSetChanged();
                            }
                        }
                    }
                }
                break;
            case REQUEST_CODE_ADD_SCENE:
                if (resultCode == RESULT_OK) {
                    SceneInfo sceneInfo = new SceneInfo();
                    sceneInfo.id = data.getStringExtra("id");
                    sceneInfo.name = data.getStringExtra("name");
                    File icon = (File) data.getSerializableExtra("icon");
                    if (icon != null) {
                        try {
                            sceneInfo.icon = new BitmapDrawable(getResources(), new FileInputStream(icon));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                    sceneInfo.type = SceneInfo.TYPE_DIY;
                    mSceneInfos.add(mSceneInfos.size() - 1, sceneInfo);
                    mSceneButtonAdapter.notifyDataSetChanged();
                }
                break;
            case REQUEST_CODE_EDIT_SCENE:
                if (resultCode == RESULT_OK) {
                    String id = data.getStringExtra("id");
                    String name = data.getStringExtra("name");
                    for (SceneInfo sceneInfo : mSceneInfos) {
                        if (sceneInfo.id.equals(id)) {
                            sceneInfo.name = name;
                            File icon = (File) data.getSerializableExtra("icon");
                            if (icon != null) {
                                try {
                                    sceneInfo.icon = new BitmapDrawable(getResources(), new FileInputStream(icon));
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                            }
                            mSceneButtonAdapter.notifyDataSetChanged();
                            break;
                        }
                    }
                }
                break;
        }
    }



}
