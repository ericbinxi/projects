package cn.com.mod.office.lightman.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import cn.com.mod.office.lightman.MyApplication;
import cn.com.mod.office.lightman.R;
import cn.com.mod.office.lightman.activity.base.BaseActivity;
import cn.com.mod.office.lightman.adapter.RecordListAdapter;
import cn.com.mod.office.lightman.api.BaseResp;
import cn.com.mod.office.lightman.api.ILightMgrApi;
import cn.com.mod.office.lightman.entity.FaultRecord;
import cn.com.mod.office.lightman.entity.FaultRecordResp;
import cn.com.mod.office.lightman.widget.SwipeListView;

public class RecordListActivity extends BaseActivity implements View.OnClickListener {

    private ImageView ivBack;
    private SwipeListView listView;
    private RecordListAdapter adapter;

    private List<FaultRecord> records;
    private String roomId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_list);
        init();
        initListener();
    }

    private void init() {
        roomId = getIntent().getStringExtra("roomId");
        ivBack = (ImageView) findViewById(R.id.ic_back);
        listView = (SwipeListView) findViewById(R.id.listview);
        listView.setRightViewWidth(150);
        records = new ArrayList<>();
        adapter = new RecordListAdapter(this, listView.getRightViewWidth(), records);
        listView.setAdapter(adapter);
    }

    private void initListener() {
        ivBack.setOnClickListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(RecordListActivity.this,FaultContentActivity.class);
                intent.putExtra("roomId",roomId);
                intent.putExtra("faultRecord",records.get(position));
                startActivity(intent);
            }
        });
        adapter.setOnRecordOperateListener(new RecordListAdapter.OnRecordOperateListener() {
            @Override
            public void delete(int position, FaultRecord record) {
                deleteRecord(record);
            }
        });
    }

    private void deleteRecord(final FaultRecord record) {
        MyApplication.getInstance().getClient().deleteFaultRecord(record.getMsg_title(), new ILightMgrApi.Callback<BaseResp>() {
            @Override
            public void callback(int code, BaseResp entity) {
                if (code == 0) {
                    if (records.contains(record)) {
                        records.remove(record);
                        adapter.setRecords(records);
                    }
                }
            }
        });
    }

    @Override
    public void loadData() {
        MyApplication.getInstance().getClient().getFaultRecords(new ILightMgrApi.Callback<FaultRecordResp>() {
            @Override
            public void callback(int code, FaultRecordResp resp) {
                if (code == 0 && resp != null) {
                    records.addAll(resp.getFault_record());
                    adapter.setRecords(records);
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
        }
    }
}
