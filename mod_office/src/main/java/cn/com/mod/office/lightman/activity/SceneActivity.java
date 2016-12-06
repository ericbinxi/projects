//package cn.com.mod.office.lightman.activity;
//
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Environment;
//import android.provider.MediaStore;
//import android.support.v4.app.FragmentTransaction;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.KeyEvent;
//import android.view.View;
//import android.widget.TextView;
//
//import com.joshua.common.util.ImageUtils;
//import com.joshua.common.util.MaskUtils;
//import com.joshua.common.util.ToastUtils;
//
//import java.io.File;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Locale;
//
//import cn.com.mod.office.lightman.MyApplication;
//import cn.com.mod.office.lightman.R;
//import cn.com.mod.office.lightman.activity.base.BaseFragmentActivity;
//import cn.com.mod.office.lightman.api.ILightMgrApi;
//import cn.com.mod.office.lightman.entity.BaseResponse;
//import cn.com.mod.office.lightman.entity.DiySceneInfo;
//import cn.com.mod.office.lightman.view.BrightnessPanel;
//import cn.com.mod.office.lightman.view.ColorTempPanel;
//import cn.com.mod.office.lightman.view.RgbPanel;
//import cn.com.mod.office.lightman.widget.ColorPicker;
//import cn.com.mod.office.lightman.widget.HorizontalPager;
//import cn.com.mod.office.lightman.widget.ImageButtonSwitch;
//import cn.com.mod.office.lightman.widget.PhotoDialog;
//import cn.com.mod.office.lightman.widget.SeekBarPicker;
//
///**
// * 灯光调节界面
// * Created by CAT on 2014/11/17.
// */
//public class SceneActivity extends BaseFragmentActivity {
//    public static final String TAG = "SceneActivity";
//
//    public static final int REQUEST_PICK_IMAGE = 1001;
//
//    public static final int TYPE_ADD = 1;
//    public static final int TYPE_EDIT = 2;
//    // 选择图片
//    public static final int REQUEST_CODE_PICK_PHOTO = 0;
//    // 拍照
//    public static final int REQUEST_CODE_TAKE_PHOTO = 1;
//    // 编辑选择图片
//    public static final int REQUEST_CODE_EDIT_PICK_PHOTO = 2;
//    // 编辑拍照图片
//    public static final int REQUEST_CODE_EDIT_TAKE_PHOTO = 3;
//    public File mIcon;
//    int[] mTabs = new int[]{
//            R.id.tab1, R.id.tab2, R.id.tab3
//    };
//    private ILightMgrApi mClient;
//    private ToastUtils mToastUtils;
//    private MaskUtils mMaskUtils;
//    private View mGoBack;
//    private View mSave;
//    private HorizontalPager mPager;
//    private BrightnessPanel mBrightnessPanel;
//    private ColorTempPanel mColorTempPanel;
//    private RgbPanel mRgbPanel;
//    private ImageButtonSwitch mSwitch;
//    private TextView mName;
//    private String[] mLeds;
//    private String mSceneId;
//    private int lastBrightness = -1;
//    private int lastColorTemp = -1;
//    private int[] lastRgb = new int[]{-1, -1, -1};
//    private TextView mTab1;
//    private TextView mTab2;
//    private TextView mTab3;
//    private SendBrightnessTask brightnessTask;
//    private SendColorTempTask colorTempTask;
//    private SendRgbTask rgbTask;
//    private PhotoDialog mDialog;
//    private Uri mPhoto;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_scene);
//
//        mClient = MyApplication.getInstance().getClient();
//        mToastUtils = new ToastUtils(this);
//        mMaskUtils = new MaskUtils(this);
//
//        mTab1 = (TextView) findViewById(R.id.tab1);
//        mTab2 = (TextView) findViewById(R.id.tab2);
//        mTab3 = (TextView) findViewById(R.id.tab3);
//        mTab1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mPager.setCurrentScreen(0, true);
//            }
//        });
//        mTab2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mPager.setCurrentScreen(1, true);
//            }
//        });
//        mTab3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mPager.setCurrentScreen(2, true);
//            }
//        });
//
//        mPager = (HorizontalPager) findViewById(R.id.pager);
//        mGoBack = findViewById(R.id.ic_back);
//        mSave = findViewById(R.id.save);
//        mSwitch = (ImageButtonSwitch) findViewById(R.id.scene_switch);
//        mName = (TextView) findViewById(R.id.name);
//
//        mSwitch.setTrack(R.drawable.switch_track);
////        mSwitch.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.default_diy));
//        mSwitch.setSwitch(true);
//
//        mGoBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//
//        mPager.setOnScreenSwitchListener(new HorizontalPager.OnScreenSwitchListener() {
//            @Override
//            public void onScreenSwitched(int screen) {
//                for (int i = 0; i < mTabs.length; i++) {
//                    if (i == screen) {
//                        findViewById(mTabs[i]).setBackgroundResource(R.drawable.tab_select);
//                        ((TextView) findViewById(mTabs[i])).setTextColor(getResources().getColor(R.color.black));
//                    } else {
//                        findViewById(mTabs[i]).setBackgroundResource(R.drawable.tab);
//                        ((TextView) findViewById(mTabs[i])).setTextColor(getResources().getColor(R.color.white));
//                    }
//                }
//            }
//        });
//
//        mBrightnessPanel = new BrightnessPanel();
//        mColorTempPanel = new ColorTempPanel();
//        mRgbPanel = new RgbPanel();
//
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.add(R.id.pager, mBrightnessPanel);
//        transaction.add(R.id.pager, mColorTempPanel);
//        transaction.add(R.id.pager, mRgbPanel);
//        transaction.commit();
//
//        mBrightnessPanel.setPickerListener(new SeekBarPicker.SeekBarPickerListener() {
//            @Override
//            public void onProgressChanged(SeekBarPicker picker, int progress) {
//            }
//
//            @Override
//            public void onStart(SeekBarPicker picker) {
//                brightnessTask.flag = true;
//                mSwitch.setSwitch(true);
//                mSwitch.postInvalidate();
//            }
//
//            @Override
//            public void onStop(SeekBarPicker picker) {
//                brightnessTask.flag = false;
//                sendBrightness();
//            }
//        });
//
//        mBrightnessPanel.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (mLeds != null && mLeds.length > 0) {
//                    int brightness = mBrightnessPanel.getBrightness();
//                    if (brightness >= 0 && brightness <= 100) {
//                        mClient.setBrightness(mLeds, brightness);
//                    }
//                }
//                return false;
//            }
//        });
//
//        mColorTempPanel.setPickerListener(new SeekBarPicker.SeekBarPickerListener() {
//            @Override
//            public void onProgressChanged(SeekBarPicker picker, int progress) {
//
//            }
//
//            @Override
//            public void onStart(SeekBarPicker picker) {
//                colorTempTask.flag = true;
//                mSwitch.setSwitch(true);
//                mSwitch.postInvalidate();
//            }
//
//            @Override
//            public void onStop(SeekBarPicker picker) {
//                colorTempTask.flag = false;
//                sendColorTemp();
//            }
//        });
//
//        mColorTempPanel.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (mLeds != null && mLeds.length > 0) {
//                    int colorTemp = mColorTempPanel.getColorTemp();
//                    if (colorTemp >= 0 && colorTemp <= 255) {
//                        mClient.setColorTemp(mLeds, colorTemp);
//                    }
//                }
//                return false;
//            }
//        });
//
//        mRgbPanel.setPickerListener(new ColorPicker.ColorPickerListener() {
//            @Override
//            public void onColorChanged(ColorPicker picker, int red, int green, int blue) {
//            }
//
//            @Override
//            public void onStart(ColorPicker picker) {
//                rgbTask.flag = true;
//                mSwitch.setSwitch(true);
//                mSwitch.postInvalidate();
//            }
//
//            @Override
//            public void onStop(ColorPicker picker) {
//                rgbTask.flag = false;
//                sendRgb();
//            }
//        });
//
//        mRgbPanel.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (mLeds != null && mLeds.length > 0) {
//                    int[] rgb = mRgbPanel.getRgb();
//                    int red = rgb[0];
//                    int green = rgb[1];
//                    int blue = rgb[2];
//                    if (red >= 0 && red <= 255 && green >= 0 && green <= 255 && blue >= 0 && blue <= 255) {
//                        Log.d(TAG, "to Set RGB, red = " + red + ", green = " + green + ", blue= " + blue);
//                        mClient.setRGB(mLeds, red, green, blue);
//                    }
//                }
//                return false;
//            }
//        });
//
//        mSwitch.setImageButtonSwitchListener(new ImageButtonSwitch.ImageButtonSwitchListener() {
//            @Override
//            public void onImageClick(ImageButtonSwitch imageSwitch) {
//                if (mDialog == null) {
//                    mDialog = new PhotoDialog(SceneActivity.this, R.style.ActionSheetDialogStyle);
//                    mDialog.setPhotoDialogListener(new PhotoDialog.PhotoDialogListener() {
//                        @Override
//                        public void onPickPhotoClick() {
//                            Intent intent = new Intent(Intent.ACTION_PICK);
//                            intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
//                            SceneActivity.this.startActivityForResult(intent, REQUEST_CODE_PICK_PHOTO);
//                        }
//
//                        @Override
//                        public void onTackPhotoClick() {
//                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                            // 相机路径
//                            File dcimDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
//                            if (!dcimDir.exists()) {
//                                dcimDir.mkdir();
//                            }
//                            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Camera");
//                            if (!mediaStorageDir.exists()) {
//                                mediaStorageDir.mkdir();
//                            }
//                            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
//                            File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
//                            mPhoto = Uri.fromFile(mediaFile);
//                            intent.putExtra(MediaStore.EXTRA_OUTPUT, mPhoto);
//                            intent.putExtra(android.provider.MediaStore.EXTRA_VIDEO_QUALITY, 0);
//                            startActivityForResult(intent, REQUEST_CODE_TAKE_PHOTO);
//                        }
//                    });
//                }
//                mDialog.show();
//            }
//
//            @Override
//            public void onSwitchChange(ImageButtonSwitch imageSwitch, boolean isSwitch) {
//                if (mLeds != null && mLeds.length > 0) {
//                    if (isSwitch) {
//                        mClient.setBrightness(mLeds, 100);
//                    } else {
//                        mClient.setBrightness(mLeds, 0);
//                    }
//                }
//            }
//        });
//
//        mLeds = getIntent().getStringArrayExtra("leds");
//        int type = getIntent().getIntExtra("type", -1);
//        switch (type) {
//            case TYPE_ADD:
//                mSave.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (!validate()) {
//                            return;
//                        }
//
//                        final String name = (mName.getText() + "").replaceAll(" ", "");
//                        mMaskUtils.show();
//                        int[] rgb = mRgbPanel.getRgb();
//                        mClient.createDiyScene(getIntent().getStringExtra("roomId"), name, mBrightnessPanel.getBrightness(),
//                                mColorTempPanel.getColorTemp(), rgb[0], rgb[1], rgb[2], mIcon, new ILightMgrApi.Callback<BaseResponse>() {
//                                    @Override
//                                    public void callback(int code, BaseResponse entity) {
//                                        mMaskUtils.cancel();
//                                        switch (code) {
//                                            case CODE_TIMEOUT:
//                                                mToastUtils.show(getString(R.string.tip_timeout));
//                                                break;
//                                            case CODE_NETWORK_ERROR:
//                                                mToastUtils.show(getString(R.string.tip_network_connect_faild));
//                                                break;
//                                            case CODE_SUCCESS:
//                                                if (!entity.success) {
//                                                    mToastUtils.show(entity.msg);
//                                                } else {
//                                                    Intent data = new Intent();
//                                                    data.putExtra("id", entity.extra);
//                                                    data.putExtra("name", name);
//                                                    data.putExtra("icon", mIcon);
//                                                    setResult(RESULT_OK, data);
//                                                    finish();
//                                                }
//                                                break;
//                                        }
//                                    }
//                                });
//                    }
//                });
//                break;
//            case TYPE_EDIT:
//                mSceneId = getIntent().getStringExtra("sceneId");
//                mName.setText(getIntent().getStringExtra("name"));
//                mClient.getDiySceneImg(mSceneId, new ILightMgrApi.Callback<Bitmap>() {
//                    @Override
//                    public void callback(int code, Bitmap entity) {
//                        Log.v("SceneActivity", code + " is success->" + (code == CODE_SUCCESS) + " -:>" + entity);
//                        switch (code) {
//                            case CODE_SUCCESS:
//                                mSwitch.setImageBitmap(entity);
//                                break;
//                        }
//                    }
//                });
//                mMaskUtils.show();
//                mClient.getDiySceneInfo(mSceneId, new ILightMgrApi.Callback<DiySceneInfo>() {
//                    @Override
//                    public void callback(int code, DiySceneInfo entity) {
//                        mMaskUtils.cancel();
//                        switch (code) {
//                            case CODE_SUCCESS:
//                                mBrightnessPanel.setBrightness(entity.brightness);
//                                mColorTempPanel.setColorTemp(entity.colortemp);
//                                mRgbPanel.setRGB(new int[]{entity.red, entity.green, entity.blue});
//                                break;
//                        }
//                    }
//                });
//
//                mSave.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (!validate()) {
//                            return;
//                        }
//                        final String name = (mName.getText() + "").replaceAll(" ", "");
//                        mMaskUtils.show();
//                        int[] rgb = mRgbPanel.getRgb();
//                        mClient.modifyDiyScene(mSceneId, name,
//                                mBrightnessPanel.getBrightness(), mColorTempPanel.getColorTemp(), rgb[0], rgb[1], rgb[2], mIcon, new ILightMgrApi.Callback<BaseResponse>() {
//                                    @Override
//                                    public void callback(int code, BaseResponse entity) {
//                                        mMaskUtils.cancel();
//                                        switch (code) {
//                                            case CODE_TIMEOUT:
//                                                mToastUtils.show(getString(R.string.tip_timeout));
//                                                break;
//                                            case CODE_NETWORK_ERROR:
//                                                mToastUtils.show(getString(R.string.tip_network_connect_faild));
//                                                break;
//                                            case CODE_SUCCESS:
//                                                if (!entity.success) {
//                                                    mToastUtils.show(entity.msg);
//                                                } else {
//                                                    Intent data = new Intent();
//                                                    data.putExtra("id", mSceneId);
//                                                    data.putExtra("name", name);
//                                                    data.putExtra("icon", mIcon);
//                                                    setResult(RESULT_OK, data);
//                                                    finish();
//                                                }
//                                                break;
//                                        }
//                                    }
//                                });
//                    }
//                });
//                break;
//        }
//        brightnessTask = new SendBrightnessTask();
//        colorTempTask = new SendColorTempTask();
//        rgbTask = new SendRgbTask();
//        brightnessTask.start();
//        colorTempTask.start();
//        rgbTask.start();
//    }
//
//    private boolean validate() {
//        String name = mName.getText() + "";
//        if (TextUtils.isEmpty(name)) {
//            mToastUtils.show(getString(R.string.tip_scene_name_empty));
//            return false;
//        }
//        if (name.getBytes().length > 5 * 3) {
//            mToastUtils.show(getString(R.string.tip_scene_name_length));
//            return false;
//        }
//        int brightness = mBrightnessPanel.getBrightness();
//        if (brightness < 0 || brightness > 100) {
//            mToastUtils.show(getString(R.string.tip_brightness_format));
//            return false;
//        }
//        int colorTemp = mColorTempPanel.getColorTemp();
//        if (colorTemp < 0 || colorTemp > 255) {
//            mToastUtils.show(getString(R.string.tip_colortemp_format));
//            return false;
//        }
//        int[] rgb = mRgbPanel.getRgb();
//        for (int var : rgb) {
//            if (var < 0 || var > 255) {
//                mToastUtils.show(getString(R.string.tip_rgb_format));
//                return false;
//            }
//        }
//        return true;
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        switch (requestCode) {
//            case REQUEST_CODE_TAKE_PHOTO:
//                if (resultCode == RESULT_OK) {
//                    Intent intent = new Intent(SceneActivity.this, PhotoActivity.class);
//                    intent.setData(mPhoto);
//                    intent.putExtra("what", REQUEST_CODE_TAKE_PHOTO);
//                    intent.putExtra("sceneId", mSceneId);
//                    intent.putExtra("way", PhotoActivity.WAY_SCENE_ICON);
//                    startActivityForResult(intent, REQUEST_CODE_EDIT_TAKE_PHOTO);
//                }
//                break;
//            case REQUEST_CODE_PICK_PHOTO:
//                if (resultCode == RESULT_OK && data != null) {
//                    Intent intent = new Intent(SceneActivity.this, PhotoActivity.class);
//                    intent.setData(data.getData());
//                    intent.putExtra("what", REQUEST_CODE_PICK_PHOTO);
//                    intent.putExtra("sceneId", mSceneId);
//                    intent.putExtra("way", PhotoActivity.WAY_SCENE_ICON);
//                    startActivityForResult(intent, REQUEST_CODE_EDIT_PICK_PHOTO);
//                }
//                break;
//            case REQUEST_CODE_EDIT_TAKE_PHOTO:
//            case REQUEST_CODE_EDIT_PICK_PHOTO:
//                if (resultCode == RESULT_OK) {
//                    mIcon = (File) data.getSerializableExtra("file");
//                    Bitmap bitmap = ImageUtils.compressCapacityFromUri(SceneActivity.this, Uri.fromFile(mIcon));
//                    mSwitch.setImageBitmap(bitmap);
//                }
//                mDialog.cancel();
//                break;
//            case REQUEST_PICK_IMAGE:
//                mRgbPanel.setResultData(resultCode, resultCode == RESULT_OK ? data.getData() : null);
//                break;
//        }
//    }
//
//    @Override
//    protected void loadData() {
//
//    }
//
//    private void sendBrightness() {
//        int brightness = mBrightnessPanel.getBrightness();
//        if (brightness != lastBrightness && mLeds.length != 0) {
//            mClient.setBrightness(mLeds, brightness);
//        }
//        lastBrightness = brightness;
//    }
//
//    private void sendColorTemp() {
//        int colorTemp = mColorTempPanel.getColorTemp();
//        if (colorTemp != lastColorTemp && mLeds.length != 0) {
//            mClient.setColorTemp(mLeds, colorTemp);
//        }
//        lastColorTemp = colorTemp;
//    }
//
//    private void sendRgb() {
//        int[] rgb = mRgbPanel.getRgb();
//        if (!(rgb[0] == lastRgb[0] && rgb[1] == lastRgb[1] && rgb[2] == lastRgb[2]) && mLeds.length != 0) {
//            mClient.setRGB(mLeds, rgb[0], rgb[1], rgb[2]);
//        }
//        lastRgb = rgb;
//    }
//
//    private class SendBrightnessTask extends Thread {
//        boolean flag = false;
//
//        public void run() {
//            for (; ; ) {
//                if (flag) {
//                    sendBrightness();
//                }
//                try {
//                    Thread.sleep(500);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//    private class SendColorTempTask extends Thread {
//        boolean flag = false;
//
//        public void run() {
//            for (; ; ) {
//                if (flag) {
//                    sendColorTemp();
//                }
//                try {
//                    Thread.sleep(500);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//    private class SendRgbTask extends Thread {
//        boolean flag = false;
//
//        public void run() {
//            for (; ; ) {
//                if (flag) {
//                    sendRgb();
//                }
//                try {
//                    Thread.sleep(500);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//}
