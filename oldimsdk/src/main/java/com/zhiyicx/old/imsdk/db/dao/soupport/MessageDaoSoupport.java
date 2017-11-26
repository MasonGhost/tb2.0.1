package com.zhiyicx.old.imsdk.db.dao.soupport;

import com.zhiyicx.old.imsdk.entity.Message;

import java.util.List;

/**
 * Created by jungle on 16/8/15.
 * com.zhiyicx.old.imsdk.db.dao.soupport
 * zhibo_android
 * email:335891510@qq.com
 */
public interface MessageDaoSoupport {

    /**
     * 插入消息
     *
     * @param message
     * @return
     */
    long insertMessage(Message message);

    /**
     * 数据库是否已经有了这条消息
     * 消息去重使用
     *
     * @param mid
     * @return
     */
    boolean hasMessage(long mid);

    /**
     * 通过会话cid获取消息数据
     * @param cid
     * @param page
     * @return
     */
    List<Message> getMessageListByCid(int cid, int page);


    /**
     * 获取最新的一条消息
     * @param cid
     * @return
     */
    Message  getLastMessageByCid(int cid);


    /**
     * 标记该消息已读
     * @param mid
     * @return
     */
    boolean readMessage(long mid);

    /**
     * 标记该消息已删除
     * @param mid
     * @return
     */
    boolean delMessage(long mid);
}
