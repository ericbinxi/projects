package cn.com.mod.office.lightman.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.joshua.common.util.MaskUtils;
import com.joshua.common.util.ToastUtils;

import cn.com.mod.office.lightman.MyApplication;
import cn.com.mod.office.lightman.R;
import cn.com.mod.office.lightman.activity.base.BaseActivity;
import cn.com.mod.office.lightman.api.ILightMgrApi;
import cn.com.mod.office.lightman.config.ConfigUtils;
import cn.com.mod.office.lightman.entity.BaseResponse;

/**
 * 修改密码界面
 * Created by CAT on 2014/10/24.
 */
public class ModifyPasswordActivity extends BaseActivity {
    public static final String TAG = "ModifyPasswordActivity";

    private ToastUtils mToastUtils;
    private MaskUtils mMaskUtils;
    private ImageView mGoBack;
    private EditText mOldPass;
    private EditText mNewPass;
    private Button mConfirm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_pass);

        mToastUtils = new ToastUtils(this);
        mMaskUtils = new MaskUtils(this);

        // 返回按钮
        mGoBack = (ImageView) findViewById(R.id.ic_back);
        mGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 旧密码
        mOldPass = (EditText) findViewById(R.id.oldPass);
        // 新密码
        mNewPass = (EditText) findViewById(R.id.newPass);
        // 确认按钮
        mConfirm = (Button) findViewById(R.id.btn_ok);
        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPass = mOldPass.getText() + "";
                String newPass = mNewPass.getText() + "";
                if (TextUtils.isEmpty(oldPass)) {
                    mToastUtils.show(getString(R.string.tip_password_empty));
                    return;
                }
                if (oldPass.length() < 4) {
                    mToastUtils.show(getString(R.string.tip_password_length));
                    return;
                }
                if (TextUtils.isEmpty(newPass)) {
                    mToastUtils.show(getString(R.string.tip_newpass_empty));
                    return;
                }
                if (newPass.length() < 4) {
                    mToastUtils.show(getString(R.string.tip_password_length));
                    return;
                }
                mMaskUtils.show();
                MyApplication.getInstance().getClient().modifyPassword(oldPass, newPass, new ILightMgrApi.Callback<BaseResponse>() {
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
                                mToastUtils.show(entity.msg);
                                ConfigUtils config = MyApplication.getInstance().getAppConfig();
                                config.setAutoLogin(false);
                                config.setPasword(null);
                                config.setRememberPass(false);
                                startActivity(new Intent(ModifyPasswordActivity.this,LoginActivity.class));
                                break;
                        }
                    }
                });
            }
        });
    }

    @Override
    public void loadData() {

    }
}
