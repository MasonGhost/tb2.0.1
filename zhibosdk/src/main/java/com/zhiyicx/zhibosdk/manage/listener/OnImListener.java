package com.zhiyicx.zhibosdk.manage.listener;

import com.zhiyicx.old.imsdk.entity.ChatRoomDataCount;
import com.zhiyicx.old.imsdk.entity.Message;

/**
 * Created by jungle on 16/7/15.
 * com.zhiyicx.zhibosdk.manage.soupport
 * zhibo_android
 * email:335891510@qq.com
 */
public interface OnImListener {

    void onBanned(long gag);

    void onMessageReceived(Message message);
    void onGiftReceived(Message message);

    void onZanReceived(Message message);
    void onAttentionMessageReceived(Message message);
    void onSomeBodyJoinMessageReceived(String usid);

    void onSomeBodyLeaveMessageReceived(String usid);

    void onChatRoomDataCountReceived(ChatRoomDataCount chatRoomDataCount);

    void onMessageACK(Message message);

    void onConvrEnd(int cid);

    void onSystemMessageReceived(String text);

    void onJoinRoomSuccessed();

}
