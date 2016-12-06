package cn.com.mod.office.lightman.entity;

import java.io.Serializable;
import java.util.List;

import cn.com.mod.office.lightman.entity.base.BaseModeEntity;

/**
 * Created by Administrator on 2016/10/23.
 */
public class NormalMode extends BaseModeEntity {
    private List<LampParam> lamps;

    public List<LampParam> getLamps() {
        return lamps;
    }

    public void setLamps(List<LampParam> lamps) {
        this.lamps = lamps;
    }
}
