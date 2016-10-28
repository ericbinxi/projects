package cn.com.mod.office.lightman.entity;

/**
 * Created by CAT on 2014/9/23.
 */
public class LedInfo {
    // 在线
    public static final int STATUS_ONLINE = 1;
    // 不在线
    public static final int STATUS_OFFLINE = 0;
    // 故障
    public static final int STATUS_WRONG = 0;
    // 落地灯
    public static final int TYPE_FLOOR_LAMP = 0;
    // 洗墙灯
    public static final int TYPE_WALL_WASHER = 1;
    // 线条灯
    public static final int TYPE_LINE_LAMP = 2;
    // 双头格栅灯
    public static final int TYPE_DOUBLE_GRILLE_LAMP = 3;
    // 射灯
    public static final int TYPE_SPOT_LAMP = 4;


    public String ledId;
    public int status;
    public int type;
    public int positionX;
    public int positionY;
}
