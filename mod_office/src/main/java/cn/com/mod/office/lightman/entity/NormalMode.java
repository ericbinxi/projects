package cn.com.mod.office.lightman.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/23.
 */
public class NormalMode implements Serializable {
//    mode_id	Int		情景模式标识
//    mode_name	String	20	情景模式名称
//    lamp_id	String		LED灯标识
//    lamp_brightness	Int(0~100)		LED灯亮度
//    lamp_colorTemp	Int(0~255)		LED灯色温
//    lamp_rgb	String		LED灯颜色，十六进制表示
//    lamp_h_degree	Int		LED灯水平角度
//    lamp_v_degree	Int		LED灯垂直角度
//    lamp_l_degree	Int		LED灯光束角度
    private int mode_id;
    private String mode_name;
    private String lamp_id;
    private int lamp_brightness;
    private int lamp_colorTemp;
    private String lamp_rgb;
    private int lamp_h_degree;
    private int lamp_v_degree;
    private int lamp_l_degree;

    public int getMode_id() {
        return mode_id;
    }

    public void setMode_id(int mode_id) {
        this.mode_id = mode_id;
    }

    public String getMode_name() {
        return mode_name;
    }

    public void setMode_name(String mode_name) {
        this.mode_name = mode_name;
    }

    public String getLamp_id() {
        return lamp_id;
    }

    public void setLamp_id(String lamp_id) {
        this.lamp_id = lamp_id;
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

    public String getLamp_rgb() {
        return lamp_rgb;
    }

    public void setLamp_rgb(String lamp_rgb) {
        this.lamp_rgb = lamp_rgb;
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
