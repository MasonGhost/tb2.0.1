package com.zhiyicx.imsdk.service;

import com.zhiyicx.imsdk.core.autobahn.WebSocketConnection;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/14
 * @Contact master.jungle68@gmail.com
 */

public class MockSocketService extends WebSocketConnection {

    public void sendBinaryMessage(byte[] payload) {
        System.out.println("payload -------------------------- = " + payload);
    }
}

