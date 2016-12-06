package cn.com.mod.office.lightman.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/16.
 */
public class Lamps implements Serializable {

    //电动可调
    public static final int LAMP_TYPE_ELEC_0 = 0;
    //电动色温
    public static final int LAMP_TYPE_ELEC_1 = 1;
    //电动RGB
    public static final int LAMP_TYPE_ELEC_2 = 2;
    //圆形亮度
    public static final int LAMP_TYPE_ELEC_3 = 3;
    //圆形色温
    public static final int LAMP_TYPE_ELEC_4 = 4;
    //圆形RGB
    public static final int LAMP_TYPE_ELEC_5 = 5;
    //条形亮度
    public static final int LAMP_TYPE_ELEC_6 = 6;
    //条形色温
    public static final int LAMP_TYPE_ELEC_7 = 7;
    //条形RGB
    public static final int LAMP_TYPE_ELEC_8 = 8;


    /**
     * lamp_id : 0001002004008000002
     * lamp_name : 四层侯梯厅灯具2
     * lamp_x : 380
     * lamp_y : 202
     */

    private String group_id;
    private String lamp_id;
    private String lamp_name;
    private int lamp_x;
    private int lamp_y;
    private String type_id;
    private String type_desc;

    private String lamp_status;//灯状态
    private int lamp_brightness;
    private int lamp_colorTemp;
    private int lamp_rgb;
    private int lamp_h_degree;
    private int lamp_v_degree;
    private int lamp_l_degree;//光速角度

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
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
        return lamp_rgb;
    }

    public void setLamp_RGB(int lamp_RGB) {
        this.lamp_rgb = lamp_RGB;
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
    //    private int lamp_movement;//位移

    public String getLamp_id() {
        return lamp_id;
    }

    public void setLamp_id(String lamp_id) {
        this.lamp_id = lamp_id;
    }

    public String getLamp_name() {
        return lamp_name;
    }

    public void setLamp_name(String lamp_name) {
        this.lamp_name = lamp_name;
    }

    public int getLamp_x() {
        return lamp_x;
    }

    public void setLamp_x(int lamp_x) {
        this.lamp_x = lamp_x;
    }

    public int getLamp_y() {
        return lamp_y;
    }

    public void setLamp_y(int lamp_y) {
        this.lamp_y = lamp_y;
    }

    public String getLamp_stutus() {
        return lamp_status;
    }

    public void setLamp_stutus(String lamp_stutus) {
        this.lamp_status = lamp_stutus;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getType_desc() {
        return type_desc;
    }

    public void setType_desc(String type_desc) {
        this.type_desc = type_desc;
    }
}
