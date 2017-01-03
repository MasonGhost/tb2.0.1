package com.zhiyicx.imsdk.service;

import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.test.ServiceTestCase;
import android.test.suitebuilder.annotation.MediumTest;

import com.zhiyicx.imsdk.entity.IMConfig;
import com.zhiyicx.imsdk.utils.common.LogUtils;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/26
 * @Contact master.jungle68@gmail.com
 */
public class SocketServiceTest extends ServiceTestCase<SocketService> {
    private static final String TAG = "SocketServiceTest";

    /**
     * Constructor
     */
    public SocketServiceTest() {
        super(SocketService.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        LogUtils.debugInfo(TAG, "setUp");
        startService();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        LogUtils.debugInfo(TAG, "tearDown");
        stopService();
    }

    /**
     * Test basic startup/shutdown of Service
     */
    private void startService() {
        Intent startIntent = new Intent();
        startIntent.setClass(getContext(), SocketService.class);
        startService(startIntent);
    }

    /**
     * Test basic startup/shutdown of Service
     */
    private void stopService() {
        Intent startIntent = new Intent();
        startIntent.setClass(getContext(), SocketService.class);
        getContext().stopService(startIntent);
    }

    /**
     * Test binding to service
     */
    @MediumTest
    public void testBindable() {
        Intent startIntent = new Intent();
        startIntent.setClass(getContext(), SocketService.class);
        IBinder service = bindService(startIntent);
    }

    /**
     * IM 登录
     */
    @MediumTest
    public void testLogin() {
        IMConfig imConfig = new IMConfig();
        imConfig.setImUid(20124);
        imConfig.setToken("lEzgo3Ko-8B45s9LbYwQv5xOAAA");
        imConfig.setWeb_socket_authority("ws://218.244.149.144:9900/");
        Bundle bundle = new Bundle();
        bundle.putInt(SocketService.EVENT_SOCKET_TAG, SocketService.TAG_IM_LOGIN);
        bundle.putSerializable(SocketService.BUNDLE_IMCONFIG, imConfig);
        getService().dealMessage(bundle);
    }

    @MediumTest
    public void testChangeHeartBeatRate() {
        testLogin();
        LogUtils.debugInfo(TAG, "testChangeHeartBeatRate");
        // socket重连
        Intent socketretry = new Intent(SocketService.SOCKET_RETRY_CONNECT);
        getContext().sendBroadcast(socketretry);
    }

    /**
     * wifi和移动网络切换
     */
    @MediumTest
    public void testNetReconnected() {
        testLogin();
        LogUtils.debugInfo(TAG, "testNetReconnected");
        // socket重连
        Intent socketretry = new Intent(SocketService.SOCKET_RETRY_CONNECT);
        getContext().sendBroadcast(socketretry);
    }

}