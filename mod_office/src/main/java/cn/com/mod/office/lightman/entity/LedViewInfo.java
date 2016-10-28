package cn.com.mod.office.lightman.entity;

import cn.com.mod.office.lightman.widget.LedView;

/**
 * Created by Administrator on 2016/10/21.
 */
public class LedViewInfo {
    private LedView  ledView;
    private Lamps lamps;

    public LedView getLedView() {
        return ledView;
    }

    public void setLedView(LedView ledView) {
        this.ledView = ledView;
    }

    public Lamps getLamps() {
        return lamps;
    }

    public void setLamps(Lamps lamps) {
        this.lamps = lamps;
    }
}
