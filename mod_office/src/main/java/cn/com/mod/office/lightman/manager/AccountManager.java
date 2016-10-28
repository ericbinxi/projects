package cn.com.mod.office.lightman.manager;

import android.content.Context;

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

    public void setSession(LoginResp.Session session) {
        this.session = session;
    }
}
