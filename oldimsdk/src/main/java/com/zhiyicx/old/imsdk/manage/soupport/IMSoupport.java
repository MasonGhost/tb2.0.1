package com.zhiyicx.old.imsdk.manage.soupport;

import com.zhiyicx.old.imsdk.entity.GiftMessage;
import com.zhiyicx.old.imsdk.entity.IMConfig;
import com.zhiyicx.old.imsdk.entity.Message;

import java.util.List;

/**
 * Created by jungle on 16/7/6.
 * com.zhiyicx.old.imsdk.manage
 * zhibo_android
 * email:335891510@qq.com
 */
public interface IMSoupport {

    /**
     * IM登录
     *
     * @param imConfig
     */
    void login(IMConfig imConfig);

    /**
     * IM登出
     */
    void loginOut();

    /**
     * IM发文本消息
     *
     * @param text
     */
    void sendTextMsg(String text, int cid,int msgid);

    void sendGiftMsg(int cid, int type, GiftMessage giftMessage,int msgid);

    void sendZan(int cid, int type,int msgid);

    void sendAttention(int cid,int msgid);

    /**
     * 总消息，可自定义
     *
     * @param message
     */
    void sendMessage(Message message);

    /**
     * IM进入房间
     *
     * @param cid
     * @param pwd 密钥
     */
    void joinConversation(int cid, String pwd,int msgid);


    /**
     * IM离开房间
     *
     * @param cid
     * @param pwd
     */
    void leaveConversation(int cid, String pwd,int msgid);

    /**
     * 查看房间成员
     *
     * @param roomIds
     * @param field
     */
    void mc(List<Integer> roomIds,String field,int msgid);

    /**
     * 获取指定序号消息
     * @param cid
     * @param seq
     */
    void pluck(int cid,List<Integer> seq,int msgid);

    /**
     * 获取指定序号消息
     * @param cid
     * @param gt
     * @param lt
     */
    void sync(int cid,int gt,int lt,int msgid);
    /**
     * 尝试重新连接
     */
    void reConnect();
}
