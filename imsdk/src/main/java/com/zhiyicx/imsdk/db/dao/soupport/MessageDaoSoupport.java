package com.zhiyicx.imsdk.db.dao.soupport;

import com.zhiyicx.imsdk.entity.Message;

import java.util.List;

/**
 * Created by jungle on 16/8/15.
 * com.zhiyicx.imsdk.db.dao.soupport
 * zhibo_android
 * email:335891510@qq.com
 */
public interface MessageDaoSoupport {

    /**
     * 插入或者更新消息
     *
     * @param message
     * @return
     */
    long insertOrUpdateMessage(Message message);

    /**
     * 插入消息
     *
     * @param message
     * @return
     */
    long insertMessage(Message message);

    /**
     * 更新消息
     *
     * @param message
     * @return
     */
    long updateMessage(Message message);

    /**
     * 数据库是否已经有了这条消息
     * 消息去重使用
     *
     * @param id
     * @return
     */
    boolean hasMessageById(long id);

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
     *
     * @param cid
     * @param page
     * @return
     */
    List<Message> getMessageListByCid(int cid, int page);

    /**
     * 通过会话cid获取消息数据
     *
     * @param cid
     * @param create_time
     * @return
     */
    List<Message> getMessageListByCidAndCreateTime(int cid, long create_time);


    /**
     * 获取最新的一条消息
     *
     * @param cid
     * @return
     */
    Message getLastMessageByCid(int cid);

    /**
     * 获取当前对话未读消息数
     *
     * @param cid 对话 id
     * @return 未读消息数量
     */
    int getUnReadMessageCount(int cid);


    /**
     * 标记该消息已读
     *
     * @param mid
     * @return
     */
    boolean readMessage(long mid);

    /**
     * 修改消息状态
     *
     * @param mid        消息 mid
     * @param sendStatus 发送状态 0,发送中，1发成功，2发送失败,
     * @return
     */
    boolean changeMessageSendStausByMid(long mid, int sendStatus);

    /**
     * 标记该消息已删除
     *
     * @param mid
     * @return
     */
    boolean delMessage(long mid);

    /**
     * 标记该消息已删除
     *
     * @param cid  conversation id
     * @return
     */
    boolean delMessageByCid(int cid);
    /**
     * 永久删除该消息
     *
     * @param cid  conversation id
     * @return
     */
    boolean delEverMessageByCid(int cid);
}
