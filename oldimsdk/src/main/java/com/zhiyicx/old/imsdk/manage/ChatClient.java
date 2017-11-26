package com.zhiyicx.old.imsdk.manage;

import android.content.Context;

import com.zhiyicx.old.imsdk.builder.MessageBuilder;
import com.zhiyicx.old.imsdk.db.dao.ConversationDao;
import com.zhiyicx.old.imsdk.entity.ChatRoomContainer;
import com.zhiyicx.old.imsdk.entity.Conversation;
import com.zhiyicx.old.imsdk.entity.Message;
import com.zhiyicx.old.imsdk.entity.MessageExt;
import com.zhiyicx.old.imsdk.manage.listener.ImMsgReceveListener;
import com.zhiyicx.old.imsdk.manage.listener.ImStatusListener;
import com.zhiyicx.old.imsdk.manage.listener.ImTimeoutListener;
import com.zhiyicx.old.imsdk.manage.soupport.ChatSoupport;
import com.zhiyicx.old.imsdk.utils.common.LogUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by jungle on 16/7/29.
 * com.zhiyicx.old.imsdk.manage
 * zhibo_android
 * email:335891510@qq.com
 */
public class ChatClient implements ChatSoupport, ImMsgReceveListener, ImStatusListener, ImTimeoutListener {
    private static final String TAG = "ChatClient";
    private boolean rt = false;

    private Context mContext;
    private ImMsgReceveListener mImMsgReceveListener;
    private ImStatusListener mImStatusListener;

    private ImTimeoutListener mImTimeoutListener;


    public ChatClient(Context mContext) {
        this.mContext = mContext.getApplicationContext();
        ZBIMClient.getInstance().addImMsgReceveListener(this);
        ZBIMClient.getInstance().addImStatusListener(this);
        ZBIMClient.getInstance().addImTimeoutListener(this);


    }

    public boolean isRt() {
        return rt;
    }

    public void setRt(boolean rt) {
        this.rt = rt;
    }

    public void setImStatusListener(ImStatusListener imStatusListener) {
        mImStatusListener = imStatusListener;
    }

    public void setImMsgReceveListener(ImMsgReceveListener imMsgReceveListener) {
        mImMsgReceveListener = imMsgReceveListener;
    }

    public void setImTimeoutListener(ImTimeoutListener imTimeoutListener) {
        mImTimeoutListener = imTimeoutListener;
    }

    /**
     * 发送文本消息
     *
     * @param text
     */
    @Override
    public void sendTextMsg(String text, int cid, String ZBUSID,int msgid) {
        sendMessage(MessageBuilder.createTextMessage(msgid,cid, ZBUSID, text, rt));
    }

    /**
     * 发送自定义消息
     *
     * @param isEnable 禁用是否能发送
     * @param jsonstr
     */
    @Override
    public void sendMessage(boolean isEnable, Map jsonstr, int cid, String ZBUSID, int customID, int msgid) {

        sendMessage(MessageBuilder.createCustomMessage(msgid,cid, ZBUSID,customID, isEnable, jsonstr, rt));
    }

    @Override
    public void sendMessage(boolean isEnable, int cid, MessageExt ext,int msgid) {
        sendMessage(MessageBuilder.createCustomMessage(msgid,cid, isEnable,ext, rt));
    }
    @Override
    public void sendMessage(Message message) {
        ZBIMClient.getInstance().sendMessage(message);
    }

    /**
     * 注销监听
     */
    @Override
    public void onDestroy() {
        ZBIMClient.getInstance().removeImMsgReceveListener(this);
        ZBIMClient.getInstance().removeImStatusListener(this);
        ZBIMClient.getInstance().removeImTimeoutListener(this);
    }



    /**
     * 消息监听
     *
     * @param message
     */
    @Override
    public void onMessageReceived(Message message) {
        if (isNeedAbandon(message.cid)) return;
        if (mImMsgReceveListener != null)
            mImMsgReceveListener.onMessageReceived(message);
    }

    @Override
    public void onMessageACKReceived(Message message) {
        if (isNeedAbandon(message.cid)) return;
        if (mImMsgReceveListener != null)
            mImMsgReceveListener.onMessageACKReceived(message);
    }

    @Override
    public void onConversationJoinACKReceived(ChatRoomContainer chatRoomContainer) {
        if (isNeedAbandon(chatRoomContainer.mChatRooms.get(0).cid)) return;
        if (mImMsgReceveListener != null)
            mImMsgReceveListener.onConversationJoinACKReceived(chatRoomContainer);
    }

    @Override
    public void onConversationLeaveACKReceived(ChatRoomContainer chatRoomContainer) {
        if (isNeedAbandon(chatRoomContainer.mChatRooms.get(0).cid)) return;
        if (mImMsgReceveListener != null)
            mImMsgReceveListener.onConversationLeaveACKReceived(chatRoomContainer);
    }

    @Override
    public void onConversationMCACKReceived(List<Conversation> conversations) {
        if (mImMsgReceveListener != null)
            mImMsgReceveListener.onConversationMCACKReceived(conversations);
    }

    /**
     * 连接状态监听
     */
    @Override
    public void onConnected() {
        if (mImStatusListener != null)
            mImStatusListener.onConnected();

    }

    @Override
    public void onDisconnect(int code, String reason) {
        if (mImStatusListener != null)
            mImStatusListener.onDisconnect(code, reason);
    }

    @Override
    public void onError(Exception error) {
        if (mImStatusListener != null)
            mImStatusListener.onError(error);
    }

    /**
     * 超时监听
     *
     * @param message
     */
    @Override
    public void onMessageTimeout(Message message) {
        if (isNeedAbandon(message.cid)) return;
        LogUtils.debugInfo(TAG, "timeout--发送消息超时----" + message.toString());
        if (mImTimeoutListener != null)
            mImTimeoutListener.onMessageTimeout(message);
    }

    @Override
    public void onConversationJoinTimeout(int roomId) {
        if (isNeedAbandon(roomId)) return;
        LogUtils.debugInfo(TAG, "timeout---加入房间超时- ----roomId = " + roomId);
        if (mImTimeoutListener != null)
            mImTimeoutListener.onConversationJoinTimeout(roomId);
    }

    @Override
    public void onConversationLeaveTimeout(int roomId) {
        if (isNeedAbandon(roomId)) return;
        LogUtils.debugInfo(TAG, "timeout---离开房间超时- ----roomId = " + roomId);
        if (mImTimeoutListener != null)
            mImTimeoutListener.onConversationLeaveTimeout(roomId);
    }

    @Override
    public void onConversationMcTimeout(List<Integer> roomIds) {
        if (isNeedAbandon(roomIds.get(0))) return;
        LogUtils.debugInfo(TAG, "timeout---查询房间人数- ----roomIds = " + roomIds);
        if (mImTimeoutListener != null)
            mImTimeoutListener.onConversationMcTimeout(roomIds);
    }

    /**
     * 抛弃本条消息
     *
     * @param cid
     * @return
     */
    private boolean isNeedAbandon(int cid) {
        Conversation conversation = ConversationDao.getInstance(mContext).getConversationByCid(cid);
        if (conversation == null || conversation.getType() == Conversation.CONVERSATION_TYPE_CHAROOM) {
            return true;
        }
        return false;
    }
}
