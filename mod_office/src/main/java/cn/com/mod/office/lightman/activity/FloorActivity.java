package cn.com.mod.office.lightman.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.joshua.common.util.MaskUtils;
import com.joshua.common.util.ToastUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.com.mod.office.lightman.MyApplication;
import cn.com.mod.office.lightman.R;
import cn.com.mod.office.lightman.activity.base.BaseActivity;
import cn.com.mod.office.lightman.adapter.ItemAdapter;
import cn.com.mod.office.lightman.adapter.SceneButtonAdapter;
import cn.com.mod.office.lightman.api.ILightMgrApi;
import cn.com.mod.office.lightman.entity.BaseResponse;
import cn.com.mod.office.lightman.entity.FloorDivideInfo;
import cn.com.mod.office.lightman.entity.RoomEntity;
import cn.com.mod.office.lightman.entity.RoomInfo;
import cn.com.mod.office.lightman.entity.SceneInfo;
import cn.com.mod.office.lightman.widget.ClockAddPanel;
import cn.com.mod.office.lightman.widget.HorizontalListView;
import cn.com.mod.office.lightman.widget.ItemListView;
import cn.com.mod.office.lightman.widget.MyLayout;
import cn.com.mod.office.lightman.widget.PowerfulLayout;

/**
 * 楼层界面
 * Created by CAT on 2014/10/24.
 */
public class FloorActivity extends BaseActivity {
    public static final String TAG = "FloorActivity";

    public static final int REQUEST_CODE_CLOCK = 1;

    private ToastUtils mToastUtils;
    private MaskUtils mMaskUtils;
    private ImageView mGoBack;
    private TextView mFloorName;
    private ImageView mMenu,background;
    private ItemListView mList;
    private HorizontalListView mSceneList;
    private SceneButtonAdapter mSceneButtonAdapter;
    private List<RoomEntity> mRooms;
    private SceneInfo mCurrentScene;
    private Set<String> mSelectedRooms = new HashSet<String>();

    private View arrow;

    private String floorId;
    private String floorName;
    private Bitmap mImage;
    private PowerfulLayout pf_floor;
    private MyLayout layout_floor;
    private List<RoomInfo> roomsInfo;
//    private Bitmap unSelectedImg,selectedImg;
    private List<RoomInfo>  selectedRooms;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floor);
        init();
    }

    // 初始化
    public void init() {
        selectedRooms = new ArrayList<>();
        // 初始化系统工具
        mToastUtils = new ToastUtils(this);
        mMaskUtils = new MaskUtils(this);
        // 初始化组件
        mMenu = (ImageView) findViewById(R.id.ic_menu);
//        background = (ImageView) findViewById(R.id.background);
        mFloorName = (TextView) findViewById(R.id.floor_name);
        mList = (ItemListView) findViewById(R.id.menu_list);
        mGoBack = (ImageView) findViewById(R.id.ic_back);
        mSceneList = (HorizontalListView) findViewById(R.id.control_panel);
        pf_floor = (PowerfulLayout) findViewById(R.id.pf_floor);
        layout_floor = (MyLayout) findViewById(R.id.layout_floor);
//        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) layout_floor.getLayoutParams();
//        params.height = params.width;
//        layout_floor.setLayoutParams(params);
        arrow = LayoutInflater.from(this).inflate(R.layout.arrow,null);
        // 默认的4个按钮
        List<SceneInfo> sceneInfos = new ArrayList<SceneInfo>();
        SceneInfo scene1 = new SceneInfo("1", getString(R.string.default_scene1), null, SceneInfo.TYPE_DEFAULT);
        SceneInfo scene2 = new SceneInfo("2", getString(R.string.default_scene2), null, SceneInfo.TYPE_DEFAULT);
        SceneInfo scene3 = new SceneInfo("3", getString(R.string.default_scene3), null, SceneInfo.TYPE_DEFAULT);
        SceneInfo scene4 = new SceneInfo("4", getString(R.string.default_scene4), null, SceneInfo.TYPE_DEFAULT);
        sceneInfos.add(scene1);
        sceneInfos.add(scene2);
        sceneInfos.add(scene3);
        sceneInfos.add(scene4);
        mSceneButtonAdapter = new SceneButtonAdapter(FloorActivity.this, sceneInfos, new SceneButtonAdapter.SceneButtonAdapterListener() {
            @Override
            public void onSceneClick(SceneInfo info) {
                if (selectedRooms.size() == 0) {
                    Log.v(TAG, "apply floor scene floorId-->" + floorId);
                    Toast.makeText(FloorActivity.this,R.string.unselected_rooms_tips,Toast.LENGTH_SHORT).show();
                } else {
                    Log.v(TAG, "apply room scene rooms -->" + mSelectedRooms);
                    MyApplication.getInstance().getClient().applyDefaultScene(info.id, null, mSelectedRooms.toArray(new String[]{}), null, null, new ILightMgrApi.Callback<BaseResponse>() {
                        @Override
                        public void callback(int code, BaseResponse response) {
                            switch (code) {
                                case CODE_SUCCESS:
                                    break;
                            }
                        }
                    });
                }
            }

            @Override
            public void onSceneClockClick(SceneInfo info) {
            }

            @Override
            public void onSceneDeleteClick(SceneInfo info) {

            }

            @Override
            public void onSceneEditClick(SceneInfo info) {

            }
        });
        mSceneList.setAdapter(mSceneButtonAdapter);

        mMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mList.getVisibility() == View.VISIBLE) {
                    mList.setVisibility(View.INVISIBLE);
                } else {
                    mList.setVisibility(View.VISIBLE);
                }
            }
        });

        mGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mList.getVisibility() == View.VISIBLE) {
            mList.setVisibility(View.INVISIBLE);
            return true;
        }
        if (keyCode == KeyEvent.ACTION_DOWN && mList.getVisibility() == View.VISIBLE) {
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
                        for (int i = 0; i < mRooms.size(); i++) {
                            if (mRooms.get(i).id.equals(id)) {
                                mRooms.get(i).hasClock = false;
                                mList.getAdapter().notifyDataSetChanged();
                            }
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void loadData() {
        // 传值的floorId
        floorId = getIntent().getStringExtra("floorId");
        // 传值的floorName
        floorName = getIntent().getStringExtra("floorName");

        if (floorId != null && floorName != null) {
            mFloorName.setText(floorName);
            // 获取楼层图片
            MyApplication.getInstance().getClient().getFloorImg(floorId, new ILightMgrApi.Callback<Bitmap>() {
                @Override
                public void callback(int code, final Bitmap image) {
                    switch (code) {
                        case CODE_TIMEOUT:
                            mToastUtils.show(getString(R.string.tip_timeout));
                            break;
                        case CODE_NETWORK_ERROR:
                            mToastUtils.show(getString(R.string.tip_network_connect_faild));
                            break;
                        case CODE_SUCCESS:
                            // 获取楼层分隔
                            MyApplication.getInstance().getClient().getFloorDivide(floorId, new ILightMgrApi.Callback<FloorDivideInfo>() {
                                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                                @Override
                                public void callback(int code, FloorDivideInfo divideInfo) {
                                    switch (code) {
                                        case CODE_SUCCESS:
                                            layout_floor.setBackground(new BitmapDrawable(image));
//                                            background.setBackground(new BitmapDrawable(image));

                                    }
                                }
                            });
                            break;
                    }
                }
            });

            // 获取楼层区域
            MyApplication.getInstance().getClient().listRooms(floorId, new ILightMgrApi.Callback<List<RoomEntity>>() {
                @Override
                public void callback(int code, List<RoomEntity> roomInfos) {
                    switch (code) {
                        case CODE_SUCCESS:
                            mRooms = roomInfos;
                            //画箭头
                            roomsInfo = new ArrayList<RoomInfo>();
                            for (final RoomEntity room : roomInfos) {
                                // LED图标的大小比例， 600px:30px
                                float rate = 30.0f / 600;
                                ImageView iv = new ImageView(FloorActivity.this);
                                final  RoomInfo info = new RoomInfo();
                                info.setRoomEntity(room);
                                info.setArrowImage(iv);
                                roomsInfo.add(info);
                                iv.setImageResource(R.drawable.room_into);
                                iv.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        info.setSelected(!info.isSelected());
                                        handleRoom(info);
                                    }
                                });
                                iv.setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View v) {
                                        info.setSelected(!info.isSelected());
                                        handleRooms(roomsInfo);
                                        return true;
                                    }
                                });
                                int x = room.getRoom_x();
                                int y = room.getRoom_y();
                                int w = layout_floor.getWidth();
                                int h = layout_floor.getHeight();
                                if (w >= h) {
                                    int size = (int) (h * rate);
                                    int pointX = (int) (h / 600.0 * x) + (w - h) / 2;
                                    int pointY = (int) (h / 600.0 * y);
                                    layout_floor.addView(iv, new AbsoluteLayout.LayoutParams(size, size, pointX, pointY));
                                } else {
                                    int size = (int) (w * rate);
                                    int pointX = (int) (w / 600.0 * x);
                                    int pointY = ((int) (w / 600.0 * y) + (h - w) / 2);
                                    layout_floor.addView(iv, new AbsoluteLayout.LayoutParams(size, size, pointX, pointY));
                                }
                            }
                            ItemAdapter adapter = new ItemAdapter(FloorActivity.this, roomsInfo,null);
                            adapter.setOnRoomClickListener(new ItemAdapter.OnRoomClickListener() {
                                @Override
                                public void onRoomClick(List<RoomInfo> infos) {
                                    handleRooms(infos);
                                }
                            });
                            mList.setAdapter(adapter);
//                            mList.setAdapter(new ItemAdapter(FloorActivity.this, roomsInfo, new ItemAdapter.ItemAdapterListener() {
//                                @Override
//                                public void onClockClick(Item item) {
//                                    Intent intent = new Intent(FloorActivity.this, ClockActivity.class);
//                                    intent.putExtra("way", ClockActivity.WAY_ROOM);
//                                    intent.putExtra("id", item.id);
//                                    intent.putExtra("name", item.name);
//                                    startActivityForResult(intent, REQUEST_CODE_CLOCK);
//                                }
//
//                                @Override
//                                public void onItemSelectedChange(String itemId, boolean isSelected) {
//                                    if (isSelected) {
//                                        mSelectedRooms.add(itemId);
//                                    } else {
//                                        mSelectedRooms.remove(itemId);
//                                    }
//                                    Log.v(TAG, "selectRoom-->" + mSelectedRooms);
//                                }
//                            }));
                            break;
                    }
                }
            });
        }
    }

    private void handleRooms(List<RoomInfo> rooms){
        if(rooms==null||rooms.size()==0){
            for (RoomInfo info:roomsInfo){
                info.getArrowImage().setImageResource(R.drawable.room_into);
            }
            selectedRooms.clear();
            return;
        }
        for (RoomInfo info:rooms){
            info.getArrowImage().setImageResource(R.drawable.ic_cb2);
            if(info.isSelected()){
                info.getArrowImage().setImageResource(R.drawable.ic_cb2_checked);
                selectedRooms.add(info);
            }else{
                if(selectedRooms.contains(info))
                    selectedRooms.remove(info);
                if(selectedRooms.size()==0){
                    for (RoomInfo room:roomsInfo){
                        info.getArrowImage().setImageResource(R.drawable.room_into);
                    }
                }
            }
        }
        mList.getAdapter().notifyDataSetChanged();
    }

    private void handleRoom(RoomInfo info){
        if(info.isSelected()){
            if(selectedRooms.size()>0){
                selectedRooms.add(info);
                info.getArrowImage().setImageResource(R.drawable.ic_cb2_checked);
                mList.getAdapter().notifyDataSetChanged();
            }else{
                //设置房间的选中参数
                for (RoomInfo roomInfo:roomsInfo){
                    roomInfo.setSelected(false);
                }
                selectedRooms.clear();
                //跳转到对应的房间
                Intent intent = new Intent(FloorActivity.this, RoomActivity.class);
                intent.putExtra("roomId", info.getRoomEntity().getRoom_id());
                intent.putExtra("roomName", info.getRoomEntity().getRoom_name());
                startActivity(intent);

            }
        }else{//checked
            if(selectedRooms.contains(info))
                selectedRooms.remove(info);
            if(selectedRooms.size()>0){
                info.getArrowImage().setImageResource(R.drawable.ic_cb2);
            }else{
                for (RoomInfo room:roomsInfo){
                    info.getArrowImage().setImageResource(R.drawable.room_into);
                }
            }
            mList.getAdapter().notifyDataSetChanged();
        }
    }
}
