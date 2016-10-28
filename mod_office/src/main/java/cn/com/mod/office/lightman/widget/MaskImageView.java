package cn.com.mod.office.lightman.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 中心圆形以外遮罩的图片
 * Created by CAT on 2014/10/27.
 */
public class MaskImageView extends ImageView {

    public MaskImageView(Context context) {
        super(context);
    }

    public MaskImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MaskImageView(Context context, AttributeSet attrs, int defStyle) {
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
        // 绘制原图
        super.onDraw(canvas);

        // 创建新的位图
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas newCanvas = new Canvas(bitmap);
        // 创建画笔
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.argb(125, 255, 255, 255));
        // 绘制遮罩层
        newCanvas.drawRect(0, 0, getWidth(), getHeight(), paint);

        paint.setAntiAlias(true);
        paint.setColor(Color.TRANSPARENT);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));

        int width = drawable.getBitmap().getWidth();
        int height = drawable.getBitmap().getHeight();
        int parentWidth = getWidth();
        int parentHeight = getHeight();

        // 计算出要挖空的圆的半径
        float result, percent;
        if (width == height) {
            percent = parentWidth > parentHeight ? (height + 0.0f) / parentHeight : (width + 0.0f) / parentWidth;
            result = width / percent;
        } else if (width > height) {
            percent = (width + 0.0f) / parentWidth;
            result = height / percent;
        } else {
            percent = (height + 0.0f) / parentHeight;
            result = width / percent;
        }
        // 绘制挖空的圆
        newCanvas.drawCircle(getWidth() / 2, getHeight() / 2, result / 2 - 1, paint);

        // 把遮罩层绘制在原图上
        canvas.drawBitmap(bitmap, 0, 0, null);
    }

    /**
     * 获取压缩后的文件
     *
     * @return
     */
    public File getCompressImage() {
        Bitmap image = ((BitmapDrawable) getDrawable()).getBitmap();
        String path = getContext().getExternalFilesDir("images").getAbsolutePath();
        File file = new File(path + "/tmp.jpg");
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}
