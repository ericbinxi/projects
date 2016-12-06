package cn.com.mod.office.lightman.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.joshua.common.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import cn.com.mod.office.lightman.MyApplication;
import cn.com.mod.office.lightman.R;
import cn.com.mod.office.lightman.activity.base.BaseActivity;
import cn.com.mod.office.lightman.adapter.FloorAdapter;
import cn.com.mod.office.lightman.api.ILightMgrApi;
import cn.com.mod.office.lightman.api.resp.FloorsResp;
import cn.com.mod.office.lightman.entity.FloorInfo;
import cn.com.mod.office.lightman.widget.ViewScroller;

/**
 * 楼层选择
 * Created by CAT on 2014/10/23.
 */
public class FloorSelectorActivity extends BaseActivity implements View.OnClickListener {
    public static final String TAG = "FloorSelectorActivity";

    private ToastUtils mToastUtils;
    private ImageView mUserInfo;
    private TextView mAbout;
    private ViewScroller mFloorScroller;
    private FloorAdapter mFloorAdapter;
    private List<FloorInfo> mFloorInfos;

    private LinearLayout ll_permissons, ll_fault_declare, ll_mod_password, ll_settings;
    private TextView logout,username;
    private SlidingMenu menu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floorselector);
        // configure the SlidingMenu
        menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT);
        // 设置触摸屏幕的模式
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setShadowWidthRes(R.dimen.shadow_width);
//        menu.setShadowDrawable(R.drawable.shadow);

        // 设置滑动菜单视图的宽度
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        // 设置渐入渐出效果的值
        menu.setFadeDegree(0.35f);
        /**
         * SLIDING_WINDOW will include the Title/ActionBar in the content
         * section of the SlidingMenu, while SLIDING_CONTENT does not.
         */
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        //为侧滑菜单设置布局
        menu.setMenu(R.layout.activity_left_menu);
        init();
    }

    // 初始化
    private void init() {
        // 初始化系统工具
        mToastUtils = new ToastUtils(this);
        // 初始化视图组件
        mUserInfo = (ImageView) findViewById(R.id.ic_user_info);
        mAbout = (TextView) findViewById(R.id.ic_about);
        mFloorScroller = (ViewScroller) findViewById(R.id.viewscroller);
        // 初始化事件
        mUserInfo.setOnClickListener(this);
        mAbout.setOnClickListener(this);

        //左菜单栏初始化
        ll_permissons = (LinearLayout) findViewById(R.id.ll_permissons);
        ll_fault_declare = (LinearLayout) findViewById(R.id.ll_fault_declare);
        ll_mod_password = (LinearLayout) findViewById(R.id.ll_mod_password);
        ll_settings = (LinearLayout) findViewById(R.id.ll_settings);
        logout = (TextView) findViewById(R.id.btn_logout);
        username = (TextView) findViewById(R.id.username);

        String name = MyApplication.getInstance().getAppConfig().getUsername();
        username.setText(name);

        ll_permissons.setOnClickListener(this);
        ll_fault_declare.setOnClickListener(this);
        ll_mod_password.setOnClickListener(this);
        ll_settings.setOnClickListener(this);
        logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ic_user_info:
                if(menu==null)return;
                if (!menu.isMenuShowing())
                    menu.showMenu();
                else
                    menu.showContent();
                break;
            case R.id.ic_about:
                Intent intent = new Intent(FloorSelectorActivity.this, AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_permissons:
                break;
            case R.id.ll_fault_declare:
                startActivity(new Intent(FloorSelectorActivity.this,FeedbackActivity.class));
                break;
            case R.id.ll_mod_password:
                startActivity(new Intent(FloorSelectorActivity.this,ModifyPasswordActivity.class));
                break;
            case R.id.ll_settings:
                Intent intent2 = new Intent(FloorSelectorActivity.this, AboutActivity.class);
                startActivity(intent2);
                break;
            case R.id.btn_logout:
                logout();
                break;
        }
    }

    private void logout() {
        MyApplication.getInstance().getClient().logout();
        MyApplication.getInstance().getAppConfig().setAutoLogin(false);
        startActivity(new Intent(this,LoginActivity.class));
        finish();
    }

    @Override
    public void loadData() {
        MyApplication.getInstance().getClient().listFloors(new ILightMgrApi.Callback<FloorsResp>() {
            @Override
            public void callback(int code, FloorsResp resp) {
                switch (code) {
                    case CODE_FAILURE:
                        mToastUtils.show(resp.getError_desc());
                        break;
                    case CODE_TIMEOUT:
                        mToastUtils.show(getString(R.string.tip_timeout));
                        break;
                    case CODE_NETWORK_ERROR:
                        mToastUtils.show(getString(R.string.tip_network_connect_faild));
                        break;
                    case CODE_SUCCESS:
                        mFloorInfos = new ArrayList<FloorInfo>();
                        List<FloorsResp.FloorIdBean> beans = resp.getFloors();
                        for (FloorsResp.FloorIdBean bean:beans){
                            FloorInfo info =  new FloorInfo();
                            info.floorId = bean.getFloor_id();
                            info.name = bean.getFloor_name();
                            mFloorInfos.add(info);
                        }
                        mFloorAdapter = new FloorAdapter(FloorSelectorActivity.this, mFloorInfos, new FloorAdapter.FloorAdapterListener() {
                            @Override
                            public void onFloorClick(FloorInfo floorInfo) {
                                Intent intent = new Intent(FloorSelectorActivity.this, FloorActivity.class);
                                intent.putExtra("floorId", floorInfo.floorId);
                                intent.putExtra("floorName", floorInfo.name);
                                startActivity(intent);
                            }
                        });
                        mFloorScroller.setAdapter(mFloorAdapter);
                        mFloorScroller.setOnItemSelectListener(mFloorAdapter);
                        mFloorAdapter.notifyDataSetChanged();

                        for (int i = 0; i < mFloorInfos.size(); i++) {
                            final FloorInfo floorInfo = mFloorInfos.get(i);
                            MyApplication.getInstance().getClient().getFloorImg(floorInfo.floorId, new ILightMgrApi.Callback<Bitmap>() {
                                @Override
                                public void callback(int code, Bitmap img) {
                                    switch (code) {
                                        case CODE_SUCCESS:
                                            floorInfo.image = img;
                                            mFloorAdapter.notifyDataSetChanged();
                                            break;
                                    }
                                }
                            });
                        }
                        break;
                }
            }
        });
    }
}
