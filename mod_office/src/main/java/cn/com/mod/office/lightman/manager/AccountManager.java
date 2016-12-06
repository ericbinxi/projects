package cn.com.mod.office.lightman.manager;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.JsonObject;

import cn.com.mod.office.lightman.MyApplication;
import cn.com.mod.office.lightman.api.resp.LoginResp;

/**
 * Created by Administrator on 2016/10/15.
 */
public class AccountManager {
    private static AccountManager instance;
    private Context context;
    private String sessionId;
    private int operatorId;
    private LoginResp.Session session;
    private String sessionString;

    private AccountManager(){
    }
    public static AccountManager getInstance(){
        if(instance==null){
            instance = new AccountManager();
        }
        return instance;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public int getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(int operatorId) {
        this.operatorId = operatorId;
    }

    public LoginResp.Session getSession() {
        return session;
    }
    public String getSessionString() {
        if(TextUtils.isEmpty(sessionString)){
            sessionString =  MyApplication.getInstance().getAppConfig().getSession();
        }
        return sessionString;
    }

    public void setSession(LoginResp.Session session) {
        this.session = session;
        JsonObject json = new JsonObject();
        json.addProperty("session_id", session.getSession_id());
        json.addProperty("operator_id", session.getOperator_id());
        this.sessionString = json.toString();
        MyApplication.getInstance().getAppConfig().setSession(json.toString());
    }
}
