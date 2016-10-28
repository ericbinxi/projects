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
 * 亮度调节面板
 * Created by CAT on 2014/11/20.
 */
public class BrightnessPanel extends Fragment {
    private SeekBarPicker mBrightnessPicker;
    private TextView mBrightness;
    private SeekBarPicker.SeekBarPickerListener mPickerListener;
    private TextView.OnEditorActionListener mOnEditorActionListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_brightness, container, false);
        mBrightnessPicker = (SeekBarPicker) v.findViewById(R.id.picker);
        mBrightness = (TextView) v.findViewById(R.id.brightness_val);

        mBrightnessPicker.setPointDrawable(R.drawable.seekbar_point_brightness);
        mBrightnessPicker.setProgressBarDrawable(R.drawable.bg_seekbar_brightness);
        mBrightnessPicker.setThumbDrawable(R.drawable.seekbar_thumb_brightness);
        mBrightnessPicker.setMax(100);
        mBrightnessPicker.setSeekBarPickerListener(new SeekBarPicker.SeekBarPickerListener() {
            @Override
            public void onProgressChanged(SeekBarPicker picker, int progress) {
                mBrightness.setText(progress + "");
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
        mBrightness.setOnEditorActionListener(mOnEditorActionListener);
        return v;
    }

    // 设置滑动条监听器
    public void setPickerListener(SeekBarPicker.SeekBarPickerListener listener) {
        this.mPickerListener = listener;
    }

    public void setOnEditorActionListener(TextView.OnEditorActionListener listener) {
        this.mOnEditorActionListener = listener;
    }

    public int getBrightness() {
        if (TextUtils.isEmpty(mBrightness.getText())) {
            return 0;
        }
        return Integer.parseInt(mBrightness.getText() + "");
    }

    public void setBrightness(int brightness) {
        mBrightnessPicker.setProgress(brightness);
    }
}
