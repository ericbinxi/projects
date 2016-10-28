package cn.com.mod.office.lightman.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/10/23.
 */
public class DynamicMode implements Serializable {
//    mode_id	Int		情景模式标识
//    mode_name	String	20	情景模式名称
//    frames	对象数组		帧列表

    private int mode_id;
    private String mode_name;
    private List<Frame> frames;

    public int getMode_id() {
        return mode_id;
    }

    public void setMode_id(int mode_id) {
        this.mode_id = mode_id;
    }

    public String getMode_name() {
        return mode_name;
    }

    public void setMode_name(String mode_name) {
        this.mode_name = mode_name;
    }

    public List<Frame> getFrames() {
        return frames;
    }

    public void setFrames(List<Frame> frames) {
        this.frames = frames;
    }
}
