package cn.com.mod.office.lightman.config;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 内部存取数据工具
 * Created by CAT on 2014/9/17.
 */
public class ConfigUtils {
    // 配置文件名称
    public static final String INFO_FILE = "mod_info";

    private static String FIRST_LAUNCH = "first_launch"; // 首次登录
    private static String AUTO_LOGIN = "auto_login"; // 自动登录
    private static String REMEMBER_PASS = "remember_pass"; // 记住密码
    private static String USERNAME = "username"; // 用户名
    private static String PASSWORD = "password"; // 密码
    private static String HOST = "host";
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;


    public ConfigUtils(Context context) {
        mPreferences = context.getSharedPreferences(INFO_FILE, Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();
    }

    public String getHost() {
        return mPreferences.getString(HOST, AppConfig.HOST);
    }

    public void setHost(String host) {
        mEditor.putString(HOST, host);
        mEditor.commit();
    }

    public String getUsername() {
        return mPreferences.getString(USERNAME, "");
    }

    public void setUsername(String username) {
        mEditor.putString(USERNAME, username);
        mEditor.commit();
    }

    public String getPassword() {
        return mPreferences.getString(PASSWORD, "");
    }

    public void setPasword(String password) {
        mEditor.putString(PASSWORD, password);
        mEditor.commit();
    }

    public boolean isFirstLaunch() {
        return mPreferences.getBoolean(FIRST_LAUNCH, true);
    }

    public void setFirstLaunch(boolean isFirstLaunch) {
        mEditor.putBoolean(FIRST_LAUNCH, isFirstLaunch);
        mEditor.commit();
    }

    public boolean isRememberPass() {
        return mPreferences.getBoolean(REMEMBER_PASS, false);
    }

    public void setRememberPass(boolean isRememberPass) {
        mEditor.putBoolean(REMEMBER_PASS, isRememberPass);
        mEditor.commit();
    }

    public boolean isAutoLogin() {
        return mPreferences.getBoolean(AUTO_LOGIN, false);
    }

    public void setAutoLogin(boolean isAutoLogin) {
        mEditor.putBoolean(AUTO_LOGIN, isAutoLogin);
        mEditor.commit();
    }
}
