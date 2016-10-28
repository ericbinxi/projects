package cn.com.mod.office.lightman.entity;

import android.widget.ImageView;

import java.util.List;

import cn.com.mod.office.lightman.entity.base.Item;

/**
 * Created by CAT on 2014/9/23.
 */
public class RoomInfo extends Item {
    private ImageView arrowImage;
    private RoomEntity roomEntity;
    private boolean isSelected;

    public ImageView getArrowImage() {
        return arrowImage;
    }

    public void setArrowImage(ImageView arrowImage) {
        this.arrowImage = arrowImage;
    }

    public RoomEntity getRoomEntity() {
        return roomEntity;
    }

    public void setRoomEntity(RoomEntity roomEntity) {
        this.roomEntity = roomEntity;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
