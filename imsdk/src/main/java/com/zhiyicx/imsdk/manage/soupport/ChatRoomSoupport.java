package com.zhiyicx.imsdk.manage.soupport;

import com.zhiyicx.imsdk.entity.ChatRoomDataCount;
import com.zhiyicx.imsdk.entity.Message;

import java.util.Map;

/**
 * Created by jungle on 16/7/6.
 * com.zhiyicx.imsdk.manage
 * zhibo_android
 * email:335891510@qq.com
 */
public interface ChatRoomSoupport {

    /**
     * IM发文本消息
     *
     * @param text
     */
    void sendTextMsg(String text);


    /**
     * 发送礼物消息
     *
     * @param jsonstr
     */
    void sendGiftMessage(Map jsonstr);

    /**
     * 发送赞消息
     *
     * @param type
     */
    void sendZan(int type);

    /**
     * 发送关注消息
     */
    void sendAttention();

    /**
     * 发送自定义消息
     *
     * @param isEnable
     * @param jsonstr
     * @param customId
     */
    void sendMessage(boolean isEnable, Map jsonstr, int customId);
    void sendMessage(Message message);

    /**
     * 自己加入聊天室通知其他人
     */
    void sendJoinRoomMessage();

    /**
     * 机器人加入聊天室通知其他人
     */
    void sendRobotJoinRoomMessage(String robotUsid);

    /**
     * 自己离开聊天室通知其他人
     */
    void sendLeaveRoomMessage();


    /**
     * 聊天室人数 浏览量 统计
     */
    void sendDataCountMessage(ChatRoomDataCount custom);

    /**
     * IM进入房间
     */
    void joinRoom();


    /**
     * IM离开房间
     */
    void leaveRoom();

    /**
     * 查看房间成员
     */
    void mc();

    /**
     * 解除IM监听
     */
    void onDestroy();

}
