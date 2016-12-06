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
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.joshua.common.util.MaskUtils;
import com.joshua.common.util.ToastUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.com.mod.office.lightman.MyApplication;
import cn.com.mod.office.lightman.R;
import cn.com.mod.office.lightman.activity.base.BaseActivity;
import cn.com.mod.office.lightman.adapter.SceneButtonAdapter;
import cn.com.mod.office.lightman.api.BaseResp;
import cn.com.mod.office.lightman.api.ILightMgrApi;
import cn.com.mod.office.lightman.api.resp.GetModesResp;
import cn.com.mod.office.lightman.api.resp.LampsResp;
import cn.com.mod.office.lightman.entity.BaseResponse;
import cn.com.mod.office.lightman.entity.DynamicMode;
import cn.com.mod.office.lightman.entity.GroupInfo;
import cn.com.mod.office.lightman.entity.LampGroup;
import cn.com.mod.office.lightman.entity.LampStatusResp;
import cn.com.mod.office.lightman.entity.Lamps;
import cn.com.mod.office.lightman.entity.NormalMode;
import cn.com.mod.office.lightman.entity.SceneInfo;
import cn.com.mod.office.lightman.entity.TieLampsResp;
import cn.com.mod.office.lightman.entity.base.BaseModeEntity;
import cn.com.mod.office.lightman.widget.HorizontalListView;
import cn.com.mod.office.lightman.widget.ItemListView;
import cn.com.mod.office.lightman.widget.LedView;
import cn.com.mod.office.lightman.widget.ScrollBar;

/**
 * 房间界面
 * Created by CAT on 2014/11/10.
 */
public class RoomActivity extends BaseActivity implements OnClickListener {
    public static final String TAG = "RoomActivity";

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
    private List<SceneInfo> mSceneInfos;
    private Map<String, List<LedView>> mLedViews;
    private List<Lamps> mSelectedLeds = new ArrayList<>();

    private LinearLayout select_menu, select_all, select_none, grouping, unmarshall;
    private LinearLayout ll_adjust_lamp;

    private String id;
    private String name;

    private LinearLayout ll_scene;
    private TextView adjust_lamp;
    private ArrayList<LedView> ledViews;
    private ArrayList<Lamps> lamps;

    private List<BaseModeEntity> modeEntities = new ArrayList<>();
    private List<DynamicMode> dynamicModes;
    private List<NormalMode> normalModes;

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
                mList.setVisibility(View.INVISIBLE);
                //index  1:模式管理  2：闹钟设置  3：故障申报
                if (index == 1) {
                    Intent intent = new Intent(RoomActivity.this, ModeManagerActivity.class);
                    intent.putExtra("roomId", id);
                    startActivity(intent);
                } else if (index == 2) {
                    Intent intent1 = new Intent(RoomActivity.this, ClockListActivity.class);
                    intent1.putExtra("roomId", id);
                    startActivity(intent1);
                } else if (index == 3) {
                    Intent intent2 = new Intent(RoomActivity.this, FaultDeclareActivity.class);
                    intent2.putExtra("roomId", id);
                    startActivity(intent2);
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
        mSceneInfos = new ArrayList<>();
        SceneInfo scene1 = new SceneInfo("1", getString(R.string.default_scene1), null, SceneInfo.TYPE_DEFAULT);
        SceneInfo scene2 = new SceneInfo("2", getString(R.string.default_scene2), null, SceneInfo.TYPE_DEFAULT);
        SceneInfo scene3 = new SceneInfo("3", getString(R.string.default_scene3), null, SceneInfo.TYPE_DEFAULT);
        SceneInfo scene4 = new SceneInfo("4", getString(R.string.default_scene4), null, SceneInfo.TYPE_DEFAULT);
        mSceneInfos.add(scene1);
        mSceneInfos.add(scene2);
        mSceneInfos.add(scene3);
        mSceneInfos.add(scene4);
        mSceneButtonAdapter = new SceneButtonAdapter(RoomActivity.this, mSceneInfos, new SceneButtonAdapter.SceneButtonAdapterListener() {
            @Override
            public void onSceneClick(final SceneInfo sceneInfo) {
                //应用模式
                mMaskUtils.show();
                if(sceneInfo.type==2){
                    MyApplication.getInstance().getClient().playDynamicMode(id, sceneInfo.id, new String[]{}, new ILightMgrApi.Callback<BaseResp>() {
                        @Override
                        public void callback(int code, BaseResp entity) {
                            mMaskUtils.cancel();
                            if(code==0){
                                ToastUtils.show(RoomActivity.this,"应用"+sceneInfo.name+"成功");
                            }else{
                                ToastUtils.show(RoomActivity.this,entity.getError_desc());
                            }
                        }
                    });
                }else{
                    MyApplication.getInstance().getClient().applyNormalMode(id, sceneInfo.id, null, new ILightMgrApi.Callback<BaseResp>() {
                        @Override
                        public void callback(int code, BaseResp entity) {
                            mMaskUtils.cancel();
                            if(code==0){
                                ToastUtils.show(RoomActivity.this,"应用"+sceneInfo.name+"成功");
                            }else{
                                ToastUtils.show(RoomActivity.this,entity.getError_desc());
                            }
                        }
                    });
                }
            }

            @Override
            public void onSceneClockClick(SceneInfo sceneInfo) {
            }

            @Override
            public void onSceneDeleteClick(final SceneInfo sceneInfo) {
//                new AlertDialog.Builder(RoomActivity.this)
//                        .setTitle(getString(R.string.delete_scene_dialog_title))
//                        .setMessage(getString(R.string.delete_scene_dialog_msg))
//                        .setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                mMaskUtils.show();
//                                MyApplication.getInstance().getClient().deleteDiyScene(sceneInfo.id, new ILightMgrApi.Callback<BaseResponse>() {
//                                    @Override
//                                    public void callback(int code, BaseResponse response) {
//                                        mMaskUtils.cancel();
//                                        switch (code) {
//                                            case CODE_TIMEOUT:
//                                                mToastUtils.show(getString(R.string.tip_timeout));
//                                                break;
//                                            case CODE_NETWORK_ERROR:
//                                                mToastUtils.show(getString(R.string.tip_network_connect_faild));
//                                                break;
//                                            case CODE_SUCCESS:
//                                                if (response.success) {
//                                                    mSceneInfos.remove(sceneInfo);
//                                                    mSceneButtonAdapter.notifyDataSetChanged();
//                                                } else {
//                                                    mToastUtils.show(response.msg);
//                                                }
//                                                break;
//                                        }
//                                    }
//                                });
//                            }
//                        }).setNegativeButton("取消", null).show();
            }

            @Override
            public void onSceneEditClick(SceneInfo sceneInfo) {
            }
        });

        mSceneList.setAdapter(mSceneButtonAdapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
            case R.id.adjust_lamp:
                Intent intent = new Intent(RoomActivity.this, PatameterSettingActivity.class);
                intent.putExtra("roomId", id);
                intent.putExtra("type", 1);
                intent.putExtra("adjust",true);
                intent.putExtra("leds",getSelectIds());
                startActivity(intent);
                break;
        }
    }

    private String[] getSelectIds(){
        if(mSelectedLeds!=null&&mSelectedLeds.size()>0){
            String[] ids = new String[mSelectedLeds.size()];
            for (int i=0;i<mSelectedLeds.size();i++){
                Lamps lamp = mSelectedLeds.get(i);
                ids[i] = lamp.getLamp_id();
            }
            return ids;
        }
        return null;
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
                mMaskUtils.show();
                MyApplication.getInstance().getClient().untieLampGroup(groupId, new ILightMgrApi.Callback<BaseResp>() {
                    @Override
                    public void callback(int code, BaseResp entity) {
                        mMaskUtils.cancel();
                        if (code == 0) {
                            ToastUtils.show(RoomActivity.this, R.string.untie_group_success);
                            handleLedViews(false);
                        } else {
                            ToastUtils.show(RoomActivity.this, entity.getError_desc());
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
            mMaskUtils.show();
            MyApplication.getInstance().getClient().tieLampGroup(id, lampIdStr, new ILightMgrApi.Callback<TieLampsResp>() {
                @Override
                public void callback(int code, TieLampsResp entity) {
                    mMaskUtils.cancel();
                    if (code == 0 && entity != null && !TextUtils.isEmpty(entity.getGroup_id())) {
                        ToastUtils.show(RoomActivity.this, R.string.untie_group_success);
                    } else {
                        ToastUtils.show(RoomActivity.this, entity.getError_desc());
                    }
                }
            });
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

    @Override
    public void loadData() {
        if (id != null && name != null) {
            mRoomName.setText(name);
            mMaskUtils.show();
            // 获取房间图片
            MyApplication.getInstance().getClient().getRoomImg(id, new ILightMgrApi.Callback<Bitmap>() {
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void callback(int code, Bitmap bitmap) {
                    mMaskUtils.cancel();
                    if (bitmap == null) {
                        mToastUtils.show(getString(R.string.room_no_map));
                        finish();
                        return;
                    }
                    switch (code) {
                        case CODE_SUCCESS:
                            mLedCanvas.setBackground(new BitmapDrawable(bitmap));
                            // 获取房间灯组
                            MyApplication.getInstance().getClient().getLampsInRoom(id, new ILightMgrApi.Callback<LampsResp>() {
                                @Override
                                public void callback(int code, LampsResp resp) {
                                    switch (code) {
                                        case CODE_SUCCESS:
                                            if (resp != null && resp.getLamps() != null) {
                                                canvasLamps(resp.getLamps());
                                            }
                                            break;
                                        case CODE_FAILURE:
                                            ToastUtils.show(RoomActivity.this, resp.getError_desc());
                                            break;
                                    }
                                }
                            });
                    }
                }
            });
            //获取房间的情景模式
            MyApplication.getInstance().getClient().getModes(id, new ILightMgrApi.Callback<GetModesResp>() {
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
                        ToastUtils.show(RoomActivity.this, resp.getError_desc());
                    }
                }
            });
        }
    }

    private void initSceneAdapter() {
        if(modeEntities.size()>0){
            mSceneInfos.clear();
            for (BaseModeEntity entity:modeEntities){
                SceneInfo scene;
                if(Integer.parseInt(entity.getMode_id())<=4){
                    scene = new SceneInfo(entity.getMode_id()+"", entity.getMode_name(), null, SceneInfo.TYPE_DEFAULT);
                }else{
                    scene = new SceneInfo(entity.getMode_id()+"", entity.getMode_name(), null, SceneInfo.TYPE_DIY);
                    scene.mode_type = entity.getModeType();
                }
                mSceneInfos.add(scene);
            }
            mSceneButtonAdapter.setSceneInfo(mSceneInfos);
        }

    }

    private void canvasLamps(List<Lamps> lamps) {
        this.lamps = new ArrayList<>();
        this.lamps.addAll(lamps);
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
                        if(!mSelectedLeds.contains(ledInfo))
                            mSelectedLeds.add(ledInfo);
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
        if (mSelectedLeds.size() > 0) {
//            mMenu.setVisibility(View.INVISIBLE);
            select_menu.setVisibility(View.VISIBLE);
            ll_adjust_lamp.setVisibility(View.VISIBLE);
            ll_scene.setVisibility(View.GONE);
        } else {
//            mMenu.setVisibility(View.VISIBLE);
            select_menu.setVisibility(View.GONE);
            ll_adjust_lamp.setVisibility(View.GONE);
            ll_scene.setVisibility(View.VISIBLE);
        }

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
