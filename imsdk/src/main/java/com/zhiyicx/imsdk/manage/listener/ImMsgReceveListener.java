package com.zhiyicx.imsdk.manage.listener;

import com.zhiyicx.imsdk.entity.ChatRoomContainer;
import com.zhiyicx.imsdk.entity.Conversation;
import com.zhiyicx.imsdk.entity.Message;

import java.util.List;

/**
 * Created by jungle on 16/7/6.
 * com.zhiyicx.imsdk.manage.listener
 * zhibo_android
 * email:335891510@qq.com
 */
public interface ImMsgReceveListener {

    /**
     * 收到消息
     *
     * @param message
     */
    void onMessageReceived(Message message);

    /**
     * 发送消息回调
     *
     * @param message
     */
    void onMessageACKReceived(Message message);

    /**
     * 加入聊天室回调
     *
     * @param chatRoomContainer
     */
    void onConversationJoinACKReceived(ChatRoomContainer chatRoomContainer);

    /**
     * 查询离开聊天室回调
     *
     * @param chatRoomContainer
     */
    void onConversationLeaveACKReceived(ChatRoomContainer chatRoomContainer);

    /**
     * 查询聊天室人数回调
     *
     * @param conversations
     */
    void onConversationMCACKReceived(List<Conversation> conversations);

    /**
     * 获取房间最新的几条消息
     * @param limit 消息条数
     */
    void synchronousInitiaMessage(int limit);
}
