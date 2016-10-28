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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ok:
                final String urlStr = mPath.getText().toString();
                MyApplication.getInstance().getAppConfig().setHost(urlStr);
                if (TextUtils.isEmpty(urlStr)) {
                    mToastUtils.show(getString(R.string.tip_path_empty));
                    return;
                }
                String reg = "(http):\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?";
                if (urlStr.matches(reg)) {
                    final String oldHost = AppConfig.HOST;
                    AppConfig.HOST = urlStr;
                    mMaskUtils.show();
                    MyApplication.getInstance().getClient().connectTest(new ILightMgrApi.Callback<Boolean>() {
                        @Override
                        public void callback(int code, Boolean entity) {
                            mMaskUtils.cancel();
                            switch (code) {
                                case CODE_TIMEOUT:
                                    mToastUtils.show(getString(R.string.tip_timeout));
                                    break;
                                case CODE_NETWORK_ERROR:
                                    mToastUtils.show(getString(R.string.tip_network_connect_faild));
                                    break;
                                case CODE_SUCCESS:
                                    if (entity) {
//                                        MyApplication.getInstance().getAppConfig().setHost(urlStr);
                                        mToastUtils.show(getString(R.string.config_success));
                                    } else {
                                        AppConfig.HOST = oldHost;
                                        mToastUtils.show(getString(R.string.config_faild));
                                    }
                                    break;
                            }
                        }
                    });
                } else {
                    mToastUtils.show(getString(R.string.config_faild));
                }
                break;
            case R.id.ic_back:
                finish();
                break;
        }
    }
}
