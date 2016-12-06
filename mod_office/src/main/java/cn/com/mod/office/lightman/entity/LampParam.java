package cn.com.mod.office.lightman.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/14.
 */
public class LampParam implements Serializable, Cloneable {

    /**
     * lamp_id : 0001002004001000001
     * lamp_brightness : 30
     * lamp_colorTemp : 3612
     * lamp_rgb : FFFFFF
     * lamp_v_degree : 45
     * lamp_h_degree : -135
     * lamp_l_degree : 30
     */

    private String lamp_id;
    private String lamp_brightness;
    private String lamp_colorTemp;
    private String lamp_rgb;
    private String lamp_v_degree;
    private String lamp_h_degree;
    private String lamp_l_degree;

    public String getLamp_id() {
        return lamp_id;
    }

    public void setLamp_id(String lamp_id) {
        this.lamp_id = lamp_id;
    }

    public String getLamp_brightness() {
        return lamp_brightness;
    }

    public void setLamp_brightness(String lamp_brightness) {
        this.lamp_brightness = lamp_brightness;
    }

    public String getLamp_colorTemp() {
        return lamp_colorTemp;
    }

    public void setLamp_colorTemp(String lamp_colorTemp) {
        this.lamp_colorTemp = lamp_colorTemp;
    }

    public String getLamp_rgb() {
        return lamp_rgb;
    }

    public void setLamp_rgb(String lamp_rgb) {
        this.lamp_rgb = lamp_rgb;
    }

    public String getLamp_v_degree() {
        return lamp_v_degree;
    }

    public void setLamp_v_degree(String lamp_v_degree) {
        this.lamp_v_degree = lamp_v_degree;
    }

    public String getLamp_h_degree() {
        return lamp_h_degree;
    }

    public void setLamp_h_degree(String lamp_h_degree) {
        this.lamp_h_degree = lamp_h_degree;
    }

    public String getLamp_l_degree() {
        return lamp_l_degree;
    }

    public void setLamp_l_degree(String lamp_l_degree) {
        this.lamp_l_degree = lamp_l_degree;
    }

    @Override
    public Object clone() {

        Object o = null;
        try {
            o = super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return o;
    }
}
