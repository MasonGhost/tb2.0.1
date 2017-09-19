package com.zhiyicx.zhibosdk.manage.listener;

import com.zhiyicx.imsdk.entity.Conversation;

/**
 * Created by jungle on 16/8/22.
 * com.zhiyicx.zhibosdk.manage.listener
 * zhibo_android
 * email:335891510@qq.com
 */
public interface OnChatCreateListener extends OnCommonCallbackListener {

    void onSuccess(Conversation conversation);
}
