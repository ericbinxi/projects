package cn.com.mod.office.lightman.activity.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.joshua.common.util.ImageUtils;

import cn.com.mod.office.lightman.R;
import cn.com.mod.office.lightman.activity.SceneActivity;
import cn.com.mod.office.lightman.widget.ColorPicker;
import cn.com.mod.office.lightman.widget.SeekBarPicker;

/**
 * Created by Administrator on 2016/10/17.
 */
public class ParameterFragment extends Fragment {

    private SeekBarPicker mBrightnessPicker;
    private TextView mBrightness;
    private SeekBarPicker.SeekBarPickerListener mPickerListener_b;
    private TextView.OnEditorActionListener mOnEditorActionListener_b;

    private SeekBarPicker mColorTempPicker;
    private TextView mColorTemp;
    private SeekBarPicker.SeekBarPickerListener mPickerListener_t;
    private TextView.OnEditorActionListener mOnEditorActionListener_t;


    private ColorPicker mColorPicker;
    private TextView mRed;
    private TextView mGreen;
    private TextView mBlue;
    private ColorPicker.ColorPickerListener mPickerListener;
    private TextView.OnEditorActionListener mOnEditorActionListener;
    private boolean mHasChange;
    private ImageView mFile;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_parameter_setting, container, false);
        mBrightnessPicker = (SeekBarPicker) v.findViewById(R.id.picker_b);
        mBrightness = (TextView) v.findViewById(R.id.brightness_val);

        mBrightnessPicker.setPointDrawable(R.drawable.seekbar_point_brightness);
        mBrightnessPicker.setProgressBarDrawable(R.drawable.bg_seekbar_brightness);
        mBrightnessPicker.setThumbDrawable(R.drawable.seekbar_thumb_brightness);
        mBrightnessPicker.setMax(100);
        mBrightnessPicker.setSeekBarPickerListener(new SeekBarPicker.SeekBarPickerListener() {
            @Override
            public void onProgressChanged(SeekBarPicker picker, int progress) {
                mBrightness.setText(progress + "");
                if (mPickerListener_b != null) {
                    mPickerListener_b.onProgressChanged(picker, progress);
                }
            }

            @Override
            public void onStart(SeekBarPicker picker) {
                if (mPickerListener_b != null) {
                    mPickerListener_b.onStart(picker);
                }
            }

            @Override
            public void onStop(SeekBarPicker picker) {
                if (mPickerListener_b != null) {
                    mPickerListener_b.onStop(picker);
                }
            }
        });
        mBrightness.setOnEditorActionListener(mOnEditorActionListener_b);


        mColorTempPicker = (SeekBarPicker) v.findViewById(R.id.picker_t);
        mColorTemp = (TextView) v.findViewById(R.id.colortemp_val);


        mColorTempPicker.setPointDrawable(R.drawable.seekbar_point_colortemp);
        mColorTempPicker.setProgressBarDrawable(R.drawable.bg_seekbar_colortemp);
        mColorTempPicker.setThumbDrawable(R.drawable.seekbar_thumb_colortemp);
        mColorTempPicker.setMax(255);
        mColorTempPicker.setSeekBarPickerListener(new SeekBarPicker.SeekBarPickerListener() {
            @Override
            public void onProgressChanged(SeekBarPicker picker, int progress) {
                mColorTemp.setText(2700 + Math.round(progress * 3800 / 255f) + "");
                if (mPickerListener_t != null) {
                    mPickerListener_t.onProgressChanged(picker, progress);
                }
            }

            @Override
            public void onStart(SeekBarPicker picker) {
                if (mPickerListener_t != null) {
                    mPickerListener_t.onStart(picker);
                }
            }

            @Override
            public void onStop(SeekBarPicker picker) {
                if (mPickerListener_t != null) {
                    mPickerListener_t.onStop(picker);
                }
            }
        });

        mColorTemp.setOnEditorActionListener(mOnEditorActionListener_t);

        //rgb
        mColorPicker = (ColorPicker) v.findViewById(R.id.color_picker);
        mRed = (TextView) v.findViewById(R.id.red);
        mGreen = (TextView) v.findViewById(R.id.green);
        mBlue = (TextView) v.findViewById(R.id.blue);
        mFile = (ImageView) v.findViewById(R.id.file);

        mFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mHasChange) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                    getActivity().startActivityForResult(intent, SceneActivity.REQUEST_PICK_IMAGE);
                } else {
                    mFile.setImageResource(R.drawable.ic_file);
                    mColorPicker.setImageResource(R.drawable.bg_color);
                    mHasChange = false;
                }
            }
        });


        mColorPicker.setColorPickerListener(new ColorPicker.ColorPickerListener() {
            @Override
            public void onColorChanged(ColorPicker picker, int red, int green, int blue) {
                mRed.setText(red + "");
                mGreen.setText(green + "");
                mBlue.setText(blue + "");
                if (mPickerListener != null) {
                    mPickerListener.onColorChanged(picker, red, green, blue);
                }
            }

            @Override
            public void onStart(ColorPicker picker) {
                int[] rgb = picker.getRGB();
                mRed.setText(rgb[0] + "");
                mGreen.setText(rgb[1] + "");
                mBlue.setText(rgb[2] + "");
                if (mPickerListener != null) {
                    mPickerListener.onStart(picker);
                }
            }

            @Override
            public void onStop(ColorPicker picker) {
                if (mPickerListener != null) {
                    mPickerListener.onStop(picker);
                }
            }
        });

        mRed.setOnEditorActionListener(mOnEditorActionListener);
        mGreen.setOnEditorActionListener(mOnEditorActionListener);
        mBlue.setOnEditorActionListener(mOnEditorActionListener);

        return v;
    }


    // 设置滑动条监听器
    public void setPickerListener(SeekBarPicker.SeekBarPickerListener listener) {
        this.mPickerListener_b = listener;
    }

    public void setOnEditorActionListener(TextView.OnEditorActionListener listener) {
        this.mOnEditorActionListener_b = listener;
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

    // 设置滑动条监听器
    public void setTPickerListener(SeekBarPicker.SeekBarPickerListener listener) {
        this.mPickerListener_t = listener;
    }

    public void setOnTEditorActionListener(TextView.OnEditorActionListener listener) {
        this.mOnEditorActionListener_t = listener;
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

    //rgb
    public int[] getRgb() {
        int red = TextUtils.isEmpty(mRed.getText()) ? 0 : Integer.parseInt(mRed.getText() + "");
        int green = TextUtils.isEmpty(mGreen.getText()) ? 0 : Integer.parseInt(mGreen.getText() + "");
        int blue = TextUtils.isEmpty(mBlue.getText()) ? 0 : Integer.parseInt(mBlue.getText() + "");
        int[] rgb = new int[]{
                red, green, blue
        };
        return rgb;
    }

    public void setRGB(int[] rgb) {
        mRed.setText(rgb[0] + "");
        mGreen.setText(rgb[1] + "");
        mBlue.setText(rgb[2] + "");
    }

    // 设置滑动条监听器
    public void setPickerListener(ColorPicker.ColorPickerListener listener) {
        this.mPickerListener = listener;
    }

    public void setOnRGBEditorActionListener(TextView.OnEditorActionListener listener) {
        this.mOnEditorActionListener = listener;
    }

    public void setResultData(int resultCode, Uri uri) {
        if (resultCode == Activity.RESULT_OK) {
            Bitmap bitmap = ImageUtils.compressCapacityFromUri(getActivity(), uri);
            mColorPicker.setImageBitmap(bitmap);
            mFile.setImageResource(R.drawable.ic_rgb);
            mHasChange = true;
        } else {
            mHasChange = false;
        }
    }

}
