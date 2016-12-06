package cn.com.mod.office.lightman.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import cn.com.mod.office.lightman.R;

/**
 * Created by Administrator on 2016/10/27.
 */
public class LeftRightView extends View {

    private Context context;
    private Drawable horizonLine;
    private Drawable lamp;

    private int offset;
    private int width, height;
    //用于计算向左向左移动的次数
    private int count = 0;

    public LeftRightView(Context context) {
        super(context);
        init(context);
    }

    public LeftRightView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void setLeft() {
        if (count <= -15) return;
        count = count - 1;
        invalidate();
    }

    public void setRight() {
        if (count >= 15) return;
        count = count + 1;
        invalidate();
    }

    public LeftRightView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);

    }

    private void init(Context context) {
        this.context = context;
        horizonLine = context.getResources().getDrawable(R.drawable.ic_left_right_bg);
        lamp = context.getResources().getDrawable(R.drawable.ic_led_lf);

        this.post(new Runnable() {
            @Override
            public void run() {
                width = getWidth();
                height = getHeight();
                offset = horizonLine.getIntrinsicWidth() / 30;
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        horizonLine.setBounds((width - horizonLine.getIntrinsicWidth()) / 2, 0, width - (width - horizonLine.getIntrinsicWidth()) / 2, horizonLine.getIntrinsicHeight());
        horizonLine.draw(canvas);
        canvas.save();
        int left = (width - lamp.getIntrinsicWidth()) / 2 + offset * count;
        int right = left + lamp.getIntrinsicWidth();
        int top = 0;
        int bottom = lamp.getIntrinsicHeight();
        lamp.setBounds(left, top, right, bottom);
        lamp.draw(canvas);
        canvas.save();
    }
}
