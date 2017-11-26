package com.zhiyicx.zhibosdk.manage.soupport;

import com.zhiyicx.old.imsdk.entity.Message;
import com.zhiyicx.zhibosdk.manage.listener.OnCommonCallbackListener;
import com.zhiyicx.zhibosdk.model.entity.ZBApiImInfo;

import java.util.Map;

/**
 * Created by jungle on 16/7/19.
 * com.zhiyicx.zhibosdk.manage.soupport
 * zhibo_android
 * email:335891510@qq.com
 */
public interface ChatRoomIMSupport {
    /**
     * 获取房间信息
     *
     * @return
     */
    ZBApiImInfo getPresenterImInfo();


    /**
     * 发送礼物消息
     *
     * @param jsonstr
     */
    void sendGiftMessage(Map jsonstr, String gift_code, String count, final OnCommonCallbackListener l);

    /**
     * 发送赞消息
     *
     * @param type
     */
    void sendZan(int type);

    /**
     * 发送关注消息
     */
    void sendAttention();


    /**
     * 查询房间人数
     */
    void mc();

    /**
     * IM发文本消息
     *
     * @param text
     */
    void sendTextMsg(String text);


    /**
     * 发送自定义消息
     *
     * @param isEnable
     * @param jsonstr
     * @param customId
     */
    void sendMessage(boolean isEnable, Map jsonstr, int customId);

    void sendMessage(Message message);
}
