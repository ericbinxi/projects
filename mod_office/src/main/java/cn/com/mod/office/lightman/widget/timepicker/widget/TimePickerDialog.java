package cn.com.mod.office.lightman.widget.timepicker.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import cn.com.mod.office.lightman.R;
import cn.com.mod.office.lightman.widget.timepicker.adapter.ArrayWheelAdapter;

/**
 * Created by Administrator on 2016/10/23.
 */
public class TimePickerDialog extends Dialog {

    private Context context;
    private String preTime;
    private WheelView wheelHour, wheelMin, wheelSec;
    private TextView confirm;
    private OnSelectTimeListener listener;

    public TimePickerDialog(Context context, OnSelectTimeListener listener) {
        this(context, R.style.dialog_tran);
        this.context = context;
        this.listener = listener;
        initView();
    }

    public TimePickerDialog(Context context, int theme) {
        super(context, theme);
    }

    protected TimePickerDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public void setPreTime(String preTime) {
        this.preTime = preTime;
    }

    private void initView() {
        View root = LayoutInflater.from(context).inflate(R.layout.dialog_time_picker, null);
        wheelHour = (WheelView) root.findViewById(R.id.wheel_hour);
        wheelMin = (WheelView) root.findViewById(R.id.wheel_min);
        wheelSec = (WheelView) root.findViewById(R.id.wheel_second);
        confirm = (TextView) root.findViewById(R.id.btn_confirm);
        initWheelView();

//        Window window = getWindow();
//        int width = window.getWindowManager().getDefaultDisplay().getWidth();
//        int height = window.getWindowManager().getDefaultDisplay().getHeight();
//        WindowManager.LayoutParams params = window.getAttributes();
//        params.width = width*4/5;
//        params.height = height*2/3;
//        params.alpha = 0.8f;
//        params.gravity = Gravity.CENTER;
//        window.setAttributes(params);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate() && listener != null) {
                    String time = (String) wheelHour.getSelectionItem() + ":" + (String) wheelMin.getSelectionItem() + ":" + (String) wheelSec.getSelectionItem();
                    listener.onSure(time);
                } else {
                    Toast.makeText(context, R.string.time_picker_tips, Toast.LENGTH_SHORT).show();
                }
            }
        });
        setContentView(root);
    }

    private void initWheelView() {
        WheelView.WheelViewStyle style = new WheelView.WheelViewStyle();
        style.selectedTextColor = Color.parseColor("#0288ce");
        style.textColor = Color.GRAY;
        style.selectedTextSize = 20;

        wheelHour.setWheelSize(5);
        wheelHour.setLoop(true);
        wheelHour.setWheelAdapter(new ArrayWheelAdapter(context));
        wheelHour.setSkin(WheelView.Skin.Holo);
        wheelHour.setWheelData(createHours());
        wheelHour.setStyle(style);

        wheelMin.setWheelSize(5);
        wheelMin.setLoop(true);
        wheelMin.setWheelAdapter(new ArrayWheelAdapter(context));
        wheelMin.setSkin(WheelView.Skin.Holo);
        wheelMin.setWheelData(createMinutes());
        wheelMin.setStyle(style);

        wheelSec.setWheelSize(5);
        wheelSec.setLoop(true);
        wheelSec.setWheelAdapter(new ArrayWheelAdapter(context));
        wheelSec.setSkin(WheelView.Skin.Holo);
        wheelSec.setWheelData(createMinutes());
        wheelSec.setStyle(style);


    }

    private ArrayList<String> createHours() {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < 24; i++) {
            if (i < 10) {
                list.add("0" + i);
            } else {
                list.add("" + i);
            }
        }
        return list;
    }

    private ArrayList<String> createMinutes() {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < 60; i++) {
            if (i < 10) {
                list.add("0" + i);
            } else {
                list.add("" + i);
            }
        }
        return list;
    }

    public void show(String preTime) {
        this.preTime = preTime;
        int hour = Integer.parseInt(preTime.split(":")[0]);
        int min = Integer.parseInt(preTime.split(":")[1]);
        int sec = Integer.parseInt(preTime.split(":")[2]);
        wheelHour.setSelection(hour);
        wheelMin.setSelection(min);
        wheelSec.setSelection(sec);
        show();
    }

    private boolean validate() {
        if (!TextUtils.isEmpty(preTime)) {
            int hour = Integer.parseInt(preTime.split(":")[0]);
            int min = Integer.parseInt(preTime.split(":")[1]);
            int sec = Integer.parseInt(preTime.split(":")[2]);

            int currentHour = Integer.parseInt((String) wheelHour.getSelectionItem());
            int currentMin = Integer.parseInt((String) wheelMin.getSelectionItem());
            int currentSec = Integer.parseInt((String) wheelSec.getSelectionItem());

            if (currentHour >= hour) {
                if (currentMin == min) {
                    if (currentSec > sec) {
                        return true;
                    } else {
                        return false;
                    }
                } else if (currentMin > min) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public interface OnSelectTimeListener {
        void onSure(String time);
    }
}
