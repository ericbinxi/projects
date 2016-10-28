package cn.com.mod.office.lightman.entity;

import java.io.Serializable;

/**
 * 动态模式中的帧
 * Created by Administrator on 2016/10/23.
 */
public class Frame implements Serializable {
//    frame_id	Int		帧标识
//    hour	Int(0~23)		开始的小时
//    minute	Int(0~59)		开始的分钟
//    second	Int(0~59)		开始的秒
//    is_smooth	Char	1	是否平滑过渡
//    lamp_id	String		LED灯标识
//    lamp_brightness	Int(0~100)		LED灯亮度
//    lamp_colorTemp	Int(0~255)		LED灯色温
//    lamp_rgb	String		LED灯颜色，十六进制表示
//    lamp_h_degree	Int		LED灯水平角度
//    lamp_v_degree	Int		LED灯垂直角度
//    lamp_l_degree	Int		LED灯光束角度

    private int frame_id;
    private int hour;
    private int minute;
    private int second;
    private char is_smooth;
    private String lamp_id;
    private int lamp_brightness;
    private int lamp_colorTemp;
    private String lamp_rgb;
    private int lamp_h_degree;
    private int lamp_v_degree;
    private int lamp_l_degree;

    public int getFrame_id() {
        return frame_id;
    }

    public void setFrame_id(int frame_id) {
        this.frame_id = frame_id;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public char getIs_smooth() {
        return is_smooth;
    }

    public void setIs_smooth(char is_smooth) {
        this.is_smooth = is_smooth;
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
