package cn.com.mod.office.lightman.activity.base;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import cn.com.mod.office.lightman.MyApplication;

/**
 * Created by CAT on 2015/1/7.
 */
public abstract class BaseFragmentActivity extends FragmentActivity {
    protected boolean hasLoadData = false;

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

    protected abstract void loadData();
}
