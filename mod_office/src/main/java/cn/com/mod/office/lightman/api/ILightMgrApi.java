package cn.com.mod.office.lightman.api;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ProgressBar;

import java.io.File;
import java.util.List;

import cn.com.mod.office.lightman.api.resp.ClocksResp;
import cn.com.mod.office.lightman.api.resp.FloorsResp;
import cn.com.mod.office.lightman.api.resp.GetModesResp;
import cn.com.mod.office.lightman.api.resp.LoginResp;
import cn.com.mod.office.lightman.entity.ApkVersionInfo;
import cn.com.mod.office.lightman.entity.BaseResponse;
import cn.com.mod.office.lightman.entity.ClockInfo;
import cn.com.mod.office.lightman.entity.DiySceneInfo;
import cn.com.mod.office.lightman.entity.FloorDivideInfo;
import cn.com.mod.office.lightman.entity.FloorInfo;
import cn.com.mod.office.lightman.entity.Frame;
import cn.com.mod.office.lightman.entity.GroupInfo;
import cn.com.mod.office.lightman.entity.Lamps;
import cn.com.mod.office.lightman.entity.LedInfo;
import cn.com.mod.office.lightman.entity.RoomEntity;
import cn.com.mod.office.lightman.entity.SceneInfo;
import cn.com.mod.office.lightman.entity.UserInfo;

/**
 * 客户端发送接口
 * Created by CAT on 2014/9/19.
 */
public interface ILightMgrApi {

    /**
     * 登录
     *
     * @param username 用户名
     * @param password 密码
     * @param callback 携带响应结果的回调函数
     */
    public void login(String username, String password, Callback<LoginResp> callback);

    /**
     * 退出登录
     */
    public void logout();

    /**
     * 获取个人信息
     *
     * @param callback 携带响应结果的回调函数
     */
    public void getInfo(Callback<UserInfo> callback);

    /**
     * 修改密码
     *
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @param callback    携带响应结果的回调函数
     */
    public void modifyPassword(String oldPassword, String newPassword, Callback<BaseResponse> callback);

    /**
     * 列出楼层
     *
     * @param callback 携带响应结果的回调函数
     */
    public void listFloors(Callback<FloorsResp> callback);

    /**
     * 获取楼层图片
     *
     * @param floorId  楼层编号
     * @param callback 携带响应结果的回调函数
     */
    public void getFloorImg(String floorId, Callback<Bitmap> callback);

    /**
     * 获取楼层区域划分方案
     *
     * @param floorId  楼层编号
     * @param callback 携带响应结果的回调函数
     */
    public void getFloorDivide(String floorId, Callback<FloorDivideInfo> callback);

    /**
     * 获取房间图片
     *
     * @param roomId   房间编号
     * @param callback 携带响应结果的回调函数
     */
    public void getRoomImg(String roomId, Callback<Bitmap> callback);

    /**
     * 获取房间内，默认情景模式的名称
     *
     * @param roomId   房间编号
     * @param callback 携带响应结果的回调函数
     */
    public void getDefaultSceneName(String roomId, Callback<List<SceneInfo>> callback);

    /**
     * 获取房间内，自定义情景列表
     *
     * @param roomId   房间编号
     * @param callback 携带响应结果的回调函数
     */
    public void listDiyScene(String roomId, Callback<List<SceneInfo>> callback);

    /**
     * 列出灯组内，LED灯列表
     *
     * @param groupId  灯组编号
     * @param callback 携带响应结果的回调函数
     */
    public void listLeds(String groupId, Callback<List<LedInfo>> callback);

    /**
     * 控制LED灯
     *
     * @param ledIds     LED灯编号集
     * @param brightness 亮度
     * @param colorTemp  色温
     */
    public void controlLed(String[] ledIds, int brightness, int colorTemp);

    /**
     * 删除自定义情景
     *
     * @param sceneId  字符串
     * @param callback 携带响应结果的回调函数
     */
    public void deleteDiyScene(String sceneId, Callback<BaseResponse> callback);

    /**
     * 应用默认情景
     *
     * @param sceneId  情景编号
     * @param floorId  楼层编号
     * @param roomIds  房间编号集
     * @param groupIds 灯组编号集
     * @param ledIds   LED灯编号集
     * @param callback 携带响应结果的回调函数
     */
    public void applyDefaultScene(String sceneId, String floorId, String[] roomIds, String[] groupIds, String[] ledIds, Callback<BaseResponse> callback);

    /**
     * 应用自定义情景编号
     *
     * @param sceneId  情景编号
     * @param roomId   房间编号
     * @param groupIds 灯组编号集
     * @param ledIds   LED灯编号集
     * @param callback 携带响应结果的回调函数
     */
    public void applyDiyScene(String sceneId, String roomId, String[] groupIds, String[] ledIds, Callback<BaseResponse> callback);

    /**
     * 获取用户头像
     *
     * @param username 用户名
     * @param callback 携带响应结果的回调函数
     */
    public void getUserImg(String username, Callback<Bitmap> callback);

    /**
     * 上传头像
     *
     * @param userImg  用户头像
     * @param callback 携带响应结果的回调函数
     */
    public void uploadUserImg(File userImg, Callback<BaseResponse> callback);

    /**
     * 获取服务条款文本
     *
     * @param callback 携带响应结果的回调函数
     */
    public void getClause(Callback<String> callback);

    /**
     * 提交反馈意见
     *
     * @param content  反馈意见
     * @param callback 携带响应结果的回调函数
     */
    public void submitFeedback(String content, Callback<BaseResponse> callback);

    /**
     * 添加闹钟
     *
     * @param floorId    楼层编号
     * @param roomIds    房间编号集
     * @param groupIds   灯组编号集
     * @param sceneId    情景编号
     * @param diySceneId 自定义情景编号
     * @param week       周期
     * @param time       时间
     * @param status     开启状态
     * @param callback   携带响应结果的回调函数
     */
    public void addClock(String floorId, String[] roomIds, String[] groupIds, String sceneId, String diySceneId, String week, String time, boolean status, Callback<BaseResponse> callback);

    /**
     * 修改闹钟
     *
     * @param clockId  闹钟编号
     * @param week     周期
     * @param time     时间
     * @param callback 携带响应结果的回调函数
     */
    public void updateClock(String clockId, String week, String time, Callback<BaseResponse> callback);

    /**
     * 开启闹钟
     *
     * @param clockId  闹钟编号
     * @param callback 携带响应结果的回调函数
     */
    public void openClock(String roomId,String clockId, Callback<BaseResponse> callback);

    /**
     * 关闭闹钟
     *
     * @param clockId  闹钟编号
     * @param callback 携带响应结果的回调函数
     */
    public void closeClock(String roomId,String clockId, Callback<BaseResponse> callback);

    /**
     * 列出房间
     *
     * @param floorId  楼层编号
     * @param callback 携带响应结果的回调函数
     */
    public void listRooms(String floorId, Callback<List<RoomEntity>> callback);

    /**
     * 获取房间内，闹钟列表
     *
     * @param roomId   房间编号
     * @param callback 携带响应结果的回调函数
     */
    public void getRoomClocks(String roomId, Callback<ClocksResp> callback);

    /**
     * 获取房间内，灯组列表
     *
     * @param roomId   房间编号
     * @param callback 携带响应结果的回调函数
     */
    public void listGroups(String roomId, Callback<List<GroupInfo>> callback);

    /**
     * 获取灯组内，闹钟列表
     *
     * @param groupId  灯组编号
     * @param callback 携带响应结果的回调函数
     */
    public void getGroupClocks(String groupId, Callback<List<ClockInfo>> callback);

    /**
     * 删除闹钟
     *
     * @param clockId  闹钟编号
     * @param callback 携带响应结果的回调函数
     */
    public void deleteClock(String roomId,String clockId, Callback<BaseResponse> callback);

    /**
     * 获取服务器Apk版本号
     *
     * @param callback 携带响应结果的回调函数
     */
    public void getApkVersion(Callback<ApkVersionInfo> callback);

    /**
     * 获取服务器最新Apk文件
     *
     * @param callback 携带响应结果的回调函数
     */
    public void getApkFile(Callback<File> callback);

    /**
     * 设置LED灯的RGB值
     *
     * @param ledIds LED灯编号集
     * @param red    R_VAL
     * @param green  G_VAL
     * @param blue   B_VAL
     */
    public void setRGB(String[] ledIds, int red, int green, int blue);

    /**
     * 设置LED灯的亮度
     *
     * @param ledIds     LED灯编号集
     * @param brightness 亮度
     */
    public void setBrightness(String[] ledIds, int brightness);

    /**
     * 设置LED灯的色温
     *
     * @param ledIds    LED灯编号集
     * @param colorTemp 色温
     */
    public void setColorTemp(String[] ledIds, int colorTemp);

    /**
     * 获取自定义情景模式
     *
     * @param sceneId  自定义情景模式
     * @param callback 携带响应结果的回调函数
     */
    public void getDiySceneInfo(String sceneId, Callback<DiySceneInfo> callback);

    /**
     * 获取自定义情景模式的图标
     *
     * @param sceneId  自定义情景模式编号
     * @param callback 携带响应结果的回调函数
     */
    public void getDiySceneImg(String sceneId, Callback<Bitmap> callback);

    /**
     * 上传头像
     *
     * @param sceneId  自定义情景模式编号
     * @param sceneImg 自定义情景图标
     * @param callback 携带响应结果的回调函数
     */
    public void uploadDiySceneImg(String sceneId, File sceneImg, Callback<BaseResponse> callback);

    /**
     * 创建自定义情景
     *
     * @param roomId     房间编号
     * @param name       自定义情景模式名称
     * @param brightness 亮度
     * @param colorTemp  色温
     * @param red        R_VAL
     * @param green      G_VAL
     * @param blue       B_VAL
     * @param file       图标
     * @param callback   携带响应结果的回调函数
     */
    public void createDiyScene(String roomId, String name, int brightness, int colorTemp, int red, int green, int blue, File file, Callback<BaseResponse> callback);

    /**
     * 修改自定义情景
     *
     * @param sceneId    情景编号
     * @param newName    修改后的自定义情景名称
     * @param brightness 修改后的亮度值
     * @param colorTemp  修改后的色温值
     * @param red        修改后的R_VAL
     * @param green      修改后的G_VAL
     * @param blue       修改后的B_VAL
     * @param file       图标
     * @param callback   携带响应结果的回调函数
     */
    public void modifyDiyScene(String sceneId, String newName, int brightness, int colorTemp,
                               int red, int green, int blue, File file, Callback<BaseResponse> callback);


    /**
     * 测试服务器连通性
     *
     * @return 是否成功
     */
    public void connectTest(Callback<Boolean> callback);


    /**
     * 绑定进度监听
     *
     * @param progressBar 需要监听进度的进度条
     */
    public void bindProgressHandler(ProgressBar progressBar);

    /**
     * 解除进度监听
     */
    public void unbindProgressHandler();

    public void onSaveInstanceState(Bundle outState);

    public void onRestoreInstanceState(Bundle savedInstanceState);

    public void getLampsInRoom(String roomId,Callback<List<Lamps>> lamps);

    public void getModes(String room_id, Callback<GetModesResp> callback);
    public void createNormalMode(String roomId,String modeName,String[] lampId,String lamp_rgb,
                                 int lamp_brightness,int lamp_colorTemp,int lamp_h_degree,
                                 int lamp_v_degree,int lamp_l_degree,Callback<BaseResp> callback);
    public void createDynamicMode(String roomId, String modeName, List<Frame> frames, int hour, int minute, int second, String lamp_rgb,
                                  int lamp_brightness, int lamp_colorTemp, int lamp_h_degree,
                                  int lamp_v_degree, int lamp_l_degree, Callback<BaseResp> callback);

    public interface Callback<T> {
        int CODE_SUCCESS = 0;
        int CODE_NETWORK_ERROR = 2;
        int CODE_TIMEOUT = 3;
        int CODE_FAILURE = 1;

        void callback(int code, T entity);
    }

    public interface ResponseHandler<T> {
        void handle(T response);
    }
}
