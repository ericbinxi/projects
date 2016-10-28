package cn.com.mod.office.lightman.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AbsoluteLayout;

/**
 * Created by Administrator on 2016/10/27.
 */
public class MyLayout extends AbsoluteLayout {
    public MyLayout(Context context) {
        super(context);
    }

    public MyLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(widthSize,widthSize);
    }

}
