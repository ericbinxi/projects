package cn.com.mod.office.lightman.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.joshua.common.util.ImageUtils;

import cn.com.mod.office.lightman.R;
import cn.com.mod.office.lightman.activity.PatameterSettingActivity;
import cn.com.mod.office.lightman.widget.ColorPicker;

/**
 * Rgb调节面板
 * Created by CAT on 2014/11/20.
 */
public class RgbPanel extends Fragment {
    public static final String TAG = "RgbPanel";

    private ColorPicker mColorPicker;
    private TextView mRed;
    private TextView mGreen;
    private TextView mBlue;
    private ColorPicker.ColorPickerListener mPickerListener;
    private TextView.OnEditorActionListener mOnEditorActionListener;
    private boolean mHasChange;
    private ImageView mFile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_rgb, container, false);

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
                    getActivity().startActivityForResult(intent, PatameterSettingActivity.REQUEST_PICK_IMAGE);
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

    public void setOnEditorActionListener(TextView.OnEditorActionListener listener) {
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
