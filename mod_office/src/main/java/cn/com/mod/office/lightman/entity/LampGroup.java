package cn.com.mod.office.lightman.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/12.
 */
public class LampGroup implements Serializable {

    private String lampGroup_id;
    private String lampGroup_name;

    public String getLampGroup_id() {
        return lampGroup_id;
    }

    public void setLampGroup_id(String lampGroup_id) {
        this.lampGroup_id = lampGroup_id;
    }

    public String getLampGroup_name() {
        return lampGroup_name;
    }

    public void setLampGroup_name(String lampGroup_name) {
        this.lampGroup_name = lampGroup_name;
    }
}
