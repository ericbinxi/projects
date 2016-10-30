package cn.com.mod.office.lightman.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import cn.com.mod.office.lightman.R;

/**
 * Created by Administrator on 2016/10/30.
 */
public class HorizonAngleView extends View {

    private Drawable compass;
    private Bitmap pin;
    private int angle;
    private int width, height;

    public HorizonAngleView(Context context) {
        super(context);
        init(context);
    }

    public HorizonAngleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HorizonAngleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        compass = context.getResources().getDrawable(R.drawable.ic_horizon_compass);
//        pin = context.getResources().getDrawable(R.drawable.ic_lamp_pin);
        pin = BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_horizon_pin);
        angle = 0;
        this.post(new Runnable() {
            @Override
            public void run() {
                width = getWidth();
                height = getHeight();
            }
        });
    }
    public void setAngle(int angle) {
        this.angle = angle;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
         //background
        compass.setBounds((width-compass.getIntrinsicWidth())/2,(height-compass.getIntrinsicHeight())/2,
                (width+compass.getIntrinsicWidth())/2,(height+compass.getIntrinsicHeight())/2);
        compass.draw(canvas);
        canvas.save();

        Paint paint = new Paint();
        paint.setAntiAlias(true);

        Bitmap newBitmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        Canvas rCanvas = new Canvas(newBitmap);
//        pin.setBounds((width-pin.getIntrinsicWidth())/2,(height-pin.getIntrinsicHeight())/2,
//                (width+pin.getIntrinsicWidth())/2,(height+pin.getIntrinsicHeight())/2);
        rCanvas.rotate(angle,newBitmap.getWidth()/2,(height-compass.getIntrinsicHeight())/2+compass.getIntrinsicHeight()/2);
        rCanvas.drawBitmap(pin,(newBitmap.getWidth()-pin.getWidth())/2,(newBitmap.getHeight()-pin.getHeight())/2,paint);
//        pin.draw(rCanvas);

//        Paint paint = new Paint();
//        paint.setAntiAlias(true);
        canvas.drawBitmap(newBitmap,(width-newBitmap.getWidth())/2,(height-newBitmap.getHeight())/2,paint);
        canvas.save();
    }
}
