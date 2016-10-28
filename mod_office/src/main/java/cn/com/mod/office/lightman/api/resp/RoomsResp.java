package cn.com.mod.office.lightman.api.resp;

import java.util.List;

import cn.com.mod.office.lightman.api.BaseResp;
import cn.com.mod.office.lightman.entity.RoomEntity;

/**
 * Created by Administrator on 2016/10/16.
 */
public class RoomsResp extends BaseResp{
    private List<RoomEntity> rooms;

    public List<RoomEntity> getRooms() {
        return rooms;
    }

    public void setRooms(List<RoomEntity> rooms) {
        this.rooms = rooms;
    }
}
