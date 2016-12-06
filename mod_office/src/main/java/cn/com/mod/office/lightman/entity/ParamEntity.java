package cn.com.mod.office.lightman.entity;

import java.io.Serializable;

/**
 * 备份参数实体
 * Created by Administrator on 2016/11/12.
 */
public class ParamEntity implements Serializable {

    private String param_name;//主键
    private String param_addInfo;//备注
    private int brightness;
    private int colorTemp;
    private String colorRgbValue;

    public String getParam_name() {
        return param_name;
    }

    public void setParam_name(String param_name) {
        this.param_name = param_name;
    }

    public String getParam_addInfo() {
        return param_addInfo;
    }

    public void setParam_addInfo(String param_addInfo) {
        this.param_addInfo = param_addInfo;
    }

    public int getBrightness() {
        return brightness;
    }

    public void setBrightness(int brightness) {
        this.brightness = brightness;
    }

    public int getColorTemp() {
        return colorTemp;
    }

    public void setColorTemp(int colorTemp) {
        this.colorTemp = colorTemp;
    }

    public String getColorRgbValue() {
        return colorRgbValue;
    }

    public void setColorRgbValue(String colorRgbValue) {
        this.colorRgbValue = colorRgbValue;
    }
}
