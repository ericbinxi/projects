package com.joshua.common.util;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.widget.ProgressBar;

import com.joshua.common.R;

/**
 * Created by CAT on 2014/8/28.
 */
public class MaskUtils {
    private Dialog mDialog;

    public MaskUtils(Context context) {
        mDialog = new Dialog(context, R.style.ProgressDialog);
        mDialog.setContentView(new ProgressBar(context));
        mDialog.setCancelable(false);
        mDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_BACK:
                        cancel();
                        break;
                }
                return true;
            }
        });
    }

    public void show() {
        mDialog.show();
    }

    public void cancel() {
        mDialog.cancel();
    }
}
