package cn.com.mod.office.lightman.utils;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.MotionEvent;

import java.lang.reflect.Method;
import java.util.UUID;

public class CommonUtil {
    protected static int mUniqueId = 0;

    public synchronized static int getUniqueId() {
        return mUniqueId++;
    }

    /**
     * 获取sdk version信息
     *
     * @return int 版本号
     */
    public static int getAndroidSDKVersion() {
        int version = 0;
        try {
            version = android.os.Build.VERSION.SDK_INT;
        } catch (NumberFormatException e) {
        }
        return version;
    }

    /**
     * 获取机器唯一标志码
     *
     * @param context
     * @param sim
     *            是否跟sim卡相关
     * @return
     */
    // public static String getUUID(boolean sim, Context context)
    // {
    // final TelephonyManager tm = (TelephonyManager)
    // context.getSystemService(Context.TELEPHONY_SERVICE);
    // final String tmDevice, tmSerial, androidId;
    // tmDevice = "" + tm.getDeviceId();
    // tmSerial = "" + (sim ? tm.getSimSerialNumber() : "sim");
    // androidId = "" + Secure.getString(context.getContentResolver(),
    // Secure.ANDROID_ID);
    // UUID deviceUuid = new UUID(androidId.hashCode(), ((long)
    // tmDevice.hashCode() << 32) | tmSerial.hashCode());
    // return deviceUuid.toString();
    // }

    /**
     * 处理版本号
     *
     * @param version
     *            版本信息
     * @return String
     */
    // public static String dealVersion(int version)
    // {
    // String value = "0000";
    // value = version + "";
    // switch (value.length())
    // {
    // case 1:
    // value = "000" + value;
    // break;
    // case 2:
    // value = "00" + value;
    // break;
    // case 3:
    // value = "0" + value;
    // break;
    // default:
    //
    // break;
    // }
    // return value;
    // }

    /**
     * 将字符串用base64编码
     *
     * @param str
     * @return
     */
    // public static final String uriEncode(String str)
    // {
    // return Uri.encode(str);
    // }

    /**
     * 将base64字符串解码
     *
     * @param str
     * @return
     */
    // public static final String uriDecode(String str)
    // {
    // return Uri.decode(str);
    // }

    /***
     * 当前machine是否支持多点触摸
     *
     * @return
     */
    public static boolean isSupportMultiTouch() {
        boolean condition1 = false;
        boolean condition2 = false;
        // Not checking for getX(int), getY(int) etc 'cause I'm lazy
        Method methods[] = MotionEvent.class.getDeclaredMethods();
        for (Method m : methods) {
            if (m.getName().equals("getPointerCount"))
                condition1 = true;
            if (m.getName().equals("getPointerId"))
                condition2 = true;
        }
        if (getAndroidSDKVersion() >= 7 || (condition1 && condition2))
            return true; // 支持多点触摸
        else
            return false;
    }

    // 策略调整， 中图都为460
    public static String getMiddleImageSize() {
        return "/460";
    }

    /**
     * 解析字符串为整形数, 转换出错默认返回0;
     *
     * @param str
     * @return
     */
    public static int parseInt(String str) {
        return parseInt(str, 0);
    }

    /**
     * 解析字符串为整数, 转换出错返回指定默认值
     *
     * @param str
     * @param defaultValue
     * @return
     */
    public static int parseInt(String str, int defaultValue) {
        int value = defaultValue;
        try {
            value = Integer.parseInt(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    public static final int getMaxHeapSize() {
        long mx = Runtime.getRuntime().maxMemory();
        // WLog.e("" + mx);
        return (int) (mx / 1024 / 1024);
    }

    public static final int GENDER_MALE = 1;
    public static final int GENDER_FEMALE = 2;

    // 请求间隔5分钟
    public static final long UPDATE_INTERVAL = 5 * 60 * 1000;


    /**
     * @return String
     * @throws @Title: getImgName
     * @Description: 产生随机图片名字 20位码
     */
    public static String getImgName() {
        String str = UUID.randomUUID().toString();
        String[] strArr = str.split("-");
        StringBuffer sb = new StringBuffer();
        for (String string : strArr) {
            sb.append(string);
        }
        return sb.toString() + ".jpg";
    }
}
