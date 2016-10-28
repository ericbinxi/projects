package cn.com.mod.office.lightman.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import cn.com.mod.office.lightman.R;

/**
 * Created by CAT on 2014/12/2.
 */
public class PhotoDialog extends Dialog {
    private PhotoDialogListener mListener;

    public PhotoDialog(Context context, int theme) {
        super(context, theme);
        init();
    }

    public void setPhotoDialogListener(PhotoDialogListener listener) {
        this.mListener = listener;
    }

    public void init() {
        View view = getLayoutInflater().inflate(R.layout.view_dialog, null);

        // 对话框退出按钮
        ImageView exit = (ImageView) view.findViewById(R.id.ic_exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });

        // 从相册选择
        Button pickPhoto = (Button) view.findViewById(R.id.btn_pick_photo);
        pickPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onPickPhotoClick();
                }
            }
        });

        // 拍照
        Button takePhoto = (Button) view.findViewById(R.id.btn_take_photo);
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onTackPhotoClick();
                }
            }
        });

        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        view.setMinimumWidth(metrics.widthPixels);
        setContentView(view);
        Window window = getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER | Gravity.BOTTOM);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.x = 0;
        lp.y = 0;
        window.setAttributes(lp);
        setCancelable(false);
        setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    cancel();
                }
                return true;
            }
        });
    }

    public interface PhotoDialogListener {
        public void onPickPhotoClick();

        public void onTackPhotoClick();
    }
}
