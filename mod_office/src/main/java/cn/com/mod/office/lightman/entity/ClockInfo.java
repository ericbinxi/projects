package cn.com.mod.office.lightman.entity;

/**
 * Created by CAT on 2014/9/23.
 */
public class ClockInfo {
    public static final int SCENE_TYPE_DEFAULT = 0;
    public static final int SCENE_TYPE_DIY = 1;

    public String clockId;
    public String sceneId;
    public int sceneType;
    public String sceneName;
    public String week;
    public String time;
    public boolean status;
}
