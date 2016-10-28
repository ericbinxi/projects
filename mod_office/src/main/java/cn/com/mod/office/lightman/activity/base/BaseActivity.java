package cn.com.mod.office.lightman.activity.base;

import android.app.Activity;
import android.os.Bundle;

import cn.com.mod.office.lightman.MyApplication;

/**
 * Created by CAT on 2015/1/7.
 */
public abstract class BaseActivity extends Activity {
    protected boolean hasLoadData;

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        MyApplication.getInstance().getClient().onRestoreInstanceState(savedInstanceState);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        MyApplication.getInstance().getClient().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!hasLoadData) {
            loadData();
            hasLoadData = true;
        }
    }

    public abstract void loadData();
}
