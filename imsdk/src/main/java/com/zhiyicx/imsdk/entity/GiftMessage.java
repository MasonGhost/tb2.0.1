package com.zhiyicx.imsdk.entity;

import java.io.Serializable;

/**
 * Created by jungle on 16/5/27.
 * com.zhiyicx.zhibo.model.entity
 * zhibo_android
 * email:335891510@qq.com
 */
@org.msgpack.annotation.Message
public class GiftMessage implements Serializable{
    public String type;//	1-8礼物类型 对应金币数量*1 *10 *20 *30 *50 *100 *500 *1000
    public int count;//1-1000 赠送礼物对应的金币数量

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public GiftMessage(String type, int count) {
        this.type = type;
        this.count = count;
    }

    public GiftMessage() {
    }
}

