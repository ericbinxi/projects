package cn.com.mod.office.lightman;

import android.app.Application;
import android.test.ApplicationTestCase;

import cn.com.mod.office.lightman.api.ILightMgrApi;
import cn.com.mod.office.lightman.api.LightMgrApi;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    public void testClient() {
        ILightMgrApi client = new LightMgrApi(getContext());
        client.connectTest(new ILightMgrApi.Callback<Boolean>() {
            @Override
            public void callback(int code, Boolean result) {
                System.out.println("code = " + code + " , " + result);
            }
        });
    }
}