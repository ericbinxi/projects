package cn.com.mod.office.lightman.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;

import com.joshua.common.util.ImageUtils;
import com.joshua.common.util.MaskUtils;
import com.joshua.common.util.ToastUtils;

import java.io.File;

import cn.com.mod.office.lightman.MyApplication;
import cn.com.mod.office.lightman.R;
import cn.com.mod.office.lightman.activity.base.BaseActivity;
import cn.com.mod.office.lightman.api.ILightMgrApi;
import cn.com.mod.office.lightman.entity.BaseResponse;
import cn.com.mod.office.lightman.widget.MaskImageView;

/**
 * 图片预览
 * Created by CAT on 2014/10/27.
 */
public class PhotoActivity extends BaseActivity {
    public static final String TAG = "PhotoActivity";

    public static final int WAY_SCENE_ICON = 1;
    public static final int WAY_USER_IMG = 2;

    private Button mPickPhoto;
    private Button mConfirm;
    private MaskImageView mImage;
    private MaskUtils mMaskUtils;
    private ToastUtils mToastUtils;
    private boolean isFirst = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        mMaskUtils = new MaskUtils(this);
        mToastUtils = new ToastUtils(this);
        mPickPhoto = (Button) findViewById(R.id.btn_pick_photo);
        mImage = (MaskImageView) findViewById(R.id.image);
        mConfirm = (Button) findViewById(R.id.btn_confirm);

        // 重新选择
        mPickPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int what = getIntent().getIntExtra("what", 0);
                Intent intent;
                switch (what) {
                    case UserInfoActivity.REQUEST_CODE_PICK_PHOTO:
                        intent = new Intent(Intent.ACTION_PICK);
                        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                        startActivityForResult(intent, UserInfoActivity.REQUEST_CODE_PICK_PHOTO);
                        break;
                    case UserInfoActivity.REQUEST_CODE_TAKE_PHOTO:
                        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, getIntent().getData());
                        startActivityForResult(intent, UserInfoActivity.REQUEST_CODE_TAKE_PHOTO);
                        break;
                }
            }
        });

        // 确定
        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = mImage.getCompressImage();
                ILightMgrApi client = MyApplication.getInstance().getClient();
                // 情景
                int way = getIntent().getIntExtra("way", -1);
                switch (way) {
                    case WAY_SCENE_ICON:
                        Intent intent = new Intent();
                        intent.putExtra("file", file);
                        setResult(RESULT_OK, intent);
                        finish();
                        break;
                    case WAY_USER_IMG:
                        mMaskUtils.show();
                        client.uploadUserImg(file, new ILightMgrApi.Callback<BaseResponse>() {
                            @Override
                            public void callback(int code, BaseResponse entity) {
                                mMaskUtils.cancel();
                                switch (code) {
                                    case CODE_TIMEOUT:
                                        mToastUtils.show(getString(R.string.tip_timeout));
                                        break;
                                    case CODE_NETWORK_ERROR:
                                        mToastUtils.show(getString(R.string.tip_network_connect_faild));
                                        break;
                                    case CODE_SUCCESS:
                                        if (entity.success) {
                                            setResult(RESULT_OK);
                                            finish();
                                        } else {
                                            mToastUtils.show(entity.msg);
                                        }
                                        break;
                                }
                            }
                        });
                        break;
                }
            }
        });

        if (getIntent() != null && getIntent().getData() != null && isFirst) {
            Bitmap bitmap = ImageUtils.compressCapacityFromUri(getApplicationContext(), getIntent().getData());
            mImage.setImageBitmap(bitmap);
            isFirst = false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case UserInfoActivity.REQUEST_CODE_TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    Bitmap bitmap = ImageUtils.compressCapacityFromUri(PhotoActivity.this, getIntent().getData());
                    mImage.setImageBitmap(bitmap);
                }
            case UserInfoActivity.REQUEST_CODE_PICK_PHOTO:
                if (resultCode == RESULT_OK && data != null) {
                    Bitmap bitmap = ImageUtils.compressCapacityFromUri(getApplicationContext(), data.getData());
                    mImage.setImageBitmap(bitmap);
                }
                break;
        }
    }

    @Override
    public void loadData() {

    }
}
