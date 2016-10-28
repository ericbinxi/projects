package cn.com.mod.office.lightman.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.com.mod.office.lightman.R;
import cn.com.mod.office.lightman.widget.SeekBarPicker;

/**
 * 色温调节面板
 * Created by CAT on 2014/11/20.
 */
public class ColorTempPanel extends Fragment {
    private SeekBarPicker mColorTempPicker;
    private TextView mColorTemp;
    private SeekBarPicker.SeekBarPickerListener mPickerListener;
    private TextView.OnEditorActionListener mOnEditorActionListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_colortemp, container, false);

        mColorTempPicker = (SeekBarPicker) v.findViewById(R.id.picker);
        mColorTemp = (TextView) v.findViewById(R.id.colortemp_val);


        mColorTempPicker.setPointDrawable(R.drawable.seekbar_point_colortemp);
        mColorTempPicker.setProgressBarDrawable(R.drawable.bg_seekbar_colortemp);
        mColorTempPicker.setThumbDrawable(R.drawable.seekbar_thumb_colortemp);
        mColorTempPicker.setMax(255);
        mColorTempPicker.setSeekBarPickerListener(new SeekBarPicker.SeekBarPickerListener() {
            @Override
            public void onProgressChanged(SeekBarPicker picker, int progress) {
                mColorTemp.setText(2700 + Math.round(progress * 3800 / 255f) + "");
                if (mPickerListener != null) {
                    mPickerListener.onProgressChanged(picker, progress);
                }
            }

            @Override
            public void onStart(SeekBarPicker picker) {
                if (mPickerListener != null) {
                    mPickerListener.onStart(picker);
                }
            }

            @Override
            public void onStop(SeekBarPicker picker) {
                if (mPickerListener != null) {
                    mPickerListener.onStop(picker);
                }
            }
        });

        mColorTemp.setOnEditorActionListener(mOnEditorActionListener);
        return v;
    }

    // 设置滑动条监听器
    public void setPickerListener(SeekBarPicker.SeekBarPickerListener listener) {
        this.mPickerListener = listener;
    }

    public void setOnEditorActionListener(TextView.OnEditorActionListener listener) {
        this.mOnEditorActionListener = listener;
    }

    public int getColorTemp() {
        if (TextUtils.isEmpty(mColorTemp.getText())) {
            return 0;
        }
        return (int) ((Integer.parseInt(mColorTemp.getText() + "") - 2700) / 3800 * 255);
    }

    public void setColorTemp(int colorTemp) {
        mColorTempPicker.setProgress(colorTemp);
    }
}
