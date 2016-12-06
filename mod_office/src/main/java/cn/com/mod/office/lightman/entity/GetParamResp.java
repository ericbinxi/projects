package cn.com.mod.office.lightman.entity;

import java.util.List;

import cn.com.mod.office.lightman.api.BaseResp;

/**
 * Created by Administrator on 2016/11/13.
 */
public class GetParamResp extends BaseResp {

    private List<ParamEntity> params;

    public List<ParamEntity> getParams() {
        return params;
    }

    public void setParams(List<ParamEntity> params) {
        this.params = params;
    }
}
