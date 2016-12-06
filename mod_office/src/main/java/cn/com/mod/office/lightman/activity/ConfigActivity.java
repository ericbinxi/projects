package cn.com.mod.office.lightman.activity;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.joshua.common.util.MaskUtils;
import com.joshua.common.util.ToastUtils;

import java.util.Locale;

import cn.com.mod.office.lightman.MyApplication;
import cn.com.mod.office.lightman.R;
import cn.com.mod.office.lightman.api.ILightMgrApi;
import cn.com.mod.office.lightman.config.AppConfig;
import cn.com.mod.office.lightman.widget.SwitchButton;

/**
 * Created by CAT on 2015/3/28.
 */
public class ConfigActivity extends Activity implements View.OnClickListener {
    EditText mPath;
    Button mOk;
    View mGoBack;
    ToastUtils mToastUtils;
    MaskUtils mMaskUtils;
    CheckBox switchButton;

    private ImageView iv_default_addr;
    private ImageView iv_current_addr;
    private TextView tv_default_addr;
    private EditText et_current_addr;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        mToastUtils = new ToastUtils(this);
        mMaskUtils = new MaskUtils(this);
        mPath = (EditText) findViewById(R.id.path);
        mPath.setText(MyApplication.getInstance().getAppConfig().getHost());
        mOk = (Button) findViewById(R.id.ok);
        mGoBack = findViewById(R.id.ic_back);

        iv_default_addr = (ImageView) findViewById(R.id.iv_default_addr);
        iv_current_addr = (ImageView) findViewById(R.id.iv_currenr_addr);
        tv_default_addr = (TextView) findViewById(R.id.tv_default_addr);
        et_current_addr = (EditText) findViewById(R.id.et_current_addr);

        iv_current_addr.setOnClickListener(this);
        iv_default_addr.setOnClickListener(this);

        tv_default_addr.setText(AppConfig.HOST);

        mOk.setOnClickListener(this);
        mGoBack.setOnClickListener(this);
        switchButton = (CheckBox) findViewById(R.id.lag_change);
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Configuration configuration = getResources().getConfiguration();
                    configuration.locale = Locale.ENGLISH;
                    getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
                }else {
                    Configuration configuration = getResources().getConfiguration();
                    configuration.locale = Locale.CHINA;
                    getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
                }
            }
        });
    }
   String currentAddr = AppConfig.HOST;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ok:
//                final String urlStr = mPath.getText().toString();
                if (TextUtils.isEmpty(currentAddr)) {
                    mToastUtils.show(getString(R.string.tip_path_empty));
                    return;
                }
                String reg = "(http):\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?";
                if (currentAddr.matches(reg)) {
//                    final String oldHost = AppConfig.HOST;
//                    AppConfig.HOST = urlStr;
//                    mMaskUtils.show();
//                    MyApplication.getInstance().getClient().connectTest(new ILightMgrApi.Callback<Boolean>() {
//                        @Override
//                        public void callback(int code, Boolean entity) {
//                            mMaskUtils.cancel();
//                            switch (code) {
//                                case CODE_TIMEOUT:
//                                    mToastUtils.show(getString(R.string.tip_timeout));
//                                    break;
//                                case CODE_NETWORK_ERROR:
//                                    mToastUtils.show(getString(R.string.tip_network_connect_faild));
//                                    break;
//                                case CODE_SUCCESS:
//                                    if (entity) {
////                                        MyApplication.getInstance().getAppConfig().setHost(urlStr);
//                                        mToastUtils.show(getString(R.string.config_success));
//                                    } else {
//                                        AppConfig.HOST = oldHost;
//                                        mToastUtils.show(getString(R.string.config_faild));
//                                    }
//                                    break;
//                            }
//                        }
//                    });
                    MyApplication.getInstance().getAppConfig().setHost(currentAddr);
                    finish();
                } else {
//                    mToastUtils.show(getString(R.string.config_faild));
                    mToastUtils.show("服务器地址不合法");
                }
                break;
            case R.id.ic_back:
                finish();
                break;
            case R.id.iv_currenr_addr:
                iv_default_addr.setBackgroundResource(R.drawable.ic_multi_select);
                iv_current_addr.setBackgroundResource(R.drawable.ic_choosed);
                String addr = et_current_addr.getText().toString().trim();
                AppConfig.HOST = addr;
                currentAddr = addr;
                break;
            case R.id.iv_default_addr:
                iv_default_addr.setBackgroundResource(R.drawable.ic_choosed);
                iv_current_addr.setBackgroundResource(R.drawable.ic_multi_select);
                String addre = tv_default_addr.getText().toString().trim();
                AppConfig.HOST = addre;
                currentAddr = addre;
                break;
        }
    }
}
