package cn.com.mod.office.lightman.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import cn.com.mod.office.lightman.R;

/**
 * Created by Administrator on 2016/12/3.
 */
public class RotateHorizonView extends View {

    private Context context;
    private Drawable pin;
    private double angle;
    private int width, height;
    private Paint paint;
    private float centerX,centerY;
    private OnRotateCallback callback;

    public RotateHorizonView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public RotateHorizonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public RotateHorizonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    public void setCallback(OnRotateCallback callback) {
        this.callback = callback;
    }

    public void setAngle(double angle) {
        this.angle = angle;
        invalidate();
    }

    private void init(){
        paint = new Paint();
        paint.setAntiAlias(true);
        pin = context.getResources().getDrawable(R.drawable.ic_horizon_pin);
//        pin = BitmapFactory.decodeResource(context.getResources(),R.drawable.img_pointer);
        this.post(new Runnable() {
            @Override
            public void run() {
                width = getWidth();
                height = getHeight();
                centerX = width/2;
                centerY = 183.0f/369*pin.getIntrinsicHeight()+(height-pin.getIntrinsicHeight())/2;
                invalidate();
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        pin.setBounds((width-pin.getIntrinsicWidth())/2,(height-pin.getIntrinsicHeight())/2,
                (width+pin.getIntrinsicWidth())/2,(height+pin.getIntrinsicHeight())/2);
        canvas.rotate((float) angle,centerX,centerY);
        Log.e("--------","angle:"+angle);
        pin.draw(canvas);
        canvas.save();
//        canvas.drawBitmap(pin,(width-pin.getIntrinsicWidth())/2,(height-pin.getIntrinsicHeight())/2,paint);
    }

    private float downX,downY;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float curX = event.getX();
                float curY = event.getY();
                double moveAngle = getAngle(downX,downY,curX,curY);
                //计算滑动方向
                if(curX<centerX&&curY<centerY){
                    if(curX<downX&&curY>=downY){
                        //向下滑动  角度减小
                        angle = angle - moveAngle;
                    }else if(curX>downX&&curY<downY){
                        angle = angle + moveAngle;
                    }
                }else if(curX<centerX&&curY>=centerY){
                    if(curX>downX&&curY>downY){
                        angle = angle - moveAngle;
                    }else if(curX<downX&&curY<downY){
                        angle = angle + moveAngle;
                    }
                }else if(curX>=centerX&&curY<centerY){
                    if(curX>downX&&curY>downY){
                        angle = angle + moveAngle;
                    }else if(curX<downX&&curY<downY){
                        angle = angle - moveAngle;
                    }
                }else if(curX>=centerX&&curY>centerY){
                    if(curX<downX&&curY>downY){
                        angle = angle + moveAngle;
                    }else if(curX>downX&&curY<downY){
                        angle = angle - moveAngle;
                    }
                }
                if(angle>=130)angle = 130;
                if(angle<=-130)angle = -130;
                invalidate();
                downX = curX;
                downY = curY;
                if(callback!=null)
                    callback.onRotateChange((int)angle,false);
                break;
            case MotionEvent.ACTION_UP:
                if(callback!=null)
                    callback.onRotateChange((int)angle,true);
                break;
        }
        return true;
    }

    private double getAngle(float downX,float downY,float curX,float curY){
//        double a=Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
//        double b=Math.sqrt((x-x2)*(x-x2)+(y-y2)*(y-y2));
//        double c=Math.sqrt((x1-x)*(x1-x)+(y1-y)*(y1-y));
//        则角A的余弦值为double cosA=(b*b+c*c-a*a)/(2*b*c);
//        则角A的弧度为double arcA = Math.acos(cosA);
//        这个弧度是0-PI之间的，你要换成角度使用double angleA = arcA * 180 /Math.PI;
        double a = Math.sqrt((downX-curX)*(downX-curX)+(downY-curY)*(downY-curY));
        double b = Math.sqrt((downX-centerX)*(downX-centerX)+(downY-centerY)*(downY-centerY));
        double c = Math.sqrt((centerX-curX)*(centerX-curX)+(centerY-curY)*(centerY-curY));
        double cosA = (b*b+c*c-a*a)/(2*b*c);
        double arcA = Math.acos(cosA);
        return arcA * 180 /Math.PI;
    }

    public interface OnRotateCallback{
        void onRotateChange(int rotate, boolean callback);
    }
}
