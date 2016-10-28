package cn.com.mod.office.lightman.widget;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Transformation;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.Scroller;

import java.util.LinkedList;

import cn.com.mod.office.lightman.R;

/**
 * 用于上下页面切换
 *
 * @author kimks
 */
public class ViewScroller extends ViewGroup {

    private static final int INVALID = -1;
    private int mNextPosition = INVALID;
    private final static int STATE_NONE = 0;
    private int mTouchState = STATE_NONE;
    private final static int STATE_SCROLLING = 1;
    private LinkedList<View> mLoadedViews;
    private LinkedList<View> mRecycledViews;
    /**
     * index of loaded views
     */
    private int mCurrentBufferIndex;
    /**
     * index of adapter
     */
    private int mCurrentAdapterIndex;
    private final DataSetObserver mDataSetObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            View v = getChildAt(mCurrentBufferIndex);
            if (v != null) {
                for (int index = 0; index < mAdapter.getCount(); index++) {
                    if (v.equals(mAdapter.getItem(index))) {
                        mCurrentAdapterIndex = index;
                        break;
                    }
                }
            }
            resetFocus();
        }

        @Override
        public void onInvalidated() {
            onChanged();
        }
    };
    private int mBufferSize;
    private Scroller mScroller;
    private float mLastMotionY;
    private int mTouchSlop;
    private int mCurrentPosition;
    private int mLastScrollDirection;
    private Adapter mAdapter;
    private OnItemSelectListener mListener;
    private int mChildSize;
    private int mCenterChildTop;
    private Camera mCamera = new Camera();
    private int mMaxRotationAngle = 80;
    private int mMaxZoom = -20;//-120;
    private int mSpacing = 0;

    public ViewScroller(Context context) {
        super(context);
        init(context);
    }

    public ViewScroller(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mBufferSize = 10;
        mLoadedViews = new LinkedList<View>();
        mRecycledViews = new LinkedList<View>();
        mScroller = new Scroller(context);

        final ViewConfiguration configuration = ViewConfiguration.get(context);
        // 一个距离，表示滑动时，移动距离大于这个距离才开始滑动
        mTouchSlop = configuration.getScaledTouchSlop();
        setStaticTransformationsEnabled(true);
        setSpacing((int) getResources().getDimension(R.dimen.space_size));
    }

    public void setSpacing(int spacing) {
        mSpacing = spacing;
    }

    public boolean isScrolling() {
        int delta = getScrollY() % mChildSize;
        return (delta != 0);
    }

    @Override
    protected boolean getChildStaticTransformation(View child, Transformation t) {
        final int childWidth = child.getWidth();
        final int childHeight = child.getHeight();

        // 旋转角
        int rotationAngle = getRotation(getDiffPercentFromCenter(child));
        t.clear();
        t.setTransformationType(Transformation.TYPE_MATRIX);

        mCamera.save();
        final Matrix matrix = t.getMatrix();
        final int rotation = Math.abs(rotationAngle);
        // 视角的上升，图片的缩放
        mCamera.translate(0.0f, 0.0f, 150.0f);

        //As the angle of the view gets less, zoom in
        if (rotation < mMaxRotationAngle) {
            float zoomAmount = (float) (mMaxZoom + (rotation * 1.5));
            mCamera.translate(0.0f, 0.0f, zoomAmount);
        }

        mCamera.rotateX(rotationAngle);
        mCamera.getMatrix(matrix);
        matrix.preTranslate(-(childWidth / 2), -(childHeight / 2));
        matrix.postTranslate((childWidth / 2), (childHeight / 2));
        mCamera.restore();

        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int childWidthMeasureSpec;
        int childHeightMeasureSpec;
        LayoutParams lp;

        // The children are given the same width and height as the workspace
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            childWidthMeasureSpec = widthMeasureSpec;
            childHeightMeasureSpec = heightMeasureSpec;

            View child = getChildAt(i);
            lp = child.getLayoutParams();
            if (lp != null) {
                if (lp.width != LayoutParams.WRAP_CONTENT &&
                        lp.width != LayoutParams.MATCH_PARENT) {
                    childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(lp.width, MeasureSpec.AT_MOST);
                }
                if (lp.height != LayoutParams.WRAP_CONTENT &&
                        lp.height != LayoutParams.MATCH_PARENT) {
                    childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(lp.height, MeasureSpec.AT_MOST);
                }
            }

            child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
        }

        mChildSize = (count > 0) ? getChildAt(0).getMeasuredHeight() : 1;
        mCenterChildTop = getMeasuredHeight() / 2 - mChildSize / 2;
        mChildSize += mSpacing;
        if (mChildSize < 0) {
            mChildSize = 1;
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childTop = mCenterChildTop;
        int childWidth = 0;
        int childHeight = 0;
        int width = r - l;

        final int count = getChildCount();
        for (int i = count - 1; i >= 0; i--) {
            final View child = getChildAt(i);
            childWidth = child.getMeasuredWidth();
            childHeight = child.getMeasuredHeight();
            child.layout((width - childWidth) / 2, childTop, (width + childWidth) / 2, childTop + childHeight);
            childTop += childHeight + mSpacing;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        final float y = ev.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastMotionY = y; // Remember where the motion event started
                break;
            case MotionEvent.ACTION_MOVE:
                final int deltaY = (int) (mLastMotionY - y);

                boolean yMoved = Math.abs(deltaY) > mTouchSlop;

                if (yMoved) {
                    // Scroll if the user moved far enough along the X axis
                    return true;
                }
                break;
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        super.onTouchEvent(ev);

        int childCount = getChildCount();
        if (childCount == 0)
            return false;

        final int action = ev.getAction();
        final float y = ev.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }

                mLastMotionY = y; // Remember where the motion event started
                mTouchState = mScroller.isFinished() ? STATE_NONE : STATE_SCROLLING;
                break;

            case MotionEvent.ACTION_MOVE:
                final int deltaY = (int) (mLastMotionY - y);

                boolean yMoved = Math.abs(deltaY) > mTouchSlop;

                if (yMoved) {
                    // Scroll if the user moved far enough along the X axis
                    mTouchState = STATE_SCROLLING;
                }

                if (mTouchState == STATE_SCROLLING) {
                    // Scroll to follow the motion event
                    mLastMotionY = y;

                    final int scrollY = getScrollY();
                    if (deltaY < 0) {
                        if (scrollY > 0) {
                            scrollBy(0, Math.max(-scrollY, deltaY));
                        }
                    } else if (deltaY > 0) {
                        final int availableToScroll = (childCount - 1) * mChildSize - scrollY;
                        if (availableToScroll > 0) {
                            scrollBy(0, Math.min(availableToScroll, deltaY));
                        }
                    } else {
                    }
                    return true;
                }
                break;

            case MotionEvent.ACTION_UP:
                if (mTouchState == STATE_SCROLLING) {
                    scrollToSlot();
                } else {
                    if (mListener != null) {
                        mListener.onItemClicked(this, mCurrentPosition);
                    }
                }

                mTouchState = STATE_NONE;
                break;

            case MotionEvent.ACTION_CANCEL:
                scrollToSlot();
                mTouchState = STATE_NONE;
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        } else if (mNextPosition != INVALID) {
            final int currentPosition = mCurrentPosition;
            mCurrentPosition = Math.max(0, Math.min(mNextPosition, getChildCount() - 1));
            mNextPosition = INVALID;
            if (mLastScrollDirection != 0) {
                postViewSwitched(mLastScrollDirection > 0, Math.abs(mCurrentPosition - currentPosition));
            }
        } else {
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            float percent = getDiffPercentFromCenter(child);
            if (mListener != null) {
                if (percent >= 0.f) {
                    mListener.onItemTransited(this, child, 0.f);
                } else {
                    mListener.onItemTransited(this, child, -percent);
                }
            }
            int rotation = getRotation(percent);
            if (rotation < mMaxRotationAngle) {
                child.setVisibility(View.VISIBLE);
            } else {
                child.setVisibility(View.INVISIBLE);
            }
        }
    }

    private float getDiffPercentFromCenter(View child) {
        final int childTop = child.getTop() - getScrollY();
        float childHeight = child.getHeight() + mSpacing;
        float diff = mCenterChildTop - childTop;
        return Math.min(Math.max(diff / childHeight, -1.f), 1.f);
    }

    private int getRotation(float diffPercent) {
        int rotationAngle = (int) (diffPercent * (mMaxRotationAngle - 50)) + 50;
        rotationAngle = Math.max(rotationAngle, 50);

        return rotationAngle;
    }

    private void scrollToSlot() {
        final int position = (getScrollY() + mChildSize / 2) / mChildSize;
        scrollToPosition(position);
    }

    private void scrollToPosition(int position) {
        mLastScrollDirection = position - mCurrentPosition;
        if (!mScroller.isFinished())
            return;

        position = Math.max(0, Math.min(position, getChildCount() - 1));
        mNextPosition = position;

        final int newY = getViewPos(position);
        final int deltaY = newY - getScrollY();
        mScroller.startScroll(0, getScrollY(), 0, deltaY, Math.abs(deltaY));
        invalidate();
    }

    private void setVisibleView(int indexInBuffer, boolean uiThread) {
        mCurrentPosition = Math.max(0, Math.min(indexInBuffer, getChildCount() - 1));
        int dy = (mCurrentPosition * mChildSize) - mScroller.getCurrY();
        mScroller.startScroll(mScroller.getCurrX(), mScroller.getCurrY(), 0, dy, 0);
        if (dy == 0)
            onScrollChanged(mScroller.getCurrX(), mScroller.getCurrY() + dy, mScroller.getCurrX(), mScroller.getCurrY() + dy);

        if (uiThread)
            invalidate();
        else
            postInvalidate();
    }

    public void setOnItemSelectListener(OnItemSelectListener listener) {
        mListener = listener;
    }

    public Adapter getAdapter() {
        return mAdapter;
    }

    public void setAdapter(Adapter adapter) {
        if (mAdapter != null) {
            mAdapter.unregisterDataSetObserver(mDataSetObserver);
        }

        mAdapter = adapter;

        if (mAdapter != null) {
            mAdapter.registerDataSetObserver(mDataSetObserver);
            int count = adapter.getCount();
            if (count > mBufferSize) {
                mBufferSize = count;
            }
        }
        setSelection(0);
    }

    public View getSelectedView() {
        return (mCurrentBufferIndex < mLoadedViews.size() ? mLoadedViews.get(mCurrentBufferIndex) : null);
    }

    public int getSelectedItemPosition() {
        return mCurrentAdapterIndex;
    }

    public void setSelection(int position) {
        mNextPosition = INVALID;
        mScroller.forceFinished(true);
        if (mAdapter == null)
            return;

        int adapterCount = mAdapter.getCount();
        if (adapterCount == 0)
            return;

        position = Math.min(Math.max(position, 0), adapterCount - 1);

        if (position == mCurrentAdapterIndex)
            return;

        arrangeViews(position);

        callItemSelectedListener();
    }

    private void recycleViews() {
        while (!mLoadedViews.isEmpty()) {
            recycleView(mLoadedViews.remove());
        }
    }

    private void recycleView(View v) {
        if (v == null)
            return;
        mRecycledViews.add(v);
        detachViewFromParent(v);
    }

    private View getRecycledView() {
        return (mRecycledViews.isEmpty() ? null : mRecycledViews.remove(0));
    }

    private void resetFocus() {
        recycleViews();
        removeAllViewsInLayout();

        for (int i = Math.max(0, mCurrentAdapterIndex - mBufferSize);
             i < Math.min(mAdapter.getCount(), mCurrentAdapterIndex + mBufferSize + 1); i++) {
            mLoadedViews.addLast(makeAndAddView(i, true));
            if (i == mCurrentAdapterIndex) {
                mCurrentBufferIndex = mLoadedViews.size() - 1;
            }
        }

        requestLayout();

        callItemSelectedListener();
    }

    private void postViewSwitched(boolean toRight, int delta) {
        final int adapterCount = mAdapter.getCount();
        int adapterIndex = mCurrentAdapterIndex;
        adapterIndex += toRight ? delta : -delta;
        adapterIndex = Math.max(Math.min(adapterCount - 1, adapterIndex), 0);
        arrangeViews(adapterIndex);

        callItemSelectedListener();
    }

    private void arrangeViews(int adapterIndex) {
        final int adapterCount = mAdapter.getCount();

        if (mCurrentAdapterIndex == INVALID) {
            recycleViews();

            if (adapterCount > 0) {
                makeAndAddView(adapterIndex, true);

                int loadCount = mBufferSize;
                int index = 0;

                for (int offset = adapterIndex - 1; offset >= 0 && index < loadCount; offset--, index++) {
                    makeAndAddView(offset, false);
                }

                mCurrentBufferIndex = index;

                index = 0;
                for (int offset = adapterIndex + 1; offset < adapterCount && index < loadCount; offset++, index++) {
                    makeAndAddView(offset, true);
                }
            }
        } else {
            int delta = adapterIndex - mCurrentAdapterIndex;
            int index = 0;

            if (delta > 0) { // to right -> remove left and add right
                index = mCurrentAdapterIndex + getChildCount() - mCurrentBufferIndex;
                for (int i = 0; i < delta && index < adapterCount; i++, index++) {
                    makeAndAddView(index, true);
                    recycleView(getChildAt(0));
                    mCurrentBufferIndex--;
                }
            } else {
                index = mCurrentAdapterIndex - mCurrentBufferIndex - 1;
                int last = getChildCount() - 1;
                for (int i = delta; i < 0 && index >= 0; i++, index--) {
                    recycleView(getChildAt(last));
                    makeAndAddView(index, false);
                    mCurrentBufferIndex++;
                }
            }

            mCurrentBufferIndex += delta;
        }

        mCurrentAdapterIndex = adapterIndex;

        requestLayout();
        setVisibleView(mCurrentBufferIndex, false);
    }

    private View setupChild(View child, boolean addToEnd, boolean recycle) {
        LayoutParams lp = child.getLayoutParams();
        if (lp == null) {
            lp = new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 0);
        }
        if (recycle) {
            attachViewToParent(child, (addToEnd ? -1 : 0), lp);
        } else {
            addViewInLayout(child, (addToEnd ? -1 : 0), lp, true);
        }
        return child;
    }

    private View makeAndAddView(int position, boolean addToEnd) {
        return makeAndAddView(position, addToEnd, getRecycledView());
    }

    private View makeAndAddView(int position, boolean addToEnd, View convertView) {
        View view = mAdapter.getView(position, convertView, this);
        if (view != convertView) {
            mRecycledViews.add(convertView);
        }
        return setupChild(view, addToEnd, view == convertView);
    }

    private int getViewPos(int position) {
        int pos = position * mChildSize;
        return pos;
    }

    private void callItemSelectedListener() {
        if (mListener != null) {
            mListener.onItemSelected(this, mLoadedViews.get(mLoadedViews.size() - 1 - mCurrentBufferIndex), mCurrentAdapterIndex);
        }
    }


    public static interface OnItemSelectListener {
        public void onItemSelected(ViewScroller viewScroller, View view, int position);

        public void onItemClicked(ViewScroller viewScroller, int position);

        public void onItemTransited(ViewScroller viewScroller, View view, float percent);
    }

}
