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

    public LedView(Context context, Lamps ledInfo) {
        super(context);
        this.mLedInfo = ledInfo;
        init();
    }

    private void init() {
        if (mLedInfo.getLamp_stutus() == 1) {
            switch (mLedInfo.getType_id()) {
                case 2:
                    this.setImageResource(R.drawable.led_2_on);
                    break;
                default:
                    this.setImageResource(R.drawable.led_0_on);
            }
        } else {
            switch (mLedInfo.getType_id()) {
                case 2:
                    this.setImageResource(R.drawable.led_2_off);
                    break;
                default:
                    this.setImageResource(R.drawable.led_0_off);
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

    public void setChecked(boolean isChecked) {
        this.mIsChecked = isChecked;
        if (isChecked) {
            if (mLedInfo.getLamp_stutus() == 1) {
                switch (mLedInfo.getType_id()) {
                    case 2:
                        this.setImageResource(R.drawable.led_2_on_selected);
                        break;
                    default:
                        this.setImageResource(R.drawable.led_0_on_selected);
                }
            } else {
                switch (mLedInfo.getType_id()) {
                    case 2:
                        this.setImageResource(R.drawable.led_2_off_selected);
                        break;
                    default:
                        this.setImageResource(R.drawable.led_0_off_selected);
                }
            }
        } else {
            if (mLedInfo.getLamp_stutus() == 1) {
                switch (mLedInfo.getType_id()) {
                    case 2:
                        this.setImageResource(R.drawable.led_2_on);
                        break;
                    default:
                        this.setImageResource(R.drawable.led_0_on);
                }
            } else {
                switch (mLedInfo.getType_id()) {
                    case 2:
                        this.setImageResource(R.drawable.led_2_off);
                        break;
                    default:
                        this.setImageResource(R.drawable.led_0_off);
                }
            }
        }
        if (mCheckStateChangeListener != null) {
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
