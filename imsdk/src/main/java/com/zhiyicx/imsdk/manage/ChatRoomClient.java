package com.zhiyicx.imsdk.manage;

import android.content.Context;

import com.zhiyicx.imsdk.builder.MessageBuilder;
import com.zhiyicx.imsdk.core.ImService;
import com.zhiyicx.imsdk.core.autobahn.DataDealUitls;
import com.zhiyicx.imsdk.entity.AuthData;
import com.zhiyicx.imsdk.entity.ChatRoomContainer;
import com.zhiyicx.imsdk.entity.ChatRoomDataCount;
import com.zhiyicx.imsdk.entity.Conversation;
import com.zhiyicx.imsdk.entity.Message;
import com.zhiyicx.imsdk.entity.MessageType;
import com.zhiyicx.imsdk.manage.listener.ImMsgReceveListener;
import com.zhiyicx.imsdk.manage.listener.ImStatusListener;
import com.zhiyicx.imsdk.manage.listener.ImTimeoutListener;
import com.zhiyicx.imsdk.manage.soupport.ChatRoomSoupport;
import com.zhiyicx.imsdk.utils.common.LogUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Created by jungle on 16/7/29.
 * com.zhiyicx.imsdk.manage
 * zhibo_android
 * email:335891510@qq.com
 */
public class ChatRoomClient implements ChatRoomSoupport, ImMsgReceveListener, ImStatusListener, ImTimeoutListener {
    private static final String TAG = "ChatRoomClient";
    private int cid;
    private String ZBUSID = "";

    private Context mContext;
    private boolean rt = true;
    private ImMsgReceveListener mImMsgReceveListener;
    private ImStatusListener mImStatusListener;
    private ImTimeoutListener mImTimeoutListener;
    private String pwd = null;//进入房间的密钥
    private HashSet<Long> midCache = new HashSet<>();//用于消息去重
    private final int MAX_CACHE_SIZE = 500;
    private int msgid = 10000;//消息id,用于检测消息超时

    /**
     * 拉取丢失的消息
     */
    private static final int DEFAULT_SEQ = -1;
    //    private static int THE_FIRST_MESSAGE_SEQ = DEFAULT_SEQ;
    private int THE_NOW_MAX_SEQ = DEFAULT_SEQ;

    public ChatRoomClient(int roomId, String ZBUSID, Context mContext) {
        this.cid = roomId;
        this.ZBUSID = ZBUSID;
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
    public void sendTextMsg(String text) {
        sendMessage(MessageBuilder.createTextMessage(++msgid, cid, ZBUSID, text, false));
    }

    @Override
    public void sendGiftMessage(Map jsonstr) {
        sendMessage(MessageBuilder.createGiftMessage(++msgid, cid, ZBUSID, jsonstr, false));
    }

    /**
     * 发送自定义消息
     *
     * @param isEnable
     * @param jsonstr
     */
    @Override
    public void sendMessage(boolean isEnable, Map jsonstr, int customId) {

        sendMessage(MessageBuilder.createCustomMessage(++msgid, cid, ZBUSID, customId, isEnable, jsonstr, rt));
    }

    /**
     * 自己加入聊天室通知其他人
     */
    @Override
    public void sendJoinRoomMessage() {
        sendMessage(MessageBuilder.createCustomMessage(++msgid, cid, ZBUSID, MessageType.MESSAGE_CUSTOM_ID_JOIN_CHATROOM, true, null, false));
    }

    /**
     * 机器人加入聊天室通知其他人
     */
    @Override
    public void sendRobotJoinRoomMessage(String robotUsid) {
        sendMessage(MessageBuilder.createCustomMessage(++msgid, cid, robotUsid, MessageType.MESSAGE_CUSTOM_ID_JOIN_CHATROOM, true, null, false));
    }

    /**
     * 自己离开聊天室通知其他人
     */
    @Override
    public void sendLeaveRoomMessage() {
        sendMessage(MessageBuilder.createCustomMessage(++msgid, cid, ZBUSID, MessageType.MESSAGE_CUSTOM_ID_LEAVE_CHATROOM, true, null, rt));
    }

    @Override
    public void sendDataCountMessage(ChatRoomDataCount custom) {
        sendMessage(MessageBuilder.createCustomMessage(++msgid, cid, ZBUSID, MessageType.MESSAGE_CUSTOM_ID_DATACOUNT, true, DataDealUitls.transBean2Map(custom), rt));
    }

    /**
     * 赞
     *
     * @param type
     */
    @Override
    public void sendZan(int type) {
        sendMessage(MessageBuilder.createZanMessage(++msgid, cid, ZBUSID, type, rt));
    }

    /**
     * 关注主播
     */
    @Override
    public void sendAttention() {
        sendMessage(MessageBuilder.createAttentionMessage(++msgid, cid, ZBUSID, rt));
    }

    @Override
    public void sendMessage(Message message) {
        ZBIMClient.getInstance().sendMessage(message);
    }

    /**
     * 加入房间
     */
    @Override
    public void joinRoom() {
        ZBIMClient.getInstance().joinConversation(cid, pwd, ++msgid);
    }

    /**
     * 离开房间
     */
    @Override
    public void leaveRoom() {
        ZBIMClient.getInstance().leaveConversation(cid, pwd, ++msgid);
    }

    /**
     * 查询房间人员
     */
    @Override
    public void mc() {
        List<Integer> ids = new ArrayList<>();
        ids.add(cid);
        ZBIMClient.getInstance().mc(ids, "mc", ++msgid);//只查询房间当前人员数量
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
        if (dealRepeat(message)) return;
        if (checkSeq(message)) return;
        if (mImMsgReceveListener != null)
            mImMsgReceveListener.onMessageReceived(message);
    }

    /**
     * 消息去重
     */
    private boolean dealRepeat(Message message) {
        if (midCache.contains(message.mid))
            return true;
        if (midCache.size() > MAX_CACHE_SIZE)
            midCache.clear();
        midCache.add(message.mid);
        return false;
    }

    /**
     * 通过消息序号同步消息
     */
    private boolean checkSeq(Message message) {
        boolean result = false;
        int seq = message.seq;
        //记录收到的第一条消息
        if (THE_NOW_MAX_SEQ == DEFAULT_SEQ) {
            THE_NOW_MAX_SEQ = seq;
        } else {
            //如果本条消息的seq大于之前的seq，重新赋值最大的seq
            if (seq > THE_NOW_MAX_SEQ) {
                if (seq - THE_NOW_MAX_SEQ > 1) {
                    ZBIMClient.getInstance().syncAsc(cid, THE_NOW_MAX_SEQ, seq + 1, ++msgid);
                    System.out.println("-----请求seq--------" + THE_NOW_MAX_SEQ + "-----" + seq + 1);
                    result = true;
                }
                THE_NOW_MAX_SEQ = seq;

            }

        }
        return result;

    }

    /**
     * 同步几条消息，用于刚进入直播间时看看历史消息
     *
     * @param limit 多少条，最多{@link ImService.SEQ_LIMIT }
     */
    @Override
    public void synchronousInitiaMessage(int limit) {
        ZBIMClient.getInstance().syncLastMessage(cid, limit, ++msgid);
    }

    @Override
    public void onMessageACKReceived(Message message) {

        if (dealRepeat(message)) return;


        if (checkSeq(message)) return;
        if (mImMsgReceveListener != null)
            mImMsgReceveListener.onMessageACKReceived(message);
    }

    @Override
    public void onConversationJoinACKReceived(ChatRoomContainer chatRoomContainer) {
        if (chatRoomContainer.mChatRooms.get(0).cid == cid)
            sendJoinRoomMessage();
        if (mImMsgReceveListener != null)
            mImMsgReceveListener.onConversationJoinACKReceived(chatRoomContainer);
    }

    @Override
    public void onConversationLeaveACKReceived(ChatRoomContainer chatRoomContainer) {
        if (mImMsgReceveListener != null)
            mImMsgReceveListener.onConversationLeaveACKReceived(chatRoomContainer);
    }

    @Override
    public void onConversationMCACKReceived(List<Conversation> conversations) {
        if (mImMsgReceveListener != null)
            mImMsgReceveListener.onConversationMCACKReceived(conversations);
    }


    @Override
    public void onAuthSuccess(AuthData authData) {
        if (mImStatusListener != null)
            mImStatusListener.onAuthSuccess(authData);
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
        LogUtils.debugInfo(TAG, "timeout--发送消息超时----" + message.toString());
        if (mImTimeoutListener != null)
            mImTimeoutListener.onMessageTimeout(message);
    }

    @Override
    public void onConversationJoinTimeout(int roomId) {
        LogUtils.debugInfo(TAG, "timeout---加入房间超时- ----roomId = " + roomId);
        if (cid == roomId)
            joinRoom();//加入聊天室失败，尝试继续加入
        if (mImTimeoutListener != null)
            mImTimeoutListener.onConversationJoinTimeout(roomId);
    }

    @Override
    public void onConversationLeaveTimeout(int roomId) {
        LogUtils.debugInfo(TAG, "timeout---离开房间超时- ----roomId = " + roomId);
        if (mImTimeoutListener != null)
            mImTimeoutListener.onConversationLeaveTimeout(roomId);
    }

    @Override
    public void onConversationMcTimeout(List<Integer> roomIds) {
        LogUtils.debugInfo(TAG, "timeout---查询房间人数- ----roomIds = " + roomIds);
        if (mImTimeoutListener != null)
            mImTimeoutListener.onConversationMcTimeout(roomIds);
    }
}
