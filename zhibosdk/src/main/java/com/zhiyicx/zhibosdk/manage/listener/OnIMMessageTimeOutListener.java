package com.zhiyicx.zhibosdk.manage.listener;

import com.zhiyicx.imsdk.entity.Message;

/**
 * Created by jungle on 16/7/15.
 * com.zhiyicx.zhibosdk.manage.soupport
 * zhibo_android
 * email:335891510@qq.com
 */
public interface OnIMMessageTimeOutListener {
    void onMessageTimeout(Message message);
}
