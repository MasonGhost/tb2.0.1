package com.zhiyicx.old.imsdk.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jungle on 16/5/20.
 * com.zhiyicx.zhibo.model.entity
 * zhibo_android
 * email:335891510@qq.com
 */
@org.msgpack.annotation.Message
public class MessageContainer implements Serializable{
    public String mEvent;
    public Message msg;
    public int roomId;
    public List<Integer> roomIds;
    public int reSendCounts;

    public MessageContainer(String event, Message msg, int roomId, List<Integer> roomIds) {
        mEvent = event;
        this.msg = msg;
        this.roomId = roomId;
        this.roomIds = roomIds;
    }

    public MessageContainer(String event, Message msg) {
        mEvent = event;
        this.msg = msg;
    }

    public MessageContainer() {
    }

    @Override
    public String toString() {
        return "MessageContainer{" +
                "mEvent='" + mEvent + '\'' +
                ", msg=" + msg +
                ", roomId=" + roomId +
                ", roomIds=" + roomIds +
                '}';
    }

}
