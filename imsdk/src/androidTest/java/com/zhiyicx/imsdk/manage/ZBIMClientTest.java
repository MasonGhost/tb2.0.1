<<<<<<< HEAD
package com.zhiyicx.imsdk.manage;

import android.test.InstrumentationTestCase;

import com.zhiyicx.imsdk.entity.IMConfig;
import com.zhiyicx.imsdk.entity.Message;

import org.mockito.Matchers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @Describe 测试方法是否被执行
 * @Author Jungle68
 * @Date 2017/1/14
 * @Contact master.jungle68@gmail.com
 */
public class ZBIMClientTest extends InstrumentationTestCase {
    ZBIMClient mockClient;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        System.setProperty(
                "dexmaker.dexcache",
                getInstrumentation().getTargetContext().getCacheDir().getPath());
        ZBIMSDK.init(getInstrumentation().getContext());
        mockClient = mock(ZBIMClient.class);
    }

    @org.junit.Test
    public void login() throws Exception {
        IMConfig imConfig = new IMConfig();
        imConfig.setImUid(1001);
        imConfig.setToken("vZ2aegWZlQVSPp9ysLrSGukDAAA");
        imConfig.setWeb_socket_authority("ws://192.168.10.222:9900");

        mockClient.login(imConfig);
        verify(mockClient).login(Matchers.eq(imConfig));
    }

    @org.junit.Test
    public void loginOut() throws Exception {
        mockClient.loginOut();
        verify(mockClient).loginOut();
    }

    @org.junit.Test
    public void joinConversation() throws Exception {
        mockClient.joinConversation(1, "12", 12);
        verify(mockClient).joinConversation(1, "12", 12);
    }

    @org.junit.Test
    public void leaveConversation() throws Exception {
        mockClient.leaveConversation(1, "12", 12);
        verify(mockClient).leaveConversation(1, "12", 12);
    }


    @org.junit.Test
    public void sync() throws Exception {

        mockClient.sync(1, 12, 20, 12);
        verify(mockClient).sync(1, 12, 20, 12);
    }


    @org.junit.Test
    public void sendMessage() throws Exception {
        Message msg = new Message();
        mockClient.sendMessage(msg);
        verify(mockClient).sendMessage(msg);
    }

=======
package com.zhiyicx.imsdk.manage;

import android.test.InstrumentationTestCase;

import com.zhiyicx.imsdk.entity.IMConfig;
import com.zhiyicx.imsdk.entity.Message;

import org.mockito.Matchers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @Describe 测试方法是否被执行
 * @Author Jungle68
 * @Date 2017/1/14
 * @Contact master.jungle68@gmail.com
 */
public class ZBIMClientTest extends InstrumentationTestCase {
    ZBIMClient mockClient;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        System.setProperty(
                "dexmaker.dexcache",
                getInstrumentation().getTargetContext().getCacheDir().getPath());
        ZBIMSDK.init(getInstrumentation().getContext());
        mockClient = mock(ZBIMClient.class);
    }

    @org.junit.Test
    public void login() throws Exception {
        IMConfig imConfig = new IMConfig();
        imConfig.setImUid(1001);
        imConfig.setToken("vZ2aegWZlQVSPp9ysLrSGukDAAA");
        imConfig.setWeb_socket_authority("ws://192.168.10.222:9900");

        mockClient.login(imConfig);
        verify(mockClient).login(Matchers.eq(imConfig));
    }

    @org.junit.Test
    public void loginOut() throws Exception {
        mockClient.loginOut();
        verify(mockClient).loginOut();
    }

    @org.junit.Test
    public void joinConversation() throws Exception {
        mockClient.joinConversation(1, "12", 12);
        verify(mockClient).joinConversation(1, "12", 12);
    }

    @org.junit.Test
    public void leaveConversation() throws Exception {
        mockClient.leaveConversation(1, "12", 12);
        verify(mockClient).leaveConversation(1, "12", 12);
    }


    @org.junit.Test
    public void sync() throws Exception {

        mockClient.sync(1, 12, 20, 12);
        verify(mockClient).sync(1, 12, 20, 12);
    }


    @org.junit.Test
    public void sendMessage() throws Exception {
        Message msg = new Message();
        mockClient.sendMessage(msg);
        verify(mockClient).sendMessage(msg);
    }

>>>>>>> 5eb1174523744bea0c0756f5af31310a1467fb94
}