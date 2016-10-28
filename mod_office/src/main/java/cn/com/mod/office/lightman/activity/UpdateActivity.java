package cn.com.mod.office.lightman.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

import cn.com.mod.office.lightman.MyApplication;
import cn.com.mod.office.lightman.R;
import cn.com.mod.office.lightman.activity.base.BaseActivity;
import cn.com.mod.office.lightman.api.ILightMgrApi;
import cn.com.mod.office.lightman.entity.ApkVersionInfo;

/**
 * 更新界面
 * Created by CAT on 2014/10/25.
 */
public class UpdateActivity extends BaseActivity {
    public static final String TAG = "UpdateActivity";

    private ImageView mGoBack;
    private TextView mVersion;
    private boolean mIsDownloading;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        mGoBack = (ImageView) findViewById(R.id.ic_back);
        mGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkForExit();
            }
        });
        mVersion = (TextView) findViewById(R.id.version);
    }

    // 退出之前检测是否正在更新，通过返回true，否则返回false
    private void checkForExit() {
        if (mIsDownloading) {
            AlertDialog dialog = new AlertDialog.Builder(UpdateActivity.this)
                    .setTitle(getString(R.string.update_cancel_dialog_title))
                    .setMessage(R.string.update_cancel_dialog_msg)
                    .setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton(getString(R.string.cancel), null)
                    .show();
        } else {
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                checkForExit();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void loadData() {
        MyApplication.getInstance().getClient().getApkVersion(new ILightMgrApi.Callback<ApkVersionInfo>() {
            @Override
            public void callback(int code, ApkVersionInfo entity) {
                switch (code) {
                    case CODE_SUCCESS:
                        mVersion.setText(entity.version);
                        try {
                            int localVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
                            int globalVersion = Integer.parseInt(entity.version);
                            if (localVersion < globalVersion) {
                                AlertDialog dialog = new AlertDialog.Builder(UpdateActivity.this)
                                        .setTitle(getString(R.string.update_dialog_title))
                                        .setMessage(getString(R.string.update_dialog_msg))
                                        .setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressbar);
                                                MyApplication.getInstance().getClient().bindProgressHandler(progressBar);
                                                mIsDownloading = true;
                                                MyApplication.getInstance().getClient().getApkFile(new ILightMgrApi.Callback<File>() {
                                                    @Override
                                                    public void callback(int code, File entity) {
                                                        MyApplication.getInstance().getClient().unbindProgressHandler();
                                                        mIsDownloading = false;
                                                        switch (code) {
                                                            case CODE_SUCCESS:
                                                                Intent installIntent = new Intent(Intent.ACTION_VIEW);
                                                                installIntent.setDataAndType(Uri.fromFile(entity), "application/vnd.android.package-archive");
                                                                startActivity(installIntent);
                                                                break;
                                                        }
                                                    }
                                                });
                                                final TextView textView = (TextView) findViewById(R.id.label_progress);
                                                new Timer().schedule(new TimerTask() {
                                                    @Override
                                                    public void run() {
                                                        runOnUiThread(new Runnable() {
                                                            DecimalFormat format = new DecimalFormat("0.00");

                                                            @Override
                                                            public void run() {
                                                                float progress = progressBar.getProgress() * 100.0f / progressBar.getMax();
                                                                textView.setText(format.format(progress) + "%");
                                                            }
                                                        });
                                                    }
                                                }, 0, 500);
                                            }
                                        })
                                        .setNegativeButton(getString(R.string.cancel), null)
                                        .show();
                            }
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        });
    }
}
