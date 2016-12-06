package cn.com.mod.office.lightman.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import cn.com.mod.office.lightman.R;

/**
 * Created by Administrator on 2016/10/30.
 */
public class LightAngleView extends View {

    private Drawable light_01;
    private Bitmap light_02;
    private int angle = 15;
    private float scaleX = 1.0f;
    private int width, height;

    private float radius;
    private float centerX;
    private float centerY;
    private OnAngleChangeListener onAngleChangeListener;

    public LightAngleView(Context context) {
        super(context);
        init(context);
    }

    public LightAngleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LightAngleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        light_01 = context.getResources().getDrawable(R.drawable.icon_light_01);
//        light_02 = context.getResources().getDrawable(R.drawable.icon_light_02);
        light_02 = BitmapFactory.decodeResource(context.getResources(),R.drawable.icon_light_02);
        angle = 15;
        this.post(new Runnable() {
            @Override
            public void run() {
                width = getWidth();
                height = getHeight();
                scaleX = (angle-10)/40.0f;
                centerX = width/2;
                centerY = 258.0f/292*light_01.getIntrinsicHeight()+(height-light_01.getIntrinsicHeight())/2;
                radius = (180.0f/390)*light_01.getIntrinsicWidth();
            }
        });
    }
    public void setAngle(int angle) {
        if(angle>=15&&angle<=50){
//            scaleX = angle/40.0f;
            scaleX = (angle-10)/40.0f;
            invalidate();
        }
    }

    public void setOnAngleChangeListener(OnAngleChangeListener onAngleChangeListener) {
        this.onAngleChangeListener = onAngleChangeListener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
         //background
        light_01.setBounds((width- light_01.getIntrinsicWidth())/2,(height- light_01.getIntrinsicHeight())/2,
                (width+ light_01.getIntrinsicWidth())/2,(height+ light_01.getIntrinsicHeight())/2);
        light_01.draw(canvas);
        canvas.save();

        Paint paint = new Paint();
        paint.setAntiAlias(true);

        Bitmap newBitmap = Bitmap.createBitmap(light_02.getWidth(),light_02.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas rCanvas = new Canvas(newBitmap);
        rCanvas.scale(scaleX,1.0f,(light_02.getWidth()/2),light_02.getHeight()/2);
        rCanvas.drawBitmap(light_02,(newBitmap.getWidth()-light_02.getWidth())/2,(newBitmap.getHeight()-light_02.getHeight())/2,paint);
        canvas.drawBitmap(newBitmap,(width-newBitmap.getWidth())/2,(height-newBitmap.getHeight())/2,paint);
        canvas.save();

    }

    float downX,downY;
    float curX,curY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                if(downX>=centerX){
                    if((downX-centerX)<=(radius+10)&&Math.abs(downY-centerY)<=30){
                        scaleX = (downX-centerX)/320;
                        if(scaleX<0.125f)scaleX = 0.125f;
                        if(scaleX>1)scaleX = 1;
                        angle = (int)(scaleX*40+10);
                        if(angle<15)angle = 15;
                        if(angle>50)angle = 50;
                        if(onAngleChangeListener!=null){
                            onAngleChangeListener.onAngleChange(angle);
                        }
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }
    public interface OnAngleChangeListener{
        void onAngleChange(int angle);
    }
}
