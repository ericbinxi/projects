package cn.com.mod.office.lightman.entity.base;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/12.
 */
public class BaseModeEntity implements Serializable {
    private String mode_id;
    private String mode_name;
    private int modeType;

    public int getModeType() {
        return modeType;
    }

    public void setModeType(int modeType) {
        this.modeType = modeType;
    }

    public String getMode_id() {
        return mode_id;
    }

    public void setMode_id(String mode_id) {
        this.mode_id = mode_id;
    }

    public String getMode_name() {
        return mode_name;
    }

    public void setMode_name(String mode_name) {
        this.mode_name = mode_name;
    }
}
