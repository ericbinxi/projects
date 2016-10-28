package cn.com.mod.office.lightman.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.joshua.common.util.ToastUtils;

import java.util.List;

import cn.com.mod.office.lightman.R;
import cn.com.mod.office.lightman.activity.base.BaseActivity;
import cn.com.mod.office.lightman.adapter.DynamicModeAdapter;
import cn.com.mod.office.lightman.entity.Frame;

public class DynamicModeActivity extends BaseActivity implements View.OnClickListener{

    private static final int REQUEST_EDIT_PARAMS = 101;

    private ImageView back;
    private TextView title,save;
    private EditText et_modename;
    private ListView listView;
    private DynamicModeAdapter adapter;
    private LinearLayout bottom;

    private List<Frame> frames;

    private String roomId;
    private View addView;
    private ImageView add;

    private int lamp_brightness,lamp_colorTemp,lamp_h_degree,lamp_v_degree,lamp_l_degree;
    private String lamp_rgb;//灯的色温  16进制表示

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_mode);
        initView();
    }

    private void initView() {
        back = (ImageView) findViewById(R.id.ic_back);
        title = (TextView) findViewById(R.id.title);
        et_modename = (EditText) findViewById(R.id.et_modename);
        save = (TextView) findViewById(R.id.ic_menu);
        listView = (ListView) findViewById(R.id.listview);
        addView = LayoutInflater.from(this).inflate(R.layout.item_add_mode,null);
        add = (ImageView) addView.findViewById(R.id.add_mode);
        listView.addFooterView(addView);
        bottom = (LinearLayout) findViewById(R.id.bottom);

        back.setOnClickListener(this);
        save.setOnClickListener(this);
        bottom.setOnClickListener(this);

        adapter = new DynamicModeAdapter(this);
        listView.setAdapter(adapter);
    }

    @Override
    public void loadData() {
        
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ic_back:
                finish();
                break;
            case R.id.ic_menu:
                createDynamicMode();
                break;
            case R.id.bottom:
                //预览
                apply();
                break;
        }
    }

    private void apply() {

    }

    private void createDynamicMode() {
        String modeName = et_modename.getText().toString().trim();
        if(TextUtils.isEmpty(modeName)){
            ToastUtils.show(this,R.string.et_mode_name);
            return;
        }
        if(lamp_l_degree==0&&lamp_v_degree==0&&lamp_h_degree==0&&lamp_brightness==0&&lamp_colorTemp==0){
            ToastUtils.show(this,R.string.setting_params_tips);
            return;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK&&requestCode == REQUEST_EDIT_PARAMS){
            //处理灯的颜色 亮度等
            if(data!=null){
                lamp_brightness = data.getIntExtra("lamp_brightness",0);
                lamp_colorTemp = data.getIntExtra("lamp_colorTemp",0);
                lamp_h_degree = data.getIntExtra("lamp_h_degree",0);
                lamp_v_degree = data.getIntExtra("lamp_v_degree",0);
                lamp_l_degree = data.getIntExtra("lamp_l_degree",0);
                lamp_rgb = data.getStringExtra("lamp_rgb");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
