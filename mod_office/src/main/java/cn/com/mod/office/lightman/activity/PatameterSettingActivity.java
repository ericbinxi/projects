package cn.com.mod.office.lightman.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.joshua.common.util.MaskUtils;
import com.joshua.common.util.ToastUtils;

import cn.com.mod.office.lightman.MyApplication;
import cn.com.mod.office.lightman.R;
import cn.com.mod.office.lightman.activity.base.BaseFragmentActivity;
import cn.com.mod.office.lightman.activity.fragment.LocationFragment;
import cn.com.mod.office.lightman.activity.fragment.ParameterFragment;
import cn.com.mod.office.lightman.api.BaseResp;
import cn.com.mod.office.lightman.api.ILightMgrApi;
import cn.com.mod.office.lightman.entity.LampParam;
import cn.com.mod.office.lightman.entity.ParamEntity;
import cn.com.mod.office.lightman.widget.ColorPicker;
import cn.com.mod.office.lightman.widget.HorizontalPager;
import cn.com.mod.office.lightman.widget.SeekBarPicker;

public class PatameterSettingActivity extends BaseFragmentActivity implements View.OnClickListener {

    public static final int REQUEST_BACKUP_GET_PARAM = 101;
    public static final int REQUEST_PICK_IMAGE = 1001;

    private ILightMgrApi mClient;
    private ToastUtils mToastUtils;
    private MaskUtils mMaskUtils;
    private View mGoBack;
    private View mSave;
    private HorizontalPager mPager;
    private String[] mLeds;
    private String roomId;

    private ParameterFragment parameterFragemnt;
    private LocationFragment locationFragment;

    //使用备份参数相关变量
    private ParamEntity paramEntity;

    private TextView mTab1;
    private TextView mTab2;

    private int lastBrightness = -1;
    private int lastColorTemp = -1;
    private int[] lastRgb = new int[]{-1, -1, -1};

    private LinearLayout ll_menu, ll_call, ll_copy;

    public boolean adjust = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patameter_setting);
        init();
        initListener();
    }

    private void initListener() {
        mSave.setOnClickListener(this);
        mGoBack.setOnClickListener(this);
        ll_call.setOnClickListener(this);
        ll_copy.setOnClickListener(this);

        //颜色
        parameterFragemnt.setColorPickerListener(new ColorPicker.ColorPickerListener() {
            @Override
            public void onColorChanged(ColorPicker picker, int red, int green, int blue) {

            }

            @Override
            public void onStart(ColorPicker picker) {
            }

            @Override
            public void onStop(ColorPicker picker) {
                sendRgb();
            }
        });
        //亮度
        parameterFragemnt.setBrightPickerListener(new SeekBarPicker.SeekBarPickerListener() {
            @Override
            public void onProgressChanged(SeekBarPicker picker, int progress) {

            }

            @Override
            public void onStart(SeekBarPicker picker) {
            }

            @Override
            public void onStop(SeekBarPicker picker) {
                sendBrightness();
            }
        });
        //色温
        parameterFragemnt.setColorTempPickerListener(new SeekBarPicker.SeekBarPickerListener() {
            @Override
            public void onProgressChanged(SeekBarPicker picker, int progress) {

            }

            @Override
            public void onStart(SeekBarPicker picker) {
            }

            @Override
            public void onStop(SeekBarPicker picker) {
                sendColorTemp();
            }
        });

        parameterFragemnt.setOnBrightEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                int brightness = parameterFragemnt.getBrightness();
                if (brightness < 0 || brightness > 100) {
                    mToastUtils.show(getString(R.string.tip_brightness_format));
                    return false;
                }

                sendBrightness();
                return false;
            }
        });
        parameterFragemnt.setOnColorTempEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                int colorTemp = parameterFragemnt.getColorTemp();
                if (colorTemp < 0 || colorTemp > 255) {
                    mToastUtils.show(getString(R.string.tip_colortemp_format));
                    return false;
                }

                sendColorTemp();
                return false;
            }
        });
        parameterFragemnt.setOnRGBEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                int[] rgb = parameterFragemnt.getRgb();
                for (int var : rgb) {
                    if (var < 0 || var > 255) {
                        mToastUtils.show(getString(R.string.tip_rgb_format));
                        return false;
                    }
                }
                sendRgb();
                return false;
            }
        });
    }

    private void init() {

        mClient = MyApplication.getInstance().getClient();
        mToastUtils = new ToastUtils(this);
        mMaskUtils = new MaskUtils(this);

        ll_menu = (LinearLayout) findViewById(R.id.ll_menu);
        ll_call = (LinearLayout) findViewById(R.id.ll_call);
        ll_copy = (LinearLayout) findViewById(R.id.ll_copy);

        mSave = findViewById(R.id.save);
        mGoBack = findViewById(R.id.ic_back);
        mPager = (HorizontalPager) findViewById(R.id.pager);

        mTab1 = (TextView) findViewById(R.id.tab1);
        mTab2 = (TextView) findViewById(R.id.tab2);
        mTab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPager.setCurrentScreen(0, true);
                mTab1.setBackgroundResource(R.drawable.tab_select);
                mTab2.setBackgroundResource(R.drawable.tab);
            }
        });
        mTab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPager.setCurrentScreen(1, true);
                mTab2.setBackgroundResource(R.drawable.tab_select);
                mTab1.setBackgroundResource(R.drawable.tab);
            }
        });
        mPager.setOnScreenSwitchListener(new HorizontalPager.OnScreenSwitchListener() {
            @Override
            public void onScreenSwitched(int screen) {
                if (screen == 0) {
                    mTab1.setBackgroundResource(R.drawable.tab_select);
                    mTab2.setBackgroundResource(R.drawable.tab);
                } else if (screen == 1) {
                    mTab2.setBackgroundResource(R.drawable.tab_select);
                    mTab1.setBackgroundResource(R.drawable.tab);
                }
            }
        });
        mPager.setCurrentScreen(0, false);

        parameterFragemnt = new ParameterFragment();
        locationFragment = new LocationFragment();
        locationFragment.setOnOperateLampDegreeListener(new LocationFragment.OnOperateLampDegreeListener() {
            @Override
            public void setLampHDegree(int degree) {
                setLampHorizon(degree);
            }

            @Override
            public void setLampVDegree(int degree) {
                setLampVertical(degree);
            }

            @Override
            public void setLampLDegree(int degree) {
                setLampLight(degree);
            }

            @Override
            public void setLampMovement(int degree) {
                setLampMove(degree);
            }
        });
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.pager, parameterFragemnt);
        transaction.add(R.id.pager, locationFragment);
        transaction.commit();

        mLeds = getIntent().getStringArrayExtra("leds");
        roomId = getIntent().getStringExtra("roomId");
        if(getIntent().hasExtra("adjust")){
            adjust = getIntent().getBooleanExtra("adjust",false);
        }
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_call://调用参数
                ll_menu.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(this, ParamsListActivity.class);
                if(adjust&&mLeds!=null&&mLeds.length>0){
                    intent.putExtra("adjust",adjust);
                    intent.putExtra("leds",mLeds);
                    intent.putExtra("roomId",roomId);
                }
                startActivityForResult(intent, REQUEST_BACKUP_GET_PARAM);
                break;
            case R.id.ll_copy://备份参数
                ll_menu.setVisibility(View.INVISIBLE);
                Intent intent1 = new Intent(this, SaveParamsActivity.class);
                //将颜色值等传递过去
                intent1.putExtra("brightness", parameterFragemnt.getBrightness());
                intent1.putExtra("temp", parameterFragemnt.getColorTemp());
                intent1.putExtra("rgb", parameterFragemnt.getRgb());
                startActivity(intent1);
                break;
            case R.id.save:
                if (ll_menu.isShown()) {
                    ll_menu.setVisibility(View.GONE);
                } else {
                    ll_menu.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.ic_back:
                setResultData();
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        setResultData();
        super.onBackPressed();
    }

    private void setResultData() {
        if(!adjust){
            LampParam param = new LampParam();
            param.setLamp_h_degree(locationFragment.getCurrentHorizonAngle()+"");
            param.setLamp_v_degree(locationFragment.getCurrentVerticalAngle()+"");
            param.setLamp_l_degree(locationFragment.getCurrentlightAngle()+"");
            int[] rgb = parameterFragemnt.getRgb();
            String red = Integer.toHexString(rgb[0]);
            if(red.length()<=1)red = "0"+red;
            String green = Integer.toHexString(rgb[1]);
            if(green.length()<=1)green = "0"+green;
            String blue = Integer.toHexString(rgb[2]);
            if(blue.length()<=1)blue = "0"+blue;
            String rgbHex = red+green+blue;
            param.setLamp_rgb(rgbHex);
            param.setLamp_brightness(parameterFragemnt.getBrightness()+"");
            param.setLamp_colorTemp(parameterFragemnt.getColorTemp()+"");

            Intent data = new Intent();
            data.putExtra("lampParam",param);
            setResult(RESULT_OK,data);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_PICK_IMAGE:
                parameterFragemnt.setResultData(resultCode, resultCode == RESULT_OK ? data.getData() : null);
                break;
            case REQUEST_BACKUP_GET_PARAM:
                if(data!=null&&data.hasExtra("param")){
                    paramEntity = (ParamEntity) data.getSerializableExtra("param");
                    int brightness = paramEntity.getBrightness();
                    parameterFragemnt.setBrightness(brightness);
                    int colorTemp = paramEntity.getColorTemp();
                    parameterFragemnt.setColorTemp(colorTemp);
                    String rgb = paramEntity.getColorRgbValue();
                    if(rgb.length()==6){
                        int[] color = new int[3];
                        color[0] = Integer.parseInt(rgb.substring(0,2),16);
                        color[1] = Integer.parseInt(rgb.substring(2,4),16);
                        color[2] = Integer.parseInt(rgb.substring(4),16);
                        parameterFragemnt.setRGB(color);
                    }
                }
                break;
        }
    }

    private void sendBrightness() {
        int brightness = parameterFragemnt.getBrightness();
        if (brightness != lastBrightness && mLeds != null && mLeds.length != 0&&adjust) {
            mClient.setBrightness(roomId,mLeds, brightness, new ILightMgrApi.Callback<BaseResp>() {
                @Override
                public void callback(int code, BaseResp entity) {

                }
            });
        }
        lastBrightness = brightness;
    }

    private void sendColorTemp() {
        int colorTemp = parameterFragemnt.getColorTemp();
        if (colorTemp != lastColorTemp && mLeds != null && mLeds.length != 0&&adjust) {
            mClient.setColorTemp(roomId,mLeds, colorTemp, new ILightMgrApi.Callback<BaseResp>() {
                @Override
                public void callback(int code, BaseResp entity) {

                }
            });
        }
        lastColorTemp = colorTemp;
    }

    private void sendRgb() {
        int[] rgb = parameterFragemnt.getRgb();
        if (!(rgb[0] == lastRgb[0] && rgb[1] == lastRgb[1] && rgb[2] == lastRgb[2]) && mLeds != null && mLeds.length != 0&&adjust) {
            mClient.setRGB(roomId,mLeds, rgb[0], rgb[1], rgb[2], new ILightMgrApi.Callback<BaseResp>() {
                @Override
                public void callback(int code, BaseResp entity) {

                }
            });
        }
        lastRgb = rgb;
    }

    private void setLampHorizon(int degree) {
        if(adjust){
            mClient.setLampHDegree(roomId, mLeds, degree, new ILightMgrApi.Callback<BaseResp>() {
                @Override
                public void callback(int code, BaseResp entity) {

                }
            });
        }
    }
    private void setLampVertical(int degree) {
        if(adjust){
            mClient.setLampVDegree(roomId, mLeds, degree, new ILightMgrApi.Callback<BaseResp>() {
                @Override
                public void callback(int code, BaseResp entity) {

                }
            });
        }
    }
    private void setLampLight(int degree) {
        if(adjust){
            mClient.setLampLDegree(roomId, mLeds, degree, new ILightMgrApi.Callback<BaseResp>() {
                @Override
                public void callback(int code, BaseResp entity) {

                }
            });
        }
    }
    private void setLampMove(int degree) {
        if(adjust){
            mClient.setLampMovement(roomId, mLeds, degree, new ILightMgrApi.Callback<BaseResp>() {
                @Override
                public void callback(int code, BaseResp entity) {

                }
            });
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.ACTION_DOWN) && ll_menu.getVisibility() == View.VISIBLE) {
            ll_menu.setVisibility(View.INVISIBLE);
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            if (ll_menu.getVisibility() == View.VISIBLE) {
                ll_menu.setVisibility(View.INVISIBLE);
            } else {
                ll_menu.setVisibility(View.VISIBLE);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
