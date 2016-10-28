package cn.com.mod.office.lightman.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.joshua.common.util.ImageUtils;

/**
 * 圆形图片
 * Created by CAT on 2014/10/24.
 */
public class CircleImageView extends ImageView {
    public CircleImageView(Context context) {
        super(context);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    public void onDraw(Canvas canvas) {
        BitmapDrawable drawable = (BitmapDrawable) getDrawable();
        if (drawable == null) {
            return;
        }
        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }
        Bitmap b = drawable.getBitmap();
        Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);
        int width = getWidth() - getPaddingLeft();
        width = getHeight() - getPaddingTop() < width ? getHeight() - getPaddingTop() : width;
        Bitmap roundBitmap = ImageUtils.createCircleBitmap(bitmap, width / 2);
        canvas.drawBitmap(roundBitmap, getWidth() / 2 - width / 2, getHeight() / 2 - width / 2, null);
    }
}
