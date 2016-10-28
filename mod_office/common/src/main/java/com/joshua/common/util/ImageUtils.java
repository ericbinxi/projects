package com.joshua.common.util;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;

import java.io.IOException;

/**
 * Created by CAT on 2014/11/3.
 */
public class ImageUtils {
    private ImageUtils() {
    }

    /**
     * 生成圆形图片
     *
     * @param bmp    原图片
     * @param radius 半径
     * @return 指定半径的圆形图片，以中心为圆点
     */
    public static Bitmap createCircleBitmap(Bitmap bmp, int radius) {
        Bitmap scaledSrcBmp;
        int diameter = radius * 2;
        // 为了防止宽高不相等，造成圆形图片变形，因此截取长方形中处于中间位置最大的正方形图片
        int bmpWidth = bmp.getWidth();
        int bmpHeight = bmp.getHeight();
        int squareWidth = 0, squareHeight = 0;
        int x = 0, y = 0;
        Bitmap squareBitmap;
        if (bmpHeight > bmpWidth) {// 高大于宽
            squareWidth = squareHeight = bmpWidth;
            x = 0;
            y = (bmpHeight - bmpWidth) / 2;
            // 截取正方形图片
            squareBitmap = Bitmap.createBitmap(bmp, x, y, squareWidth,
                    squareHeight);
        } else if (bmpHeight < bmpWidth) {// 宽大于高
            squareWidth = squareHeight = bmpHeight;
            x = (bmpWidth - bmpHeight) / 2;
            y = 0;
            squareBitmap = Bitmap.createBitmap(bmp, x, y, squareWidth,
                    squareHeight);
        } else {
            squareBitmap = bmp;
        }

        if (squareBitmap.getWidth() != diameter
                || squareBitmap.getHeight() != diameter) {
            scaledSrcBmp = Bitmap.createScaledBitmap(squareBitmap, diameter,
                    diameter, true);

        } else {
            scaledSrcBmp = squareBitmap;
        }
        Bitmap output = Bitmap.createBitmap(scaledSrcBmp.getWidth(),
                scaledSrcBmp.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, scaledSrcBmp.getWidth(),
                scaledSrcBmp.getHeight());

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(scaledSrcBmp.getWidth() / 2, scaledSrcBmp.getHeight() / 2, scaledSrcBmp.getWidth() / 2 - 1, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(scaledSrcBmp, rect, rect, paint);
//        bitmap回收(recycle导致在布局文件XML看不到效果)
        bmp.recycle();
        squareBitmap.recycle();
        scaledSrcBmp.recycle();
        bmp = null;
        squareBitmap = null;
        scaledSrcBmp = null;
        return output;
    }


    /**
     * 从URI获取压缩图像
     *
     * @param uri 图像URI
     * @return 压缩后的图像
     */
    public static Bitmap compressCapacityFromUri(Context context, Uri uri) {
        try {
            AssetFileDescriptor descriptor = context.getContentResolver().openAssetFileDescriptor(uri, "r");
            BitmapFactory.Options option = new BitmapFactory.Options();
            option.inJustDecodeBounds = true;
            Bitmap tempBit = BitmapFactory.decodeFileDescriptor(descriptor.getFileDescriptor(), null, option);
            option.inJustDecodeBounds = false;
            int w = option.outWidth;
            int h = option.outHeight;
            float ww = 200f, hh = 200f;
            int be = 1;
            if (w > h && w > ww) {
                be = (int) (w / ww);
            } else if (w < h && h > hh) {
                be = (int) (h / hh);
            } else if (w == h) {
                if (w > ww) {
                    be = (int) (w / ww);
                } else if (h > hh) {
                    be = (int) (h / hh);
                }
            }
            if (be <= 0) {
                be = 1;
            }
            option.inSampleSize = be;
            option.inPreferredConfig = Bitmap.Config.RGB_565;
            option.inPurgeable = true;
            option.inInputShareable = true;
            tempBit = BitmapFactory.decodeFileDescriptor(descriptor.getFileDescriptor(), null, option);
            return tempBit;
        } catch (IOException e) {
        }
        return null;
    }
}
