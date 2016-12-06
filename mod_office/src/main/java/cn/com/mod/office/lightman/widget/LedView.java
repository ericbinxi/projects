package cn.com.mod.office.lightman.widget;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import cn.com.mod.office.lightman.R;
import cn.com.mod.office.lightman.entity.Lamps;
import cn.com.mod.office.lightman.entity.LedInfo;

/**
 * LED灯
 * Created by CAT on 2014/11/10.
 */
public class LedView extends ImageView {
//    private LedInfo mLedInfo;
    private Lamps mLedInfo;
    private boolean mIsChecked;
    private OnCheckStateChangeListener mCheckStateChangeListener;
    private boolean needCallback = true;

    public LedView(Context context, Lamps ledInfo) {
        super(context);
        this.mLedInfo = ledInfo;
        init();
    }

    private void init() {
        int type  = Integer.parseInt(mLedInfo.getType_id());
        if ("在线".equals(mLedInfo.getLamp_stutus())) {
            switch (type) {
                case 0:
                    this.setImageResource(R.drawable.icon_0_on);
                    break;
                case 1:
                    this.setImageResource(R.drawable.icon_1_on);
                    break;
                case 2:
                    this.setImageResource(R.drawable.icon_2_on);
                    break;
                case 3:
                    this.setImageResource(R.drawable.icon_3_on);
                    break;
                case 4:
                    this.setImageResource(R.drawable.icon_4_on);
                    break;
                case 5:
                    this.setImageResource(R.drawable.icon_5_on);
                    break;
                case 6:
                    this.setImageResource(R.drawable.icon_6_on);
                    break;
                case 7:
                    this.setImageResource(R.drawable.icon_7_on);
                    break;
                case 8:
                    this.setImageResource(R.drawable.icon_8_on);
                    break;
            }
        } else {
            switch (type) {
                case 0:
                    this.setImageResource(R.drawable.icon_0_off);
                    break;
                case 1:
                    this.setImageResource(R.drawable.icon_1_off);
                    break;
                case 2:
                    this.setImageResource(R.drawable.icon_2_off);
                    break;
                case 3:
                    this.setImageResource(R.drawable.icon_3_off);
                    break;
                case 4:
                    this.setImageResource(R.drawable.icon_4_off);
                    break;
                case 5:
                    this.setImageResource(R.drawable.icon_5_off);
                    break;
                case 6:
                    this.setImageResource(R.drawable.icon_6_off);
                    break;
                case 7:
                    this.setImageResource(R.drawable.icon_7_off);
                    break;
                case 8:
                    this.setImageResource(R.drawable.icon_8_off);
                    break;
            }
        }
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setChecked(!isChecked());
            }
        });
    }

    public boolean isChecked() {
        return this.mIsChecked;
    }

    public void setNeedCallback(boolean needCallback) {
        this.needCallback = needCallback;
    }

    public void setLinkedChecked(boolean isChecked){
        this.mIsChecked = isChecked;
        int type  = Integer.parseInt(mLedInfo.getType_id());
        if (isChecked) {
            if ("在线".equals(mLedInfo.getLamp_stutus())) {
                switch (type) {
                    case 0:
                        this.setImageResource(R.drawable.icon_0_on_select);
                        break;
                    case 1:
                        this.setImageResource(R.drawable.icon_1_on_select);
                        break;
                    case 2:
                        this.setImageResource(R.drawable.icon_2_on_select);
                        break;
                    case 3:
                        this.setImageResource(R.drawable.icon_3_on_select);
                        break;
                    case 4:
                        this.setImageResource(R.drawable.icon_4_on_select);
                        break;
                    case 5:
                        this.setImageResource(R.drawable.icon_5_on_select);
                        break;
                    case 6:
                        this.setImageResource(R.drawable.icon_6_on_select);
                        break;
                    case 7:
                        this.setImageResource(R.drawable.icon_7_on_select);
                        break;
                    case 8:
                        this.setImageResource(R.drawable.icon_8_on_select);
                        break;
                }
            } else {
                switch (type) {
                    case 0:
                        this.setImageResource(R.drawable.icon_0_off_select);
                        break;
                    case 1:
                        this.setImageResource(R.drawable.icon_1_off_select);
                        break;
                    case 2:
                        this.setImageResource(R.drawable.icon_2_off_select);
                        break;
                    case 3:
                        this.setImageResource(R.drawable.icon_3_off_select);
                        break;
                    case 4:
                        this.setImageResource(R.drawable.icon_4_off_select);
                        break;
                    case 5:
                        this.setImageResource(R.drawable.icon_5_off_select);
                        break;
                    case 6:
                        this.setImageResource(R.drawable.icon_6_off_select);
                        break;
                    case 7:
                        this.setImageResource(R.drawable.icon_7_off_select);
                        break;
                    case 8:
                        this.setImageResource(R.drawable.icon_8_off_select);
                        break;
                }
            }
        } else {
            if ("在线".equals(mLedInfo.getLamp_stutus())) {
                switch (type) {
                    case 0:
                        this.setImageResource(R.drawable.icon_0_on);
                        break;
                    case 1:
                        this.setImageResource(R.drawable.icon_1_on);
                        break;
                    case 2:
                        this.setImageResource(R.drawable.icon_2_on);
                        break;
                    case 3:
                        this.setImageResource(R.drawable.icon_3_on);
                        break;
                    case 4:
                        this.setImageResource(R.drawable.icon_4_on);
                        break;
                    case 5:
                        this.setImageResource(R.drawable.icon_5_on);
                        break;
                    case 6:
                        this.setImageResource(R.drawable.icon_6_on);
                        break;
                    case 7:
                        this.setImageResource(R.drawable.icon_7_on);
                        break;
                    case 8:
                        this.setImageResource(R.drawable.icon_8_on);
                        break;
                }
            } else {
                switch (type) {
                    case 0:
                        this.setImageResource(R.drawable.icon_0_off);
                        break;
                    case 1:
                        this.setImageResource(R.drawable.icon_1_off);
                        break;
                    case 2:
                        this.setImageResource(R.drawable.icon_2_off);
                        break;
                    case 3:
                        this.setImageResource(R.drawable.icon_3_off);
                        break;
                    case 4:
                        this.setImageResource(R.drawable.icon_4_off);
                        break;
                    case 5:
                        this.setImageResource(R.drawable.icon_5_off);
                        break;
                    case 6:
                        this.setImageResource(R.drawable.icon_6_off);
                        break;
                    case 7:
                        this.setImageResource(R.drawable.icon_7_off);
                        break;
                    case 8:
                        this.setImageResource(R.drawable.icon_8_off);
                        break;
                }
            }
        }
    }

    public void setChecked(boolean isChecked) {
        this.mIsChecked = isChecked;
        int type  = Integer.parseInt(mLedInfo.getType_id());
        if (isChecked) {
            if ("在线".equals(mLedInfo.getLamp_stutus())) {
                switch (type) {
                    case 0:
                        this.setImageResource(R.drawable.icon_0_on_select);
                        break;
                    case 1:
                        this.setImageResource(R.drawable.icon_1_on_select);
                        break;
                    case 2:
                        this.setImageResource(R.drawable.icon_2_on_select);
                        break;
                    case 3:
                        this.setImageResource(R.drawable.icon_3_on_select);
                        break;
                    case 4:
                        this.setImageResource(R.drawable.icon_4_on_select);
                        break;
                    case 5:
                        this.setImageResource(R.drawable.icon_5_on_select);
                        break;
                    case 6:
                        this.setImageResource(R.drawable.icon_6_on_select);
                        break;
                    case 7:
                        this.setImageResource(R.drawable.icon_7_on_select);
                        break;
                    case 8:
                        this.setImageResource(R.drawable.icon_8_on_select);
                        break;
                }
            } else {
                switch (type) {
                    case 0:
                        this.setImageResource(R.drawable.icon_0_off_select);
                        break;
                    case 1:
                        this.setImageResource(R.drawable.icon_1_off_select);
                        break;
                    case 2:
                        this.setImageResource(R.drawable.icon_2_off_select);
                        break;
                    case 3:
                        this.setImageResource(R.drawable.icon_3_off_select);
                        break;
                    case 4:
                        this.setImageResource(R.drawable.icon_4_off_select);
                        break;
                    case 5:
                        this.setImageResource(R.drawable.icon_5_off_select);
                        break;
                    case 6:
                        this.setImageResource(R.drawable.icon_6_off_select);
                        break;
                    case 7:
                        this.setImageResource(R.drawable.icon_7_off_select);
                        break;
                    case 8:
                        this.setImageResource(R.drawable.icon_8_off_select);
                        break;
                }
            }
        } else {
            if ("在线".equals(mLedInfo.getLamp_stutus())) {
                switch (type) {
                    case 0:
                        this.setImageResource(R.drawable.icon_0_on);
                        break;
                    case 1:
                        this.setImageResource(R.drawable.icon_1_on);
                        break;
                    case 2:
                        this.setImageResource(R.drawable.icon_2_on);
                        break;
                    case 3:
                        this.setImageResource(R.drawable.icon_3_on);
                        break;
                    case 4:
                        this.setImageResource(R.drawable.icon_4_on);
                        break;
                    case 5:
                        this.setImageResource(R.drawable.icon_5_on);
                        break;
                    case 6:
                        this.setImageResource(R.drawable.icon_6_on);
                        break;
                    case 7:
                        this.setImageResource(R.drawable.icon_7_on);
                        break;
                    case 8:
                        this.setImageResource(R.drawable.icon_8_on);
                        break;
                }
            } else {
                switch (type) {
                    case 0:
                        this.setImageResource(R.drawable.icon_0_off);
                        break;
                    case 1:
                        this.setImageResource(R.drawable.icon_1_off);
                        break;
                    case 2:
                        this.setImageResource(R.drawable.icon_2_off);
                        break;
                    case 3:
                        this.setImageResource(R.drawable.icon_3_off);
                        break;
                    case 4:
                        this.setImageResource(R.drawable.icon_4_off);
                        break;
                    case 5:
                        this.setImageResource(R.drawable.icon_5_off);
                        break;
                    case 6:
                        this.setImageResource(R.drawable.icon_6_off);
                        break;
                    case 7:
                        this.setImageResource(R.drawable.icon_7_off);
                        break;
                    case 8:
                        this.setImageResource(R.drawable.icon_8_off);
                        break;
                }
            }
        }
        if (mCheckStateChangeListener != null&&needCallback) {
            mCheckStateChangeListener.onCheckStateChange(mLedInfo, isChecked);
        }
    }

    public Lamps getLedInfo() {
        return this.mLedInfo;
    }

    public void setOnCheckStateChangeListener(OnCheckStateChangeListener listener) {
        mCheckStateChangeListener = listener;
    }

    public interface OnCheckStateChangeListener {
        public void onCheckStateChange(Lamps ledInfo, boolean isChecked);
    }
}
