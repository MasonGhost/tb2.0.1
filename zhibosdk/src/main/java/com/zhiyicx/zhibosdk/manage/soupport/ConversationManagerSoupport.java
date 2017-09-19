package com.zhiyicx.zhibosdk.manage.soupport;

import com.google.gson.JsonObject;
import com.zhiyicx.zhibosdk.manage.listener.ZBCloudApiCallback;

import rx.Observable;

/**
 * Created by jungle on 16/8/18.
 * com.zhiyicx.zhibosdk.manage.soupport
 * zhibo_android
 * email:335891510@qq.com
 */
public interface ConversationManagerSoupport {

    /**
     * 创建私有对话
     *
     * @param toUsid 发起聊天对方的标识
     * @return
     */
    void createPrivateChat(String toUsid, ZBCloudApiCallback callback);

    /**
     * 创建私有对话
     *
     * @param toUsid 发起聊天对方的标识
     * @return
     */
    Observable<JsonObject> createPrivateChatForRx(String toUsid);

    /**
     * 创建群组对话
     *
     * @param toUsid           发起聊天对方的标识
     * @param conversationName 群组的名字
     * @param pwd              该群组的加入密码,默认为空
     * @return
     */
    void createTeamChat(String toUsid, String conversationName, String pwd, ZBCloudApiCallback callback);

    Observable<JsonObject> createTeamChatForRx(String toUsid, String conversationName, String pwd);
}
