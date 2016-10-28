package com.joshua.common.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by CAT on 2014/9/19.
 */
public class ToastUtils {
    private Context mContenxt;
    private Toast mToast;

    public ToastUtils(Context context) {
        this.mContenxt = context;
    }

    public void show(String msg) {
        show(msg, Toast.LENGTH_SHORT);
    }

    public void show(String msg, int duration) {
        if (mToast != null) {
            mToast.setText(msg);
            mToast.setDuration(duration);
        } else {
            mToast = Toast.makeText(mContenxt, msg, duration);
        }
        mToast.show();
    }
    public void cancel() {
        mToast.cancel();
    }

    public static void show(Context context,int msg){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }
    public static void show(Context context,String msg){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }
}
