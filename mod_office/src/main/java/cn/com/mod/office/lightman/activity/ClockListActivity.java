package cn.com.mod.office.lightman.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.joshua.common.util.MaskUtils;
import com.joshua.common.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import cn.com.mod.office.lightman.MyApplication;
import cn.com.mod.office.lightman.R;
import cn.com.mod.office.lightman.activity.base.BaseActivity;
import cn.com.mod.office.lightman.adapter.ClockListAdapter;
import cn.com.mod.office.lightman.api.BaseResp;
import cn.com.mod.office.lightman.api.ILightMgrApi;
import cn.com.mod.office.lightman.api.resp.ClocksResp;
import cn.com.mod.office.lightman.entity.BaseResponse;
import cn.com.mod.office.lightman.entity.Clock;
import cn.com.mod.office.lightman.entity.ClockInfo;
import cn.com.mod.office.lightman.widget.SwipeListView;

/**
 * Created by Administrator on 2016/10/23.
 */
public class ClockListActivity extends BaseActivity implements View.OnClickListener{

    public static final int REQUEST_CODE_EDIT_CLOCK = 101;
    public static final int REQUEST_CODE_ADD_CLOCK = 102;

    private ImageView back,addClock;
    private SwipeListView listview;
    private ClockListAdapter adapter;
    private List<Clock> clocks;
    private MaskUtils maskUtils;

    private String roomId;
    //记录当前修改的闹钟的位置
    private int currentPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acrivity_clocks_list);
        intiView();
        roomId = getIntent().getStringExtra("roomId");
    }

    private void intiView() {
        back = (ImageView) findViewById(R.id.ic_back);
        addClock = (ImageView) findViewById(R.id.ic_menu);
        listview = (SwipeListView) findViewById(R.id.listview);
        maskUtils = new MaskUtils(this);
        clocks = new ArrayList<>();

        back.setOnClickListener(this);
        addClock.setOnClickListener(this);

        listview.setRightViewWidth(150);
        adapter = new ClockListAdapter(this,clocks,listview.getRightViewWidth());
        listview.setAdapter(adapter);
        adapter.setOnClockOperateListener(new ClockListAdapter.OnClockOperateListener() {
            @Override
            public void onClockOpen(String clockId) {
                MyApplication.getInstance().getClient().openClock(roomId, clockId, new ILightMgrApi.Callback<BaseResp>() {
                    @Override
                    public void callback(int code, BaseResp resp) {
                        if(code==0){
                            ToastUtils.show(ClockListActivity.this,R.string.open_clock_success);
                        }else{
                            ToastUtils.show(ClockListActivity.this,resp.getError_desc());
                        }
                    }
                });
            }

            @Override
            public void onClockClosed(String clockId) {
                MyApplication.getInstance().getClient().closeClock(roomId, clockId, new ILightMgrApi.Callback<BaseResp>() {
                    @Override
                    public void callback(int code, BaseResp resp) {
                        if(code==0){
                            ToastUtils.show(ClockListActivity.this,R.string.close_clock_success);
                        }else{
                            ToastUtils.show(ClockListActivity.this,resp.getError_desc());
                        }
                    }
                });
            }

            @Override
            public void onClockDelete(int position, String clockId) {
                MyApplication.getInstance().getClient().deleteClock(roomId, clockId + "", new ILightMgrApi.Callback<BaseResp>() {
                    @Override
                    public void callback(int code, BaseResp resp) {
                        if(code==0){
                            ToastUtils.show(ClockListActivity.this,R.string.delete_clock_success);
                        }else{
                            ToastUtils.show(ClockListActivity.this,resp.getError_desc());
                        }
                    }
                });
            }

            @Override
            public void onItemClick(int position) {
                //跳转到编辑闹钟界面
                Intent intent = new Intent(ClockListActivity.this,ClockSettingActivity.class);
                intent.putExtra("type",2);
                intent.putExtra("roomId",roomId);
                intent.putExtra("clock",clocks.get(position));
                intent.putExtra("mode_id",clocks.get(position).getMode_id());
                startActivityForResult(intent,REQUEST_CODE_EDIT_CLOCK);
            }
        });
    }

    @Override
    public void loadData() {
        if(!TextUtils.isEmpty(roomId)){
            maskUtils.show();
            MyApplication.getInstance().getClient().getRoomClocks(roomId, new ILightMgrApi.Callback<ClocksResp>() {
                @Override
                public void callback(int code, ClocksResp resp) {
                    maskUtils.cancel();
                    if(resp.getStatus()==0){
                        clocks.clear();
                        clocks.addAll(resp.getClocks());
                        adapter.notifyDataSetChanged();
                    }else{
                        ToastUtils.show(ClockListActivity.this,R.string.get_clocks_fail);
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
                //add clock
                //跳转到编辑闹钟界面
                Intent intent = new Intent(ClockListActivity.this,ClockSettingActivity.class);
                intent.putExtra("type",1);
                intent.putExtra("roomId",roomId);
                startActivityForResult(intent,REQUEST_CODE_ADD_CLOCK);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            if(requestCode==REQUEST_CODE_EDIT_CLOCK){//修改闹钟
                loadData();
            }else if(requestCode == REQUEST_CODE_ADD_CLOCK){//添加闹钟
                loadData();
            }
        }
    }
}
