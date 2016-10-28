package cn.com.mod.office.lightman.entity;

import android.widget.ImageView;

import java.io.Serializable;

import cn.com.mod.office.lightman.entity.base.Item;

/**
 * Created by Administrator on 2016/10/16.
 */
public class RoomEntity extends Item implements Serializable{

    /**
     * room_id : 0001002004001000000
     * room_name : 展厅
     * room_x : 186  箭头横坐标
     * room_y : 32   箭头纵坐标
     * resolution_x : 500  分辨率
     * resolution_y : 148
     */

    private String room_id;
    private String room_name;
    private int room_x;
    private int room_y;
    private int resolution_x;
    private int resolution_y;

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }

    public String getRoom_name() {
        return room_name;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }

    public int getRoom_x() {
        return room_x;
    }

    public void setRoom_x(int room_x) {
        this.room_x = room_x;
    }

    public int getRoom_y() {
        return room_y;
    }

    public void setRoom_y(int room_y) {
        this.room_y = room_y;
    }

    public int getResolution_x() {
        return resolution_x;
    }

    public void setResolution_x(int resolution_x) {
        this.resolution_x = resolution_x;
    }

    public int getResolution_y() {
        return resolution_y;
    }

    public void setResolution_y(int resolution_y) {
        this.resolution_y = resolution_y;
    }

}
