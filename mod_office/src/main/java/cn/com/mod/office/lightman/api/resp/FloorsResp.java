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

    public List<FloorIdBean> getFloor_id() {
        return floor_id;
    }

    public void setFloor_id(List<FloorIdBean> floor_id) {
        this.floor_id = floor_id;
    }

    public static class FloorIdBean {
        private String org_id;

        public String getOrg_id() {
            return org_id;
        }

        public void setOrg_id(String org_id) {
            this.org_id = org_id;
        }
    }
}
