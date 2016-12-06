package cn.com.mod.office.lightman.utils;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import cn.com.mod.office.lightman.R;
import cn.com.mod.office.lightman.widget.timepicker.adapter.ArrayWheelAdapter;
import cn.com.mod.office.lightman.widget.timepicker.widget.WheelView;

/**
 * Created by Administrator on 2016/11/26.
 */
public class PopWindowUtils {
    private Context context;
    private String preTime;
    private WheelView wheelHour, wheelMin, wheelSec;
    private TextView confirm;
    private OnSelectTimeListener listener;

    public PopWindowUtils(Context context, OnSelectTimeListener listener) {
        this.context = context;
        this.listener = listener;
        init();
    }

    public void setListener(OnSelectTimeListener listener) {
        this.listener = listener;
    }

    private PopupWindow popupWindow;
    private void init(){
        if(popupWindow==null){
            popupWindow = new PopupWindow(context);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setTouchable(true);
            View root = LayoutInflater.from(context).inflate(R.layout.dialog_time_picker, null);
            wheelHour = (WheelView) root.findViewById(R.id.wheel_hour);
            wheelMin = (WheelView) root.findViewById(R.id.wheel_min);
            wheelSec = (WheelView) root.findViewById(R.id.wheel_second);
            confirm = (TextView) root.findViewById(R.id.btn_confirm);
            initWheelView();
            popupWindow.setContentView(root);

            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (validate() && listener != null) {
                        String time = (String) wheelHour.getSelectionItem() + ":" + (String) wheelMin.getSelectionItem() + ":" + (String) wheelSec.getSelectionItem();
                        listener.onSure(time);
                        popupWindow.dismiss();
                    } else {
                        Toast.makeText(context, R.string.time_picker_tips, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    public void show(View parent,String preTime) {
        this.preTime = preTime;
        popupWindow.showAtLocation(parent, Gravity.CENTER,0,0);
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
