package cn.com.mod.office.lightman.entity;

import java.io.Serializable;
import java.util.List;

import cn.com.mod.office.lightman.entity.base.BaseModeEntity;

/**
 * Created by Administrator on 2016/10/23.
 */
public class DynamicMode extends BaseModeEntity {
    private List<Frame> frames;

    public List<Frame> getFrames() {
        return frames;
    }

    public void setFrames(List<Frame> frames) {
        this.frames = frames;
    }
}
