package cn.com.mod.office.lightman.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.com.mod.office.lightman.MyApplication;
import cn.com.mod.office.lightman.R;
import cn.com.mod.office.lightman.activity.base.BaseActivity;
import cn.com.mod.office.lightman.api.ILightMgrApi;

/**
 * 服务条款界面
 * Created by CAT on 2014/10/27.
 */
public class ServiceTermActivity extends BaseActivity {
    public static final String TAG = "ServiceTermActivity";

    private ILightMgrApi mClient;
    private TextView mServiceTerm;
    private ImageView mGoBack;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serviceterm);

        mClient = MyApplication.getInstance().getClient();
        // 获取服务条款
        mServiceTerm = (TextView) findViewById(R.id.serviceterm);
        mClient.getClause(new ILightMgrApi.Callback<String>() {
            @Override
            public void callback(int code, String entity) {
                switch (code) {
                    case CODE_SUCCESS:
                        mServiceTerm.setText(entity);
                        break;
                }
            }
        });

        // 返回按钮
        mGoBack = (ImageView) findViewById(R.id.ic_back);
        mGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void loadData() {

    }
}
