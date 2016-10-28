package cn.com.mod.office.lightman.widget;

import android.app.TimePickerDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.CheckedTextView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import cn.com.mod.office.lightman.R;

/**
 * 添加闹钟的面板
 * Created by CAT on 2014/11/8.
 */
public class ClockAddPanel extends FrameLayout {
    private static int[] texts = {R.string.monday, R.string.tuesday, R.string.wednesday, R.string.thursday, R.string.friday, R.string.saturday, R.string.sunday, R.string.sure};
    private View mRoot;
    private TextView mHour;
    private TextView mMinute;
    private CheckedTextView[] mWeeks;
    private SwitchButton mClockSwitch;
    private ClockAddPanelListener mListener;

    public ClockAddPanel(Context context) {
        super(context);
        init();
    }

    public ClockAddPanel(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public void init() {
        mRoot = View.inflate(getContext(), R.layout.view_clock_add_panel, null);
        mHour = (TextView) mRoot.findViewById(R.id.hour);
        mMinute = (TextView) mRoot.findViewById(R.id.minute);
        mClockSwitch = (SwitchButton) mRoot.findViewById(R.id.clock_switch);

        OnClickListener pickTimeListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog dialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String hourStr = hourOfDay < 10 ? "0" + hourOfDay : hourOfDay + "";
                        String minuteStr = minute < 10 ? "0" + minute : minute + "";
                        mHour.setText(hourStr);
                        mMinute.setText(minuteStr);
                    }
                }, 9, 0, true);
                dialog.show();
            }
        };
        mHour.setOnClickListener(pickTimeListener);
        mMinute.setOnClickListener(pickTimeListener);

        final LinearLayout layout = (LinearLayout) mRoot.findViewById(R.id.week);

        mWeeks = new CheckedTextView[7];
        for (int i = 0; i < texts.length; i++) {
            final CheckedTextView textView = new CheckedTextView(getContext());
            textView.setText(texts[i]);
            textView.setTextColor(getContext().getResources().getColor(R.color.orange));
            textView.setTextSize(12);
            textView.setGravity(Gravity.CENTER);
            textView.setBackgroundResource(R.drawable.btn_week);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            final LinearLayout content = new LinearLayout(getContext());
            content.setGravity(Gravity.CENTER);
            content.addView(textView);
            layout.addView(content, params);

            if (i != texts.length - 1) {
                mWeeks[i] = textView;
                textView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        textView.setChecked(!textView.isChecked());
                        if (textView.isChecked()) {
                            textView.setTextColor(getContext().getResources().getColor(R.color.black));
                            textView.setBackgroundResource(R.drawable.btn_week_press);
                        } else {
                            textView.setTextColor(getContext().getResources().getColor(R.color.orange));
                            textView.setBackgroundResource(R.drawable.btn_week);
                        }
                    }
                });
            } else {
                textView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        StringBuilder weekBuilder = new StringBuilder();
                        for (int i = 0; i < mWeeks.length; i++) {
                            if (mWeeks[i].isChecked()) {
                                if (weekBuilder.toString() != "") {
                                    weekBuilder.append(",");
                                }
                                weekBuilder.append((i + 1) % 7 + "");
                            }
                        }
                        String week = weekBuilder.toString();
                        String time = mHour.getText() + ":" + mMinute.getText();
                        boolean isOpen = !mClockSwitch.isChecked();

                        if (mListener != null) {
                            mListener.onSubmit(week, time, isOpen);
                        }
                    }
                });
            }
        }

        ViewTreeObserver vto = layout.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            private boolean flag = true;

            @Override
            public boolean onPreDraw() {
                if (flag) {
                    for (int i = 0; i < layout.getChildCount(); i++) {
                        LinearLayout content = (LinearLayout) layout.getChildAt(i);
                        CheckedTextView weekItem = (CheckedTextView) content.getChildAt(0);
                        int size = weekItem.getWidth();
                        size = size < weekItem.getHeight() ? size : weekItem.getHeight();
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);
                        weekItem.setLayoutParams(params);
                        flag = false;
                    }
                }
                return true;
            }
        });
        addView(mRoot);
    }

    // 还原初始状态
    public void restore() {
        String[] time = "09:00".split(":");
        mHour.setText(time[0]);
        mMinute.setText(time[1]);
        for (int i = 0; i < mWeeks.length; i++) {
            CheckedTextView week = mWeeks[i];
            week.setTextColor(getContext().getResources().getColor(R.color.orange));
            week.setBackgroundResource(R.drawable.btn_week);
            week.setChecked(false);
        }
    }

    public void setClockAddPanelListener(ClockAddPanelListener listener) {
        this.mListener = listener;
    }

    public interface ClockAddPanelListener {
        public void onSubmit(String week, String time, boolean isOpen);
    }

}
