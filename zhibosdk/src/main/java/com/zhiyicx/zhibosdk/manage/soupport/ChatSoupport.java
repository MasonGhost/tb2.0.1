package com.zhiyicx.zhibosdk.manage.soupport;

import com.zhiyicx.imsdk.entity.Conversation;
import com.zhiyicx.imsdk.entity.Message;
import com.zhiyicx.zhibosdk.manage.listener.OnChatCreateListener;

import java.util.List;

/**
 * Created by jungle on 16/8/22.
 * com.zhiyicx.zhibosdk.manage.soupport
 * zhibo_android
 * email:335891510@qq.com
 */
public interface ChatSoupport extends BaseIMSoupport {

    /**
     * 创建私有对话
     *
     * @param usid
     * @param l
     */
    void createPrivateConversation(String usid, OnChatCreateListener l);

    /**
     * 创建群组对话
     *
     * @param usids
     * @param name
     * @param pwd
     * @param l
     */
    void createTeamConversation(String usids, String name, String pwd, OnChatCreateListener l);


    /**
     * 获取除了聊天室以外的所有对话
     * @param page    分页，从第一页开始(page=1)
     * @return
     */
    List<Conversation> getChatConversations(int page);


    /**
     * 通过cid获取对话消息
     * @param cid
     * @param page
     * @return
     */
    List<Message>  getMessagesByCid(int cid,int page);


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
