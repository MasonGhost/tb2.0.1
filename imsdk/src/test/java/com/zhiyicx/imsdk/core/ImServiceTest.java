package com.zhiyicx.imsdk.core;

import com.zhiyicx.imsdk.core.autobahn.WebSocket;
import com.zhiyicx.imsdk.core.autobahn.WebSocketConnection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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

}