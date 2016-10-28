package cn.com.mod.office.lightman.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.com.mod.office.lightman.R;

/**
 * 关于界面
 * Created by CAT on 2014/10/25.
 */
public class AboutActivity extends Activity {
    public static final String TAG = AboutActivity.class.getSimpleName();

    private TextView mLinkService;
    private ImageView mGoBack;
    private TextView mUpdate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        mLinkService = (TextView) findViewById(R.id.service);
        mGoBack = (ImageView) findViewById(R.id.ic_back);
        mUpdate = (TextView) findViewById(R.id.ic_update);

        // 设置下划线
        mLinkService.setText(Html.fromHtml("<u>" + mLinkService.getText() + "</u>"));
        mLinkService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutActivity.this, ServiceTermActivity.class);
                startActivity(intent);
            }
        });

        // 返回按钮
        mGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 更新按钮
        mUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutActivity.this, UpdateActivity.class);
                startActivity(intent);
            }
        });
    }
}
