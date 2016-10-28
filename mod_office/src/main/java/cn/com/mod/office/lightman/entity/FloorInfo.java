package cn.com.mod.office.lightman.entity;

import android.graphics.Bitmap;

/**
 * Created by CAT on 2014/9/23.
 */
public class FloorInfo {
    public String floorId;
    public String name;
    public int layer;
    public Bitmap image;

    @Override
    public String toString() {
        return "FloorInfo{" +
                "floorId='" + floorId + '\'' +
                ", name='" + name + '\'' +
                ", layer=" + layer +
                '}';
    }
}
