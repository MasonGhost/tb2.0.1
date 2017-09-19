package com.zhiyicx.zhibosdk.manage.soupport;

import com.zhiyicx.imsdk.entity.MessageExt;

import java.util.Map;

/**
 * Created by jungle on 16/8/22.
 * com.zhiyicx.zhibosdk.manage.soupport
 * zhibo_android
 * email:335891510@qq.com
 */
public interface BaseIMSoupport {
    /**
     * IM发文本消息
     *
     * @param text
     */
    void sendTextMsg(String text,int cid,String usid,int msgid);


    /**
     * 发送自定义消息
     *
     * @param isEnable
     * @param jsonstr
     */
    void sendCustomMessage(boolean isEnable, Map jsonstr, int cid, String usid, int customId, int msgid);
    /**
     * 发送自定义消息
     *
     * @param isEnable
     * @param cid
     * @param ext
     */
    void sendCustomMessage(boolean isEnable, int cid, MessageExt ext,int msgid);

    /**
     * 发送自定义消息
     *
     * @param cid
     * @param ext
     */
    void sendEnableCustomMessage(int cid, MessageExt ext,int msgid);
    /**
     * 发送自定义消息
     *
     * @param cid
     * @param ext
     */
    void sendDisableCustomMessage(int cid, MessageExt ext,int msgid);
}
