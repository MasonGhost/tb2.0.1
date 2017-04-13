package com.zhiyicx.imsdk.manage.soupport;

import com.zhiyicx.imsdk.entity.GiftMessage;
import com.zhiyicx.imsdk.entity.IMConfig;
import com.zhiyicx.imsdk.entity.Message;

import java.util.List;

/**
 * Created by jungle on 16/7/6.
 * com.zhiyicx.imsdk.manage
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
    void sendTextMsg(String text, int cid, int msgid);

    void sendGiftMsg(int cid, int type, GiftMessage giftMessage, int msgid);

    void sendZan(int cid, int type, int msgid);

    void sendAttention(int cid, int msgid);

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
    void joinConversation(int cid, String pwd, int msgid);


    /**
     * IM离开房间
     *
     * @param cid
     * @param pwd
     */
    void leaveConversation(int cid, String pwd, int msgid);

    /**
     * 查看房间成员
     *
     * @param roomIds
     * @param field
     */
    void mc(List<Integer> roomIds, String field, int msgid);

    /**
     * 获取指定序号消息
     *
     * @param cid
     * @param seq
     */
    void pluck(int cid, List<Integer> seq, int msgid);

    /**
     * 获取指定序号消息
     *
     * @param cid
     * @param gt
     * @param lt
     */
    void syncAsc(int cid, int gt, int lt, int msgid);

    /**
     * 获取指定序号消息
     *    "order":0, // 服务端查询时的排序方式，可选，默认0； 0正序、1倒序。 注意返回的始终是正序
     *    "limit": 100, // 需要获取的消息数量，可选，最大100，默认100；一般在未指定lt时提供
     * @param cid 要获取到对话的ID， 无符号长整型，必填
     * @param gt   获取消息序号将大于此序号，可选，默认0
     * @param lt 获取消息序号将小于此序号，可选，默认不限制； lt-gt应<=100
     */
    void syncDesc(int cid, int gt, int lt, int msgid);

    /**
     * 获取房间中已有的最新几条消息消息
     *
     * @param cid   房间号
     * @param limit 消息数量
     * @param msgid 消息id
     */
    void syncLastMessage(int cid, int limit, int msgid);

    /**
     * 尝试重新连接
     */
    void reConnect();
}
