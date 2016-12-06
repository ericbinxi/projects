package cn.com.mod.office.lightman.entity;

import cn.com.mod.office.lightman.api.BaseResp;

/**
 * Created by Administrator on 2016/11/13.
 */
public class LampStatusResp extends BaseResp {

    private int lamp_stutus;//灯状态
    private int lamp_brightness;
    private int lamp_colorTemp;
    private int lamp_RGB;
    private int lamp_h_degree;
    private int lamp_v_degree;
    private int lamp_l_degree;//光速角度

    public int getLamp_stutus() {
        return lamp_stutus;
    }

    public void setLamp_stutus(int lamp_stutus) {
        this.lamp_stutus = lamp_stutus;
    }

    public int getLamp_brightness() {
        return lamp_brightness;
    }

    public void setLamp_brightness(int lamp_brightness) {
        this.lamp_brightness = lamp_brightness;
    }

    public int getLamp_colorTemp() {
        return lamp_colorTemp;
    }

    public void setLamp_colorTemp(int lamp_colorTemp) {
        this.lamp_colorTemp = lamp_colorTemp;
    }

    public int getLamp_RGB() {
        return lamp_RGB;
    }

    public void setLamp_RGB(int lamp_RGB) {
        this.lamp_RGB = lamp_RGB;
    }

    public int getLamp_h_degree() {
        return lamp_h_degree;
    }

    public void setLamp_h_degree(int lamp_h_degree) {
        this.lamp_h_degree = lamp_h_degree;
    }

    public int getLamp_v_degree() {
        return lamp_v_degree;
    }

    public void setLamp_v_degree(int lamp_v_degree) {
        this.lamp_v_degree = lamp_v_degree;
    }

    public int getLamp_l_degree() {
        return lamp_l_degree;
    }

    public void setLamp_l_degree(int lamp_l_degree) {
        this.lamp_l_degree = lamp_l_degree;
    }
}
