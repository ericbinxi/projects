package cn.com.mod.office.lightman.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.joshua.common.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import cn.com.mod.office.lightman.MyApplication;
import cn.com.mod.office.lightman.R;
import cn.com.mod.office.lightman.activity.base.BaseActivity;
import cn.com.mod.office.lightman.adapter.ParamsListAdapter;
import cn.com.mod.office.lightman.api.BaseResp;
import cn.com.mod.office.lightman.api.ILightMgrApi;
import cn.com.mod.office.lightman.entity.GetParamResp;
import cn.com.mod.office.lightman.entity.ParamEntity;
import cn.com.mod.office.lightman.widget.SwipeListView;

/**
 * 关于界面
 * Created by CAT on 2014/10/25.
 */
public class ParamsListActivity extends BaseActivity {
    public static final String TAG = ParamsListActivity.class.getSimpleName();

    private ImageView back;
    private SwipeListView listView;
    private ParamsListAdapter adapter;
    private List<ParamEntity> paramEntities;

    private String[] leds;
    private boolean adjust = false;
    private String roomId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paramslist);
        back = (ImageView) findViewById(R.id.ic_back);
        listView = (SwipeListView) findViewById(R.id.listview);
        listView.setRightViewWidth(150);
        paramEntities = new ArrayList<>();
        adapter = new ParamsListAdapter(this,listView.getRightViewWidth(),paramEntities);
        listView.setAdapter(adapter);
        if(getIntent().hasExtra("adjust")){
            adjust = getIntent().getBooleanExtra("adjust",false);
        }
        if(getIntent().hasExtra("leds")){
            leds = getIntent().getStringArrayExtra("leds");
        }
        if(getIntent().hasExtra("roomId")){
            roomId = getIntent().getStringExtra("roomId");
        }
        initListener();
    }

    private void initListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        adapter.setOnBackupParamOperateListener(new ParamsListAdapter.OnBackupParamOperateListener() {
            @Override
            public void deleteParam(int position) {
                delete(paramEntities.get(position).getParam_name(),position);
            }

            @Override
            public void applyParam(int position) {
                apply(position);
                adapter.setCheckedPosition(position);
            }
        });
    }

    private void apply(int position) {
        if(adjust&&leds!=null&&leds.length>0&&!TextUtils.isEmpty(roomId)){
            ParamEntity entity = paramEntities.get(position);
            MyApplication.getInstance().getClient().setBrightness(roomId, leds, entity.getBrightness(), new ILightMgrApi.Callback<BaseResp>() {
                @Override
                public void callback(int code, BaseResp entity) {
                    if(code==0){
                        ToastUtils.show(ParamsListActivity.this,"设置亮度成功");
                    }else{
                        ToastUtils.show(ParamsListActivity.this,entity.getError_desc());
                    }
                }
            });
            int temp = entity.getColorTemp();
            if(entity.getColorTemp()>255)
                temp = (int)((entity.getColorTemp() - 2700) / 3800.0f * 255);
            MyApplication.getInstance().getClient().setColorTemp(roomId, leds, temp, new ILightMgrApi.Callback<BaseResp>() {
                @Override
                public void callback(int code, BaseResp entity) {
                    if(code==0){
                        ToastUtils.show(ParamsListActivity.this,"设置色温成功");
                    }else{
                        ToastUtils.show(ParamsListActivity.this,entity.getError_desc());
                    }
                }
            });
            String color = entity.getColorRgbValue();
            if(color.length()!=6)return;
            int red = Integer.parseInt(color.substring(0,2),16);
            int green = Integer.parseInt(color.substring(2,4),16);
            int blue = Integer.parseInt(color.substring(4),16);
            MyApplication.getInstance().getClient().setRGB(roomId, leds, red,green,blue, new ILightMgrApi.Callback<BaseResp>() {
                @Override
                public void callback(int code, BaseResp entity) {
                    if(code==0){
                        ToastUtils.show(ParamsListActivity.this,"设置颜色成功");
                    }else{
                        ToastUtils.show(ParamsListActivity.this,entity.getError_desc());
                    }
                }
            });
            return;
        }

        //应用参数
        Intent intent = new Intent();
        intent.putExtra("param",paramEntities.get(position));
        setResult(RESULT_OK,intent);
        finish();
    }

    private void delete(String param_name, final int position) {
        if(!TextUtils.isEmpty(param_name)){
            MyApplication.getInstance().getClient().deleteBackupParam(param_name, new ILightMgrApi.Callback<BaseResp>() {
                @Override
                public void callback(int code, BaseResp entity) {
                    if(code==0){
                        paramEntities.remove(position);
                        adapter.setDatas(paramEntities);
                    }else{
                        ToastUtils.show(ParamsListActivity.this,entity.getError_desc());
                    }
                }
            });
        }
    }

    @Override
    public void loadData() {
        MyApplication.getInstance().getClient().getBackupParam(new ILightMgrApi.Callback<GetParamResp>() {
            @Override
            public void callback(int code, GetParamResp resp) {
                if(code==0){
                    if(resp.getParams()!=null){
                        paramEntities.addAll(resp.getParams());
                        adapter.setDatas(paramEntities);
                    }
                }else{
                    ToastUtils.show(ParamsListActivity.this,resp.getError_desc());
                }
            }
        });
    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.ic_back:
//                //应用参数
//                Intent intent = new Intent();
//                intent.putExtra("param",paramEntities.get(position));
//                setResult(RESULT_OK,intent);
//                finish();
//                break;
//        }
//    }
//
//    @Override
//    public void onBackPressed() {
//        //应用参数
//        Intent intent = new Intent();
//        intent.putExtra("param",paramEntities.get(position));
//        setResult(RESULT_OK,intent);
//        super.onBackPressed();
//    }
}
