package com.zhiyicx.imsdk.manage.listener;

/**
 * Created by jungle on 16/7/6.
 * com.zhiyicx.imsdk.manage.listener
 * zhibo_android
 * email:335891510@qq.com
 */
public interface ImStatusListener {

    void onConnected();

    void onDisconnect(int code, String reason);

    void onError(Exception error);



}
