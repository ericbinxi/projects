package cn.com.mod.office.lightman.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

import com.joshua.common.util.FrameAnimationController;
import com.joshua.common.util.ImageUtils;

import cn.com.mod.office.lightman.R;

/**
 * 图片按钮开关
 * Created by CAT on 2014/11/22.
 */
public class ImageButtonSwitch extends View {
    private Paint mPaint;
    private Rect mTrackRect;
    private Rect mImageRect;
    private Drawable mTrack;
    private Bitmap mRoundBitmap;
    private boolean hasBitmap;
    private Bitmap border;

    // 图片的半径
    private int mRadius;
    // 组件宽度
    private int mWidth;
    // 组件高度
    private int mHeight;
    // 按下的坐标
    private float mDownX;
    private float mDownY;
    // 滑动的距离
    private int mMoveDistance;
    // 是否有滑动过
    private boolean hasScroll;
    // 滑动方向，>0 向右， < 0 向左
    private int mOrientation = 0;
    private boolean mIsSwitch;

    private ImageButtonSwitchListener mListener;

    public ImageButtonSwitch(Context context) {
        super(context);
        init(context);
    }

    public ImageButtonSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ImageButtonSwitch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    // 初始化
    public void init(Context context) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setFilterBitmap(true);
        mPaint.setDither(true);
        mTrackRect = new Rect();
        mImageRect = new Rect();

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mWidth = getWidth();
                mHeight = getHeight();
                mRadius = mHeight / 2;
                mMoveDistance = mRadius;
                border = BitmapFactory.decodeResource(getResources(), R.drawable.border_circle_img);
                if (mRoundBitmap == null) {
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.default_diy);
                    mRoundBitmap = ImageUtils.createCircleBitmap(bitmap, mRadius - 1);
                    bitmap.recycle();
                }
                FrameAnimationController.requestAnimationFrame(new TurnRightAnimation());
            }
        });

    }

    public void setImageBitmap(Bitmap bitmap) {
        Bitmap b = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        mRoundBitmap = ImageUtils.createCircleBitmap(b, mRadius - 1);
        b.recycle();
        hasBitmap = true;
        invalidate();
    }

    public void setTrack(int res) {
        mTrack = getContext().getResources().getDrawable(res);
        invalidate();
    }

    public boolean getSwitch() {
        return this.mIsSwitch;
    }

    public void setSwitch(boolean isSwitch) {
        if (isSwitch) {
            FrameAnimationController.requestAnimationFrame(new TurnRightAnimation());
        } else {
            FrameAnimationController.requestAnimationFrame(new TurnLeftAnimation());
        }
        mIsSwitch = isSwitch;
    }

    @Override
    public void onDraw(Canvas canvas) {
        int trackHeight = (int) (mHeight / 4f);
        mTrackRect.set(0 + mRadius / 2, trackHeight, mWidth - mRadius / 2, mHeight - trackHeight);
        mTrack.setBounds(mTrackRect);
        mTrack.draw(canvas);
        mImageRect.set(mMoveDistance - mRadius, 0, mMoveDistance + mRadius, mHeight);
        if (!hasBitmap) {
            canvas.drawBitmap(mRoundBitmap, mImageRect.left, mImageRect.top, mPaint);
        } else {
            canvas.drawBitmap(border, new Rect(0, 0, border.getWidth(), border.getHeight())
                    , mImageRect, null);
            canvas.drawBitmap(mRoundBitmap, new Rect(0, 0, mRoundBitmap.getWidth(), mRoundBitmap.getHeight())
                    , new RectF(mImageRect.left + mImageRect.width() * 0.01f, mImageRect.top + mImageRect.height() * 0.01f,
                    mImageRect.left + mImageRect.width() * 0.99f, mImageRect.top + mImageRect.height() * 0.99f), null);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = e.getX();
                mDownY = e.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                hasScroll = true;
                mOrientation = (int) (e.getX() - mDownX);
                mMoveDistance = (int) e.getX();
                if (mMoveDistance - mRadius < 0) {
                    mMoveDistance = mRadius;
                } else if (mMoveDistance > mWidth - mRadius) {
                    mMoveDistance = mWidth - mRadius;
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                if (hasScroll == true) {
                    // turn right
                    if (mOrientation > 0) {
                        if (e.getX() > mWidth / 2) {
                            FrameAnimationController.requestAnimationFrame(new TurnRightAnimation());
                            if (mListener != null && mIsSwitch != true) {
                                mListener.onSwitchChange(this, true);
                            } else {
                                if (mImageRect.contains((int) e.getX(), (int) e.getY())) {
                                    if (mListener != null) {
                                        mListener.onImageClick(this);
                                    }
                                }
                            }
                            mIsSwitch = true;
                        } else {
                            FrameAnimationController.requestAnimationFrame(new TurnLeftAnimation());
                            if (mImageRect.contains((int) e.getX(), (int) e.getY())) {
                                if (mListener != null) {
                                    mListener.onImageClick(this);
                                }
                            }
                        }
                    }
                    // turn left
                    else {
                        if (e.getX() < mWidth / 2) {
                            FrameAnimationController.requestAnimationFrame(new TurnLeftAnimation());
                            if (mListener != null && mIsSwitch != false) {
                                mListener.onSwitchChange(this, false);
                            } else {
                                if (mImageRect.contains((int) e.getX(), (int) e.getY())) {
                                    if (mListener != null) {
                                        mListener.onImageClick(this);
                                    }
                                }
                            }
                            mIsSwitch = false;
                        } else {
                            FrameAnimationController.requestAnimationFrame(new TurnRightAnimation());
                            if (mImageRect.contains((int) e.getX(), (int) e.getY())) {
                                if (mListener != null) {
                                    mListener.onImageClick(this);
                                }
                            }
                        }
                    }
                }
                hasScroll = false;
                break;
        }
        return true;
    }

    public void setImageButtonSwitchListener(ImageButtonSwitchListener listener) {
        this.mListener = listener;
    }

    public interface ImageButtonSwitchListener {
        public void onImageClick(ImageButtonSwitch imageSwitch);

        public void onSwitchChange(ImageButtonSwitch imageSwitch, boolean isSwitch);
    }

    public class TurnRightAnimation implements Runnable {

        @Override
        public void run() {
            while (mMoveDistance < mWidth - mRadius) {
                mMoveDistance += 1;
                if (mMoveDistance > mWidth - mRadius) {
                    mMoveDistance = mWidth - mRadius;
                }
                invalidate();
            }
        }
    }

    public class TurnLeftAnimation implements Runnable {

        @Override
        public void run() {
            while (mMoveDistance > mRadius) {
                mMoveDistance -= 1;
                if (mMoveDistance < mRadius) {
                    mMoveDistance = mRadius;
                }
                invalidate();
            }
        }
    }
}
