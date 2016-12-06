package cn.com.mod.office.lightman.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/23.
 */
public class Clock implements Serializable {

    /**
     * clockId : 18
     * mode_id : 2
     * weekday : 1,2,3,4,5,6
     * hour : 9
     * minute : 25
     * mode_name : 会议模式
     */

    private String clockId;
    private String mode_id;
    private String weekday;
    private String hour;
    private String minute;
    private String mode_name;
    private String clock_status;

    public String getClockId() {
        return clockId;
    }

    public void setClockId(String clockId) {
        this.clockId = clockId;
    }

    public String getMode_id() {
        return mode_id;
    }

    public void setMode_id(String mode_id) {
        this.mode_id = mode_id;
    }

    public String getWeekday() {
        return weekday;
    }

    public void setWeekday(String weekday) {
        this.weekday = weekday;
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

    public String getMode_name() {
        return mode_name;
    }

    public void setMode_name(String mode_name) {
        this.mode_name = mode_name;
    }

    public String getClock_status() {
        return clock_status;
    }

    public void setClock_status(String clock_status) {
        this.clock_status = clock_status;
    }
}
