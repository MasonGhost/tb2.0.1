package com.zhiyicx.zhibosdk.manage.listener;

/**
 * Created by jungle on 16/7/15.
 * com.zhiyicx.zhibosdk.manage.soupport
 * zhibo_android
 * email:335891510@qq.com
 */
public interface OnImStatusListener {

    void onConnected();

    void onDisconnect(int code, String reason);
}
