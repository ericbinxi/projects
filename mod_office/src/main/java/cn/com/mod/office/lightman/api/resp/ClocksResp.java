package cn.com.mod.office.lightman.api.resp;

import java.util.List;

import cn.com.mod.office.lightman.api.BaseResp;
import cn.com.mod.office.lightman.entity.Clock;

/**
 * Created by Administrator on 2016/10/23.
 */
public class ClocksResp extends BaseResp {

    private List<Clock> clocks;

    public List<Clock> getClocks() {
        return clocks;
    }

    public void setClocks(List<Clock> clocks) {
        this.clocks = clocks;
    }
}
