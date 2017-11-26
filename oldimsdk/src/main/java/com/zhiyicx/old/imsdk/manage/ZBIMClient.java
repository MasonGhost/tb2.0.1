package com.zhiyicx.old.imsdk.manage;

import android.os.Bundle;
import android.text.TextUtils;

import com.zhiyicx.old.imsdk.de.tavendo.autobahn.DataDealUitls;
import com.zhiyicx.old.imsdk.entity.EventContainer;
import com.zhiyicx.old.imsdk.entity.GiftMessage;
import com.zhiyicx.old.imsdk.entity.IMConfig;
import com.zhiyicx.old.imsdk.entity.Message;
import com.zhiyicx.old.imsdk.entity.MessageContainer;
import com.zhiyicx.old.imsdk.entity.MessageExt;
import com.zhiyicx.old.imsdk.entity.MessageType;
import com.zhiyicx.old.imsdk.manage.listener.ImMsgReceveListener;
import com.zhiyicx.old.imsdk.manage.listener.ImStatusListener;
import com.zhiyicx.old.imsdk.manage.listener.ImTimeoutListener;
import com.zhiyicx.old.imsdk.manage.soupport.IMSoupport;
import com.zhiyicx.old.imsdk.service.ImService;
import com.zhiyicx.old.imsdk.service.SocketService;
import com.zhiyicx.old.imsdk.utils.common.LogUtils;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Im
 * 用于im通信
 * Created by jungle on 16/7/6.
 * com.zhiyicx.old.imsdk.manage
 * zhibo_android
 * email:335891510@qq.com
 */
public class ZBIMClient implements IMSoupport {
    public static final String KEY_EVENTCONTAINER = "EventContainer";
    public static final String KEY_DISCONNECTED_CODE = "disconnected_code";
    public static final String KEY_DISCONNECTED_REASON = "disconnected_reason";
    public static final String KEY_CONNECTED_ERR = "connected_err";


    private volatile static ZBIMClient sZBIMClient;
    private List<ImMsgReceveListener> mImMsgReceveListener = new ArrayList<>();
    private List<ImStatusListener> mImStatusListener = new ArrayList<>();
    private List<ImTimeoutListener> mImTimeOutListener = new ArrayList<>();

    private int DEFAULT_MSGID = 20000;//当传入的消息id为0时，赋予默认值并自增长


    public void addImTimeoutListener(ImTimeoutListener imTimeoutListener) {
        mImTimeOutListener.add(imTimeoutListener);
    }

    public void removeImTimeoutListener(ImTimeoutListener imTimeoutListener) {
        mImTimeOutListener.remove(imTimeoutListener);
    }


    public void addImStatusListener(ImStatusListener imStatusListener) {
        mImStatusListener.add(imStatusListener);
    }

    public void removeImStatusListener(ImStatusListener imStatusListener) {
        mImStatusListener.remove(imStatusListener);
    }

    public void addImMsgReceveListener(ImMsgReceveListener imMsgReceveListener) {
        mImMsgReceveListener.add(imMsgReceveListener);
    }

    public void removeImMsgReceveListener(ImMsgReceveListener imMsgReceveListener) {
        mImMsgReceveListener.remove(imMsgReceveListener);
    }

    private ZBIMClient() {

        EventBus.getDefault().register(this);//注册eventbus
    }

    public static ZBIMClient getInstance() {

        if (sZBIMClient == null) {
            synchronized (ZBIMClient.class) {
                if (sZBIMClient == null) {
                    sZBIMClient = new ZBIMClient();
                }
            }
        }
        return sZBIMClient;
    }

    /**
     * IM登录
     *
     * @param imConfig
     */
    @Override
    public void login(IMConfig imConfig) {
        Bundle bundle = new Bundle();
        bundle.putInt(SocketService.EVENT_SOCKET_TAG, SocketService.TAG_IM_LOGIN);
        bundle.putSerializable(SocketService.BUNDLE_IMCONFIG, imConfig);
        toIMSocketService(bundle);
    }

    /**
     * IM登出
     */
    @Override
    public void loginOut() {
        Bundle bundle = new Bundle();
        bundle.putInt(SocketService.EVENT_SOCKET_TAG, SocketService.TAG_IM_LOGINOUT);
        toIMSocketService(bundle);
    }

    @Override
    public void sendTextMsg(String text, int cid, int msgid) {
        if (TextUtils.isEmpty(text)) return;
        Message msg = new Message();
        msg.cid = cid;
        msg.txt = text;
        msg.id = msgid;
        msg.type = MessageType.MESSAGE_TYPE_TEXT;
        sendMessage(msg);
    }

    @Override
    public void sendGiftMsg(int cid, int msgType, GiftMessage giftMessage, int msgid) {
        if (msgType < 128 || msgType > 199) {
            try {
                throw new Exception("msgtype越界");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        Message msg = new Message();
        msg.id = msgid;
        msg.cid = cid;
        msg.type = msgType;
        MessageExt ext = new MessageExt(null, DataDealUitls.transBean2Map(giftMessage));
        msg.ext = ext;
//        MessageExt ext = new MessageExt();
//        ext.gift = giftMessage;
//        msg.msg = new Gson().toJson(giftMessage);
        sendMessage(msg);
    }

    @Override
    public void sendZan(int cid, int msgType, int msgid) {
        if (msgType < 200 || msgType > 254) {
            try {
                throw new Exception("msgtype越界");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        Message msg = new Message();
        msg.id = msgid;
        msg.cid = cid;
        msg.type = msgType;//关注
        sendMessage(msg);

    }

    @Override
    public void sendAttention(int cid, int msgid) {
        Message msg = new Message();
        msg.id = msgid;
        msg.cid = cid;
        msg.type = MessageType.MESSAGE_CUSTOM_ID_FLLOW;//关注
        sendMessage(msg);

    }


    /**
     * IM进入房间
     *
     * @param roomId
     * @param pwd
     */
    @Override
    public void joinConversation(int roomId, String pwd, int msgid) {
        Bundle bundle = new Bundle();
        bundle.putInt(SocketService.EVENT_SOCKET_TAG, SocketService.TAG_IM_JOIN_CONVERSATION);
        bundle.putInt(SocketService.BUNDLE_ROOMID, roomId);
        bundle.putInt(SocketService.BUNDLE_MSG_ID, msgid);
        bundle.putString(SocketService.BUNDLE_CONVR_PWD, pwd);
        toIMSocketService(bundle);
    }

    /**
     * IM离开房间
     *
     * @param roomId
     * @param pwd
     */
    @Override
    public void leaveConversation(int roomId, String pwd, int msgid) {
        Bundle bundle = new Bundle();
        bundle.putInt(SocketService.EVENT_SOCKET_TAG, SocketService.TAG_IM_LEAVE_CONVERSATION);
        bundle.putInt(SocketService.BUNDLE_ROOMID, roomId);
        bundle.putInt(SocketService.BUNDLE_MSG_ID, msgid);
        bundle.putString(SocketService.BUNDLE_CONVR_PWD, pwd);
        toIMSocketService(bundle);
    }

    @Override
    public void mc(List<Integer> roomIds, String field, int msgid) {
        Bundle bundle = new Bundle();
        bundle.putInt(SocketService.EVENT_SOCKET_TAG, SocketService.TAG_IM_MC);
        bundle.putSerializable(SocketService.BUNDLE_ROOMIDS, (Serializable) roomIds);
        bundle.putInt(SocketService.BUNDLE_MSG_ID, msgid);
        bundle.putString(SocketService.BUNDLE_CONVERSATION_FIELD, field);
        toIMSocketService(bundle);
    }

    /**
     * 通过消息序号同步消息
     *
     * @param cid
     * @param seq
     */
    @Override
    public void pluck(int cid, List<Integer> seq, int msgid) {
        Bundle bundle = new Bundle();
        bundle.putInt(SocketService.EVENT_SOCKET_TAG, SocketService.TAG_IM_PLUCK);
        bundle.putInt(SocketService.BUNDLE_ROOMID, cid);
        bundle.putSerializable(SocketService.BUNDLE_MSG_SEQ, (Serializable) seq);
        bundle.putInt(SocketService.BUNDLE_MSG_ID, msgid);
        toIMSocketService(bundle);
    }

    /**
     * 通过消息序号同步消息
     *
     * @param cid
     * @param gt
     * @param lt
     */
    @Override
    public void sync(int cid, int gt, int lt, int msgid) {
        Bundle bundle = new Bundle();
        bundle.putInt(SocketService.EVENT_SOCKET_TAG, SocketService.TAG_IM_SYNC);
        bundle.putInt(SocketService.BUNDLE_ROOMID, cid);
        bundle.putInt(SocketService.BUNDLE_MSG_GT, gt);
        bundle.putInt(SocketService.BUNDLE_MSG_LT, lt);
        bundle.putInt(SocketService.BUNDLE_MSG_ID, msgid);
        toIMSocketService(bundle);
    }


    @Override
    public void reConnect() {
        Bundle bundle = new Bundle();
        bundle.putInt(SocketService.EVENT_SOCKET_TAG, SocketService.TAG_IM_RECONNECT);
        toIMSocketService(bundle);
    }

    /**
     * IM总的发消息
     *
     * @param message
     */
    @Override
    public void sendMessage(Message message) {
        doSendMessage(message);
    }

    private void doSendMessage(Message message) {
        if (message == null) return;
        if (message.id == 0)
            message.id = ++DEFAULT_MSGID;
        Bundle bundle = new Bundle();
        bundle.putInt(SocketService.EVENT_SOCKET_TAG, SocketService.TAG_IM_SEND_MESSAGE);
        bundle.putSerializable(SocketService.BUNDLE_MESSAGECONTAINER, new MessageContainer(ImService.MSG, message));
        toIMSocketService(bundle);
    }

    /**
     * 和imServie通信，发信息到Service
     *
     * @param bundle
     */
    private void toIMSocketService(Bundle bundle) {

        EventBus.getDefault().post(bundle
                , SocketService.EVENT_SOCKET_DEAL_MESSAGE);//通过eventbus发送

    }

    /**
     * 从IM service收到消息进行分发
     *
     * @param bundle
     */
    @Subscriber(tag = SocketService.EVENT_SOCKET_RECEIVE_MESSAGE, mode = ThreadMode.MAIN)
    private void receiveIMMessage(Bundle bundle) {


        EventContainer eventContainer = (EventContainer) bundle.getSerializable(KEY_EVENTCONTAINER);

        switch (eventContainer.mEvent) {//分发事件
            case ImService.MSG:
                if (eventContainer.mMessageContainer == null || eventContainer.mMessageContainer.msg == null)
                    break;

                if (mImMsgReceveListener != null) {
                    for (ImMsgReceveListener listener : mImMsgReceveListener) {
                        if (listener != null)
                            listener.onMessageReceived(eventContainer.mMessageContainer.msg);
                    }
                }

                break;
            case ImService.MSG_ACK:
                if (eventContainer.mMessageContainer == null || eventContainer.mMessageContainer.msg == null)
                    break;
                if (mImMsgReceveListener != null)
                    for (ImMsgReceveListener listener : mImMsgReceveListener) {
                        if (listener != null)
                            listener.onMessageACKReceived(eventContainer.mMessageContainer.msg);
                    }
                break;
            case ImService.CONVERSATION_JOIN_ACK:
                if (eventContainer.mChatRoomContainer == null) break;

                if (mImMsgReceveListener != null) {
                    for (ImMsgReceveListener listener : mImMsgReceveListener) {
                        if (listener != null)
                            listener.onConversationJoinACKReceived(eventContainer.mChatRoomContainer);
                    }
                }
                break;
            case ImService.CONVERSATION_LEAVE_ACK:
                if (eventContainer.mChatRoomContainer == null) break;
                if (mImMsgReceveListener != null) {
                    for (ImMsgReceveListener listener : mImMsgReceveListener) {
                        if (listener != null)
                            listener.onConversationLeaveACKReceived(eventContainer.mChatRoomContainer);
                    }
                }
                break;
            case ImService.GET_CONVERSATON_INFO:
                if (eventContainer.mConversations == null) break;
                if (mImMsgReceveListener != null) {
                    for (ImMsgReceveListener listener : mImMsgReceveListener) {
                        if (listener != null)
                            listener.onConversationMCACKReceived(eventContainer.mConversations);
                    }
                }
                break;

            case ImService.WEBSOCKET_CONNECTED:
                LogUtils.debugInfo("---------测试断线连接-------1----------WEBSOCKET_CONNECTED----------");
                if (mImStatusListener != null) {
                    for (ImStatusListener listener : mImStatusListener) {
                        if (listener != null)
                            listener.onConnected();
                    }
                }
                break;
            case ImService.WEBSOCKET_DISCONNECTED:
                LogUtils.debugInfo("---------测试断线连接-------2----------WEBSOCKET_DISCONNECTED----------");
                if (mImStatusListener != null) {
                    for (ImStatusListener listener : mImStatusListener) {
                        if (listener != null)
                            listener.onDisconnect(bundle.getInt(KEY_DISCONNECTED_CODE), bundle.getString(KEY_DISCONNECTED_REASON));
                    }
                }
                break;
            case ImService.WEBSOCKET_CONNECTED_ERR:
                if (mImStatusListener != null) {
                    for (ImStatusListener listener : mImStatusListener) {
                        if (listener != null)
                            listener.onError((Exception) bundle.getSerializable(KEY_CONNECTED_ERR));
                    }
                }
                break;
            /**
             * 发送消息超时
             */
            case ImService.WEBSOCKET_SENDMESSAGE_TIMEOUT:
                if (mImTimeOutListener != null) {
                    for (ImTimeoutListener listener : mImTimeOutListener) {
                        if (listener != null)
                            listener.onMessageTimeout(eventContainer.mMessageContainer.msg);
                    }
                }
                break;
            /**
             * 加入聊天室超时
             */
            case ImService.CONVERSATION_JOIN:
                if (mImTimeOutListener != null) {
                    for (ImTimeoutListener listener : mImTimeOutListener) {
                        if (listener != null)
                            listener.onConversationJoinTimeout(eventContainer.mMessageContainer.roomId);
                    }
                }
                break;

            /**
             * 离开聊天室超时
             */
            case ImService.CONVERSATION_LEAVE:
                if (mImTimeOutListener != null) {
                    for (ImTimeoutListener listener : mImTimeOutListener) {
                        if (listener != null)
                            listener.onConversationLeaveTimeout(eventContainer.mMessageContainer.roomId);
                    }
                }
                break;

            /**
             * 查询聊天室成员超时
             */
            case ImService.GET_CONVERSATON_INFO_TIMEOUT:
                if (mImTimeOutListener != null) {
                    for (ImTimeoutListener listener : mImTimeOutListener) {
                        if (listener != null)
                            listener.onConversationMcTimeout(eventContainer.mMessageContainer.roomIds);
                    }
                }
                break;
            /**
             * 获取指定序号消息超时
             */
            case ImService.CONVR_MSG_PLUCK:

                break;

            default:
                break;
        }

    }

}
