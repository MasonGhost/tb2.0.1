package com.zhiyicx.imsdk.manage.soupport;

import com.zhiyicx.imsdk.entity.Message;
import com.zhiyicx.imsdk.entity.MessageExt;

import java.util.Map;

/**
 * Created by jungle on 16/7/6.
 * com.zhiyicx.imsdk.manage
 * zhibo_android
 * email:335891510@qq.com
 */
public interface ChatSoupport {

    /**
     * IM发文本消息
     *
     * @param text
     */
    Message sendTextMsg(String text, int cid, String ZBUSID);


    /**
     * 发送自定义消息
     *
     * @param isEnable
     * @param jsonstr
     * @param cid
     * @param ZBUSID
     * @param customID
     */
    void sendMessage(boolean isEnable, Map jsonstr, int cid, String ZBUSID, int customID);

    /**
     * 发送自定义消息
     *
     * @param isEnable
     * @param cid
     * @param ext
     */
    void sendMessage(boolean isEnable, int cid, MessageExt ext);

    /**
     * @param message
     */
    void sendMessage(Message message);

    /**
     * 解除IM监听
     */
    void onDestroy();

}
