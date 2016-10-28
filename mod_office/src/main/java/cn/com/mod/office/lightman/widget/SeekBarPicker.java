package cn.com.mod.office.lightman.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

import cn.com.mod.office.lightman.R;

/**
 * 滑动条
 * Created by CAT on 2014/11/21.
 */
public class SeekBarPicker extends View {
    private Drawable mProgressBarDrawable;
    private Drawable mThumbDrawable;
    private Drawable mPointDrawable;
    private Paint mPaint;
    private int mMax = 255;
    private float mCurrent;
    private boolean isShowFilter;
    private int mProgress;
    private int mLastValue = -1;
    private SeekBarPickerListener mListener;

    private int mWidth;
    private int mHeight;
    private int mBarSize;
    private int mPointSize;

    public SeekBarPicker(Context context) {
        super(context);
        init(context);
    }

    public SeekBarPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SeekBarPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    // 初始化组件
    public void init(Context context) {
        mProgressBarDrawable = context.getResources().getDrawable(R.drawable.bg_seekbar_brightness);
        mThumbDrawable = context.getResources().getDrawable(R.drawable.seekbar_thumb_brightness);
        mPointDrawable = context.getResources().getDrawable(R.drawable.seekbar_point_brightness);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mWidth = getWidth();
                mHeight = getHeight();
                // 大圆直径
                mPointSize = (int) (mHeight * 0.6f + 0.5f);
                // 滑动条高度
                mBarSize = mHeight - mPointSize;
            }
        });
    }

    // 设置进度条图片
    public void setProgressBarDrawable(int res) {
        mProgressBarDrawable = getContext().getResources().getDrawable(res);
        invalidate();
    }

    // 设置刻度指示器图片
    public void setThumbDrawable(int res) {
        mThumbDrawable = getContext().getResources().getDrawable(res);
        invalidate();
    }

    // 设置滤镜图片
    public void setPointDrawable(int res) {
        mPointDrawable = getContext().getResources().getDrawable(res);
        invalidate();
    }

    // 获取最大值
    public int getMax() {
        return this.mMax;
    }

    // 设置最大值
    public void setMax(int max) {
        this.mMax = max;
    }

    // 获取当前刻度
    public int getProgress() {
        return this.mProgress;
    }

    // 设置当前刻度
    public void setProgress(int progress) {
//        progress * (mWidth - mPointSize) * mMax) - mBarSize / 2f + mPointSize / 2f= left;
        mCurrent = progress * 1.0f / mMax * ((mWidth - mPointSize)) + mPointSize / 2f + 0.5f;
        invalidate();
    }

    // 设置监听器
    public void setSeekBarPickerListener(SeekBarPickerListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (mWidth < 0 || mHeight < 0) {
            return;
        }

        Bitmap bitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        Canvas tmpCanvas = new Canvas(bitmap);

        mProgressBarDrawable.setBounds(0 + mPointSize / 2 - mBarSize / 2, mPointSize, mWidth - mPointSize / 2 + mBarSize / 2, mHeight);
        mProgressBarDrawable.draw(tmpCanvas);

        int left = (int) (mCurrent - mBarSize / 2f);
        if (left < mPointSize / 2f - mBarSize / 2f) {
            left = (int) (mPointSize / 2f - mBarSize / 2f);
        }
        if (left > mWidth - mPointSize / 2 - mBarSize / 2) {
            left = mWidth - mPointSize / 2 - mBarSize / 2;
        }
        int right = left + mBarSize;
        int color = bitmap.getPixel(left + mBarSize / 2, mPointSize + mBarSize / 2);
        mThumbDrawable.setBounds(left, mPointSize, right, mHeight);
        Drawable thumb = mThumbDrawable;
        if (thumb instanceof LayerDrawable) {
            thumb = ((LayerDrawable) thumb).findDrawableByLayerId(android.R.id.content);
            thumb.setColorFilter(color, PorterDuff.Mode.SRC_IN);
            mThumbDrawable.draw(tmpCanvas);
        } else {
            mThumbDrawable.draw(tmpCanvas);
        }

        if (isShowFilter) {
            mPointDrawable.setBounds(left - mPointSize / 2 + mBarSize / 2, 0, left - mPointSize / 2 + mPointSize + mBarSize / 2, mPointSize);
            Drawable point = mPointDrawable;
            if (point instanceof LayerDrawable) {
                point = ((LayerDrawable) point).findDrawableByLayerId(android.R.id.content);
                point.setColorFilter(color, PorterDuff.Mode.SRC_IN);
                mPointDrawable.draw(tmpCanvas);
            } else {
                mPointDrawable.draw(tmpCanvas);
            }
        }

        // 偏移量 / 滑动范围
        mProgress = (int) ((left - mPointSize / 2f + mBarSize / 2f) * 1.0f / (mWidth - mPointSize) * mMax);
        if (mListener != null && mProgress != mLastValue) {
            mListener.onProgressChanged(this, mProgress);
        }
        mLastValue = mProgress;

        canvas.drawBitmap(bitmap, 0, 0, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mCurrent = event.getX();
                isShowFilter = true;
                invalidate();
                if (mListener != null) {
                    mListener.onStart(this);
                    mListener.onProgressChanged(this, mProgress);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                mCurrent = event.getX();
                isShowFilter = true;
                invalidate();
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_UP:
                isShowFilter = false;
                invalidate();
                if (mListener != null) {
                    mListener.onStop(this);
                }
                break;
        }
        return true;
    }


    public interface SeekBarPickerListener {
        // 刻度值改变时
        public void onProgressChanged(SeekBarPicker picker, int progress);

        // 开始滑动时
        public void onStart(SeekBarPicker picker);

        // 结束滑动时
        public void onStop(SeekBarPicker picker);
    }

}
