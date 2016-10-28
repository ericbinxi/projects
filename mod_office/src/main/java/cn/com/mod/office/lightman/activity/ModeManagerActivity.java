package cn.com.mod.office.lightman.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import cn.com.mod.office.lightman.R;
import cn.com.mod.office.lightman.activity.base.BaseActivity;
import cn.com.mod.office.lightman.adapter.ModeManagerAdapter;
import cn.com.mod.office.lightman.widget.SwipeListView;

public class ModeManagerActivity extends BaseActivity implements View.OnClickListener{

    private ImageView back,add;
    private SwipeListView listView;
    private ModeManagerAdapter adapter;
    private String roomId;
    private LinearLayout ll_menu,normal_mode,dynamic_mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode_manager);
        roomId = getIntent().getStringExtra("roomId");
        back = (ImageView) findViewById(R.id.ic_back);
        add = (ImageView) findViewById(R.id.ic_menu);
        ll_menu = (LinearLayout) findViewById(R.id.ll_menu);
        normal_mode = (LinearLayout) findViewById(R.id.normal_mode);
        dynamic_mode = (LinearLayout) findViewById(R.id.dynamic_mode);
        normal_mode.setOnClickListener(this);
        dynamic_mode.setOnClickListener(this);
        listView = (SwipeListView) findViewById(R.id.listview);
        listView.setRightViewWidth(150);
        adapter = new ModeManagerAdapter(this,listView.getRightViewWidth());
        listView.setAdapter(adapter);
        adapter.setListener(new ModeManagerAdapter.OnModeOperaterListener() {
            @Override
            public void editMode(int position) {
                Intent intent = new Intent(ModeManagerActivity.this,NormalModeActivity.class);
                intent.putExtra("mode_type",2);//1：创建  2：编辑模式
                intent.putExtra("mode_id",0);
                intent.putExtra("roomId",roomId);
                startActivity(intent);
            }

            @Override
            public void deleteMode(int position) {

            }
        });

        back.setOnClickListener(this);
        add.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ic_back:
                finish();
                break;
            case R.id.ic_menu:
                //添加模式
                if(ll_menu.isShown()){
                    ll_menu.setVisibility(View.GONE);
                }else{
                    ll_menu.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.normal_mode:
                Intent intent = new Intent(this,NormalModeActivity.class);
                intent.putExtra("mode_type",1);//1：创建  2：编辑模式
                intent.putExtra("mode_id",0);
                intent.putExtra("roomId",roomId);
                startActivity(intent);
                break;
            case R.id.dynamic_mode:
                Intent intent1 = new Intent(this,DynamicModeActivity.class);
                intent1.putExtra("mode_type",1);//1：创建  2：编辑模式
                startActivity(intent1);
                break;
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && ll_menu.getVisibility() == View.VISIBLE) {
            ll_menu.setVisibility(View.INVISIBLE);
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            if (ll_menu.getVisibility() == View.VISIBLE) {
                ll_menu.setVisibility(View.INVISIBLE);
            } else {
                ll_menu.setVisibility(View.VISIBLE);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void loadData() {

    }
}
