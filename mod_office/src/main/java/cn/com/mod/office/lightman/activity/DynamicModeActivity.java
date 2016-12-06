package cn.com.mod.office.lightman.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.joshua.common.util.MaskUtils;
import com.joshua.common.util.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.mod.office.lightman.MyApplication;
import cn.com.mod.office.lightman.R;
import cn.com.mod.office.lightman.activity.base.BaseActivity;
import cn.com.mod.office.lightman.adapter.DynamicModeAdapter;
import cn.com.mod.office.lightman.api.BaseResp;
import cn.com.mod.office.lightman.api.ILightMgrApi;
import cn.com.mod.office.lightman.api.resp.GetModesResp;
import cn.com.mod.office.lightman.entity.DynamicMode;
import cn.com.mod.office.lightman.entity.Frame;
import cn.com.mod.office.lightman.entity.LampParam;
import cn.com.mod.office.lightman.entity.Lamps;
import cn.com.mod.office.lightman.utils.PopWindowUtils;
import cn.com.mod.office.lightman.widget.timepicker.adapter.ArrayWheelAdapter;
import cn.com.mod.office.lightman.widget.timepicker.widget.TimePickerDialog;
import cn.com.mod.office.lightman.widget.timepicker.widget.WheelView;

public class DynamicModeActivity extends BaseActivity implements View.OnClickListener {

    private static final int REQUEST_EDIT_PARAMS = 101;
    private static final int REQUEST_ADD_FRAME = 102;

    private ImageView back;
    private TextView title, save, apply;
    private EditText et_modename;
    private ListView listView;
    private DynamicModeAdapter adapter;
    private RelativeLayout bottom;

    private List<Frame> frames;
    private Map<String, List<String>> frameIds = new HashMap<>();

    private String roomId;
    private View addView;
    private ImageView add;
    private TimePickerDialog dialog;

    private int type = 0;//1创建  2编辑
    private String mode_id;

    private int lamp_brightness, lamp_colorTemp, lamp_h_degree, lamp_v_degree, lamp_l_degree;
    private String lamp_rgb;//灯的色温  16进制表示
    private ArrayList<String> selectedLampIds;

    private int currentposiont = -1;

    private DynamicMode mode;
    private MaskUtils maskUtils;
    private PopWindowUtils pop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_mode);
        initView();
    }

    private void initView() {
        back = (ImageView) findViewById(R.id.ic_back);
        title = (TextView) findViewById(R.id.title);
        apply = (TextView) findViewById(R.id.apply);
        et_modename = (EditText) findViewById(R.id.et_modename);
        save = (TextView) findViewById(R.id.ic_menu);
        listView = (ListView) findViewById(R.id.listview);
        addView = LayoutInflater.from(this).inflate(R.layout.item_add_mode, null);
        add = (ImageView) addView.findViewById(R.id.add_mode);
        listView.addFooterView(addView);
        bottom = (RelativeLayout) findViewById(R.id.bottom);

        back.setOnClickListener(this);
        save.setOnClickListener(this);
        bottom.setOnClickListener(this);
        add.setOnClickListener(this);
        maskUtils = new MaskUtils(this);

        type = getIntent().getIntExtra("mode_type", 1);
        roomId = getIntent().getStringExtra("roomId");

        if (type == 1) {
            title.setText(R.string.create_dynamic_mode);
            frames = new ArrayList<>();
            bottom.setEnabled(false);
            bottom.setClickable(false);

            Frame frame = new Frame();
            frame.setFrame_id((frames.size() + 1) + "");
            frame.setIs_smooth("0");
            frame.setHour("00");
            frame.setMinute("00");
            frame.setSecond("00");
            frames.add(frame);

        } else if (type == 2) {
            mode = (DynamicMode) getIntent().getSerializableExtra("dynamicMode");
            mode_id = getIntent().getStringExtra("mode_id");
            title.setText(R.string.edit_dynamic_mode);
            frames = mode.getFrames();
            et_modename.setText(mode.getMode_name());
        }
        adapter = new DynamicModeAdapter(this, frames);
        listView.setAdapter(adapter);
        adapter.setOnFrameOperateListener(new DynamicModeAdapter.OnFrameOperateListener() {

            @Override
            public void editFrame(int position) {
                currentposiont = position;
                Intent intent = new Intent(DynamicModeActivity.this, FrameActivity.class);
                intent.putExtra("index", position + 1);
                intent.putExtra("roomId", roomId);
                intent.putExtra("type", 2);
                startActivityForResult(intent, REQUEST_EDIT_PARAMS);

            }

            @Override
            public void deleteFrame(final int position) {
                if (type == 2) {
                    if (mode != null) {
                        String modeId = mode.getMode_id();
                        Frame frame = frames.get(position);
                        final String framId = frame.getFrame_id();
                        if (mode.getFrames() != null && mode.getFrames().size() > 0) {
                            for (Frame frame1 : mode.getFrames()) {
                                if (frame1.getFrame_id().equals(frame1.getFrame_id())) {
                                    AlertDialog dialog = new AlertDialog.Builder(DynamicModeActivity.this).
                                            setCancelable(false).setMessage("确定删除第" + (position + 1) + "帧？").
                                            setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    maskUtils.show();
                                                    MyApplication.getInstance().getClient().deleteFrame(framId, roomId, new ILightMgrApi.Callback<BaseResp>() {
                                                        @Override
                                                        public void callback(int code, BaseResp entity) {
                                                            maskUtils.cancel();
                                                            if (code == 0) {
                                                                frames.remove(position);
                                                                adapter.setDatas(frames);
                                                            } else {
                                                                ToastUtils.show(DynamicModeActivity.this, entity.getError_desc());
                                                            }
                                                        }
                                                    });
                                                    dialog.dismiss();
                                                }
                                            }).
                                            setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            }).create();
                                    dialog.show();

                                    return;
                                }
                            }
                        }
                    }

                }
                frames.remove(position);
                adapter.setDatas(frames);

            }

            @Override
            public void editTime(int position) {
                String preTime = "00:00:00";
                if (position > 0) {
                    Frame f = frames.get(position - 1);
                    preTime = f.getHour() + ":" + f.getMinute() + ":" + f.getSecond();
                }
                getFrameShowTime(position, preTime);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (frames != null) {
                    Frame frame = new Frame();
                    frame.setFrame_id((frames.size() + 1) + "");
                    frame.setIs_smooth("0");
                    frame.setHour("00");
                    frame.setMinute("00");
                    frame.setSecond("00");
                    frames.add(frame);
                    adapter.setDatas(frames);
                }
                ToastUtils.show(DynamicModeActivity.this, "请编辑时间和灯的参数");

            }
        });
        hideSoftKeyboard();
    }

    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private int curPosition = -1;

    private void getFrameShowTime(int position, String preTime) {
        curPosition = position;
        if (dialog == null) {
            dialog = new TimePickerDialog(DynamicModeActivity.this, new TimePickerDialog.OnSelectTimeListener() {
                @Override
                public void onSure(String time) {
                    Frame f = frames.get(curPosition);
                    String[] times = time.split(":");
                    f.setHour(times[0]);
                    f.setMinute(times[1]);
                    f.setSecond(times[2]);
                    adapter.setDatas(frames);
                    dialog.dismiss();
                    curPosition = -1;
                }
            });
        }
        dialog.show(preTime);

    }


    @Override
    public void loadData() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ic_back:
                finish();
                break;
            case R.id.ic_menu:
                createDynamicMode();
                break;
            case R.id.bottom:
                //预览
                apply();
                break;
        }
    }

    private boolean isPalyingMode = false;

    private void apply() {
        if (type == 2) {
            maskUtils.show();
            if (!isPalyingMode) {
                MyApplication.getInstance().getClient().playDynamicMode(roomId, mode_id, new String[]{}, new ILightMgrApi.Callback<BaseResp>() {
                    @Override
                    public void callback(int code, BaseResp entity) {
                        maskUtils.cancel();
                        if (code == 0) {
                            isPalyingMode = true;
                            apply.setText(R.string.stop_apply_dynamic);
                            ToastUtils.show(DynamicModeActivity.this, entity.getError_desc());
                        } else {
                            ToastUtils.show(DynamicModeActivity.this, entity.getError_desc());
                        }
                    }
                });
            } else {
                MyApplication.getInstance().getClient().stopDynamicMode(roomId, mode_id, new ILightMgrApi.Callback<BaseResp>() {
                    @Override
                    public void callback(int code, BaseResp entity) {
                        maskUtils.cancel();
                        if (code == 0) {
                            isPalyingMode = false;
                            apply.setText(R.string.apply_dynamic);
                            ToastUtils.show(DynamicModeActivity.this, entity.getError_desc());
                        } else {
                            ToastUtils.show(DynamicModeActivity.this, entity.getError_desc());
                        }
                    }
                });
            }

        }
    }

    private void createDynamicMode() {
        String modeName = et_modename.getText().toString().trim();
        if (TextUtils.isEmpty(modeName)) {
            ToastUtils.show(this, R.string.et_mode_name);
            return;
        }

        MyApplication.getInstance().getClient().saveDynamicMode(roomId, mode_id, modeName, frames, new ILightMgrApi.Callback<BaseResp>() {
            @Override
            public void callback(int code, BaseResp entity) {
                //do something
                if (code == 0) {
                    if (type == 1) {
                        ToastUtils.show(DynamicModeActivity.this, "模式添加成功");
                    } else {
                        ToastUtils.show(DynamicModeActivity.this, "模式修改成功");
                    }
                    finish();
                } else {
                    ToastUtils.show(DynamicModeActivity.this, entity.getError_desc());
                }

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_EDIT_PARAMS) {
            //处理灯的颜色 亮度等
            if (data != null) {
                if (currentposiont >= 0) {
                    ArrayList<LampParam> params = (ArrayList<LampParam>) data.getSerializableExtra("lampParams");
                    Frame frame = frames.get(currentposiont);
                    frame.setLamps(params);
                    currentposiont = -1;
                }

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}
