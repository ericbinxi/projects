package com.joshua.common.util;

import android.content.Context;

/**
 * Created by CAT on 2014/9/2.
 */
public class PxAndDp {
    private PxAndDp() {

    }

    public static int dip2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static int px2dip(Context context, float px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }
}
