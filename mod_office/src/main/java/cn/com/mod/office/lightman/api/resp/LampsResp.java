package cn.com.mod.office.lightman.api.resp;

import java.util.List;

import cn.com.mod.office.lightman.api.BaseResp;
import cn.com.mod.office.lightman.entity.Lamps;

/**
 * Created by Administrator on 2016/10/16.
 */
public class LampsResp extends BaseResp {

    private List<Lamps> lamps;

    public List<Lamps> getLamps() {
        return lamps;
    }

    public void setLamps(List<Lamps> lamps) {
        this.lamps = lamps;
    }
}
