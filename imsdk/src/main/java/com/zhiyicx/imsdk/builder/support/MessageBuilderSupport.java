package com.zhiyicx.imsdk.builder.support;

import com.zhiyicx.imsdk.entity.Message;

/**
 * Created by jungle on 16/7/28.
 * com.zhiyicx.imsdk.builder
 * zhibo_android
 * email:335891510@qq.com
 */
public interface MessageBuilderSupport {
    /**
     * 文本消息
     *
     * @param text
     * @param cid
     * @param rt
     * @return
     */
    Message createTextMessage(int cid, String text, boolean rt);


//    Message createGiftMessage(int cid, GiftMessage giftMessage, boolean rt);


    Message createZanMessage(int cid, int msgType);


    /**
     * 自定义关注消息
     *
     * @param cid
     */
    Message createAttentionMessage(int cid);

    Message createCustomMessage(int cid, int type, Object ext, boolean rt);
}
