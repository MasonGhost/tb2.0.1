package com.zhiyicx.imsdk.core;

import com.zhiyicx.imsdk.core.autobahn.WebSocket;
import com.zhiyicx.imsdk.core.autobahn.WebSocketConnection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/14
 * @Contact master.jungle68@gmail.com
 */
public class ImServiceTest {
    ImService mImService;
    WebSocket mWebSocket;

    @Before
    public void before() {
        mWebSocket = mock(WebSocketConnection.class);
        mImService = new ImService(mWebSocket);
    }

    /**
     * 测试发送获取对话信息
     *
     * @throws Exception
     */
    @Test
    public void sendGetConversatonInfo() throws Exception {
        Assert.assertFalse(mImService.sendGetConversatonInfo(0, "cid"));
        Assert.assertTrue(mImService.sendGetConversatonInfo(123, "cid"));
    }

    /**
     * 测试发送获取对话信息
     *
     * @throws Exception
     */
    @Test
    public void sendGetConversatonInfoList() throws Exception {
        Assert.assertFalse(mImService.sendGetConversatonInfo(null, "cid"));
        List<Integer> list = new ArrayList<>();
        list.add(123);
        Assert.assertTrue(mImService.sendGetConversatonInfo(list, "cid"));
    }

    @Test
    public void setUrl() {
        String Url = "http:zhiyicx.com";
        mImService.setUrl(Url);
        Assert.assertEquals(Url, mImService.getUri());
    }

    @Test
    public void setParams() {
        String web_socket_authority = "0";
        String token = "1";
        int serial = 2;
        int comprs = 3;
        String params = "token=" + token + "&serial=" + serial + "&comprs=" + comprs;
        mImService.setParams(web_socket_authority, token, serial, comprs);
        Assert.assertEquals(params, mImService.getParams());
    }

}