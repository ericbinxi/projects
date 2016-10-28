package cn.com.mod.office.lightman.widget;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import java.util.List;

import cn.com.mod.office.lightman.R;
import cn.com.mod.office.lightman.entity.RoomEntity;


/**
 * 随手势放大缩小的楼层图片
 * 楼层信息 View
 */
public class FloorView extends View implements ScaleGestureDetector.OnScaleGestureListener {

    private static final String TAG = "FloorView";

    private Bitmap mFloor;
    private Bitmap arrow;

    private OnAreaListener mOnAreaListener;

    private ScaleGestureDetector mScaleDectector;

    private float mScale;
    private float mPrevScale;
    private float mOldScale;

    private boolean mIsMoved;
    private boolean mIsScaled;
    private float mLastDownX;
    private float mLastDownY;
    private float mX;
    private float mY;
    private int offsetX,offsetY;

    private Paint mPaint;
    private Paint arrowPaint;
    private Rect mSrcRect = new Rect();
    private Rect mDstRect = new Rect();

//    private int mRow;
//    private int mCol;
    private List<RoomEntity> rooms;
    private boolean isDraw = false;

    public FloorView(Context context) {
        super(context);
        init(context);
    }

    public FloorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FloorView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        mScale = 1;
        mPrevScale = -1;
        mOldScale = 1;
        mX = 0;
        mY = 0;
        mPaint = new Paint();
        arrowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mScaleDectector = new ScaleGestureDetector(context, this);
        arrow = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_button_end);
    }

    public void setOnAreaClickListener(OnAreaListener listener) {
        mOnAreaListener = listener;
    }

    public void initData(Bitmap bitmap, int row, int col) {
        mFloor = bitmap;
//        mRow = row;
//        mCol = col;
        refreshArea();
        invalidate();
    }

    public void initArrow(List<RoomEntity> rooms){
        this.rooms = rooms;
        refreshArea();
        invalidate();
    }

    public float getScale() {
        return mScale;
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (mFloor == null) {
            mPaint.setColor(Color.WHITE);
            mPaint.setTextSize(32);
            mPaint.setTextAlign(Align.CENTER);
            canvas.drawText("Loading...", getWidth() / 2, getHeight() / 2, mPaint);
        } else {
            int imgWidth = mFloor.getWidth();
            int imgHeight = mFloor.getHeight();
            mSrcRect.set((int) mX, (int) mY, (int) (mX + imgWidth / mScale), (int) (mY + imgHeight / mScale));
            try {
                canvas.drawBitmap(mFloor, mSrcRect, mDstRect, null);
            } catch (Exception e) {
            }
            //画箭头
//            if(isDraw)return;
            if(rooms!=null&&rooms.size()>0){
                float ratioX = 600/(float)getWidth();
                float ratioY = 600/(float)getHeight();
                Log.e("arrow","ratioX:"+ratioX+"  ratioY:"+ratioY);
                for (RoomEntity room:rooms){
                    Log.e("arrow","x:"+room.getRoom_x()*ratioX+"  y:"+room.getRoom_y()*ratioY);
                    canvas.drawBitmap(arrow,mX+room.getRoom_x()/ratioX*mScale,mY+room.getRoom_y()/ratioY*mScale,arrowPaint);
                }
                isDraw = true;
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

//        refreshArea();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (mFloor == null)
            return false;

        final int action = event.getAction() & MotionEvent.ACTION_MASK;
        final int pointerCount = event.getPointerCount();

        mScaleDectector.onTouchEvent(event);

        if (pointerCount == 1) {
            float x = event.getX();
            float y = event.getY();
            float gapX, gapY;

            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    mLastDownX = x;
                    mLastDownY = y;
                    super.onTouchEvent(event);
                    break;
                case MotionEvent.ACTION_MOVE:
                    gapX = x - mLastDownX;
                    gapY = y - mLastDownY;
                    if (mScale == 1) {
                        super.onTouchEvent(event);
                    } else if (!mIsScaled) {    //scrolling
                        mX -= gapX;
                        mY -= gapY;
                        mLastDownX = event.getX();
                        mLastDownY = event.getY();
                        normalPosition();
                    }
                    if (Math.abs(gapX) > 5 || Math.abs(gapY) > 5) {
                        mIsMoved = true;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (mIsMoved == false) {
                        if (mFloor != null &&
                                mDstRect.contains((int) x, (int) y)) {
                            x -= mDstRect.left;
                            y -= mDstRect.top;
                            int imgWidth = mFloor.getWidth();
                            int imgHeight = mFloor.getHeight();
                            // clicked room
//                            int divSX = imgWidth / mCol;
//                            int divSY = imgHeight / mRow;
                            int rx = (int) (mX + x / mDstRect.width() * imgWidth / mScale);
                            int ry = (int) (mY + y / mDstRect.height() * imgHeight / mScale);
//                            int ax = rx / divSX;
//                            int ay = ry / divSY;

//                            Log.d(TAG, String.format("Floor area clicked {%d, %d}", ax, ay));
                            if (mOnAreaListener != null) {
//                                mOnAreaListener.onAreaClicked(ax, ay);
                                mOnAreaListener.onAreaClicked(rx, ry);
                            }
                        }
                    } else {
                        Log.d(TAG, "touch Moved");
                    }

                    mIsMoved = false;
                    mIsScaled = false;
                    break;
            }
        }

        invalidate();
        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        mPrevScale = mScale;
        return true;
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        mScale = mPrevScale * detector.getScaleFactor();

        if (mScale < 1)
            mScale = 1;

        if (mFloor != null) {
            float imgWidth = mFloor.getWidth();
            float imgHeight = mFloor.getHeight();
            float width = getWidth();
            float height = getHeight();

            float maxScale = getMaxScale();
            if (mScale > maxScale) {
                mScale = maxScale;
            }

            float focusX = detector.getFocusX();
            float focusY = detector.getFocusY();
            float w = imgWidth / mOldScale;
            float h = imgHeight / mOldScale;
            float w1 = imgWidth / mScale;
            float h1 = imgHeight / mScale;
            mX += (int) ((w - w1) * (focusX / width));
            mY += (int) ((h - h1) * (focusY / height));
        }

        mOldScale = mScale;
        normalPosition();
        invalidate();
        mIsScaled = true;

        return false;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        mPrevScale = -1;
        zoom();
    }

    private void normalPosition() {
        if (mX <= 0)
            mX = 0;

        if (mY <= 0)
            mY = 0;

        if (mFloor != null) {
            int width = mFloor.getWidth();
            int height = mFloor.getHeight();
            if (mX >= width - width / mScale)
                mX = (int) (width - width / mScale);

            if (mY >= height - height / mScale)
                mY = (int) (height - height / mScale);
        } else {
            mX = 0;
            mY = 0;
        }
    }

    private void zoom() {
        float maxScale = getMaxScale();
        if (mScale >= maxScale) {
            float imgWidth = mFloor.getWidth();
            float imgHeight = mFloor.getHeight();
//            float divX = imgWidth / mCol;
//            float divY = imgHeight / mRow;

//            if (mOnAreaListener != null) {
//                mOnAreaListener.onAreaZoomed((int) (mX / divX), (int) (mY / divY),
//                        (int) ((mX + imgWidth / mScale) / divX), (int) ((mY + imgHeight / mScale) / divY));
//            }
        }
    }

    private float getMaxScale() {
        float imgWidth = mFloor.getWidth();
        float imgHeight = mFloor.getHeight();
        float width = getWidth();
        float height = getHeight();

        float maxScale = Math.max(imgWidth / width, imgHeight / height);
        if (maxScale < 5) {
            maxScale = 5;
        }

        return maxScale;
    }

    private void refreshArea() {
        if (mFloor != null) {
            int vWidth = getWidth();
            int vHeight = getHeight();
            int width = vWidth;
            int height = vHeight;
            int imgWidth = mFloor.getWidth();
            int imgHeight = mFloor.getHeight();

            if (imgWidth * vHeight > vWidth * imgHeight) {
                height = imgHeight * vWidth / imgWidth;
            } else {
                width = imgWidth * vHeight / imgHeight;
            }
            Log.e("111","width:"+width+"    height:"+height);
            Log.e("111","offset  width:"+(vWidth - width) / 2+"  offset  height:"+(vHeight - height) / 2);
            offsetX = (vWidth - width) / 2;
            offsetY = (vHeight - height) / 2;
            mDstRect.set(0, 0, width, height);
//            mDstRect.offset((vWidth - width) / 2, (vHeight - height) / 2);
        }
    }

    public static interface OnAreaListener {
        public void onAreaClicked(int x, int y);

        public void onAreaZoomed(int x1, int y1, int x2, int y2);
    }

}

