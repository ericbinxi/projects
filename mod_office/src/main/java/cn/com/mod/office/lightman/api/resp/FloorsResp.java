package cn.com.mod.office.lightman.api.resp;

import java.util.List;

import cn.com.mod.office.lightman.api.BaseResp;

/**
 * Created by Administrator on 2016/10/15.
 */
public class FloorsResp extends BaseResp {


    /**
     * floor_num : 6
     * floor_id : [{"org_id":"0001002001000000000"},{"org_id":"0001002002000000000"},{"org_id":"0001002003000000000"},{"org_id":"0001002004000000000"},{"org_id":"0001002005000000000"},{"org_id":"0001002006000000000"}]
     */

    private String floor_num;
    /**
     * org_id : 0001002001000000000
     */

    private List<FloorIdBean> floor_id;

    public String getFloor_num() {
        return floor_num;
    }

    public void setFloor_num(String floor_num) {
        this.floor_num = floor_num;
    }

    public List<FloorIdBean> getFloors() {
        return floor_id;
    }

    public void setFloors(List<FloorIdBean> floors) {
        this.floor_id = floors;
    }

    public static class FloorIdBean {
        private String floor_id;
        private String floor_name;

        public String getFloor_id() {
            return floor_id;
        }

        public void setFloor_id(String floor_id) {
            this.floor_id = floor_id;
        }

        public String getFloor_name() {
            return floor_name;
        }

        public void setFloor_name(String floor_name) {
            this.floor_name = floor_name;
        }
    }
}
