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
    public long disa; //  用户被禁用，disa为自动解禁时间

    public String errMsg;
    public String mEvent;
    public boolean blk;
    public int expire;

    public MessageContainer mMessageContainer;
    public ChatRoomContainer mChatRoomContainer;
    public Conversation mConver;
    public List<Conversation> mConversations;
    public AuthData mAuthData;

    public EventContainer() {
    }

    @Override
    public String toString() {
        return "EventContainer{" +
                "err=" + err +
                ", disa=" + disa +
                ", errMsg='" + errMsg + '\'' +
                ", mEvent='" + mEvent + '\'' +
                ", blk=" + blk +
                ", expire=" + expire +
                ", mMessageContainer=" + mMessageContainer +
                ", mChatRoomContainer=" + mChatRoomContainer +
                ", mConver=" + mConver +
                ", mConversations=" + mConversations +
                ", mAuthData=" + mAuthData +
                '}';
    }
}
