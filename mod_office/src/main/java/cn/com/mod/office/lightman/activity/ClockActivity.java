//package cn.com.mod.office.lightman.activity;
//
//import android.app.AlertDialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.KeyEvent;
//import android.view.View;
//import android.widget.ExpandableListView;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.joshua.common.util.MaskUtils;
//import com.joshua.common.util.ToastUtils;
//
//import java.util.List;
//
//import cn.com.mod.office.lightman.MyApplication;
//import cn.com.mod.office.lightman.R;
//import cn.com.mod.office.lightman.activity.base.BaseActivity;
//import cn.com.mod.office.lightman.adapter.ClockExpandableListAdapter;
//import cn.com.mod.office.lightman.api.ILightMgrApi;
//import cn.com.mod.office.lightman.entity.BaseResponse;
//import cn.com.mod.office.lightman.entity.ClockInfo;
//
///**
// * 闹钟列表界面
// * Created by CAT on 2014/11/10.
// */
//public class ClockActivity extends BaseActivity {
//    public static final String TAG = ClockActivity.class.getSimpleName();
//
//    public static final int WAY_ROOM = 0;
//    public static final int WAY_GROUP = 1;
//
//    private MaskUtils mMaskUtils;
//    private ToastUtils mToastUtils;
//    private TextView mZoomName;
//    private ImageView mGoBack;
//    private ExpandableListView mClockList;
//    private ClockExpandableListAdapter mAdapter;
//    private List<ClockInfo> mClockInfos;
//
//    private String id;
//    private String name;
//
//    // 事件监听
//    private ClockExpandableListAdapter.ClockExpandableListAdapterListener adapterListener = new ClockExpandableListAdapter.ClockExpandableListAdapterListener() {
//        @Override
//        public void onEditClick(int groupPosition) {
//            if (mClockList.isGroupExpanded(groupPosition)) {
//                mClockList.collapseGroup(groupPosition);
//            } else {
//                for (int i = 0; i < mAdapter.getGroupCount(); i++) {
//                    if (mClockList.isGroupExpanded(i))
//                        mClockList.collapseGroup(i);
//                }
//                mClockList.expandGroup(groupPosition);
//            }
//        }
//
//        @Override
//        public void onClockSwitchChange(final ClockInfo clockInfo, boolean isOpen) {
//            mMaskUtils.show();
//            if (isOpen) {
//                MyApplication.getInstance().getClient().openClock("",clockInfo.clockId, new ILightMgrApi.Callback<BaseResponse>() {
//                    @Override
//                    public void callback(int code, BaseResponse response) {
//                        mMaskUtils.cancel();
//                        switch (code) {
//                            case CODE_SUCCESS:
//                                if (!response.success) {
//                                    mToastUtils.show(response.msg);
//                                }
//                                clockInfo.status = response.success ? true : false;
//                                break;
//                        }
//                    }
//                });
//            } else {
//                MyApplication.getInstance().getClient().closeClock(clockInfo.clockId, new ILightMgrApi.Callback<BaseResponse>() {
//                    @Override
//                    public void callback(int code, BaseResponse response) {
//                        mMaskUtils.cancel();
//                        switch (code) {
//                            case CODE_SUCCESS:
//                                if (!response.success) {
//                                    mToastUtils.show(response.msg);
//                                }
//                                clockInfo.status = response.success ? false : true;
//                                break;
//                        }
//                    }
//                });
//            }
//        }
//
//        @Override
//        public void onClockDelete(final ClockInfo clockInfo) {
//            new AlertDialog.Builder(ClockActivity.this)
//                    .setTitle(getString(R.string.delete_clock_dialog_title))
//                    .setMessage(getString(R.string.delete_clock_dialog_msg, clockInfo.sceneName))
//                    .setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            mMaskUtils.show();
//                            MyApplication.getInstance().getClient().deleteClock(clockInfo.clockId, new ILightMgrApi.Callback<BaseResponse>() {
//                                @Override
//                                public void callback(int code, BaseResponse entity) {
//                                    mMaskUtils.cancel();
//                                    switch (code) {
//                                        case CODE_SUCCESS:
//                                            if (entity.success) {
//                                                mClockInfos.remove(clockInfo);
//                                                mAdapter.notifyDataSetChanged();
//                                                for (int i = 0; i < mAdapter.getGroupCount(); i++) {
//                                                    if (mClockList.isGroupExpanded(i)) {
//                                                        mClockList.collapseGroup(i);
//                                                    }
//                                                }
//                                            } else {
//                                                mToastUtils.show(entity.msg);
//                                            }
//                                            break;
//                                    }
//                                }
//                            });
//                        }
//                    })
//                    .setNegativeButton(getString(R.string.cancel), null).show();
//        }
//
//        @Override
//        public void onSubmit(final ClockInfo clockInfo, final String week, final String time) {
//            mMaskUtils.show();
//            MyApplication.getInstance().getClient().updateClock(clockInfo.clockId, week, time, new ILightMgrApi.Callback<BaseResponse>() {
//                @Override
//                public void callback(int code, BaseResponse entity) {
//                    mMaskUtils.cancel();
//                    switch (code) {
//                        case CODE_TIMEOUT:
//                            mToastUtils.show(getString(R.string.tip_timeout));
//                            break;
//                        case CODE_NETWORK_ERROR:
//                            mToastUtils.show(getString(R.string.tip_network_connect_faild));
//                            break;
//                        case CODE_SUCCESS:
//                            mToastUtils.show(entity.msg);
//                            if (entity.success) {
//                                clockInfo.time = time;
//                                clockInfo.week = week;
//                                mAdapter.notifyDataSetChanged();
//                            }
//                            break;
//                    }
//                }
//            });
//        }
//    };
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_clock);
//
//        mMaskUtils = new MaskUtils(this);
//        mToastUtils = new ToastUtils(this);
//
//        mZoomName = (TextView) findViewById(R.id.zoom_name);
//        mGoBack = (ImageView) findViewById(R.id.ic_back);
//        mClockList = (ExpandableListView) findViewById(R.id.expand);
//    }
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        switch (keyCode) {
//            case KeyEvent.KEYCODE_BACK:
//                if (mClockInfos.size() == 0) {
//                    Intent intent = new Intent();
//                    intent.putExtra("id", id);
//                    setResult(RESULT_OK, intent);
//                }
//                break;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
//
//    @Override
//    public void loadData() {
//        id = getIntent().getStringExtra("id");
//        name = getIntent().getStringExtra("name");
//
//        if (id != null && name != null) {
//            mGoBack.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (mClockInfos != null && mClockInfos.size() == 0) {
//                        Intent intent = new Intent();
//                        intent.putExtra("id", id);
//                        setResult(RESULT_OK, intent);
//                    }
//                    finish();
//                }
//            });
//
//            mZoomName.setText(name);
//            switch (getIntent().getIntExtra("way", -1)) {
//                case WAY_ROOM:
//                    MyApplication.getInstance().getClient().getRoomClocks(id, new ILightMgrApi.Callback<List<ClockInfo>>() {
//                        @Override
//                        public void callback(int code, List<ClockInfo> clockInfos) {
//                            switch (code) {
//                                case CODE_SUCCESS:
//                                    mClockInfos = clockInfos;
//                                    mAdapter = new ClockExpandableListAdapter(ClockActivity.this, mClockInfos, adapterListener);
//                                    mClockList.setAdapter(mAdapter);
//                                    break;
//                            }
//                        }
//                    });
//                    break;
//                case WAY_GROUP:
//                    MyApplication.getInstance().getClient().getGroupClocks(id, new ILightMgrApi.Callback<List<ClockInfo>>() {
//                        @Override
//                        public void callback(int code, List<ClockInfo> clockInfos) {
//                            switch (code) {
//                                case CODE_SUCCESS:
//                                    mClockInfos = clockInfos;
//                                    mAdapter = new ClockExpandableListAdapter(ClockActivity.this, mClockInfos, adapterListener);
//                                    mClockList.setAdapter(mAdapter);
//                                    break;
//                            }
//                        }
//                    });
//                    break;
//            }
//        }
//    }
//}
