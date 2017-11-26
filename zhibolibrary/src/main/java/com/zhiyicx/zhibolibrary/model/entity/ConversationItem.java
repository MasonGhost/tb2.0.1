package com.zhiyicx.zhibolibrary.model.entity;

import com.zhiyicx.old.imsdk.entity.Conversation;

import java.io.Serializable;

/**
 * Created by jungle on 16/8/19.
 * com.zhiyicx.zhibo.model.entity
 * zhibo_android
 * email:335891510@qq.com
 */
public class ConversationItem implements Serializable{

    private Conversation conversation;

    private UserInfo user;


    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "ConversationItem{" +
                "conversation=" + conversation +
                ", user=" + user +
                '}';
    }
}
