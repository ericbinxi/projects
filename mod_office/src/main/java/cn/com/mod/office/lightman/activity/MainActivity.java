package cn.com.mod.office.lightman.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import cn.com.mod.office.lightman.MyApplication;
import cn.com.mod.office.lightman.R;
import cn.com.mod.office.lightman.config.AppConfig;
import cn.com.mod.office.lightman.config.ConfigUtils;

/**
 * 启动界面
 * Created by CAT on 2014/9/17.
 */
public class MainActivity extends Activity {
    public static final String TAG = "MainActivity";
    // 加载结果-正常启动
    private static final int LOAD_RESULT_SUCCESS = 1;
    // 加载结果-首次启动
    private static final int LOAD_RESULT_FIRST = 2;

    // 加载资源，返回加载结果
    private int loading() {
        // 让应用加载资源
        MyApplication.getInstance().loadLocalCache();
        ConfigUtils config = MyApplication.getInstance().getAppConfig();
        if (config.isFirstLaunch()) {
            return LOAD_RESULT_FIRST;
        }
        return LOAD_RESULT_SUCCESS;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... params) {
                long startTime = System.currentTimeMillis();
                long loadingTime = System.currentTimeMillis() - startTime;
                int result = loading();
                if (loadingTime < AppConfig.LOAD_TIME_LIMIT) {
                    try {
                        Thread.sleep(AppConfig.LOAD_TIME_LIMIT - loadingTime);
                    } catch (Exception e) {
                    }
                }
                return result;
            }

            @Override
            protected void onPostExecute(Integer result) {
                Intent intent = null;
                switch (result) {
                    case LOAD_RESULT_SUCCESS:
                        intent = new Intent(MainActivity.this, LoginActivity.class);
                        break;
                    case LOAD_RESULT_FIRST:
                        intent = new Intent(MainActivity.this, IntroduceActivity.class);
                        break;
                }
                startActivity(intent);
                finish();
            }

        }.execute(new Void[]{});
    }
}
