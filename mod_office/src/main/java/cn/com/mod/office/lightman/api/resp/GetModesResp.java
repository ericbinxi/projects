package cn.com.mod.office.lightman.api.resp;

import java.util.List;

import cn.com.mod.office.lightman.api.BaseResp;
import cn.com.mod.office.lightman.entity.DynamicMode;
import cn.com.mod.office.lightman.entity.NormalMode;

/**
 * Created by Administrator on 2016/10/23.
 */
public class GetModesResp extends BaseResp {
    private List<NormalMode> normal_modes;
    private List<DynamicMode> dynamic_modes;

    public List<NormalMode> getNormal_modes() {
        return normal_modes;
    }

    public void setNormal_modes(List<NormalMode> normal_modes) {
        this.normal_modes = normal_modes;
    }

    public List<DynamicMode> getDynamic_modes() {
        return dynamic_modes;
    }

    public void setDynamic_modes(List<DynamicMode> dynamic_modes) {
        this.dynamic_modes = dynamic_modes;
    }
}
