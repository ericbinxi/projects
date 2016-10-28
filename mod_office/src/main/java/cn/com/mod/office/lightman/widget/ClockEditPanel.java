package cn.com.mod.office.lightman.widget;

import android.app.TimePickerDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.CheckedTextView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import cn.com.mod.office.lightman.R;
import cn.com.mod.office.lightman.entity.ClockInfo;

/**
 * 编辑闹钟的面板
 * Created by CAT on 2014/11/8.
 */
public class ClockEditPanel extends FrameLayout {
    private static int[] texts = {R.string.monday, R.string.tuesday, R.string.wednesday, R.string.thursday, R.string.friday, R.string.saturday, R.string.sunday, R.string.sure};
    private ClockInfo mClockInfo;
    private View mRoot;
    private TextView mHour;
    private TextView mMinute;
    private CheckedTextView[] mWeeks;
    private ClockEditPanelListener mListener;

    public ClockEditPanel(Context context) {
        super(context);
        init();
    }

    public void setClockInfo(ClockInfo clockInfo) {
        this.mClockInfo = clockInfo;
        refresh();
    }

    public void init() {
        mRoot = View.inflate(getContext(), R.layout.view_clock_edit_panel, null);
        mHour = (TextView) mRoot.findViewById(R.id.hour);
        mMinute = (TextView) mRoot.findViewById(R.id.minute);
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

        TextView delete = (TextView) mRoot.findViewById(R.id.delete);
        delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onDelete(mClockInfo);
                }
            }
        });

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
                textView.setOnClickListener(new View.OnClickListener() {
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
                        if (mListener != null) {
                            mListener.onSubmit(mClockInfo, week, time);
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

    public void refresh() {
        String[] time = mClockInfo.time.split(":");
        mHour.setText(time[0]);
        mMinute.setText(time[1]);
        String[] weeks = mClockInfo.week.split(",");
        for (int i = 0; i < mWeeks.length; i++) {
            CheckedTextView week = mWeeks[i];
            boolean hasFind = false;
            for (String weekStr : weeks) {
                if (weekStr.equals((i + 1) % 7 + "")) {
                    hasFind = true;
                }
            }
            if (hasFind) {
                week.setTextColor(getContext().getResources().getColor(R.color.black));
                week.setBackgroundResource(R.drawable.btn_week_press);
            } else {
                week.setTextColor(getContext().getResources().getColor(R.color.orange));
                week.setBackgroundResource(R.drawable.btn_week);
            }
            week.setChecked(hasFind);
        }
    }

    public void setClockEditPanelListener(ClockEditPanelListener listener) {
        this.mListener = listener;
    }

    public interface ClockEditPanelListener {
        public void onDelete(ClockInfo clockInfo);

        public void onSubmit(ClockInfo clockInfo, String week, String time);
    }
}
