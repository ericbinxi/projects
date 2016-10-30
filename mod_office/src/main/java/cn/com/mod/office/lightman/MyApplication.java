package cn.com.mod.office.lightman;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cn.com.mod.office.lightman.api.ILightMgrApi;
import cn.com.mod.office.lightman.api.LightMgrApi;
import cn.com.mod.office.lightman.config.AppConfig;
import cn.com.mod.office.lightman.config.ConfigUtils;
import cn.com.mod.office.lightman.entity.UserInfo;

/**
 * Created by CAT on 2014/10/22.
 */
public class MyApplication extends Application {
    public static MyApplication instance;
    // 与服务器交互的客户端服务组件
    private ILightMgrApi mClient;
    // 应用配置
    private ConfigUtils mConfigUtils;
    // 加载本地头像资源
    private Map<String, Bitmap> mLocalHeaders;

    public static MyApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        instance = this;
        AppConfig.HOST = getAppConfig().getHost();
    }

    // 获取客户端服务组件
    public ILightMgrApi getClient() {
        if (mClient == null) {
            mClient = new LightMgrApi(getApplicationContext());
        }
        return mClient;
    }

    // 获取应用配置
    public ConfigUtils getAppConfig() {
        if (mConfigUtils == null) {
            mConfigUtils = new ConfigUtils(getApplicationContext());
        }
        return mConfigUtils;
    }

    // 获取本地资源中，制定用户名的头像
    public Bitmap getLocalHeader(String username) {
        if (mLocalHeaders == null) {
            loadLocalCache();
        }
        if (mLocalHeaders.keySet().contains(username)) {
            return mLocalHeaders.get(username);
        } else {
            return null;
        }
    }

    // 加载本地资源
    public void loadLocalCache() {
        File dir;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            dir = new File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath());
        } else {
            dir = new File(getFilesDir() + File.separator + Environment.DIRECTORY_PICTURES);
        }
        mLocalHeaders = new HashMap<>();
        if (dir.exists()) {
            String[] files = dir.list();
            if (files != null) {
                for (String fileName : files) {
                    String username = fileName.split("\\.")[0];
                    mLocalHeaders.put(username, BitmapFactory.decodeFile(dir.getAbsolutePath() + File.separator + fileName));
                }
            }
        }
    }

    // 把用户头像资源写入本地缓存
    public void writeLocalHeader(UserInfo userInfo, Bitmap img) {
        String dir;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            dir = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
        } else {
            dir = getFilesDir() + File.separator + Environment.DIRECTORY_PICTURES;
            File dirF = new File(dir);
            if (!dirF.exists()) {
                dirF.mkdir();
            }
        }
        FileOutputStream os = null;
        File outputImg = new File(dir + File.separator + userInfo.username + ".jpg");
        try {
            if (outputImg.exists()) {
                outputImg.delete();
            }
            outputImg.createNewFile();
            os = new FileOutputStream(outputImg);
            img.compress(Bitmap.CompressFormat.JPEG, 100, os);
            mLocalHeaders.put(userInfo.username, BitmapFactory.decodeFile(outputImg.getAbsolutePath()));
        } catch (IOException e) {

        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
