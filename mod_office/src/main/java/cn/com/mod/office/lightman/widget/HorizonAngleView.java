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
public class HorizonAngleView extends View {

    private Drawable compass;
    private Bitmap pin;
    private int angle;
    private int width, height;

    private float radius = 160;
    private float centerX,centerY;
    private OnAngleChangeListener onAngleChangeListener;

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

    public void setOnAngleChangeListener(OnAngleChangeListener onAngleChangeListener) {
        this.onAngleChangeListener = onAngleChangeListener;
    }

    private void init(Context context) {
//        getParent().requestDisallowInterceptTouchEvent(true);
        compass = context.getResources().getDrawable(R.drawable.ic_horizon_compass);
//        pin = context.getResources().getDrawable(R.drawable.ic_lamp_pin);
        pin = BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_horizon_pin);
        setPinListener();
        angle = 0;
        this.post(new Runnable() {
            @Override
            public void run() {
                width = getWidth();
                height = getHeight();
                radius = (160.0f/440)*compass.getIntrinsicWidth();
                centerX = width/2;
                centerY = 196.0f/314*pin.getHeight()+(height-pin.getHeight())/2;
            }
        });
    }

    private void setPinListener() {

    }

    public void setAngle(int angle) {
        if(angle>=-130&&angle<=130){
            this.angle = angle;
            invalidate();
        }
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
        float y = 196.0f/314*pin.getHeight()+(newBitmap.getHeight()-pin.getHeight())/2;
        rCanvas.rotate(angle,newBitmap.getWidth()/2,y);
        rCanvas.drawBitmap(pin,(newBitmap.getWidth()-pin.getWidth())/2,(newBitmap.getHeight()-pin.getHeight())/2,paint);
        canvas.drawBitmap(newBitmap,(width-newBitmap.getWidth())/2,(height-newBitmap.getHeight())/2,paint);
        canvas.save();
    }

    float downX,downY;
    //左右基准点
    float leftX;
    float leftY;
    float rightX;
    float rightY;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                leftX = centerX-radius;
                leftY = centerY;
                rightX = centerX+radius;
                rightY = centerY;
                double r = Math.sqrt((downX-centerX)*(downX-centerX)+(downY-centerY)*(downY-centerY));
                if(Math.abs(r-radius)<25){
                    double a = radius;
                    double b = getRadius(downX,downY);
                    if(downX<centerX&&downY<centerY){
                        double c = getRectangleLeftC(downX,downY);
                        double acrAngle = getCosAngle(a,b,c);
                        angle = (int)acrAngle-90;
                    }else if(downX<centerX&&downY>=centerY){
                        double c = getRectangleLeftC(downX,downY);
                        double acrAngle = getCosAngle(a,b,c);
                        angle = -(int)acrAngle-90;
                    }else if(downX>=centerX&&downY<centerY){
                        double c = getRectangleRightC(downX,downY);
                        double acrAngle = getCosAngle(a,b,c);
                        angle = 90-(int)acrAngle;
                    }else if(downX>centerX&&downY>centerY){
                        double c = getRectangleRightC(downX,downY);
                        double acrAngle = getCosAngle(a,b,c);
                        angle = (int)acrAngle+90;
                    }
                    if(angle<-130)angle = -130;
                    if(angle>130)angle = 130;
                    invalidate();
                    if (onAngleChangeListener!=null)
                        onAngleChangeListener.onAngleChange(angle);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }
    //        double a=Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
//        double b=Math.sqrt((x-x2)*(x-x2)+(y-y2)*(y-y2));
//        double c=Math.sqrt((x1-x)*(x1-x)+(y1-y)*(y1-y));
//        则角A的余弦值为double cosA=(b*b+c*c-a*a)/(2*b*c);
//        则角A的弧度为double arcA = Math.acos(cosA);
//        这个弧度是0-PI之间的，你要换成角度使用double angleA = arcA * 180 /Math.PI;

    private double getRadius(float x,float y){
        return Math.sqrt((x-centerX)*(x-centerX)+(y-centerY)*(y-centerY));
    }
    private double getRectangleLeftC(float x,float y){
        return Math.sqrt((x-leftX)*(x-leftX)+(y-leftY)*(y-leftY));
    }
    private double getRectangleRightC(float x,float y){
        return Math.sqrt((x-rightX)*(x-rightX)+(y-rightY)*(y-rightY));
    }

    private double getCosAngle(double a,double b,double c){
        double cosC = (a*a+b*b-c*c)/(2*a*b);
        double arcC = Math.acos(cosC);
        return arcC * 180 /Math.PI;
    }

    public interface OnAngleChangeListener{
        void onAngleChange(int angle);
    }
}
