package cn.com.mod.office.lightman.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.app.SlidingPreferenceActivity;
import com.joshua.common.util.ToastUtils;

import java.util.UUID;

import cn.com.mod.office.lightman.MyApplication;
import cn.com.mod.office.lightman.R;
import cn.com.mod.office.lightman.activity.base.BaseActivity;
import cn.com.mod.office.lightman.api.BaseResp;
import cn.com.mod.office.lightman.api.ILightMgrApi;

/**
 * 备份参数
 */
public class SaveParamsActivity extends BaseActivity implements View.OnClickListener {

    private ImageView back;
    private TextView save, params_desc;
    private EditText et_remark;
    private int brightness, temp;
    private int[] rgb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_params);
        back = (ImageView) findViewById(R.id.ic_back);
        save = (TextView) findViewById(R.id.ic_menu);
        params_desc = (TextView) findViewById(R.id.params_desc);
        et_remark = (EditText) findViewById(R.id.et_remark);

        back.setOnClickListener(this);
        save.setOnClickListener(this);

        brightness = getIntent().getIntExtra("brightness", 0);
        temp = getIntent().getIntExtra("temp", 0);
        rgb = getIntent().getIntArrayExtra("rgb");

        String desc = String.format(getString(R.string.params_desc), brightness, rgb[0], rgb[1], rgb[2], temp);
        params_desc.setText(desc);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ic_back:
                finish();
                break;
            case R.id.ic_menu:
                //保存操作在这里
                saveParam();
                break;
        }
    }

    private void saveParam() {
        String remark = et_remark.getText().toString();
        if(TextUtils.isEmpty(remark)){
            ToastUtils.show(SaveParamsActivity.this, R.string.add_remark_please);
            et_remark.requestFocus();
            return;
        }
        String RGB = Integer.toHexString(rgb[0]) + Integer.toHexString(rgb[1]) + Integer.toHexString(rgb[2]) + "";
        MyApplication.getInstance().getClient().addBackupParam(UUID.randomUUID().toString(), remark, brightness, temp, RGB, new ILightMgrApi.Callback<BaseResp>() {
            @Override
            public void callback(int code, BaseResp entity) {
                if (code == 0) {
                    ToastUtils.show(SaveParamsActivity.this, R.string.param_save_success);
                    finish();
                } else {
                    ToastUtils.show(SaveParamsActivity.this, entity.getError_desc());
                }
            }
        });
    }

    @Override
    public void loadData() {

    }
}
