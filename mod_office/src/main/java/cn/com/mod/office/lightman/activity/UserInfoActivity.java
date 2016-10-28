package cn.com.mod.office.lightman.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.joshua.common.util.ToastUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cn.com.mod.office.lightman.MyApplication;
import cn.com.mod.office.lightman.R;
import cn.com.mod.office.lightman.activity.base.BaseActivity;
import cn.com.mod.office.lightman.api.ILightMgrApi;
import cn.com.mod.office.lightman.entity.UserInfo;
import cn.com.mod.office.lightman.widget.CircleImageView;
import cn.com.mod.office.lightman.widget.PhotoDialog;

/**
 * 用户个人信息界面
 * Created by CAT on 2014/10/24.
 */
public class UserInfoActivity extends BaseActivity {
    public static final String TAG = "UserInfoActivity";

    // 选择图片
    public static final int REQUEST_CODE_PICK_PHOTO = 0;
    // 拍照
    public static final int REQUEST_CODE_TAKE_PHOTO = 1;
    // 编辑选择图片
    public static final int REQUEST_CODE_EDIT_PICK_PHOTO = 2;
    // 编辑拍照图片
    public static final int REQUEST_CODE_EDIT_TAKE_PHOTO = 3;

    private ToastUtils mToastUtils;
    private ImageView mGoBack;
    private TextView mUsername;
    private TextView mDepartment;
    private TextView mPosition;
    private ImageView mUserHeader;
    private Button mFeedback;
    private Button mModifyPass;
    private PhotoDialog mDialog;
    private Uri mPhoto;
    private UserInfo mUserInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);

        mToastUtils = new ToastUtils(this);
        mGoBack = (ImageView) findViewById(R.id.ic_back);
        mUsername = (TextView) findViewById(R.id.username);
        mDepartment = (TextView) findViewById(R.id.department);
        mPosition = (TextView) findViewById(R.id.position);
        mUserHeader = (ImageView) findViewById(R.id.user_header);
        mFeedback = (Button) findViewById(R.id.btn_feedback);
        mModifyPass = (Button) findViewById(R.id.btn_modify_pass);

        // 返回按钮
        mGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 反馈意见
        mFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserInfoActivity.this, FeedbackActivity.class);
                startActivity(intent);
            }
        });

        // 修改密码
        mModifyPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserInfoActivity.this, ModifyPasswordActivity.class);
                startActivity(intent);
            }
        });

        // 用户头像
        mUserHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDialog == null) {
                    mDialog = new PhotoDialog(UserInfoActivity.this, R.style.ActionSheetDialogStyle);
                    mDialog.setPhotoDialogListener(new PhotoDialog.PhotoDialogListener() {
                        @Override
                        public void onPickPhotoClick() {
                            Intent intent = new Intent(Intent.ACTION_PICK);
                            intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                            UserInfoActivity.this.startActivityForResult(intent, REQUEST_CODE_PICK_PHOTO);
                        }

                        @Override
                        public void onTackPhotoClick() {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            // 相机路径
                            File dcimDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                            if (!dcimDir.exists()) {
                                dcimDir.mkdir();
                            }
                            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Camera");
                            if (!mediaStorageDir.exists()) {
                                mediaStorageDir.mkdir();
                            }
                            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
                            File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
                            mPhoto = Uri.fromFile(mediaFile);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, mPhoto);
                            intent.putExtra(android.provider.MediaStore.EXTRA_VIDEO_QUALITY, 0);
                            startActivityForResult(intent, REQUEST_CODE_TAKE_PHOTO);
                        }
                    });
                }
                mDialog.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    Intent intent = new Intent(UserInfoActivity.this, PhotoActivity.class);
                    intent.setData(mPhoto);
                    intent.putExtra("what", REQUEST_CODE_TAKE_PHOTO);
                    intent.putExtra("way", PhotoActivity.WAY_USER_IMG);
                    startActivityForResult(intent, REQUEST_CODE_EDIT_TAKE_PHOTO);
                }
                break;
            case REQUEST_CODE_PICK_PHOTO:
                if (resultCode == RESULT_OK && data != null) {
                    Intent intent = new Intent(UserInfoActivity.this, PhotoActivity.class);
                    intent.setData(data.getData());
                    intent.putExtra("what", REQUEST_CODE_PICK_PHOTO);
                    intent.putExtra("way", PhotoActivity.WAY_USER_IMG);
                    startActivityForResult(intent, REQUEST_CODE_EDIT_PICK_PHOTO);
                }
                break;
            case REQUEST_CODE_EDIT_TAKE_PHOTO:
            case REQUEST_CODE_EDIT_PICK_PHOTO:
                if (resultCode == RESULT_OK) {
                    if (mUserInfo != null) {
                        MyApplication.getInstance().getClient().getUserImg(mUserInfo.username, new ILightMgrApi.Callback<Bitmap>() {
                            @Override
                            public void callback(int code, Bitmap bitmap) {
                                switch (code) {
                                    case CODE_SUCCESS:
                                        MyApplication.getInstance().writeLocalHeader(mUserInfo, bitmap);
                                        mUserHeader.setImageBitmap(bitmap);
                                        break;
                                }
                            }
                        });
                    }
                }
                mDialog.cancel();
                break;
        }
    }

    @Override
    public void loadData() {
        // 获取用户信息
        MyApplication.getInstance().getClient().getInfo(new ILightMgrApi.Callback<UserInfo>() {
            @Override
            public void callback(int code, UserInfo userInfo) {
                switch (code) {
                    case CODE_TIMEOUT:
                        mToastUtils.show(getString(R.string.tip_timeout));
                        break;
                    case CODE_NETWORK_ERROR:
                        mToastUtils.show(getString(R.string.tip_network_connect_faild));
                        break;
                    case CODE_SUCCESS:
                        mUserInfo = userInfo;
                        Bitmap bitmap = ((MyApplication) getApplicationContext()).getLocalHeader(mUserInfo.username);
                        if (bitmap != null) {
                            mUserHeader.setImageBitmap(bitmap);
                        }
                        mUsername.setText(userInfo.username);
                        mDepartment.setText(userInfo.department);
                        mPosition.setText(userInfo.position);
                        MyApplication.getInstance().getClient().getUserImg(userInfo.username, new ILightMgrApi.Callback<Bitmap>() {
                            @Override
                            public void callback(int code, Bitmap bitmap) {
                                switch (code) {
                                    case CODE_SUCCESS:
                                        MyApplication.getInstance().writeLocalHeader(mUserInfo, bitmap);
                                        mUserHeader.setImageBitmap(bitmap);
                                        break;
                                }
                            }
                        });
                        break;
                }
            }
        });
    }
}
