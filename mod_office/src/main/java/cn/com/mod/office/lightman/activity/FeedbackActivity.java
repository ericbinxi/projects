package cn.com.mod.office.lightman.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.joshua.common.util.MaskUtils;
import com.joshua.common.util.ToastUtils;

import cn.com.mod.office.lightman.MyApplication;
import cn.com.mod.office.lightman.R;
import cn.com.mod.office.lightman.activity.base.BaseActivity;
import cn.com.mod.office.lightman.api.ILightMgrApi;
import cn.com.mod.office.lightman.entity.BaseResponse;

/**
 * 反馈意见界面
 * Created by CAT on 2014/10/24.
 */
public class FeedbackActivity extends BaseActivity {
    public static final String TAG = "FeedbackActivity";

    private ImageView mGoBack;
    private EditText mFeedback;
    private Button mSend;
    private ToastUtils mToastUtils;
    private MaskUtils mMaskUtils;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        mToastUtils = new ToastUtils(this);
        mMaskUtils = new MaskUtils(this);

        // 返回按钮
        mGoBack = (ImageView) findViewById(R.id.ic_back);
        mGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 文本框
        mFeedback = (EditText) findViewById(R.id.feedback);

        // 发送按钮
        mSend = (Button) findViewById(R.id.send);
        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = mFeedback.getText() + "";
                if (TextUtils.isEmpty(content)) {
                    mToastUtils.show(getString(R.string.tip_feedback_empty));
                    return;
                }
                if (content.length() < 15) {
                    mToastUtils.show(getString(R.string.tip_feedback_length));
                    return;
                }
                mMaskUtils.show();
                MyApplication.getInstance().getClient().submitFeedback(content, new ILightMgrApi.Callback<BaseResponse>() {
                    @Override
                    public void callback(int code, BaseResponse entity) {
                        mMaskUtils.cancel();
                        switch (code) {
                            case CODE_TIMEOUT:
                                mToastUtils.show(getString(R.string.tip_timeout));
                                break;
                            case CODE_NETWORK_ERROR:
                                mToastUtils.show(getString(R.string.tip_network_connect_faild));
                                break;
                            case CODE_SUCCESS:
                                mToastUtils.show(entity.msg);
                                break;
                        }
                    }
                });
            }
        });
    }

    @Override
    public void loadData() {

    }
}
