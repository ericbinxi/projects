package cn.com.mod.office.lightman.entity;

import java.io.Serializable;
import java.util.List;

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

    private String frame_id;
    private String hour;
    private String minute;
    private String second;
    private String is_smooth;
    private List<LampParam> lamps;

    public String getFrame_id() {
        return frame_id;
    }

    public void setFrame_id(String frame_id) {
        this.frame_id = frame_id;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = second;
    }

    public String getIs_smooth() {
        return is_smooth;
    }

    public void setIs_smooth(String is_smooth) {
        this.is_smooth = is_smooth;
    }

    public List<LampParam> getLamps() {
        return lamps;
    }

    public void setLamps(List<LampParam> lamps) {
        this.lamps = lamps;
    }
}
