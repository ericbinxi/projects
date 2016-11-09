package cn.com.mod.office.lightman.entity;

import java.util.List;

import cn.com.mod.office.lightman.api.BaseResp;

/**
 * Created by Administrator on 2016/11/9.
 */
public class FaultRecordResp extends BaseResp{
    private List<FaultRecord> fault_record;

    public List<FaultRecord> getFault_record() {
        return fault_record;
    }

    public void setFault_record(List<FaultRecord> fault_record) {
        this.fault_record = fault_record;
    }
}
