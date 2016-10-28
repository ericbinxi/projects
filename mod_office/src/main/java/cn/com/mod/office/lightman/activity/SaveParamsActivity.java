package cn.com.mod.office.lightman.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import cn.com.mod.office.lightman.R;

/**
 * 备份参数
 */
public class SaveParamsActivity extends Activity implements View.OnClickListener{

    private ImageView back;
    private TextView save,params_desc;
    private EditText et_remark;
    private int brightness,temp;
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

        brightness = getIntent().getIntExtra("brightness",0);
        temp = getIntent().getIntExtra("temp",0);
        rgb = getIntent().getIntArrayExtra("rgb");

        String desc = String.format(getString(R.string.params_desc),brightness,rgb[0],rgb[1],rgb[2],temp);
        params_desc.setText(desc);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ic_back:
                finish();
                break;
            case R.id.ic_menu:
                //保存操作在这里
                break;
        }
    }
}
