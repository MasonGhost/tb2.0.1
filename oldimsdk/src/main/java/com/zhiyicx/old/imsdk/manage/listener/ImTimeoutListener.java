package com.zhiyicx.old.imsdk.manage.listener;

import com.zhiyicx.old.imsdk.entity.Message;

import java.util.List;

/**
 * Created by jungle on 16/7/6.
 * com.zhiyicx.old.imsdk.manage.listener
 * zhibo_android
 * email:335891510@qq.com
 */
public interface ImTimeoutListener {

    /**
     * 发送消息超时
     *
     * @param message
     */
    void onMessageTimeout(Message message);

    /**
     * 加入对话超时
     * @param roomId
     */
    void onConversationJoinTimeout(int roomId);
    /**
     * 离开对话超时
     * @param roomId
     */
    void onConversationLeaveTimeout(int roomId);
    /**
     * 查询对话人数超时
     * @param roomIds    
     */
    void onConversationMcTimeout(List<Integer> roomIds);
}
