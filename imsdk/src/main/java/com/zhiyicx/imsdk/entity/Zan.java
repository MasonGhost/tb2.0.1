package com.zhiyicx.imsdk.entity;

import java.io.Serializable;

/**
 * Created by jungle on 16/8/25.
 * com.zhiyicx.imsdk.entity
 * zhibo_android
 * email:335891510@qq.com
 */
public class Zan implements Serializable {
    public int comment;

    public Zan() {
    }

    public Zan(int comment) {
        this.comment = comment;
    }

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }
}
