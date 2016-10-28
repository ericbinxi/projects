package cn.com.mod.office.lightman.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.joshua.common.util.PxAndDp;

import cn.com.mod.office.lightman.R;

/**
 * 颜色选择器
 * Created by CAT on 2014/11/8.
 */
public class ColorPicker extends ImageView implements View.OnTouchListener {
    public static final String TAG = "ColorPicker";

    private int mRed = 255;
    private int mGreen = 255;
    private int mBlue = 255;
    private float mLastX = -100;
    private float mLastY = -100;
    private Paint mPanint;
    private int mPickerWidth;
    private int mPickerHeight;
    private int mBitmapWidth;
    private int mBitmapHeight;
    private Rect mRect;
    private Bitmap mBitmap;
    private Drawable mThumbDrawable;
    private Drawable mPointDrawable;
    private ColorPickerListener mListener;
    private boolean isShowFilter;
    private Rect mPickerRect;

    public ColorPicker(Context context) {
        super(context);
        init();
    }

    public ColorPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init() {
        this.setOnTouchListener(this);
        mPanint = new Paint();
        mPanint.setAntiAlias(true);
        mThumbDrawable = getResources().getDrawable(R.drawable.seekbar_thumb_colortemp);
        mPointDrawable = getResources().getDrawable(R.drawable.seekbar_point_colortemp);

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mPickerWidth = getWidth();
                mPickerHeight = getHeight();

                mBitmap = ((BitmapDrawable) getDrawable()).getBitmap();
                mBitmapWidth = mBitmap.getWidth();
                mBitmapHeight = mBitmap.getHeight();

                // TODO 偏移
                    /*
                        height          (realheight)
                       ---            -----
                        width        realWidth
                     */
//                // 横向先顶到边
//                if (mBitmapWidth > mBitmapHeight) {
//                    // 实际显示高度
//                    float realHeight = mBitmapHeight * 1.0f / mBitmapWidth *
//                            (mPickerWidth - getPaddingLeft() - getPaddingRight());
//                    float offset = (mPickerHeight - getPaddingTop() - getPaddingBottom() - realHeight) / 2;
//                    mRect = new Rect(getPaddingLeft(), getPaddingTop() + (int) offset,
//                            mPickerWidth - getPaddingRight(), mPickerHeight - getPaddingBottom() - (int) offset);
//                }
//                // 纵向先顶到边
//                else
//                {
//                    // 实际显示宽度
//                    float realWidth = mBitmapWidth * 1.0f / mBitmapHeight *
//                            (mPickerHeight - getPaddingTop() - getPaddingBottom());
//                    float offset = (mPickerWidth - getPaddingLeft() - getPaddingRight() - realWidth) / 2;
//                    offset = Math.abs(offset);
//                    mRect = new Rect(getPaddingLeft() + (int) offset, getPaddingTop(),
//                            mPickerWidth - getPaddingRight() - (int) offset, mPickerHeight - getPaddingBottom());
//                }

                mRect = new Rect(getPaddingLeft(), getPaddingTop(),
                        mPickerWidth - getPaddingRight(), mPickerHeight - getPaddingBottom());
                mPickerRect = new Rect(getPaddingLeft(), getPaddingTop(), mPickerWidth - getPaddingRight(), mPickerHeight);
            }
        });
    }

    @Override
    public void setImageResource(int resId) {
        mLastX = -100;
        mLastY = -100;
        super.setImageResource(resId);
    }

    @Override
    public void setImageBitmap(Bitmap bitmap) {
        mLastX = -100;
        mLastY = -100;
        super.setImageBitmap(bitmap);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int color = Color.rgb(mRed, mGreen, mBlue);

        int radius = PxAndDp.dip2px(getContext(), 16);
        mThumbDrawable.setBounds((int) mLastX - radius, (int) mLastY - radius, (int) mLastX + radius, (int) mLastY + radius);
        Drawable thumb = mThumbDrawable;
        if (thumb instanceof LayerDrawable) {
            thumb = ((LayerDrawable) thumb).findDrawableByLayerId(android.R.id.content);
            thumb.setColorFilter(color, PorterDuff.Mode.SRC_IN);
            mThumbDrawable.draw(canvas);
        } else {
            mThumbDrawable.draw(canvas);
        }

        if (isShowFilter) {
            int radius2 = PxAndDp.dip2px(getContext(), 24);
            mPointDrawable.setBounds((int) mLastX - radius2, (int) mLastY - radius - radius2 * 2, (int) mLastX + radius2, (int) mLastY - radius);
            Drawable pointDrawable = mPointDrawable;
            if (pointDrawable instanceof LayerDrawable) {
                pointDrawable = ((LayerDrawable) pointDrawable).findDrawableByLayerId(android.R.id.content);
                pointDrawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
                mPointDrawable.draw(canvas);
            } else {
                mPointDrawable.draw(canvas);
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                isShowFilter = true;
                handleDraw(event);
                if (mListener != null) {
                    mListener.onColorChanged(this, mRed, mGreen, mBlue);
                }
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_DOWN:
                if (!mPickerRect.contains((int) (event.getX()), (int) (event.getY()))) {
                    return false;
                }

                isShowFilter = true;
                handleDraw(event);
                if (mListener != null) {
                    mListener.onStart(this);
                }
                break;
            case MotionEvent.ACTION_UP:
                isShowFilter = false;
                handleDraw(event);
                if (mListener != null) {
                    mListener.onStop(this);
                }
                break;
        }
        return true;
    }

    private void handleDraw(MotionEvent event) {
        mLastX = event.getX();
        mLastY = event.getY();

        if (mLastX < mRect.left) {
            mLastX = mRect.left;
        }
        if (mLastY < mRect.top) {
            mLastY = mRect.top;
        }
        if (mLastX > mRect.right) {
            mLastX = mRect.right;
        }
        if (mLastY > mRect.bottom) {
            mLastY = mRect.bottom;
        }

        try {
            float x = (mLastX - mRect.left) * mBitmapWidth / mRect.width();
            float y = (mLastY - mRect.top) * mBitmapHeight / mRect.height();

            if (x == mBitmapWidth) {
                x -= 1;
            }
            if (y == mBitmapHeight) {
                y -= 1;
            }
            int pixel = mBitmap.getPixel((int) x, (int) y);
            mRed = Color.red(pixel);
            mGreen = Color.green(pixel);
            mBlue = Color.blue(pixel);

        } catch (Exception e) {
        }
        invalidate();
    }

    public int[] getRGB() {
        return new int[]{mRed, mGreen, mBlue};
    }

    // 设置监听器
    public void setColorPickerListener(ColorPickerListener listener) {
        this.mListener = listener;
    }

    public interface ColorPickerListener {
        public void onColorChanged(ColorPicker picker, int red, int green, int blue);

        public void onStart(ColorPicker picker);

        public void onStop(ColorPicker picker);
    }
}
