package cn.com.mod.office.lightman.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/9.
 */
public class FaultRecord implements Serializable {

    private String msg_title;
    private String status;
    private String msg_content;
    private String lamp_id;
    private long op_time;

    public String getMsg_title() {
        return msg_title;
    }

    public void setMsg_title(String msg_title) {
        this.msg_title = msg_title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg_content() {
        return msg_content;
    }

    public void setMsg_content(String msg_content) {
        this.msg_content = msg_content;
    }

    public String getLamp_id() {
        return lamp_id;
    }

    public void setLamp_id(String lamp_id) {
        this.lamp_id = lamp_id;
    }

    public long getOp_time() {
        return op_time;
    }

    public void setOp_time(long op_time) {
        this.op_time = op_time;
    }
}
