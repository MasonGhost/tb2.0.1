package com.zhiyicx.imsdk.entity;

import java.io.Serializable;
import java.util.Map;

/**
 * 自定义消息内容
 * Created by jungle on 16/5/19.
 * com.zhiyicx.zhibo.model.entity
 * zhibo_android
 * email:335891510@qq.com
 */
@org.msgpack.annotation.Message
public class MessageExt implements Serializable {

    public String ZBUSID;
    public int customID;
    public Map custom;

    public MessageExt(String ZBUSID, Map custom) {
        this.ZBUSID = ZBUSID;
        this.custom = custom;
    }

    public MessageExt() {
    }

    public String getZBUSID() {
        return ZBUSID;
    }

    public Map getCustom() {
        return custom;
    }

    public void setCustom(Map custom) {
        this.custom = custom;
    }

    public void setZBUSID(String ZBUSID) {
        this.ZBUSID = ZBUSID;
    }

    public int getCustomID() {
        return customID;
    }

    public void setCustomID(int customID) {
        this.customID = customID;
    }
}
