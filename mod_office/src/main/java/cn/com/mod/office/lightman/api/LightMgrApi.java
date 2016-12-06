package cn.com.mod.office.lightman.api;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.joshua.common.util.ConnectUtils;
import com.joshua.common.util.MD5Utils;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.cookie.Cookie;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.mod.office.lightman.api.parser.BaseParse;
import cn.com.mod.office.lightman.api.resp.ClocksResp;
import cn.com.mod.office.lightman.api.resp.FloorsResp;
import cn.com.mod.office.lightman.api.resp.GetModesResp;
import cn.com.mod.office.lightman.api.resp.LampsResp;
import cn.com.mod.office.lightman.api.resp.LoginResp;
import cn.com.mod.office.lightman.api.resp.RoomsResp;
import cn.com.mod.office.lightman.config.AppConfig;
import cn.com.mod.office.lightman.entity.ApkVersionInfo;
import cn.com.mod.office.lightman.entity.BaseResponse;
import cn.com.mod.office.lightman.entity.ClockInfo;
import cn.com.mod.office.lightman.entity.DiySceneInfo;
import cn.com.mod.office.lightman.entity.FaultRecordResp;
import cn.com.mod.office.lightman.entity.FloorDivideInfo;
import cn.com.mod.office.lightman.entity.FloorInfo;
import cn.com.mod.office.lightman.entity.Frame;
import cn.com.mod.office.lightman.entity.GetParamResp;
import cn.com.mod.office.lightman.entity.GroupInfo;
import cn.com.mod.office.lightman.entity.LampParam;
import cn.com.mod.office.lightman.entity.LampStatusResp;
import cn.com.mod.office.lightman.entity.LedInfo;
import cn.com.mod.office.lightman.entity.RoomEntity;
import cn.com.mod.office.lightman.entity.SceneInfo;
import cn.com.mod.office.lightman.entity.TieLampsResp;
import cn.com.mod.office.lightman.entity.UserInfo;
import cn.com.mod.office.lightman.manager.AccountManager;

/**
 * 客户端发送接口的实现
 * Created by CAT on 2014/9/18.
 */
public class LightMgrApi implements ILightMgrApi {
    public static final String TAG = "LightMgrClient";

    // 验证所需的KEY
    public static final String VALID_KEY_COOKIE = "mod_user_id";
    public static final String VALID_KEY_SESSION = "JSESSIONID";
    // 验证所需的VALUE
    private String valid_val;
    private String jsession_id;
    private AQuery mAq;
    private ProgressBar mProgressBar;

    public LightMgrApi(Context context) {
        mAq = new AQuery(context.getApplicationContext());
    }

    /**
     * 一些在Aquery发送请求之前，接收响应之后的通用业务处理
     *
     * @param url      请求路径
     * @param params   参数
     * @param type     AjaxCallback的响应类型
     * @param handle   响应结果的处理
     * @param callback 控制器的回调函数
     */
    private void doService(String url, Map<String, Object> params, Class type, final ResponseHandler handle, final Callback callback) {
        // 检测网络连接
        if (!ConnectUtils.isConnect(mAq.getContext()) && callback != null) {
            callback.callback(Callback.CODE_NETWORK_ERROR, null);
            return;
        }
        // 封装处理
        AjaxCallback ajaxCallback = new AjaxCallback() {
            @Override
            public void callback(String url, Object response, AjaxStatus status) {
                Log.d(TAG, "url=" + url + " response=" + response + " status=" + status + " cookie=" + valid_val + "->" + jsession_id);

                if (status.getCookies() != null) {
                    List<Cookie> cookies = status.getCookies();
                    for (Cookie c : cookies) {
                        if (c.getName().equals(VALID_KEY_COOKIE)) {
                            valid_val = c.getValue();
                        }
                        if (c.getName().equals(VALID_KEY_SESSION)) {
                            jsession_id = c.getValue();
                        }
                    }
                }

                if (status.getCode() == AjaxStatus.NETWORK_ERROR && callback != null) {
                    callback.callback(Callback.CODE_TIMEOUT, null);
                    return;
                }
                if (handle != null) {
                    handle.handle(response);
                }
            }
        };
        // 设置超时时间
        ajaxCallback.timeout(AppConfig.NETWORK_TIMEOUT_LIMIT);
        // 设置权限标识
        if (valid_val != null) {
            ajaxCallback.cookie(VALID_KEY_COOKIE, valid_val);
        }
        if (jsession_id != null) {
            ajaxCallback.cookie(VALID_KEY_SESSION, jsession_id);
        }
        if (mProgressBar != null) {
            mAq.progress(mProgressBar);
        }
        // 发送请求
        mAq.ajax(AppConfig.HOST + url, params, type, ajaxCallback);
    }

    @Override
    public void login(String username, String password, final Callback<LoginResp> callback) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", username);
        password = password + "MOD";
        params.put("password", MD5Utils.MD5(password));
        doService("/user/signin", params, JSONObject.class, new ResponseHandler<JSONObject>() {
            @Override
            public void handle(JSONObject json) {
                LoginResp resp = null;
                try {
                    BaseParse parse = new BaseParse();
                    resp = (LoginResp) parse.parse(json.toString(), LoginResp.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                callback.callback(Callback.CODE_SUCCESS, resp);
            }
        }, callback);
    }

    @Override
    public void logout() {
        doService("/logout", null, null, null, null);
        valid_val = null;
    }

    @Override
    public void getInfo(final Callback<UserInfo> callback) {
        doService("/getInfo", null, JSONObject.class, new ResponseHandler<JSONObject>() {
            @Override
            public void handle(JSONObject json) {
                UserInfo info = new UserInfo();
                try {
                    info.username = json.getString("username");
                    info.department = json.getString("department");
                    info.position = json.getString("position");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                callback.callback(Callback.CODE_SUCCESS, info);
            }
        }, callback);
    }

    @Override
    public void modifyPassword(String oldPassword, String newPassword, final Callback<BaseResp> callback) {
        Map<String, Object> params = new HashMap<String, Object>();
        JsonObject object = new JsonObject();
        object.add("session", getSessonJson());
        String token = "MOD";
        oldPassword = oldPassword+token;
        newPassword = newPassword+token;
        object.addProperty("oldPassword", MD5Utils.MD5(oldPassword));
        object.addProperty("newPassword", MD5Utils.MD5(newPassword));
        params.put("json", object.toString());
        doService("/user/modifyPassword", params, JSONObject.class, new ResponseHandler<JSONObject>() {
            @Override
            public void handle(JSONObject json) {
                BaseResp resp = new BaseParse<>().parse(json.toString(),BaseResp.class);
                callback.callback(resp.getStatus(), resp);
            }
        }, callback);
    }

    @Override
    public void listFloors(final Callback<FloorsResp> callback) {
        Map map = new HashMap<>();
        map.put("json", getSessionString());
        doService("/org/getFloor", map, JSONObject.class, new ResponseHandler<JSONObject>() {
            @Override
            public void handle(JSONObject json) {
                List<FloorInfo> infos = new ArrayList<FloorInfo>();
                FloorsResp resp = null;
                try {
                    resp = (FloorsResp) new BaseParse<>().parse(json.toString(), FloorsResp.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                callback.callback(resp.getStatus(), resp);
            }
        }, callback);
    }

    @Override
    public void getFloorImg(String floorId, final Callback<Bitmap> callback) {
        Map<String, Object> params = new HashMap<String, Object>();
        JsonObject object = new JsonObject();
        object.add("session", getSessonJson());
        object.addProperty("level", 3);
        object.addProperty("org_id", floorId);
//        params.put("level",3);//3:楼层  4：房间
        params.put("json", object.toString());
//        params.put("org_id",floorId);
        doService("/org/downloadMap", params, Bitmap.class, new ResponseHandler<Bitmap>() {
            @Override
            public void handle(Bitmap bitmap) {
                callback.callback(Callback.CODE_SUCCESS, bitmap);
            }
        }, callback);
    }

    @Override
    public void getFloorDivide(String floorId, final Callback<FloorDivideInfo> callback) {
        Map<String, Object> params = new HashMap<String, Object>();
        JsonObject object = new JsonObject();
        object.add("session", getSessonJson());
        object.addProperty("floor_id", floorId);
        params.put("json", object.toString());
        doService("/org/getRoomsInFloor", params, JSONObject.class, new ResponseHandler<JSONObject>() {
            @Override
            public void handle(JSONObject json) {
                FloorDivideInfo floorDivideInfo = new FloorDivideInfo();
                try {
                    floorDivideInfo.row = json.getInt("row");
                    floorDivideInfo.col = json.getInt("col");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                callback.callback(Callback.CODE_SUCCESS, floorDivideInfo);
            }
        }, callback);
    }

    @Override
    public void getRoomImg(String roomId, final Callback<Bitmap> callback) {
        Map<String, Object> params = new HashMap<String, Object>();
        JsonObject object = new JsonObject();
        object.add("session", getSessonJson());
        object.addProperty("level", 4);
        object.addProperty("org_id", roomId);
        params.put("json", object.toString());
        doService("/org/downloadMap", params, Bitmap.class, new ResponseHandler<Bitmap>() {
            @Override
            public void handle(Bitmap bitmap) {
                callback.callback(Callback.CODE_SUCCESS, bitmap);
            }
        }, callback);
    }

    @Override
    public void getDefaultSceneName(String roomId, final Callback<List<SceneInfo>> callback) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("roomId", roomId);
        doService("/getDefaultSceneName", params, JSONObject.class, new ResponseHandler<JSONObject>() {
            @Override
            public void handle(JSONObject json) {
                List<SceneInfo> infos = new ArrayList<SceneInfo>();
                try {
                    JSONArray names = json.getJSONArray("names");
                    for (int i = 0; i < names.length(); i++) {
                        JSONObject name = names.getJSONObject(i);
                        SceneInfo info = new SceneInfo();
                        info.id = name.getString("id");
                        info.name = name.getString("name");
                        infos.add(info);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                callback.callback(Callback.CODE_SUCCESS, infos);
            }
        }, callback);
    }

    @Override
    public void listDiyScene(String roomId, final Callback<List<SceneInfo>> callback) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("roomId", roomId);
        doService("/listDiyScene", params, JSONObject.class, new ResponseHandler<JSONObject>() {
            @Override
            public void handle(JSONObject json) {
                List<SceneInfo> infos = new ArrayList<SceneInfo>();
                try {
                    JSONArray names = json.getJSONArray("scenes");
                    for (int i = 0; i < names.length(); i++) {
                        JSONObject name = names.getJSONObject(i);
                        SceneInfo info = new SceneInfo();
                        info.id = name.getString("sceneId");
                        info.name = name.getString("name");
                        infos.add(info);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                callback.callback(Callback.CODE_SUCCESS, infos);
            }
        }, callback);
    }

    @Override
    public void getLampsInRoom(String roomId, final Callback<LampsResp> callback) {
        Map<String, Object> params = new HashMap<String, Object>();
        JsonObject object = new JsonObject();
        object.add("session", getSessonJson());
        object.addProperty("room_id", roomId);
        params.put("json", object.toString());
        doService("/org/getLampsInRoom", params, JSONObject.class, new ResponseHandler<JSONObject>() {
            @Override
            public void handle(JSONObject json) {
                LampsResp resp = null;
                try {
                    resp = new BaseParse<LampsResp>().parse(json.toString(), LampsResp.class);
                    callback.callback(Callback.CODE_SUCCESS, resp);
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.callback(Callback.CODE_FAILURE, null);
                }

            }
        }, callback);
    }

    @Override
    public void getLampStatus(String lamp_id, final Callback<LampStatusResp> callback) {
//        Map<String, Object> params = new HashMap<String, Object>();
//        JsonObject object = new JsonObject();
//        object.add("session", getSessonJson());
//        object.addProperty("lamp_id", lamp_id);
//        params.put("json", object.toString());
//        doService("/getLampStatus", params, JSONObject.class, new ResponseHandler<JSONObject>() {
//            @Override
//            public void handle(JSONObject json) {
//                LampStatusResp resp = null;
//                try {
//                    resp = new BaseParse<LampStatusResp>().parse(json.toString(), LampStatusResp.class);
//                    callback.callback(Callback.CODE_SUCCESS, resp);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    callback.callback(Callback.CODE_FAILURE, null);
//                }
//
//            }
//        }, callback);
    }

    @Override
    public void getLampsInLampGroup(String lampGroup_id, final Callback<LampsResp> callback) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("lampGroup_id", lampGroup_id);
        params.put("json", getSessionString());
        doService("/org/getLampsInRoom", params, JSONObject.class, new ResponseHandler<JSONObject>() {
            @Override
            public void handle(JSONObject json) {
                LampsResp resp = null;
                try {
                    resp = new BaseParse<LampsResp>().parse(json.toString(), LampsResp.class);
                    callback.callback(Callback.CODE_SUCCESS, resp);
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.callback(Callback.CODE_FAILURE, null);
                }

            }
        }, callback);
    }

    @Override
    public void getModes(String room_id, final Callback<GetModesResp> callback) {
        Map<String, Object> params = new HashMap<String, Object>();
        JsonObject object = new JsonObject();
        object.add("session", getSessonJson());
        object.addProperty("room_id", room_id);
        params.put("json", object.toString());
        doService("/getModes", params, JSONObject.class, new ResponseHandler<JSONObject>() {
            @Override
            public void handle(JSONObject json) {
                try {
                    GetModesResp resp = new BaseParse<GetModesResp>().parse(json.toString(), GetModesResp.class);
                    callback.callback(resp.getStatus(), resp);
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.callback(Callback.CODE_FAILURE, null);
                }

            }
        }, callback);
    }

    @Override
    public void createNormalMode(String roomId, String mode_id, String modeName, List<String> lampId,List<LampParam> lampParams, final Callback<BaseResp> callback) {
        Map<String, Object> params = new HashMap<String, Object>();
        JsonObject object = new JsonObject();
        object.add("session", getSessonJson());
        object.addProperty("room_id", roomId);
        object.addProperty("mode_id", mode_id);
        object.addProperty("mode_name", modeName);
        JsonArray array = new JsonArray();
        for (LampParam p : lampParams) {
            JsonObject obj = new JsonObject();
            obj.addProperty("lamp_id", p.getLamp_id());
            obj.addProperty("lamp_rgb", p.getLamp_rgb());
            obj.addProperty("lamp_brightness", p.getLamp_brightness());
            obj.addProperty("lamp_colorTemp", p.getLamp_colorTemp());
            obj.addProperty("lamp_h_degree", p.getLamp_h_degree());
            obj.addProperty("lamp_v_degree", p.getLamp_v_degree());
            obj.addProperty("lamp_l_degree", p.getLamp_l_degree());
            array.add(obj);
        }
        object.add("normal_modes", array);
        params.put("json", object.toString());
        doService("/saveNormalMode", params, JSONObject.class, new ResponseHandler<JSONObject>() {
            @Override
            public void handle(JSONObject response) {
                BaseResp resp = new BaseParse<BaseResp>().parse(response.toString(), BaseResp.class);
                callback.callback(resp.getStatus(), resp);
            }
        }, callback);
    }

    @Override
    public void createDynamicMode(String roomId,String mode_id, String modeName, List<Frame> frames,String lamp_rgb,
                                  int lamp_brightness, int lamp_colorTemp, int lamp_h_degree, int lamp_v_degree, int lamp_l_degree, final Callback<BaseResp> callback) {
        Map<String, Object> params = new HashMap<String, Object>();
        JsonObject object = new JsonObject();
        object.add("session", getSessonJson());
        object.addProperty("room_id", roomId);
        object.addProperty("mode_id", mode_id);
        object.addProperty("mode_name", modeName);
        JsonArray array = new JsonArray();
        for (int i = 0; i < frames.size(); i++) {
            JsonObject obj = new JsonObject();
            obj.addProperty("frame_id", frames.get(i).getFrame_id());
//            obj.addProperty("lamp_id", frames.get(i).getLamp_id());
            obj.addProperty("is_smooth", 1);
            obj.addProperty("hour", frames.get(i).getHour());
            obj.addProperty("minute", frames.get(i).getMinute());
            obj.addProperty("second", frames.get(i).getSecond());
            obj.addProperty("lamp_rgb", lamp_rgb);
            obj.addProperty("lamp_brightness", lamp_brightness);
            obj.addProperty("lamp_colorTemp", lamp_colorTemp);
            obj.addProperty("lamp_h_degree", lamp_h_degree);
            obj.addProperty("lamp_v_degree", lamp_v_degree);
            obj.addProperty("lamp_l_degree", lamp_l_degree);
            array.add(obj);
        }
        object.add("dynamic_modes", array);
        params.put("json", object.toString());
        doService("/saveDynamicMode", params, JSONObject.class, new ResponseHandler<JSONObject>() {
            @Override
            public void handle(JSONObject response) {
                BaseResp resp = new BaseParse<BaseResp>().parse(response.toString(), BaseResp.class);
                callback.callback(resp.getStatus(), resp);
            }
        }, callback);
    }

    @Override
    public void saveDynamicMode(String roomId, String mode_id, String mode_name, List<Frame> frames, final Callback<BaseResp> callback) {
        Map<String, Object> params = new HashMap<>();
        JsonObject object = new JsonObject();
        object.add("session", getSessonJson());
        object.addProperty("room_id", roomId);
        object.addProperty("mode_id", mode_id);
        object.addProperty("mode_name", mode_name);
        JsonArray array = new JsonArray();
        for (int i = 0; i < frames.size(); i++) {
            Frame frame = frames.get(i);
            List<LampParam> lampParams = frame.getLamps();
            if(lampParams!=null){
                for (LampParam param:lampParams){
                    JsonObject obj = new JsonObject();
                    obj.addProperty("frame_id", frame.getFrame_id());
                    obj.addProperty("lamp_id", param.getLamp_id());
                    obj.addProperty("is_smooth", frame.getIs_smooth());
                    obj.addProperty("hour", frame.getHour());
                    obj.addProperty("minute", frame.getMinute());
                    obj.addProperty("second", frame.getSecond());
                    obj.addProperty("lamp_rgb", param.getLamp_rgb());
                    obj.addProperty("lamp_brightness", param.getLamp_brightness());
                    obj.addProperty("lamp_colorTemp", param.getLamp_colorTemp());
                    obj.addProperty("lamp_h_degree", param.getLamp_h_degree());
                    obj.addProperty("lamp_v_degree", param.getLamp_v_degree());
                    obj.addProperty("lamp_l_degree", param.getLamp_l_degree());
                    array.add(obj);
                }
            }

        }
        object.add("dynamic_modes", array);
        params.put("json", object.toString());
        doService("/saveDynamicMode", params, JSONObject.class, new ResponseHandler<JSONObject>() {
            @Override
            public void handle(JSONObject response) {
                BaseResp resp = new BaseParse<BaseResp>().parse(response.toString(), BaseResp.class);
                callback.callback(resp.getStatus(), resp);
            }
        }, callback);
    }

    @Override
    public void deleteFrame(String frame_id, String mode_id,final Callback<BaseResp> callback) {
        JsonObject object = new JsonObject();
        object.add("session", getSessonJson());
        object.addProperty("frame_id", frame_id);
        object.addProperty("mode_id", mode_id);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("json", object.toString());
        doService("/deleteFrame", params, JSONObject.class, new ResponseHandler<JSONObject>() {
            @Override
            public void handle(JSONObject response) {
                BaseResp resp = new BaseParse<BaseResp>().parse(response.toString(), BaseResp.class);
                callback.callback(resp.getStatus(), resp);
            }
        }, callback);
    }

    @Override
    public void playDynamicMode(String room_id, String mode_id, String[] lamp_ids,final Callback<BaseResp> callback) {
        JsonObject object = new JsonObject();
        object.add("session", getSessonJson());
        object.addProperty("room_id", room_id);
        object.addProperty("mode_id", mode_id);
        String lampIds = "";
        if(lamp_ids!=null&&lamp_ids.length>0){
            for (String id:lamp_ids){
                lampIds = ","+id;
            }
        }
        lampIds = lampIds.substring(1);
        object.addProperty("lamp_ids", lampIds);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("json", object.toString());
        doService("/playDynamicMode", params, JSONObject.class, new ResponseHandler<JSONObject>() {
            @Override
            public void handle(JSONObject response) {
                BaseResp resp = new BaseParse<BaseResp>().parse(response.toString(), BaseResp.class);
                callback.callback(resp.getStatus(), resp);
            }
        }, callback);
    }

    @Override
    public void stopDynamicMode(String room_id, String mode_id,final Callback<BaseResp> callback) {
        JsonObject object = new JsonObject();
        object.add("session", getSessonJson());
        object.addProperty("room_id", room_id);
        object.addProperty("mode_id", mode_id);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("json", object.toString());
        doService("/stopDynamicMode", params, JSONObject.class, new ResponseHandler<JSONObject>() {
            @Override
            public void handle(JSONObject response) {
                BaseResp resp = new BaseParse<BaseResp>().parse(response.toString(), BaseResp.class);
                callback.callback(resp.getStatus(), resp);
            }
        }, callback);
    }

    @Override
    public void applyNormalMode(String room_id, String mode_id, String lamp_ids,final Callback<BaseResp> callback) {
        JsonObject object = new JsonObject();
        object.add("session", getSessonJson());
        object.addProperty("room_id", room_id);
        object.addProperty("mode_id", mode_id);
        object.addProperty("lamp_ids", lamp_ids);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("json", object.toString());
        doService("/applyNormalMode", params, JSONObject.class, new ResponseHandler<JSONObject>() {
            @Override
            public void handle(JSONObject response) {
                BaseResp resp = new BaseParse<BaseResp>().parse(response.toString(), BaseResp.class);
                callback.callback(resp.getStatus(), resp);
            }
        }, callback);
    }

    @Override
    public void listLeds(String groupId, final Callback<List<LedInfo>> callback) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("groupId", groupId);
        doService("/listLeds", params, JSONObject.class, new ResponseHandler<JSONObject>() {
            @Override
            public void handle(JSONObject json) {
                List<LedInfo> infos = new ArrayList<LedInfo>();
                try {
                    JSONArray names = json.getJSONArray("leds");
                    for (int i = 0; i < names.length(); i++) {
                        JSONObject led = names.getJSONObject(i);
                        LedInfo info = new LedInfo();
                        info.ledId = led.getString("ledId");
                        info.status = led.getInt("status");
                        info.type = led.getInt("type");
                        info.positionX = led.getInt("positionX");
                        info.positionY = led.getInt("positionY");
                        infos.add(info);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                callback.callback(Callback.CODE_SUCCESS, infos);
            }
        }, callback);
    }

    @Override
    public void controlLed(String[] ledIds, int brightness, int colorTemp) {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        for (int i = 0, len = ledIds.length; i < len; i++) {
            pairs.add(new BasicNameValuePair("ledIds", ledIds[i]));
        }
        pairs.add(new BasicNameValuePair("brightness", brightness + ""));
        pairs.add(new BasicNameValuePair("colorTemp", colorTemp + ""));
        Map<String, Object> params = new HashMap<String, Object>();
        try {
            params.put(AQuery.POST_ENTITY, new UrlEncodedFormEntity(pairs, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        doService("/controlLed", params, null, null, null);
    }

    @Override
    public void deleteDiyScene(String room_id, String mode_id, final Callback<BaseResp> callback) {
        JsonObject object = new JsonObject();
        object.add("session", getSessonJson());
        object.addProperty("room_id", room_id);
        object.addProperty("mode_id", mode_id);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("json", object.toString());
        doService("/deleteMode", params, JSONObject.class, new ResponseHandler<JSONObject>() {
            @Override
            public void handle(JSONObject response) {
                BaseResp resp = new BaseParse<BaseResp>().parse(response.toString(), BaseResp.class);
                callback.callback(resp.getStatus(), resp);
            }
        }, callback);
    }

    @Override
    public void applyDefaultScene(String sceneId, String floorId, String[] roomIds, String[] groupIds, String[] ledIds, final Callback<BaseResponse> callback) {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("sceneId", sceneId));
        if (floorId != null) {
            pairs.add(new BasicNameValuePair("floorId", floorId));
        } else if (roomIds != null) {
            for (String roomId : roomIds) {
                pairs.add(new BasicNameValuePair("roomIds", roomId));
            }
        } else if (groupIds != null) {
            for (String groupId : groupIds) {
                pairs.add(new BasicNameValuePair("groupIds", groupId));
            }
        } else if (ledIds != null) {
            for (String ledId : ledIds) {
                pairs.add(new BasicNameValuePair("ledIds", ledId));
            }
        }
        Map<String, Object> params = new HashMap<String, Object>();
        try {
            params.put(AQuery.POST_ENTITY, new UrlEncodedFormEntity(pairs, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        doService("/applyDefaultScene", params, JSONObject.class, new ResponseHandler<JSONObject>() {
            @Override
            public void handle(JSONObject json) {
                BaseResponse response = new BaseResponse();
                try {
                    response.success = json.getBoolean("success");
                    response.msg = json.getString("msg");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                callback.callback(Callback.CODE_SUCCESS, response);
            }
        }, callback);
    }

    @Override
    public void applyDiyScene(String sceneId, String roomId, String[] groupIds, String[] ledIds, final Callback<BaseResponse> callback) {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("sceneId", sceneId));
        if (roomId != null) {
            pairs.add(new BasicNameValuePair("roomId", roomId));
        } else if (groupIds != null) {
            for (String groupId : groupIds) {
                pairs.add(new BasicNameValuePair("groupIds", groupId));
            }
        } else if (ledIds != null) {
            for (String ledId : ledIds) {
                pairs.add(new BasicNameValuePair("ledIds", ledId));
            }
        }
        Map<String, Object> params = new HashMap<String, Object>();
        try {
            params.put(AQuery.POST_ENTITY, new UrlEncodedFormEntity(pairs, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        doService("/applyDiyScene", params, JSONObject.class, new ResponseHandler<JSONObject>() {
            @Override
            public void handle(JSONObject json) {
                BaseResponse response = new BaseResponse();
                try {
                    response.success = json.getBoolean("success");
                    response.msg = json.getString("msg");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                callback.callback(Callback.CODE_SUCCESS, response);
            }
        }, callback);
    }

    @Override
    public void getUserImg(String username, final Callback<Bitmap> callback) {
        Map<String, Object> params = new HashMap<String, Object>();
        if (username != null) {
            params.put("username", username);
        }
        doService("/getUserImg", params, Bitmap.class, new ResponseHandler<Bitmap>() {
            @Override
            public void handle(Bitmap bitmap) {
                callback.callback(Callback.CODE_SUCCESS, bitmap);
            }
        }, callback);
    }

    @Override
    public void uploadUserImg(File userImg, final Callback<BaseResponse> callback) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("upload", userImg);
        doService("/uploadUserImg", params, JSONObject.class, new ResponseHandler<JSONObject>() {
            @Override
            public void handle(JSONObject json) {
                BaseResponse response = new BaseResponse();
                try {
                    response.success = json.getBoolean("success");
                    response.msg = json.getString("msg");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                callback.callback(Callback.CODE_SUCCESS, response);
            }
        }, callback);
    }

    @Override
    public void getClause(final Callback<String> callback) {
        doService("/getClause", null, JSONObject.class, new ResponseHandler<JSONObject>() {
            @Override
            public void handle(JSONObject json) {
                String clause = null;
                try {
                    clause = json.getString("clause");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                callback.callback(Callback.CODE_SUCCESS, clause);
            }
        }, callback);
    }

    @Override
    public void submitFeedback(String content, final Callback<BaseResponse> callback) {
        doService("/submitFeedback?content=" + content, null, JSONObject.class, new ResponseHandler<JSONObject>() {
            @Override
            public void handle(JSONObject json) {
                BaseResponse response = new BaseResponse();
                try {
                    response.success = json.getBoolean("success");
                    response.msg = json.getString("msg");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                callback.callback(Callback.CODE_SUCCESS, response);
            }
        }, callback);
    }

    @Override
    public void addClock(String room_id,String clock_name,String mode_id, String weekday,String hour, String minute,final Callback<BaseResp> callback) {
        Map<String, Object> params = new HashMap<String, Object>();
        JsonObject object = new JsonObject();
        object.add("session",getSessonJson());
        object.addProperty("room_id", room_id);
        object.addProperty("clock_name", clock_name);
        object.addProperty("mode_id", mode_id);
        object.addProperty("weekday", weekday);
        object.addProperty("hour", hour);
        object.addProperty("minute", minute);
        params.put("json", object.toString());
        doService("/addClock", params, JSONObject.class, new ResponseHandler<JSONObject>() {
            @Override
            public void handle(JSONObject json) {
                BaseResp resp = new BaseParse<BaseResp>().parse(json.toString(),BaseResp.class);
                callback.callback(resp.getStatus(), resp);
            }
        }, callback);
    }

    @Override
    public void updateClock(String mode_id,String clockId, String week,String hour, String minute, final Callback<BaseResp> callback) {
        Map<String, Object> params = new HashMap<String, Object>();
        JsonObject object = new JsonObject();
        object.add("session",getSessonJson());
        object.addProperty("mode_id", clockId);
        object.addProperty("clock_id", clockId);
        object.addProperty("weekday", clockId);
        object.addProperty("hour", clockId);
        object.addProperty("minute", clockId);
        params.put("json", object.toString());
        doService("/modifyClock", params, JSONObject.class, new ResponseHandler<JSONObject>() {
            @Override
            public void handle(JSONObject json) {
                BaseResp resp = new BaseParse<BaseResp>().parse(json.toString(),BaseResp.class);
                callback.callback(resp.getStatus(), resp);
            }
        }, callback);
    }

    @Override
    public void openClock(String roomId, String clockId, final Callback<BaseResp> callback) {
        Map<String, Object> params = new HashMap<String, Object>();
        JsonObject object = new JsonObject();
        object.add("session",getSessonJson());
        object.addProperty("room_id", roomId);
        object.addProperty("clock_id", clockId);
        params.put("json", object.toString());
        doService("/startClock", params, JSONObject.class, new ResponseHandler<JSONObject>() {
            @Override
            public void handle(JSONObject json) {
                BaseResp resp = new BaseParse<BaseResp>().parse(json.toString(),BaseResp.class);
                callback.callback(resp.getStatus(), resp);
            }
        }, callback);
    }

    @Override
    public void closeClock(String roomId, String clockId, final Callback<BaseResp> callback) {
        Map<String, Object> params = new HashMap<String, Object>();
        JsonObject object = new JsonObject();
        object.add("session",getSessonJson());
        object.addProperty("room_id", roomId);
        object.addProperty("clock_id", clockId);
        params.put("json", object.toString());
        doService("/stopClock", params, JSONObject.class, new ResponseHandler<JSONObject>() {
            @Override
            public void handle(JSONObject json) {
                BaseResp resp = new BaseParse<BaseResp>().parse(json.toString(),BaseResp.class);
                callback.callback(resp.getStatus(), resp);
            }
        }, callback);
    }

    @Override
    public void listRooms(String floorId, final Callback<List<RoomEntity>> callback) {
        Map<String, Object> params = new HashMap<String, Object>();
        JsonObject object = new JsonObject();
        object.add("session", getSessonJson());
        object.addProperty("floor_id", floorId);
        params.put("json", object.toString());
        doService("/org/getRoomsInFloor", params, JSONObject.class, new ResponseHandler<JSONObject>() {
            @Override
            public void handle(JSONObject json) {
                List<RoomEntity> roomInfos = new ArrayList<RoomEntity>();
                RoomsResp resp = null;
                try {
                    resp = (RoomsResp) new BaseParse<>().parse(json.toString(), RoomsResp.class);
                    roomInfos = resp.getRooms();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                callback.callback(Callback.CODE_SUCCESS, roomInfos);
            }
        }, callback);
    }

    @Override
    public void getRoomClocks(String roomId, final Callback<ClocksResp> callback) {
        Map<String, Object> params = new HashMap<String, Object>();
        JsonObject object = new JsonObject();
        object.add("session",getSessonJson());
        object.addProperty("room_id", roomId);
        params.put("json", object.toString());
        doService("/getClock", params, JSONObject.class, new ResponseHandler<JSONObject>() {
            @Override
            public void handle(JSONObject json) {
                try {
                    ClocksResp resp = new BaseParse<ClocksResp>().parse(json.toString(), ClocksResp.class);
                    callback.callback(resp.getStatus(), resp);
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.callback(Callback.CODE_FAILURE, null);
                }
            }
        }, callback);
    }

    @Override
    public void listGroups(String roomId, final Callback<List<GroupInfo>> callback) {
        Map<String, Object> params = new HashMap<String, Object>();
        JsonObject object = new JsonObject();
        object.add("session", getSessonJson());
        object.addProperty("room_id", roomId);
//        params.put("level",3);//3:楼层  4：房间
        params.put("json", object.toString());
        doService("/org/getLampsInRoom", params, JSONObject.class, new ResponseHandler<JSONObject>() {
            @Override
            public void handle(JSONObject json) {
                List<GroupInfo> groupInfos = new ArrayList<GroupInfo>();
                try {
                    JSONArray groups = json.getJSONArray("groups");
                    for (int i = 0; i < groups.length(); i++) {
                        JSONObject group = groups.getJSONObject(i);
                        GroupInfo groupInfo = new GroupInfo();
                        groupInfo.id = group.getString("groupId");
                        groupInfo.name = group.getString("name");
                        groupInfo.roomName = group.getString("roomName");
                        groupInfo.hasClock = group.getBoolean("hasClock");
                        groupInfos.add(groupInfo);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                callback.callback(Callback.CODE_SUCCESS, groupInfos);
            }
        }, callback);
    }

    @Override
    public void getGroupClocks(String groupId, final Callback<List<ClockInfo>> callback) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("groupId", groupId);
        doService("/getGroupClocks", params, JSONObject.class, new ResponseHandler<JSONObject>() {
            @Override
            public void handle(JSONObject json) {
                List<ClockInfo> clockInfos = new ArrayList<ClockInfo>();
                try {
                    JSONArray clocks = json.getJSONArray("clocks");
                    for (int i = 0; i < clocks.length(); i++) {
                        JSONObject clock = clocks.getJSONObject(i);
                        ClockInfo clockInfo = new ClockInfo();
                        clockInfo.clockId = clock.getString("clockId");
                        clockInfo.sceneType = clock.getInt("sceneType");
                        clockInfo.sceneId = clock.getString("sceneId");
                        clockInfo.sceneName = clock.getString("sceneName");
                        clockInfo.week = clock.getString("week");
                        clockInfo.time = clock.getString("time");
                        clockInfo.status = clock.getBoolean("status");
                        clockInfos.add(clockInfo);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                callback.callback(Callback.CODE_SUCCESS, clockInfos);
            }
        }, callback);
    }

    @Override
    public void deleteClock(String roomId, String clockId, final Callback<BaseResp> callback) {
        Map<String, Object> params = new HashMap<String, Object>();
        JsonObject object = new JsonObject();
        object.add("session", getSessonJson());
        object.addProperty("room_id", roomId);
        object.addProperty("clock_id", clockId);
        params.put("json", object.toString());
        doService("/deleteClock", params, JSONObject.class, new ResponseHandler<JSONObject>() {
            @Override
            public void handle(JSONObject json) {
                BaseResp resp = new BaseParse<BaseResp>().parse(json.toString(),BaseResp.class);
                callback.callback(resp.getStatus(), resp);
            }
        }, callback);
    }

    @Override
    public void getApkVersion(final Callback<ApkVersionInfo> callback) {
        doService("/getApkVersion", null, JSONObject.class, new ResponseHandler<JSONObject>() {
            @Override
            public void handle(JSONObject json) {
                ApkVersionInfo info = new ApkVersionInfo();
                try {
                    info.version = json.getString("version");
                    info.isForce = json.getBoolean("isForce");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                callback.callback(Callback.CODE_SUCCESS, info);
            }
        }, callback);
    }

    @Override
    public void getApkFile(final Callback<File> callback) {
        doService("/getApkFile", null, File.class, new ResponseHandler<File>() {
            @Override
            public void handle(File file) {
                callback.callback(Callback.CODE_SUCCESS, file);
            }
        }, callback);
    }

    @Override
    public void setRGB(String room_id,String[] ledIds, int red, int green, int blue, final Callback<BaseResp> callback) {
        String ids = "";
        for (String id:ledIds) {
            ids = ids+","+id;
        }
        ids = ids.substring(1);
        String rgb = Integer.toHexString(red)+Integer.toHexString(green)+Integer.toHexString(blue);

        Map<String, Object> params = new HashMap<String, Object>();
        JsonObject object = new JsonObject();
        object.add("session", getSessonJson());
        object.addProperty("lamp_ids", ids);
        object.addProperty("lamp_RGB", rgb);
        params.put("json", object.toString());
        doService("/setLampRGB", params, JSONObject.class, new ResponseHandler<JSONObject>() {
            @Override
            public void handle(JSONObject json) {
                BaseResp resp = new BaseParse<BaseResp>().parse(json.toString(),BaseResp.class);
                callback.callback(resp.getStatus(),resp);
            }
        }, callback);
    }

    @Override
    public void setBrightness(String room_id,String[] ledIds, int brightness, final Callback<BaseResp> callback) {
        String ids = "";
        for (String id:ledIds) {
            ids = ids+","+id;
        }
        ids = ids.substring(1);
        Map<String, Object> params = new HashMap<String, Object>();
        JsonObject object = new JsonObject();
        object.add("session", getSessonJson());
        object.addProperty("lamp_ids", ids);
        object.addProperty("lamp_brightness", brightness);
        params.put("json", object.toString());
        doService("/setLampBrightness", params, JSONObject.class, new ResponseHandler<JSONObject>() {
            @Override
            public void handle(JSONObject json) {
                BaseResp resp = new BaseParse<BaseResp>().parse(json.toString(),BaseResp.class);
                callback.callback(resp.getStatus(),resp);
            }
        }, callback);
    }

    @Override
    public void setColorTemp(String room_id,String[] ledIds, int colorTemp, final Callback<BaseResp> callback) {
        String ids = "";
        for (String id:ledIds) {
            ids = ids+","+id;
        }
        ids = ids.substring(1);
        Map<String, Object> params = new HashMap<String, Object>();
        JsonObject object = new JsonObject();
        object.add("session", getSessonJson());
        object.addProperty("lamp_ids", ids);
        object.addProperty("lamp_colorTemp", colorTemp);
        params.put("json", object.toString());
        doService("/setLampColorTemp", params, JSONObject.class, new ResponseHandler<JSONObject>() {
            @Override
            public void handle(JSONObject json) {
                BaseResp resp = new BaseParse<BaseResp>().parse(json.toString(),BaseResp.class);
                callback.callback(resp.getStatus(),resp);
            }
        }, callback);
    }

    @Override
    public void setLampHDegree(String room_id,String[] ledIds, int lamp_h_degree, final Callback<BaseResp> callback) {
        String ids = "";
        for (String id:ledIds) {
            ids = ids+","+id;
        }
        ids = ids.substring(1);
        Map<String, Object> params = new HashMap<String, Object>();
        JsonObject object = new JsonObject();
        object.add("session", getSessonJson());
        object.addProperty("lamp_ids", ids);
        object.addProperty("lamp_h_degree", lamp_h_degree);
        params.put("json", object.toString());
        doService("/setLampHDegree", params, JSONObject.class, new ResponseHandler<JSONObject>() {
            @Override
            public void handle(JSONObject json) {
                BaseResp resp = new BaseParse<BaseResp>().parse(json.toString(),BaseResp.class);
                callback.callback(resp.getStatus(),resp);
            }
        }, callback);
    }

    @Override
    public void setLampVDegree(String room_id,String[] ledIds, int lamp_v_degree, final Callback<BaseResp> callback) {
        String ids = "";
        for (String id:ledIds) {
            ids = ids+","+id;
        }
        ids = ids.substring(1);
        Map<String, Object> params = new HashMap<String, Object>();
        JsonObject object = new JsonObject();
        object.add("session", getSessonJson());
        object.addProperty("lamp_ids", ids);
        object.addProperty("lamp_v_degree", lamp_v_degree);
        params.put("json", object.toString());
        doService("/setLampVDegree", params, JSONObject.class, new ResponseHandler<JSONObject>() {
            @Override
            public void handle(JSONObject json) {
                BaseResp resp = new BaseParse<BaseResp>().parse(json.toString(),BaseResp.class);
                callback.callback(resp.getStatus(),resp);
            }
        }, callback);
    }

    @Override
    public void setLampLDegree(String room_id,String[] ledIds, int lamp_l_degree, final Callback<BaseResp> callback) {
        String ids = "";
        for (String id:ledIds) {
            ids = ids+","+id;
        }
        ids = ids.substring(1);
        Map<String, Object> params = new HashMap<String, Object>();
        JsonObject object = new JsonObject();
        object.add("session", getSessonJson());
        object.addProperty("lamp_ids", ids);
        object.addProperty("lamp_l_degree", lamp_l_degree);
        params.put("json", object.toString());
        doService("/setLampLDegree", params, JSONObject.class, new ResponseHandler<JSONObject>() {
            @Override
            public void handle(JSONObject json) {
                BaseResp resp = new BaseParse<BaseResp>().parse(json.toString(),BaseResp.class);
                callback.callback(resp.getStatus(),resp);
            }
        }, callback);
    }

    @Override
    public void setLampMovement(String room_id,String[] ledIds, int lamp_movement, final Callback<BaseResp> callback) {
        String ids = "";
        for (String id:ledIds) {
            ids = ids+","+id;
        }
        ids = ids.substring(1);
        Map<String, Object> params = new HashMap<String, Object>();
        JsonObject object = new JsonObject();
        object.add("session", getSessonJson());
        object.addProperty("lamp_ids", ids);
        object.addProperty("lamp_movement", lamp_movement);
        params.put("json", object.toString());
        doService("/setLampMovement", params, JSONObject.class, new ResponseHandler<JSONObject>() {
            @Override
            public void handle(JSONObject json) {
                BaseResp resp = new BaseParse<BaseResp>().parse(json.toString(),BaseResp.class);
                callback.callback(resp.getStatus(),resp);
            }
        }, callback);
    }

    @Override
    public void getDiySceneInfo(String sceneId, final Callback<DiySceneInfo> callback) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("sceneId", sceneId);
        doService("/getDiySceneInfo", params, JSONObject.class, new ResponseHandler<JSONObject>() {
            @Override
            public void handle(JSONObject json) {
                DiySceneInfo info = new DiySceneInfo();
                try {
                    info.brightness = json.getInt("brightness");
                    info.colortemp = json.getInt("colorTemp");
                    info.red = json.getInt("rgb_r");
                    info.green = json.getInt("rgb_g");
                    info.blue = json.getInt("rgb_b");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                callback.callback(Callback.CODE_SUCCESS, info);
            }
        }, callback);
    }

    @Override
    public void getDiySceneImg(String sceneId, final Callback<Bitmap> callback) {
        Map<String, Object> params = new HashMap<String, Object>();
        if (sceneId != null) {
            params.put("sceneId", sceneId);
        }
        doService("/getDiySceneImg", params, Bitmap.class, new ResponseHandler<Bitmap>() {
            @Override
            public void handle(Bitmap bitmap) {
                callback.callback(Callback.CODE_SUCCESS, bitmap);
            }
        }, callback);
    }

    @Override
    public void uploadDiySceneImg(String sceneId, File sceneImg, final Callback<BaseResponse> callback) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("sceneId", sceneId);
        params.put("upload", sceneImg);
        doService("/uploadDiySceneImg", params, JSONObject.class, new ResponseHandler<JSONObject>() {
            @Override
            public void handle(JSONObject json) {
                BaseResponse response = new BaseResponse();
                try {
                    response.success = json.getBoolean("success");
                    response.msg = json.getString("msg");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                callback.callback(Callback.CODE_SUCCESS, response);
            }
        }, callback);
    }

    @Override
    public void createDiyScene(String roomId, String name, int brightness, int colorTemp, final int red, int green, int blue, File file, final Callback<BaseResponse> callback) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("roomId", roomId);
//        params.put("name", name);
        params.put("brightness", brightness);
        params.put("colorTemp", colorTemp);
        params.put("rgb_r", red);
        params.put("rgb_g", green);
        params.put("rgb_b", blue);
        params.put("upload", file);
        doService("/createDiyScene?name=" + name, params, JSONObject.class, new ResponseHandler<JSONObject>() {
            @Override
            public void handle(JSONObject json) {
                BaseResponse response = new BaseResponse();
                try {
                    response.success = json.getBoolean("success");
                    response.msg = json.getString("msg");
                    response.extra = json.getString("sceneId");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                callback.callback(Callback.CODE_SUCCESS, response);
            }
        }, callback);
    }

    @Override
    public void modifyDiyScene(String sceneId, String newName, int brightness, int colorTemp, int red, int green, int blue, File file, final Callback<BaseResponse> callback) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("sceneId", sceneId);
//        params.put("name", newName);
        params.put("brightness", brightness);
        params.put("colorTemp", colorTemp);
        params.put("rgb_r", red);
        params.put("rgb_g", green);
        params.put("rgb_b", blue);
        params.put("upload", file);
        doService("/modifyDiyScene?name=" + newName, params, JSONObject.class, new ResponseHandler<JSONObject>() {
            @Override
            public void handle(JSONObject json) {
                BaseResponse response = new BaseResponse();
                try {
                    response.success = json.getBoolean("success");
                    response.msg = json.getString("msg");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                callback.callback(Callback.CODE_SUCCESS, response);
            }
        }, callback);
    }

    @Override
    public void addFaultRecord(String msg_title, String msg_content, String lamp_ids, final Callback<BaseResp> callback) {
        JsonObject object = new JsonObject();
        object.add("session", getSessonJson());
        object.addProperty("msg_title", msg_title);
        object.addProperty("msg_content", msg_content);
        object.addProperty("lamp_ids", lamp_ids);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("json", object.toString());
        doService("/addFaultRecord", params, JSONObject.class, new ResponseHandler<JSONObject>() {
            @Override
            public void handle(JSONObject response) {
                BaseResp resp = new BaseParse<BaseResp>().parse(response.toString(), BaseResp.class);
                callback.callback(resp.getStatus(), resp);
            }
        }, callback);
    }

    @Override
    public void deleteFaultRecord(String msg_title, final Callback<BaseResp> callback) {
        Map<String, Object> params = new HashMap<String, Object>();
        JsonObject object = new JsonObject();
        object.add("session", getSessonJson());
        object.addProperty("msg_title", msg_title);
        params.put("json", object.toString());
        doService("/deleteFaultRecord", params, JSONObject.class, new ResponseHandler<JSONObject>() {
            @Override
            public void handle(JSONObject response) {
                BaseResp resp = new BaseParse<BaseResp>().parse(response.toString(), BaseResp.class);
                callback.callback(resp.getStatus(), resp);
            }
        }, callback);
    }

    @Override
    public void getFaultRecords(final Callback<FaultRecordResp> callback) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("json", getSessionString());
        doService("/getFaultRecord", params, JSONObject.class, new ResponseHandler<JSONObject>() {
            @Override
            public void handle(JSONObject response) {
                FaultRecordResp resp = new BaseParse<FaultRecordResp>().parse(response.toString(), FaultRecordResp.class);
                callback.callback(resp.getStatus(), resp);
            }
        }, callback);
    }

    @Override
    public void getBackupParam(final Callback<GetParamResp> callback) {
        Map<String, Object> params = new HashMap<String, Object>();
        JsonObject object = new JsonObject();
        object.add("session", getSessonJson());
        params.put("json", object.toString());
        doService("/getBackupParam", params, JSONObject.class, new ResponseHandler<JSONObject>() {
            @Override
            public void handle(JSONObject response) {
                GetParamResp resp = new BaseParse<GetParamResp>().parse(response.toString(), GetParamResp.class);
                callback.callback(resp.getStatus(), resp);
            }
        }, callback);
    }

    @Override
    public void addBackupParam(String backupName, String addInfo, int lamp_brightness, int lamp_colorTemp, String lamp_RGB, final Callback<BaseResp> callback) {
        Map<String, Object> params = new HashMap<String, Object>();
        JsonObject object = new JsonObject();
        object.add("session", getSessonJson());
        object.addProperty("backupName", backupName);
        object.addProperty("addInfo", addInfo);
        object.addProperty("lamp_brightness", lamp_brightness);
        object.addProperty("lamp_colorTemp", lamp_colorTemp);
        object.addProperty("lamp_RGB", lamp_RGB);
        params.put("json", object.toString());
        doService("/addBackupParam", params, JSONObject.class, new ResponseHandler<JSONObject>() {
            @Override
            public void handle(JSONObject response) {
                BaseResp resp = new BaseParse<BaseResp>().parse(response.toString(), BaseResp.class);
                callback.callback(resp.getStatus(), resp);
            }
        }, callback);
    }

    @Override
    public void deleteBackupParam(String backupName, final Callback<BaseResp> callback) {
        Map<String, Object> params = new HashMap<String, Object>();
        JsonObject object = new JsonObject();
        object.add("session", getSessonJson());
        object.addProperty("backupName", backupName);
        params.put("json", object.toString());
        doService("/deleteBackupParam", params, JSONObject.class, new ResponseHandler<JSONObject>() {
            @Override
            public void handle(JSONObject response) {
                BaseResp resp = new BaseParse<BaseResp>().parse(response.toString(), BaseResp.class);
                callback.callback(resp.getStatus(), resp);
            }
        }, callback);
    }

    @Override
    public void tieLampGroup(String room_id, String lamps_id, final Callback<TieLampsResp> callback) {
        Map<String, Object> params = new HashMap<String, Object>();
        JsonObject object = new JsonObject();
        object.add("session", getSessonJson());
        object.addProperty("room_id", room_id);
        object.addProperty("lamp_ids", lamps_id);
        params.put("json", object.toString());
        doService("/tieLampGroup", params, JSONObject.class, new ResponseHandler<JSONObject>() {
            @Override
            public void handle(JSONObject response) {
                TieLampsResp resp = new BaseParse<TieLampsResp>().parse(response.toString(), TieLampsResp.class);
                callback.callback(resp.getStatus(), resp);
            }
        }, callback);
    }

    @Override
    public void untieLampGroup(String group_id, final Callback<BaseResp> callback) {
        Map<String, Object> params = new HashMap<String, Object>();
        JsonObject object = new JsonObject();
        object.add("session", getSessonJson());
        object.addProperty("group_id", group_id);
        params.put("json", object.toString());
        doService("/untieLampGroup", params, JSONObject.class, new ResponseHandler<JSONObject>() {
            @Override
            public void handle(JSONObject response) {
                BaseResp resp = new BaseParse<BaseResp>().parse(response.toString(), BaseResp.class);
                callback.callback(resp.getStatus(), resp);
            }
        }, callback);
    }

    @Override
    public void connectTest(final Callback<Boolean> callback) {
        doService("/connectTest", null, JSONObject.class, new ResponseHandler<JSONObject>() {
            @Override
            public void handle(JSONObject json) {
                if (json == null) {
                    callback.callback(Callback.CODE_SUCCESS, false);
                } else {
                    try {
                        callback.callback(Callback.CODE_SUCCESS, json.getInt("msg") == 1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, callback);
    }


    @Override
    public void bindProgressHandler(ProgressBar progressBar) {
        mProgressBar = progressBar;
    }

    @Override
    public void unbindProgressHandler() {
        mProgressBar = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (valid_val != null) {
            outState.putString(VALID_KEY_COOKIE, valid_val);
        }
        if (jsession_id != null) {
            outState.putString(VALID_KEY_SESSION, jsession_id);
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        String cookie_val = savedInstanceState.getString(VALID_KEY_COOKIE);
        if (cookie_val != null) {
            valid_val = cookie_val;
        }
        String session_id = savedInstanceState.getString(VALID_KEY_SESSION);
        if (session_id != null) {
            jsession_id = session_id;
        }
    }


    public String getSessionString() {
//        JsonObject json = new JsonObject();
//        LoginResp.Session session = AccountManager.getInstance().getSession();
//        json.addProperty("session_id", session.getSession_id());
//        json.addProperty("operator_id", session.getOperator_id());
        String jsonString = AccountManager.getInstance().getSessionString();
        JSONObject json = null;
        JSONObject target = null;
        try {
            json = new JSONObject(jsonString);
            target = new JSONObject();
            target.put("session", json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return target.toString();
    }

    private JsonObject getSessonJson() {

        JsonObject json = new JsonObject();
        try {
            String jsonString = AccountManager.getInstance().getSessionString();
            JSONObject object = new JSONObject(jsonString);
            json.addProperty("session_id", object.optString("session_id"));
            json.addProperty("operator_id", object.optString("operator_id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    private String getToken(){
        String token = "";
        try {
            String jsonString = AccountManager.getInstance().getSessionString();
            JSONObject object = new JSONObject(jsonString);
            token = object.optString("session_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return token;
    }
}
