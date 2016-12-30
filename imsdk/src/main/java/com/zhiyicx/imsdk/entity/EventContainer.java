package com.zhiyicx.imsdk.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jungle on 16/5/23.
 * com.zhiyicx.zhibo.model.entity
 * zhibo_android
 * email:335891510@qq.com
 */
public class EventContainer implements Serializable {
    public int err;


    public String errMsg;
    public String mEvent;
    public boolean blk;
    public int expire;

    public MessageContainer mMessageContainer;
    public ChatRoomContainer mChatRoomContainer;
    public Conver mConver;
    public List<Conversation> mConversations;

    public EventContainer() {
    }

    @Override
    public String toString() {
        return "EventContainer{" +
                "err=" + err +
                ", errMsg='" + errMsg + '\'' +
                ", mEvent='" + mEvent + '\'' +
                ", mMessageContainer=" + mMessageContainer +
                ", mChatRoomContainer=" + mChatRoomContainer +
                ", mConver=" + mConver +
                ", mConversations=" + mConversations +
                '}';
    }

}
