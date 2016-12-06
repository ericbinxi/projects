package cn.com.mod.office.lightman.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.joshua.common.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import cn.com.mod.office.lightman.MyApplication;
import cn.com.mod.office.lightman.R;
import cn.com.mod.office.lightman.activity.base.BaseActivity;
import cn.com.mod.office.lightman.adapter.ModeManagerAdapter;
import cn.com.mod.office.lightman.api.BaseResp;
import cn.com.mod.office.lightman.api.ILightMgrApi;
import cn.com.mod.office.lightman.api.resp.GetModesResp;
import cn.com.mod.office.lightman.entity.DynamicMode;
import cn.com.mod.office.lightman.entity.Frame;
import cn.com.mod.office.lightman.entity.NormalMode;
import cn.com.mod.office.lightman.entity.base.BaseModeEntity;
import cn.com.mod.office.lightman.widget.SwipeListView;

public class ModeManagerActivity extends BaseActivity implements View.OnClickListener {

    private ImageView back, add;
    private SwipeListView listView;
    private ModeManagerAdapter adapter;
    private String roomId;
    private LinearLayout ll_menu, normal_mode, dynamic_mode;

    private List<BaseModeEntity> modeEntities;
    private List<DynamicMode> dynamicModes;
    private List<NormalMode> normalModes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode_manager);

        roomId = getIntent().getStringExtra("roomId");

        back = (ImageView) findViewById(R.id.ic_back);
        add = (ImageView) findViewById(R.id.ic_menu);
        ll_menu = (LinearLayout) findViewById(R.id.ll_menu);
        normal_mode = (LinearLayout) findViewById(R.id.normal_mode);
        dynamic_mode = (LinearLayout) findViewById(R.id.dynamic_mode);
        normal_mode.setOnClickListener(this);
        dynamic_mode.setOnClickListener(this);
        listView = (SwipeListView) findViewById(R.id.listview);
        listView.setRightViewWidth(150);
        modeEntities = new ArrayList<>();
        adapter = new ModeManagerAdapter(this, listView.getRightViewWidth(), modeEntities);
        listView.setAdapter(adapter);
        adapter.setListener(new ModeManagerAdapter.OnModeOperaterListener() {
            @Override
            public void editMode(int position) {
                BaseModeEntity mode = modeEntities.get(position);
                if(mode.getModeType()==1){
                    Intent intent = new Intent(ModeManagerActivity.this, NormalModeActivity.class);
                    intent.putExtra("mode_type", 2);//1：创建  2：编辑模式
                    intent.putExtra("mode_id", mode.getMode_id());
                    intent.putExtra("roomId", roomId);
                    intent.putExtra("normalMode",getNormalMode(position));
                    startActivity(intent);
                }else if(mode.getModeType()==2){
                    Intent intent = new Intent(ModeManagerActivity.this, DynamicModeActivity.class);
                    intent.putExtra("mode_type", 2);//1：创建  2：编辑模式
                    intent.putExtra("mode_id", mode.getMode_id());
                    intent.putExtra("roomId", roomId);
                    intent.putExtra("dynamicMode", getDynamicMode(position));
                    startActivity(intent);
                }
            }

            @Override
            public void deleteMode(int position) {
                BaseModeEntity mode = modeEntities.get(position);
                int mode_id = Integer.parseInt(mode.getMode_id());
                if(mode_id<=4&&mode_id>0){
                    return;
                }
                deleteDiyMode(mode.getMode_id(), position);
            }
        });

        back.setOnClickListener(this);
        add.setOnClickListener(this);

    }

    private NormalMode getNormalMode(int position) {
        NormalMode normalMode = null;
        BaseModeEntity entity = modeEntities.get(position);
        for (NormalMode mode:normalModes){
            if (entity.getMode_id()==mode.getMode_id()){
                normalMode = mode;
                break;
            }
        }
        return normalMode;
    }

    private DynamicMode getDynamicMode(int position){
        DynamicMode dynamicMode = null;
        BaseModeEntity entity = modeEntities.get(position);
        for (DynamicMode mode:dynamicModes){
            if (entity.getMode_id()==mode.getMode_id()){
                dynamicMode = mode;
                break;
            }
        }
        return dynamicMode;
    }

    private void deleteDiyMode(String mode_id, final int position) {
        MyApplication.getInstance().getClient().deleteDiyScene(roomId, mode_id, new ILightMgrApi.Callback<BaseResp>() {
            @Override
            public void callback(int code, BaseResp resp) {
                if (code == 0) {
                    ToastUtils.show(ModeManagerActivity.this, R.string.delete_mode_success);
                    modeEntities.remove(position);
                    adapter.setModes(modeEntities);
                } else {
                    ToastUtils.show(ModeManagerActivity.this, resp.getError_desc());
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ic_back:
                finish();
                break;
            case R.id.ic_menu:
                //添加模式
                if (ll_menu.isShown()) {
                    ll_menu.setVisibility(View.GONE);
                } else {
                    ll_menu.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.normal_mode:
                ll_menu.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(this, NormalModeActivity.class);
                intent.putExtra("mode_type", 1);//1：创建  2：编辑模式
                intent.putExtra("mode_id", 0);
                intent.putExtra("roomId", roomId);
                startActivity(intent);
                break;
            case R.id.dynamic_mode:
                ll_menu.setVisibility(View.INVISIBLE);
                Intent intent1 = new Intent(this, DynamicModeActivity.class);
                intent1.putExtra("mode_type", 1);//1：创建  2：编辑模式
                intent1.putExtra("roomId", roomId);
                startActivity(intent1);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && ll_menu.getVisibility() == View.VISIBLE) {
            ll_menu.setVisibility(View.INVISIBLE);
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            if (ll_menu.getVisibility() == View.VISIBLE) {
                ll_menu.setVisibility(View.INVISIBLE);
            } else {
                ll_menu.setVisibility(View.VISIBLE);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (ll_menu.getVisibility() == View.VISIBLE) {
            ll_menu.setVisibility(View.INVISIBLE);
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onResume() {
        if(hasLoadData){
            loadData();
        }
        super.onResume();
    }

    @Override
    public void loadData() {
        if (!TextUtils.isEmpty(roomId)) {
            MyApplication.getInstance().getClient().getModes(roomId, new ILightMgrApi.Callback<GetModesResp>() {
                @Override
                public void callback(int code, GetModesResp resp) {
                    if (code == 0) {
                        modeEntities.clear();
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
                        adapter.setModes(modeEntities);
                    } else {
                        ToastUtils.show(ModeManagerActivity.this, resp.getError_desc());
                    }
                }
            });
        }
    }
}
