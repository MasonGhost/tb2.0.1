package com.zhiyicx.zhibosdk.manage.im.soupport;

/**
 * Created by jungle on 16/8/19.
 * com.zhiyicx.zhibosdk.manage.im.soupport
 * zhibo_android
 * email:335891510@qq.com
 */
public interface PrivateChatClientSoupport {


    /**
     * 创建私有对话
     */
    void createPrivateConversation();

    /**
     * 加入对话
     */
    void joinConversation();


    /**
     * 离开对话
     */
    void leaveConversation();


    /**
     * 解除IM监听
     */
    void onDestroy();


    /**
     * IM发文本消息
     *
     * @param text
     */
    void sendTextMsg(String text);


    /**
     * 发送礼物消息
     * @param jsonstr
     */
    void sendGiftMessage(Object jsonstr);

    /**
     * 发送赞消息
     * @param type
     */
    void sendZan( int type);

    /**
     * 发送关注消息
     */
    void sendAttention();

    /**
     * 发送自定义消息
     * @param isEnable
     * @param jsonstr
     */
    void sendMessage(boolean isEnable, Object jsonstr);


}
