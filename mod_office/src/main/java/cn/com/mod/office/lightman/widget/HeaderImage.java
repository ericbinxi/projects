package cn.com.mod.office.lightman.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.joshua.common.util.ImageUtils;

import cn.com.mod.office.lightman.R;

/**
 * 圆形图片
 * Created by CAT on 2014/10/24.
 */
public class HeaderImage extends ImageView {
    private Bitmap border;

    public HeaderImage(Context context) {
        super(context);
    }

    public HeaderImage(Context context, AttributeSet attrs) {
        super(context, attrs);
        border = ((BitmapDrawable) getResources().getDrawable(R.drawable.border_circle_img)).getBitmap();
    }

    public HeaderImage(Context context, AttributeSet attrs, int defStyle) {
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

        canvas.drawBitmap(border, new Rect(0, 0, border.getWidth(), border.getHeight()), new Rect(0, 0, getWidth(), getHeight()), null);
        canvas.drawBitmap(roundBitmap, new Rect(0, 0, roundBitmap.getWidth(), roundBitmap.getHeight()),
                new RectF(getWidth() * 0.01f, getHeight() * 0.01f, getWidth() * 0.99f, getHeight() * 0.99f), null);
    }
}
