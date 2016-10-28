package cn.com.mod.office.lightman.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import cn.com.mod.office.lightman.R;

/**
 * Created by Administrator on 2016/10/27.
 */
public class UpDownView extends View {

    private Context context;
    private Drawable horizonDrawable;
    private Drawable upDownDrawable;

    private boolean isUp = false;
    private int upOffset;
    private int width,height;

    public UpDownView(Context context) {
        super(context);
        init(context);
    }

    public UpDownView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public UpDownView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        horizonDrawable = context.getResources().getDrawable(R.drawable.ic_horizon);
        upDownDrawable = context.getResources().getDrawable(R.drawable.ic_up_down);
        upOffset = upDownDrawable.getIntrinsicHeight()/2;
        this.post(new Runnable() {
            @Override
            public void run() {
                width = getWidth();
                height = getHeight();
            }
        });
    }

    public void setUp(boolean up) {
        isUp = up;
        invalidate();
    }

    public boolean isUp() {
        return isUp;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        horizonDrawable.setBounds((width-horizonDrawable.getIntrinsicWidth())/2,height*2/3,width-(width-horizonDrawable.getIntrinsicWidth())/2,height*2/3+horizonDrawable.getIntrinsicHeight());
        horizonDrawable.draw(canvas);
        canvas.save();
        if(isUp){
            int left = width/2-upDownDrawable.getIntrinsicWidth()/2;
            int right = left+upDownDrawable.getIntrinsicWidth();
            int bottom = height*2/3-20;
            int top = bottom-upDownDrawable.getIntrinsicHeight();
            upDownDrawable.setBounds(left,top,right,bottom);
        }else{
            int left = width/2-upDownDrawable.getIntrinsicWidth()/2;
            int right = left+upDownDrawable.getIntrinsicWidth();
            int bottom = height*2/3-upOffset;
            int top = bottom-upDownDrawable.getIntrinsicHeight();
            upDownDrawable.setBounds(left,top,right,bottom);
        }
        upDownDrawable.draw(canvas);
        canvas.save();

    }
}
