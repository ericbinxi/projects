package cn.com.mod.office.lightman.api.resp;

import java.util.List;

import cn.com.mod.office.lightman.api.BaseResp;
import cn.com.mod.office.lightman.entity.DynamicMode;
import cn.com.mod.office.lightman.entity.NormalMode;

/**
 * Created by Administrator on 2016/10/23.
 */
public class GetModesResp extends BaseResp {
//    mode_type	Char	1	0：普通模式，1：动态模式
//    normal_modes	对象数组		普通模式参数
//    dynamic_modes	对象数组		动态模式参数
    private char mode_type;
    private List<NormalMode> normal_modes;
    private List<DynamicMode> dynamic_modes;

    public char getMode_type() {
        return mode_type;
    }

    public void setMode_type(char mode_type) {
        this.mode_type = mode_type;
    }

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
