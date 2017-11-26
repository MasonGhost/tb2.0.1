package com.zhiyicx.zhibolibrary.model.entity;

import com.zhiyicx.old.imsdk.entity.Message;

import java.io.Serializable;

/**
 * Created by jungle on 16/5/26.
 * com.zhiyicx.zhibo.model.entity
 * zhibo_android
 * email:335891510@qq.com
 */
public class UserMessage implements Serializable {
    public UserInfo mUserInfo;
    public Message msg;

    public UserMessage(Message msg) {
        this.msg = msg;
    }

    public UserMessage(UserInfo userInfo, Message msg) {
        mUserInfo = userInfo;
        this.msg = msg;
    }
}
