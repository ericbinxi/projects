package cn.com.mod.office.lightman.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 底部滚动条
 * Created by CAT on 2014/11/22.
 */
public class ScrollBar extends ImageView {
    private int sub = 5;
    private Paint p;
    private float offset;
    private RectF mRect;
    private boolean isFinish = true;

    public ScrollBar(Context context) {
        super(context);
        p = new Paint();
        p.setColor(Color.WHITE);
    }

    public ScrollBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        p = new Paint();
        p.setColor(Color.WHITE);
    }

    public void setSubCount(int sub) {
        this.sub = sub;
        invalidate();

    }

    public void scrollBy(float offset) {
        this.offset += offset * (4f / sub);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int w = getWidth();
        if (offset < 0) {
            offset = 0;
            isFinish = true;
        } else if (offset > w - w * 1.0f * (4f / sub)) {
            offset = w - w * 1.0f * (4f / sub);
            isFinish = true;
        } else {
            isFinish = false;
        }
        mRect = new RectF(offset, 0, offset + w * 1.0f * (4f / sub), getHeight());
        canvas.drawRect(mRect, p);
    }

    public void setColor(int color) {
        p.setColor(color);
        invalidate();
    }

    public boolean isFinished() {
        return isFinish;
    }
}