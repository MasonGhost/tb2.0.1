package com.zhiyicx.zhibosdk.manage;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.zhiyicx.imsdk.db.dao.ConversationDao;
import com.zhiyicx.imsdk.db.dao.MessageDao;
import com.zhiyicx.imsdk.entity.AuthData;
import com.zhiyicx.imsdk.entity.ChatRoomContainer;
import com.zhiyicx.imsdk.entity.Conversation;
import com.zhiyicx.imsdk.entity.Message;
import com.zhiyicx.imsdk.entity.MessageExt;
import com.zhiyicx.imsdk.manage.ChatClient;
import com.zhiyicx.imsdk.manage.listener.ImMsgReceveListener;
import com.zhiyicx.imsdk.manage.listener.ImStatusListener;
import com.zhiyicx.imsdk.manage.listener.ImTimeoutListener;
import com.zhiyicx.zhibosdk.manage.listener.OnChatCreateListener;
import com.zhiyicx.zhibosdk.manage.soupport.ChatSoupport;
import com.zhiyicx.zhibosdk.model.api.ZBApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import rx.functions.Action1;

/**
 * Created by jungle on 16/8/22.
 * com.zhiyicx.zhibosdk.manage
 * zhibo_android
 * email:335891510@qq.com
 */
public class ZBChatClient implements ChatSoupport, ImMsgReceveListener, ImStatusListener, ImTimeoutListener {
    private Context mContext;
    private ChatClient mChatClient;
    private volatile static ZBChatClient sPrivateChatClient;

    private ImMsgReceveListener mImMsgReceveListener;

    private ImStatusListener mImStatusListener;

    private ImTimeoutListener mImTimeoutListener;

    public void setImMsgReceveListener(ImMsgReceveListener imMsgReceveListener) {
        mImMsgReceveListener = imMsgReceveListener;
    }

    public void setImStatusListener(ImStatusListener imStatusListener) {
        mImStatusListener = imStatusListener;
    }

    public void setImTimeoutListener(ImTimeoutListener imTimeoutListener) {
        mImTimeoutListener = imTimeoutListener;
    }

    private ZBChatClient(Context context) {
        this.mContext = context.getApplicationContext();
        mChatClient = ChatClient.getInstance(mContext);
        mChatClient.setImStatusListener(this);
        mChatClient.setImMsgReceveListener(this);
        mChatClient.setImTimeoutListener(this);
    }

    public static ZBChatClient getInstance(Context context) {

        if (sPrivateChatClient == null) {
            synchronized (ZBChatClient.class) {
                if (sPrivateChatClient == null) {
                    sPrivateChatClient = new ZBChatClient(context);
                }
            }
        }
        return sPrivateChatClient;
    }

    /**
     * 创建私有对话
     *
     * @param usid
     * @param l
     */
    @Override
    public void createPrivateConversation(String usid, final OnChatCreateListener l) {

        createPrivateConvr(usid, l);

    }

    /**
     * 创建群组对话
     *
     * @param usids
     * @param name
     * @param pwd
     * @param l
     */
    @Override
    public void createTeamConversation(String usids, String name, String pwd, final OnChatCreateListener l) {

        createTeamConvr(usids, name, pwd, l);


    }


    /**
     * 获取除了聊天室以外的所有对话
     *
     * @param page 分页，从第一页开始(page=1)
     * @return
     */
    @Override
    public List<Conversation> getChatConversations(int page) {


        return findConversations(page);
    }

    /**
     * 通过cid获取对话消息
     *
     * @param cid
     * @param page
     * @return
     */
    @Override
    public List<Message> getMessagesByCid(int cid, int page) {
        return findMessagesByCid(cid, page);
    }

    @Override
    public boolean readMessage(long mid) {
        return updateRead(mid);
    }


    @Override
    public boolean delMessage(long mid) {
        return updateDel(mid);
    }


    @Override
    public void sendTextMsg(String text, int cid, String usid, int msgid) {
        mChatClient.sendTextMsg(text, cid, usid);
    }

    @Override
    public void sendCustomMessage(boolean isEnable, Map jsonstr, int cid, String usid, int customId, int msgid) {
        mChatClient.sendMessage(isEnable, jsonstr, cid, usid, customId);
    }

    @Override
    public void sendCustomMessage(boolean isEnable, int cid, MessageExt ext, int msgid) {

    }

    @Override
    public void sendEnableCustomMessage(int cid, MessageExt ext, int msgid) {

    }

    @Override
    public void sendDisableCustomMessage(int cid, MessageExt ext, int msgid) {

    }

    @Override
    public void onMessageReceived(Message message) {
        if (mImMsgReceveListener != null)
            mImMsgReceveListener.onMessageReceived(message);

    }

    @Override
    public void onMessageACKReceived(Message message) {
        if (mImMsgReceveListener != null)
            mImMsgReceveListener.onMessageACKReceived(message);

    }

    @Override
    public void onConversationJoinACKReceived(ChatRoomContainer chatRoomContainer) {
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
    public void synchronousInitiaMessage(int limit) {

    }

    @Override
    public void onAuthSuccess(AuthData authData) {

    }

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

    @Override
    public void onMessageTimeout(Message message) {
        if (mImTimeoutListener != null)
            mImTimeoutListener.onMessageTimeout(message);
    }

    @Override
    public void onConversationJoinTimeout(int roomId) {
        if (mImTimeoutListener != null)
            mImTimeoutListener.onConversationJoinTimeout(roomId);
    }

    @Override
    public void onConversationLeaveTimeout(int roomId) {
        if (mImTimeoutListener != null)
            mImTimeoutListener.onConversationLeaveTimeout(roomId);
    }

    @Override
    public void onConversationMcTimeout(List<Integer> roomIds) {

        if (mImTimeoutListener != null)
            mImTimeoutListener.onConversationMcTimeout(roomIds);
    }

    private List<Conversation> findConversations(int page) {
        return ConversationDao.getInstance(mContext).getConversationList(page);
    }

    private List<Message> findMessagesByCid(int cid, int page) {
        return MessageDao.getInstance(mContext).getMessageListByCid(cid, page);
    }

    private boolean updateRead(long mid) {
        return MessageDao.getInstance(mContext).readMessage(mid);
    }

    private boolean updateDel(long mid) {
        return MessageDao.getInstance(mContext).delMessage(mid);
    }

    private void createPrivateConvr(final String usid, final OnChatCreateListener l) {
        ZBConversationManager.getInstance().createPrivateChatForRx(usid).subscribe(new Action1<JsonObject>() {
            @Override
            public void call(JsonObject result) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result.toString());


                    String a = jsonObject.get("code").toString();
                    System.out.println("a = " + a);
                    if (jsonObject.get("code").toString().equals(ZBApi.REQUEST_SUCESS)) {
                        if (l != null) {
                            l.onSuccess();
                            Conversation conversation = new Gson().fromJson(jsonObject.getString("data"), Conversation.class);
                            conversation.setType(Conversation.CONVERSATION_TYPE_PRIVATE);
                            conversation.setUsids(usid);
                            conversation.setLast_message_time(System.currentTimeMillis());
                            /**
                             * 创建本地会话
                             */
                            ConversationDao.getInstance(mContext).insertConversation(conversation);

                            l.onSuccess(conversation);
                        }
                    } else {
                        if (l != null)
                            l.onFail(jsonObject.get("code").toString(), jsonObject.get("message").toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if (l != null)
                        l.onError(e);
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                if (l != null)
                    l.onError(throwable);
            }
        });
    }

    private void createTeamConvr(final String usids, String name, String pwd, final OnChatCreateListener l) {
        ZBConversationManager.getInstance().createTeamChatForRx(usids, name, pwd).subscribe(new Action1<JsonObject>() {
            @Override
            public void call(JsonObject result) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result.toString());


                    if (jsonObject.get("code").equals(ZBApi.REQUEST_SUCESS)) {
                        if (l != null) {
                            l.onSuccess();
                            Conversation conversation = new Gson().fromJson(jsonObject.getString("data"), Conversation.class);
                            conversation.setType(Conversation.CONVERSATION_TYPE_TEAM);
                            conversation.setUsids(usids);
                            conversation.setLast_message_time(System.currentTimeMillis());
                            /**
                             * 创建本地会话
                             */
                            ConversationDao.getInstance(mContext).insertConversation(conversation);

                            l.onSuccess(conversation);
                        }

                    } else {
                        if (l != null)
                            l.onFail(jsonObject.get("code").toString(), jsonObject.get("message").toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if (l != null)
                        l.onError(e);
                }

            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                if (l != null)
                    l.onError(throwable);
            }
        });
    }
}
