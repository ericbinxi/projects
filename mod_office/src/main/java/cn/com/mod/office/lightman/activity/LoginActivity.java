package cn.com.mod.office.lightman.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.joshua.common.util.MaskUtils;
import com.joshua.common.util.ToastUtils;

import cn.com.mod.office.lightman.MyApplication;
import cn.com.mod.office.lightman.R;
import cn.com.mod.office.lightman.api.ILightMgrApi;
import cn.com.mod.office.lightman.api.resp.LoginResp;
import cn.com.mod.office.lightman.config.AppConfig;
import cn.com.mod.office.lightman.config.ConfigUtils;
import cn.com.mod.office.lightman.entity.BaseResponse;
import cn.com.mod.office.lightman.manager.AccountManager;

/**
 * 登录界面
 * Created by CAT on 2014/10/22.
 */
public class LoginActivity extends Activity {
    public static final String TAG = "LoginActivity";

    private ConfigUtils mConfigUtils;
    private ToastUtils mToastUtils;
    private MaskUtils mMaskUtils;
    private ImageView mUserHeader;
    private EditText mUsername;
    private EditText mPassword;
    private TextView mPassAlert;
    private CheckBox mRemember;
    private CheckBox mAuto;
    private TextView mLabelRemember;
    private TextView mLabelAuto;
    private Button mLogin;
    // 最后一次点击退出按钮的时间
    private long lastExit;

    private View mConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    // 初始化
    private void init() {
        // 初始化系统工具
        mConfigUtils = MyApplication.getInstance().getAppConfig();
        mMaskUtils = new MaskUtils(this);
        mToastUtils = new ToastUtils(this);

        // 初始化视图组件
        mUserHeader = (ImageView) findViewById(R.id.login_header);
        mUsername = (EditText) findViewById(R.id.login_username);
        mPassword = (EditText) findViewById(R.id.login_password);
        mPassAlert = (TextView) findViewById(R.id.login_pass_alert);
        mRemember = (CheckBox) findViewById(R.id.login_remember);
        mAuto = (CheckBox) findViewById(R.id.login_auto);
        mLabelRemember = (TextView) findViewById(R.id.login_label_remember);
        mLabelAuto = (TextView) findViewById(R.id.login_label_auto);
        mLogin = (Button) findViewById(R.id.login_btn);
        mConfig = findViewById(R.id.gate);

        // 从配置中读取组件信息
        mUsername.setText(mConfigUtils.getUsername());
        mPassword.setText(mConfigUtils.getPassword());
        mRemember.setChecked(mConfigUtils.isRememberPass());
        mAuto.setChecked(mConfigUtils.isAutoLogin());
        if (mConfigUtils.isAutoLogin()) {
            mLogin.performClick();
        }

        // 初始化事件
        mConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ConfigActivity.class));
            }
        });

        // 点击记住密码文字选中复选框
        mLabelRemember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRemember.performClick();
            }
        });

        // 点击自动登录文字选中复选框
        mLabelAuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuto.performClick();
            }
        });

        // 自动登录点选时选中记住密码
        mAuto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mRemember.setChecked(true);
                }
            }
        });

        // 登录按钮
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        // 用户名输入框失去焦点的时候
        mUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus == false) {
                    refreshHeader(mUsername.getText() + "");
                }
            }
        });
        if(mAuto.isChecked()){
            login();
        }
    }

    private void login(){
        if (mPassAlert.getVisibility() == View.VISIBLE) {
            mPassAlert.setVisibility(View.INVISIBLE);
        }
        String username = mUsername.getText() + "";
        String password = mPassword.getText() + "";
        // 如果用户名为空
        if (TextUtils.isEmpty(username)) {
            mToastUtils.show(getString(R.string.tip_username_empty));
            mUsername.requestFocus();
            return;
        }
        // 如果用户名长度小于5
        if (username.length() < 4) {
            mToastUtils.show(getString(R.string.tip_username_length));
            mUsername.requestFocus();
            return;
        }
        // 如果密码为空
        if (TextUtils.isEmpty(password)) {
            mToastUtils.show(getString(R.string.tip_password_empty));
            mPassword.requestFocus();
            return;
        }
        // 如果密码长度小于4
        if (password.length() < 4) {
            mToastUtils.show(getString(R.string.tip_password_length));
            mPassword.requestFocus();
            return;
        }
        saveConfiguration();
        mMaskUtils.show();
        // 登录请求
//                startActivity(new Intent(LoginActivity.this, FloorSelectorActivity.class));
        MyApplication.getInstance().getClient().login(username, password, new ILightMgrApi.Callback<LoginResp>() {
            @Override
            public void callback(int code, LoginResp response) {
                mMaskUtils.cancel();
                switch (code) {
                    case CODE_TIMEOUT:
                        mToastUtils.show(getString(R.string.tip_timeout));
                        break;
                    case CODE_NETWORK_ERROR:
                        mToastUtils.show(getString(R.string.tip_network_connect_faild));
                        break;
                    case CODE_SUCCESS:
                        if (response.getStatus()==0) {
                            AccountManager.getInstance().setSession(response.getSession());
                            startActivity(new Intent(LoginActivity.this, FloorSelectorActivity.class));
                            finish();
                        } else {
                            mPassAlert.setVisibility(View.VISIBLE);
                            mPassword.requestFocus();
                        }
                        break;
                }
            }
        });
    }

    // 保存配置信息
    private void saveConfiguration() {
        // 记录用户名
        mConfigUtils.setUsername(mUsername.getText() + "");
        // 如果自动登录
        if (mAuto.isChecked()) {
            // 记录密码
            mConfigUtils.setPasword(mPassword.getText() + "");
            mConfigUtils.setAutoLogin(true);
        } else {
            // 清空记录的密码
            mConfigUtils.setPasword(null);
            mConfigUtils.setAutoLogin(false);
        }
        // 如果记住密码
        if (mRemember.isChecked()) {
            mConfigUtils.setPasword(mPassword.getText() + "");
            mConfigUtils.setRememberPass(true);
        } else {
            // 清空记录的密码
            mConfigUtils.setPasword(null);
            mConfigUtils.setRememberPass(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshHeader(mUsername.getText() + "");
    }

    // 刷新用户头像
    private void refreshHeader(String username) {
        Bitmap bitmap = ((MyApplication) getApplicationContext()).getLocalHeader(username);
        if (bitmap != null) {
            mUserHeader.setImageBitmap(bitmap);
        } else {
            mUserHeader.setImageResource(R.drawable.default_header);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - lastExit) > 2000) {
                mToastUtils.show(getString(R.string.tip_app_exit));
                lastExit = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
