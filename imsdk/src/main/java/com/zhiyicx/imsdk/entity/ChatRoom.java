package com.zhiyicx.imsdk.entity;

import java.io.Serializable;

/**
 * Created by jungle on 16/5/23.
 * com.zhiyicx.zhibo.model.entity
 * zhibo_android
 * email:335891510@qq.com
 */
public class ChatRoom implements Serializable{

    public int cid;
    public int mc;
    public long expire=-1;//为禁言，0永久，大于0为自动解禁时间


    public ChatRoom() {
    }

    public ChatRoom(int cid) {
        this.cid = cid;
    }

    public ChatRoom(int cid, int mc) {
        this.cid = cid;
        this.mc = mc;
    }

    public ChatRoom(int cid, int mc, long expire) {
        this.cid = cid;
        this.mc = mc;
        this.expire = expire;
    }
}
