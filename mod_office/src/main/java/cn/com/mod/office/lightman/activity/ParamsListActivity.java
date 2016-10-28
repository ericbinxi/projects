package cn.com.mod.office.lightman.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import cn.com.mod.office.lightman.R;
import cn.com.mod.office.lightman.adapter.ParamsListAdapter;
import cn.com.mod.office.lightman.widget.SwipeListView;

/**
 * 关于界面
 * Created by CAT on 2014/10/25.
 */
public class ParamsListActivity extends Activity {
    public static final String TAG = ParamsListActivity.class.getSimpleName();

    private ImageView back;
    private SwipeListView listView;
    private ParamsListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paramslist);
        back = (ImageView) findViewById(R.id.ic_back);
        listView = (SwipeListView) findViewById(R.id.listview);
        listView.setRightViewWidth(150);
        adapter = new ParamsListAdapter(this,listView.getRightViewWidth());
        listView.setAdapter(adapter);
        initListener();
    }

    private void initListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
