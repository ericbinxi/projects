package cn.com.mod.office.lightman.entity;

import android.graphics.drawable.Drawable;

/**
 * Created by CAT on 2014/9/23.
 */
public class SceneInfo {
    public static final int TYPE_DEFAULT = 0;
    public static final int TYPE_DIY = 1;
    public static final int TYPE_NONE = 2;
    public static final int TYPE_DIY_NORMAL = 3;
    public static final int TYPE_DIY_DYNAMIC = 4;

    public String id;
    public String name;
    public Drawable icon;
    public int type;
    public int mode_type;

    public SceneInfo() {

    }

    public SceneInfo(String id, String name, Drawable icon, int type) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.type = type;
    }
}
