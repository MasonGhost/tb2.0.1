package com.zhiyicx.old.imsdk.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhiyicx.old.imsdk.db.base.BaseDao;
import com.zhiyicx.old.imsdk.db.dao.ConversationDao;
import com.zhiyicx.old.imsdk.db.dao.MessageDao;
import com.zhiyicx.old.imsdk.de.tavendo.autobahn.WebSocket;
import com.zhiyicx.old.imsdk.entity.ChatRoom;
import com.zhiyicx.old.imsdk.entity.ChatRoomContainer;
import com.zhiyicx.old.imsdk.entity.ChatRoomErr;
import com.zhiyicx.old.imsdk.entity.Conver;
import com.zhiyicx.old.imsdk.entity.Conversation;
import com.zhiyicx.old.imsdk.entity.EventContainer;
import com.zhiyicx.old.imsdk.entity.IMConfig;
import com.zhiyicx.old.imsdk.entity.Message;
import com.zhiyicx.old.imsdk.entity.MessageContainer;
import com.zhiyicx.old.imsdk.entity.MessageType;
import com.zhiyicx.old.imsdk.manage.ZBIMClient;
import com.zhiyicx.old.imsdk.policy.timeout.TimeOutListener;
import com.zhiyicx.old.imsdk.policy.timeout.TimeOutTask;
import com.zhiyicx.old.imsdk.policy.timeout.TimeOutTaskManager;
import com.zhiyicx.old.imsdk.policy.timeout.TimeOutTaskPool;
import com.zhiyicx.old.imsdk.utils.MessageHelper;
import com.zhiyicx.old.imsdk.utils.common.DeviceUtils;
import com.zhiyicx.old.imsdk.utils.common.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.msgpack.MessagePack;
import org.msgpack.template.Templates;
import org.msgpack.type.Value;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by jungle on 16/7/6.
 */
public class SocketService extends BaseService implements ImService.ImListener {
    private final String TAG = "old"+this.getClass().getSimpleName();

    public static final int TAG_IM_LOGIN = 10000;//登录im
    public static final int TAG_IM_LOGINOUT = 10001;//断开im
    public static final int TAG_IM_JOIN_CONVERSATION = 10002;//进入房间
    public static final int TAG_IM_LEAVE_CONVERSATION = 10003;//离开房间
    public static final int TAG_IM_SEND_MESSAGE = 10004;//发送消息
    public static final int TAG_IM_MC = 10005;//查看房间人数
    public static final int TAG_IM_RECONNECT = 10006;//尝试重连
    public static final int TAG_IM_PLUCK = 10007;//获取指定序号消息
    public static final int TAG_IM_SYNC = 10008;//获取指定序号消息

    public static final String BUNDLE_MESSAGECONTAINER = "messageContainer";
    public static final String BUNDLE_IMCONFIG = "imConfig";
    public static final String BUNDLE_ROOMID = "room_id";
    public static final String BUNDLE_ROOMIDS = "room_ids";
    public static final String BUNDLE_MSG_ID = "message_id";
    public static final String BUNDLE_CONVERSATION_FIELD = "conversation_field";
    public static final String BUNDLE_CONVR_PWD = "convr_pwd";
    public static final String BUNDLE_MSG_SEQ = "message_seq";
    public static final String BUNDLE_MSG_GT = "gt";
    public static final String BUNDLE_MSG_LT = "lt";
    public static final String BUNDLE_MSG_LIMIT = "limit";


    /**
     * Im错误事件回执码
     */
    public static final int SUCCESS_CODE = 0;//通信成功
    public static final int SERVER_EXCEPTION = 1000;//服务器发生了未知的异常，导致处理中断
    public static final int PACKET_EXCEPTION_ERR_DATA = 1010;//无效的数据包
    public static final int PACKET_EXCEPTION_ERR_PACKET_TYPE = 1011;//无效的数据包类型
    public static final int PACKET_EXCEPTION_ERR_BODY_TYPE = 1012;//无效的消息主体
    public static final int PACKET_EXCEPTION_ERR_SERILIZE_TYPE = 1013;//无效的序列化类型
    public static final int PACKET_EXCEPTION_ERR_KEY_TYPE = 1014;//无效的键名路由

    public static final int AUTH_FAILED_NO_UID_OR_PWD = 1020;//未提供认证需要的uid和pwd
    public static final int AUTH_FAILED_ERR_UID_OR_PWD = 1021;//认证失败，可能原因是uid不存在或密码错误，或账号已被禁用

    public static final int CHATROOM_JOIN_FAILED = 2001;//对话加入失败
    public static final int CHATROOM_LEAVE_FAILED = 2002;//对话离开失败
    public static final int CHATROOM_MC_FAILED = 2003;//对话成员查询失败

    public static final int CHATROOM_SEND_MESSAGE_FAILED = 3001;//消息发送失败
    public static final int CHATROOM_BANNED_WORDS = 3004;//对话成员被禁言
    public static final int CHATROOM_NOT_JOIN_ROOM = 3003;//未加入房间


    public static final String SOCKET_RETRY_CONNECT = "com.zhiyicx.old.zhibo_socket_retry_connect";
    public static final String EVENT_SOCKET_DEAL_MESSAGE = "old_event_socket_deal_message";//分发处理消息
    public static final String EVENT_SOCKET_RECEIVE_MESSAGE = "old_event_socket_receive_message";//分发处理消息
    public static final String EVENT_SOCKET_TAG = "old_event_socket_tag";

    /**
     * 心跳维持时常
     */
    private long HEART_BEAT_RATE = HEART_BEAT_RATE_MIDDLE;
    private static final long HEART_BEAT_RATE_MAX_PING = 250 * 1000;
    private static final long HEART_BEAT_RATE_MAX = 70 * 1000;
    private static final long HEART_BEAT_RATE_HEIGHT = 110 * 1000;
    private static final long HEART_BEAT_RATE_MIDDLE = 170 * 1000;
    private static final long HEART_BEAT_RATE_LOW = 230 * 1000;
    private static final long HEART_BEAT_RATE_HEIGHT_BACKGROUND = 130 * 1000;
    private static final long HEART_BEAT_RATE_MIDDLE_BACKGROUND = 190 * 1000;
    private static final long HEART_BEAT_RATE_LOW_BACKGROUND = HEART_BEAT_RATE_MAX_PING;


    private static final long HEART_PING_PONG_RATE = 10 * 1000;//间隔10s重连,10秒没有收到服务器回应
    private static final long DISCONNECT_NOTIFY_TIME = 10 * 1000;//IM超过10s没有连上，通知下发
    private static final long SLEEP_TIME = 6 * 1000;//线程沉睡时间,防止占用cpu过高
    private long disconnect_start_time = 0;//重连开始时间


    private ImService mService;
    private Context mContext;
    private long sendTime = 0L;
    private long responsTime = 0L;
    private boolean isBackground = false;//判断应用处于前台还是后台
    private boolean isDisconnecting = false;

    // For heart Beat
    private Thread mThread;

    private Thread mSendMessageThread;
    public volatile boolean exit = false;//终止线程
    private SocketRetryReceiver mSocketRetryReceiver;
    private TimeOutTaskPool timeOutTaskPool;

    public boolean isNeedReConnected() {
        return isNeedReConnected;
    }

    public void setNeedReConnected(boolean needReConnected) {
        isNeedReConnected = needReConnected;
    }

    private boolean isNeedReConnected = true;//是否需要重连
    private boolean connected = false;//当前连接状态
    private int send_serilize_type = ImService.BIN_MSGPACK;//向服务器发送的数据类型

    private Queue<MessageContainer> mMessageContainers = new ConcurrentLinkedQueue<>();//线程安全的队列
    private Map<Integer, TimeOutTask> timeTasks = new HashMap<>();

    private Map<Integer, EventContainer> mEventContainerCache = new HashMap<>();

    private Runnable heartBeatRunnable = new Runnable() {

        @Override
        public void run() {
            while (!exit) {
                if (isNeedReConnected) {
                    /**
                     * ping后或者发送普通消息5s收不到回应则重连
                     */
                    if (System.currentTimeMillis() - sendTime > HEART_PING_PONG_RATE && sendTime > responsTime) {
                        socketReconnect();
                        continue;
                    }
                    /**
                     *发送心跳包与超时重连
                     */
                    if (System.currentTimeMillis() - sendTime > HEART_BEAT_RATE && System.currentTimeMillis() - responsTime > HEART_BEAT_RATE) {
                        if (DeviceUtils.netIsConnected(mContext) && mService.isConnected()) {
                            if (DeviceUtils.isBackground(mContext) != isBackground) {
                                isBackground = !isBackground;
                                changeHeartBeatRate();
                            }
                            resetTime();
                            mService.ping();
                            LogUtils.debugInfo(TAG, "----------ping-------");
                        }
                        else {
                            socketReconnect();
                        }
                    }
                    else {
                        //防止cpu占用过高
                        try {
                            Thread.sleep(4000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }


                }
            }
        }
    };

    /**
     * 根据软件在前台还是后台，以及当前的网络状况，修改心跳的频率
     */
    private void changeHeartBeatRate() {
        if (DeviceUtils.isBackground(mContext)) {
            switch (DeviceUtils.getNetworkType(mContext)) {
                case DeviceUtils.NETTYPE_WIFI:
                    HEART_BEAT_RATE = HEART_BEAT_RATE_LOW_BACKGROUND;
                    break;
                case DeviceUtils.NETTYPE_CMNET:
                    HEART_BEAT_RATE = HEART_BEAT_RATE_MIDDLE_BACKGROUND;
                    break;
                case DeviceUtils.NETTYPE_CMWAP:
                    HEART_BEAT_RATE = HEART_BEAT_RATE_HEIGHT_BACKGROUND;
                    break;
                default:
                    break;
            }
        }
        else {
            switch (DeviceUtils.getNetworkType(mContext)) {
                case DeviceUtils.NETTYPE_WIFI:
                    HEART_BEAT_RATE = HEART_BEAT_RATE_LOW;
                    break;
                case DeviceUtils.NETTYPE_CMNET:
                    HEART_BEAT_RATE = HEART_BEAT_RATE_MIDDLE;
                    break;
                case DeviceUtils.NETTYPE_CMWAP:
                    HEART_BEAT_RATE = HEART_BEAT_RATE_HEIGHT;
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 智能心跳
     * 每重连一次，重连间隔时间-10
     */
    private void changeHeartBeatRateByReconnect() {

        if (HEART_BEAT_RATE > HEART_BEAT_RATE_MAX)
            HEART_BEAT_RATE = HEART_BEAT_RATE - 10 * 1000;
    }

    /**
     * 消息发送
     */
    private Runnable messageRunnable = new Runnable() {
        @Override
        public void run() {
            while (!exit) {
                if (!mMessageContainers.isEmpty()) {
                    MessageContainer messageContainer = mMessageContainers.poll();

                    if (messageContainer != null) {
                        if (messageContainer.msg.ext.customID != MessageType.MESSAGE_CUSTOM_ID_ZAN)//点赞不处理超时
                            setTimeoutTask(messageContainer);
                        sendMessage(messageContainer);
                    }

                }
                //防止cpu占用过高
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    };

    private void setTimeoutTask(MessageContainer messageContainer) {
        TimeOutTask timeOutTask = new TimeOutTask(messageContainer, System.currentTimeMillis(), new TimeOutListener() {
            @Override
            public void timeOut(MessageContainer messageContainer) {
                sendTimeOutMsg(messageContainer);
            }
        });
        timeTasks.put(messageContainer.msg.id, timeOutTask);
        TimeOutTaskManager.getInstance().addTimeoutTask(timeOutTask);
    }

    /**
     * 发送信息超时
     *
     * @param messageContainer
     */
    private void sendTimeOutMsg(MessageContainer messageContainer) {
        EventContainer eventContainer = new EventContainer();
        eventContainer.mMessageContainer = messageContainer;
        eventContainer.mEvent = messageContainer.mEvent;
        if (messageContainer.mEvent.equals(ImService.MSG)) {
            eventContainer.mEvent = ImService.WEBSOCKET_SENDMESSAGE_TIMEOUT;
        }
        sendImBroadCast(eventContainer);
    }


    /**
     * 发送消息时的时间
     */
    private void resetTime() {
        sendTime = System.currentTimeMillis();
    }

    /**
     * 服务器消息回应时间
     */
    private void responseTime() {
        responsTime = System.currentTimeMillis();
    }


    @Override
    public void init() {
        mContext = getApplicationContext();
        mService = new ImService();
        initSocketListener();
        LogUtils.debugInfo(TAG,"---init---");
    }

    /**
     * 初始化socket回调
     */
    private void initSocketListener() {
        mService.setListener(this);

    }

    /**
     * im建立连接
     *
     * @param imConfig
     */
    private void login(IMConfig imConfig) {
        LogUtils.debugInfo("----imlogin----------------"+imConfig.toString());
        isNeedReConnected = true;
        mService.setParams(imConfig.getWeb_socket_authority(), imConfig.getToken(),
                imConfig.getSerial(), imConfig.getComprs());
        mThread = new Thread(heartBeatRunnable);
        /**
         * 开始维持心跳
         */
        mThread.start();
        /**
         * 发送消息线程
         */
        mSendMessageThread = new Thread(messageRunnable);
        mSendMessageThread.start();
        //并启动TimeOut线程池
        timeOutTaskPool = new TimeOutTaskPool();
        new Thread(timeOutTaskPool).start();
        registerAction(SOCKET_RETRY_CONNECT);
    }

    /**
     * 注册网络重连广播
     *
     * @param action
     */
    public void registerAction(String action) {
        mSocketRetryReceiver = new SocketRetryReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(action);
        registerReceiver(mSocketRetryReceiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mService.disconnect();
        isDisconnecting = true;
        timeOutTaskPool.setStop(true);
        mService = null;
        exit = true;

        unregisterReceiver(mSocketRetryReceiver);
    }

    @Override
    public void onConnected() {
        disconnect_start_time = 0;
        responseTime();
        sendConnectedMsg();
        connected = true;
        LogUtils.debugInfo(TAG, "--------connected---------");
    }

    private void sendConnectedMsg() {
        EventContainer eventContainer = new EventContainer();
        eventContainer.mEvent = ImService.WEBSOCKET_CONNECTED;
        sendImBroadCast(eventContainer);
    }

    /**
     * 文本消息，暂未使用
     *
     * @param message
     */
    @Override
    public void onMessage(String message) {
        responseTime();

    }

    /**
     * 二进制消息
     *
     * @param data
     */
    @Override
    public void onMessage(byte[] data) {
        responseTime();
        checkDataType(data);
    }

    @Subscriber(tag = EVENT_SOCKET_DEAL_MESSAGE, mode = ThreadMode.ASYNC)
    public void dealMessage(Bundle bundle) {
        if (bundle == null) return;
        try {
            switch (bundle.getInt(EVENT_SOCKET_TAG)) {
                /**
                 * IM登录
                 */
                case TAG_IM_LOGIN:
                    login((IMConfig) bundle.getSerializable(BUNDLE_IMCONFIG));
                    break;
                /**
                 * IM登出
                 */
                case TAG_IM_LOGINOUT:
                    disConnect();
                    break;
                /**
                 * 重连
                 */
                case TAG_IM_RECONNECT:
                    connect();
                    break;
                /**
                 * 加入对话
                 */
                case TAG_IM_JOIN_CONVERSATION:
                    join(bundle.getInt(BUNDLE_ROOMID), bundle.getInt(BUNDLE_MSG_ID), bundle.getString(BUNDLE_CONVR_PWD));
                    break;
                /**
                 * 加入消息队列
                 */
                case TAG_IM_SEND_MESSAGE:

                    mMessageContainers.add((MessageContainer) bundle.getSerializable(BUNDLE_MESSAGECONTAINER));
                    break;
                /***
                 * 离开会话
                 */
                case TAG_IM_LEAVE_CONVERSATION:
                    leave(bundle.getInt(BUNDLE_ROOMID), bundle.getInt(BUNDLE_MSG_ID), bundle.getString(BUNDLE_CONVR_PWD));
                    break;

                /**
                 * 查询会话消息
                 */
                case TAG_IM_MC:
                    mc((List<Integer>) bundle.getSerializable(BUNDLE_ROOMIDS), bundle.getInt(BUNDLE_MSG_ID), bundle.getString(BUNDLE_CONVERSATION_FIELD));
                    break;
                /**
                 * 通过消息序号同步消息
                 */
                case TAG_IM_PLUCK:
                    sendPluckMessage(bundle.getInt(BUNDLE_ROOMID), (List<Integer>) bundle.getSerializable(BUNDLE_MSG_SEQ), bundle.getInt(BUNDLE_MSG_ID));
                    break;
                /**
                 * 通过消息序号同步消息
                 */
                case TAG_IM_SYNC:
                    sendSyncMessage(bundle.getInt(BUNDLE_ROOMID), bundle.getInt(BUNDLE_MSG_GT), bundle.getInt(BUNDLE_MSG_LT, 0), bundle.getInt(BUNDLE_MSG_ID));
                    break;


                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 向IM服务器发送msgpack类型数据
     */
    public void sendMessage(MessageContainer messageContainer) {
        if (!messageContainer.msg.rt)
            resetTime();
        if (!DeviceUtils.netIsConnected(getApplicationContext()) || !connected) return;
        checkMessageType(messageContainer);
        switch (send_serilize_type) {

            case ImService.BIN_JSON:

                mService.sendJsonData(new Gson().toJson(messageContainer.msg), messageContainer.mEvent, messageContainer.msg.id);
                break;

            case ImService.BIN_MSGPACK:

                mService.sendMsgpackData(messageContainer);

                break;
        }


    }

    /**
     * 检测消息类型，并设置消息文本默认值
     *
     * @param messageContainer
     */
    private void checkMessageType(MessageContainer messageContainer) {
        switch (messageContainer.msg.type) {
            case MessageType.MESSAGE_TYPE_AUDIO:
                setDefaultTxt(messageContainer, MessageType.MESSAGE_TYPE_AUDIO_TXT);
                break;
            case MessageType.MESSAGE_TYPE_VIDEO:
                setDefaultTxt(messageContainer, MessageType.MESSAGE_TYPE_VIDEO_TXT);
                break;
            case MessageType.MESSAGE_TYPE_IMAGE:
                setDefaultTxt(messageContainer, MessageType.MESSAGE_TYPE_IMAGE_TXT);
                break;
            case MessageType.MESSAGE_TYPE_LOCATION:
                setDefaultTxt(messageContainer, MessageType.MESSAGE_TYPE_LOCATION_TXT);
                break;
            case MessageType.MESSAGE_TYPE_FILE:
                setDefaultTxt(messageContainer, MessageType.MESSAGE_TYPE_FILE_TXT);
                break;
            case MessageType.MESSAGE_TYPE_CUSTOM_DISABLE:
            case MessageType.MESSAGE_TYPE_CUSTOM_ENAABLE:
                setDefaultTxt(messageContainer, MessageType.MESSAGE_TYPE_CUSTOM_TXT);
                break;
            default:
                setDefaultTxt(messageContainer, MessageType.MESSAGE_TYPE_CUSTOM_TXT);
                break;
        }
    }

    /**
     * 设置默认文本
     *
     * @param messageContainer
     * @param txt
     */
    private void setDefaultTxt(MessageContainer messageContainer, String txt) {
        if (TextUtils.isEmpty(messageContainer.msg.txt))
            messageContainer.msg.txt = txt;
    }

    /**
     * 加入会话
     *
     * @param id
     * @param msgid
     * @param pwd
     */
    public void join(int id, int msgid, String pwd) {

        setTimeoutTask(new MessageContainer(ImService.CONVERSATION_JOIN, new Message(msgid), id, null));
        mService.joinConversation(id, msgid, pwd);
        resetTime();
    }

    /**
     * 离开会话
     *
     * @param id
     * @param msgid
     * @param pwd
     */
    public void leave(int id, int msgid, String pwd) {
        setTimeoutTask(new MessageContainer(ImService.CONVERSATION_LEAVE, new Message(msgid), id, null));
        mService.leaveConversation(id, msgid, pwd);
        resetTime();
    }

    public void mc(List<Integer> id, int msgid, String field) {
        setTimeoutTask(new MessageContainer(ImService.GET_CONVERSATON_INFO_TIMEOUT, new Message(msgid), 0, id));
        mService.sendGetConversatonInfo(id, field);
        resetTime();
    }

    public void sendPluckMessage(int cid, List<Integer> seq, int msgid) {
        setTimeoutTask(new MessageContainer(ImService.CONVR_MSG_PLUCK, new Message(msgid), cid, null));
        mService.sendPluckMessage(cid, seq, msgid);
        resetTime();
    }

    public void sendSyncMessage(int cid, int gt, int lt, int msgid) {
        setTimeoutTask(new MessageContainer(ImService.CONVR_MSG_SYNC, new Message(msgid), cid, null));
        mService.sendSyncMessage(cid, gt, lt, 100, msgid);
        resetTime();
    }

    /**
     * 检测消息类型
     *
     * @param data
     */
    private void checkDataType(byte[] data) {
        if (data == null) return;
        switch (MessageHelper.getPackageType(data)) {

            case MessageHelper.TITLE_TYPE_PING:
                LogUtils.debugInfo(TAG, "----packgetype = " + "TITLE_TYPE_PING");
                break;
            case MessageHelper.TITLE_TYPE_PONG:
                LogUtils.debugInfo(TAG, "----packgetype = " + "TITLE_TYPE_PONG");
                break;
            case MessageHelper.TITLE_TYPE_MESSAGE:
                LogUtils.debugInfo(TAG, "----packgetype = " + "TITLE_TYPE_MESSAGE");
                dealMessage(data, MessageHelper.TITLE_TYPE_MESSAGE);
                break;
            case MessageHelper.TITLE_TYPE_MESSAGE_ACK:
                LogUtils.debugInfo(TAG, "----packgetype = " + "TITLE_TYPE_MESSAGE_ACK");
                dealMessage(data, MessageHelper.TITLE_TYPE_MESSAGE_ACK);
                break;
            case MessageHelper.TITLE_TYPE_MESSAGE_ERR_ACK:
                LogUtils.debugInfo(TAG, "----packgetype = " + "TITLE_TYPE_MESSAGE_ERR_ACK");
                dealMessage(data, MessageHelper.TITLE_TYPE_MESSAGE_ERR_ACK);
                break;
            default:
                LogUtils.debugInfo(TAG, "packgetype = " + "unkonw");
                break;

        }
    }

    /**
     * 处理消息
     *
     * @param data
     */
    private void dealMessage(byte[] data, int title_type) {
        EventContainer eventContainer = new EventContainer();
        switch (MessageHelper.getPackageSerilType(data)) {

            case MessageHelper.SERILIZE_TYPE_JSON:
                LogUtils.debugInfo(TAG, "ser = " + "json");
                eventContainer = parseJsonData(data, eventContainer, title_type);
                break;
            case MessageHelper.SERILIZE_TYPE_MSGPACK:
                LogUtils.debugInfo(TAG, "ser = " + "msgpack");
                eventContainer = parseMsgpackData(data, eventContainer, title_type);
                break;
            default:
                LogUtils.debugInfo(TAG, "ser = " + "unkonw");
                break;
        }
        if (checkData(eventContainer))
            sendImBroadCast(eventContainer);
    }

    /**
     * 检查socket错误信息
     *
     * @param eventContainer
     * @return
     */
    private boolean checkData(EventContainer eventContainer) {
        if (eventContainer == null) return false;
        switch (eventContainer.err) {
            case SERVER_EXCEPTION:
                break;
            case PACKET_EXCEPTION_ERR_DATA:
                break;
            case PACKET_EXCEPTION_ERR_PACKET_TYPE:
                break;
            case PACKET_EXCEPTION_ERR_BODY_TYPE:
                break;
            case PACKET_EXCEPTION_ERR_SERILIZE_TYPE:
                break;
            case PACKET_EXCEPTION_ERR_KEY_TYPE:
                break;
            case AUTH_FAILED_NO_UID_OR_PWD:
                break;
            case AUTH_FAILED_ERR_UID_OR_PWD:
                break;

        }
        LogUtils.debugInfo(TAG, "errcode : " + eventContainer.err + "---errMsg : " + eventContainer.errMsg);
        return true;
    }

    /**
     * 把Json数据解析为可用的对象
     *
     * @param data
     * @param eventContainer
     * @return
     *
     * @throws JSONException
     * @throws NullPointerException
     */
    private EventContainer parseJsonData(byte[] data, EventContainer eventContainer, int title_type) {
        switch (title_type) {
            case MessageHelper.TITLE_TYPE_MESSAGE:
                return parseJsonDataFroMessage(data, eventContainer);
            case MessageHelper.TITLE_TYPE_MESSAGE_ACK:
                return parseJsonDataFroMessageACK(data, eventContainer);
            case MessageHelper.TITLE_TYPE_MESSAGE_ERR_ACK:
                return parseJsonDataFroMessageErrACK(data, eventContainer);
            default:
                return eventContainer;

        }

    }

    /**
     * 解析收到的消息(json)
     *
     * @param data
     * @param eventContainer
     * @return
     */
    private EventContainer parseJsonDataFroMessage(byte[] data, EventContainer eventContainer) {
        Gson gson = new Gson();
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(MessageHelper.getRecievedBody(data));
            eventContainer.mEvent = jsonArray.get(0).toString();
            eventContainer.mEvent = eventContainer.mEvent.replace("\"", "");
            if (jsonArray.length() >= 3)
                cancleTimeoutListen(jsonArray.get(2).toString());
            else
                cancleTimeoutListen(0 + "");

            switch (eventContainer.mEvent) {
                /**
                 * 收到消息回调
                 */
                case ImService.MSG:
                    setMessageContianer(eventContainer, gson, jsonArray.get(1).toString());
                    /**
                     * 消息去重
                     */
                    if (checkDuplicateMessages(eventContainer))
                        return null;
                    break;
                /**
                 * 会话结束
                 */
                case ImService.CONVR_END:
                    Conver conver = new Conver();
                    try {
                        conver.cid = Integer.valueOf(jsonArray.get(1).toString());
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    eventContainer.mConver = conver;

                    break;

            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        } catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        }
        return eventContainer;
    }

    /**
     * 解析收到的发送消息回执
     *
     * @param data
     * @param eventContainer
     * @return
     */
    private EventContainer parseJsonDataFroMessageACK(byte[] data, EventContainer eventContainer) {
        Gson gson = new Gson();
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(MessageHelper.getRecievedBody(data));
            eventContainer.mEvent = jsonArray.get(0).toString();
            eventContainer.mEvent = eventContainer.mEvent.replace("\"", "");
            MessageContainer messageContainer = null;
            if (jsonArray.length() >= 3)
                messageContainer = cancleTimeoutListen(jsonArray.get(2).toString());
            else
                messageContainer = cancleTimeoutListen(0 + "");

            switch (eventContainer.mEvent) {
                /**
                 * 查看对话成员回调
                 */
                case ImService.CHATROOM_MC_ACK:
                    setMcEventContainer(eventContainer, gson, jsonArray.get(1).toString());


                    break;


                /**
                 * 加入对话回调
                 */
                case ImService.CONVERSATION_JOIN:
                    eventContainer.mEvent = ImService.CONVERSATION_JOIN_ACK;
                    setJoinEventContainer(eventContainer, (JSONObject) jsonArray.get(1));


                    break;
                /**
                 * 离开对话回调
                 */
                case ImService.CONVERSATION_LEAVE:
                    eventContainer.mEvent = ImService.CONVERSATION_LEAVE_ACK;
                    setLeaveEventContainer(eventContainer, jsonArray.get(1).toString());

                    break;

                /**
                 * 发送消息回调
                 */
                case ImService.MSG:
                    eventContainer.mEvent = ImService.MSG_ACK;
                    setMessageContianer(eventContainer, gson, jsonArray.get(1).toString());
                    InsertSendMessage2DB(eventContainer, messageContainer);
                    break;

                /**
                 * 获取对话信息回调
                 */
                case ImService.GET_CONVERSATON_INFO:
                    eventContainer = dealConversation(eventContainer, gson, jsonArray.get(1).toString());
                    break;
                /**
                 * 获取对话信息回调
                 */
                case ImService.CONVR_MSG_SYNC:
                    eventContainer = dealPluck(eventContainer, gson, jsonArray.get(1).toString());


                    break;

                default:

                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        } catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        }
        return eventContainer;
    }

    private EventContainer dealPluck(EventContainer eventContainer, Gson gson, String content) {
        System.out.println("------------dealPluck---------- = " + content);
        List<Message> messages = gson.fromJson(content, new TypeToken<List<Message>>() {
        }.getType());
        if (messages != null && messages.size() > 0) {
            int size = messages.size();
            for (int i = 0; i < size; i++) {
                EventContainer tmp = new EventContainer();
                MessageContainer messageContainer = new MessageContainer();
                messageContainer.msg = messages.get(i);
                messageContainer.mEvent = ImService.MSG;
                tmp.mEvent = ImService.MSG;
                tmp.mMessageContainer = messageContainer;
                System.out.println("----------dealPluck---------messageContainer = " + messageContainer.toString());
                if (checkData(tmp))
                    sendImBroadCast(tmp);

            }


        }
        eventContainer = null;
        return eventContainer;


    }

    /**
     * 解析收到的消息
     *
     * @param data
     * @param eventContainer
     * @return
     */
    private EventContainer parseJsonDataFroMessageErrACK(byte[] data, EventContainer eventContainer) {
        try {
            JSONArray jsonArray = null;
            jsonArray = new JSONArray(MessageHelper.getRecievedBody(data));
            eventContainer.mEvent = jsonArray.get(0).toString();
            eventContainer.mEvent = eventContainer.mEvent.replace("\"", "");
            if (eventContainer.mEvent.equals(ImService.MSG))
                eventContainer.mEvent = ImService.MSG_ACK;

            if (jsonArray.length() >= 3)
                cancleTimeoutListen(jsonArray.get(2).toString());
            else
                cancleTimeoutListen(0 + "");
            JSONObject jsonObject = jsonArray.getJSONObject(1);
            MessageContainer messageContainer = new MessageContainer();
            Message message = new Message();
            messageContainer.msg = message;
            eventContainer.mMessageContainer = messageContainer;

            if (jsonObject != null && jsonObject.has("code")) {
                eventContainer.err = jsonObject.getInt("code");
                eventContainer.mMessageContainer.msg.err = eventContainer.err;
            }
            if (jsonObject != null && jsonObject.has("msg"))
                eventContainer.errMsg = jsonObject.getString("msg");

            if (jsonObject.has("expire")) {
                eventContainer.expire = jsonObject.getInt("expire");
                eventContainer.mMessageContainer.msg.expire = eventContainer.expire;
            }


        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        } catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        }
        return eventContainer;
    }

    private void setMcEventContainer(EventContainer eventContainer, Gson gson, String msg) throws JSONException {
        ChatRoomContainer chatRommcontainer = new ChatRoomContainer();
        try {
            JSONArray bodyarray = new JSONArray(msg);
            List<ChatRoom> chatrooms = gson.fromJson(bodyarray.toString(), new TypeToken<List<ChatRoom>>() {
            }.getType());
            eventContainer.mChatRoomContainer = chatRommcontainer;
            eventContainer.mChatRoomContainer.mChatRooms = chatrooms;
        } catch (JSONException e) {
            e.printStackTrace();
            JSONObject bodyobject = new JSONObject(msg);
            ChatRoomErr chatroomerr = gson.fromJson(bodyobject.toString(), ChatRoomErr.class);
            List<Integer> cidstmp = chatroomerr.cid;
            List<ChatRoom> chatrooms = new ArrayList<>();
            for (Integer value : cidstmp) {
                chatrooms.add(new ChatRoom(value));
            }
            eventContainer.mChatRoomContainer = chatRommcontainer;
            eventContainer.mChatRoomContainer.err = chatroomerr.err;

            eventContainer.mChatRoomContainer.mChatRooms = chatrooms;
        }
    }

    private void setJoinEventContainer(EventContainer eventContainer, JSONObject joinack) throws JSONException {
        List<ChatRoom> chatrooms = new ArrayList<>();
        ChatRoomContainer chatRommcontainer = new ChatRoomContainer();
        if (joinack.has("err")) {
            eventContainer.mChatRoomContainer = chatRommcontainer;
            eventContainer.mChatRoomContainer.err = joinack.getInt("err");
            chatrooms.add(new ChatRoom(joinack.getInt("cid")));
            eventContainer.mChatRoomContainer.mChatRooms = chatrooms;
        }
        else {
//            ["convr.join", {"cid":4,"time":1472119730}, 1]
            int mc = 0;
            long expire = -1L;
            if (joinack.has("mc"))
                mc = joinack.getInt("mc");
            if (joinack.has("expire"))
                expire = joinack.getInt("expire");
            chatrooms.add(new ChatRoom(joinack.getInt("cid"), mc, expire));
            eventContainer.mChatRoomContainer = chatRommcontainer;
            eventContainer.mChatRoomContainer.mChatRooms = chatrooms;
        }
    }

    private void setLeaveEventContainer(EventContainer eventContainer, String msg) throws JSONException {
        JSONObject leaveack = null;

        List<ChatRoom> chatrooms = new ArrayList<>();
        ChatRoomContainer chatRommcontainer = new ChatRoomContainer();
        try {
            leaveack = new JSONObject(msg);
            if (leaveack.has("err")) {
                eventContainer.mChatRoomContainer = chatRommcontainer;
                eventContainer.mChatRoomContainer.err = leaveack.getInt("err");
                chatrooms.add(new ChatRoom(leaveack.getInt("cid")));
                eventContainer.mChatRoomContainer.mChatRooms = chatrooms;
            }
            else {

                int cid = 0;
                if (leaveack.has("cid")) {
                    cid = leaveack.getInt("cid");
                }
                else {
                    String cidstr = leaveack.toString().replace("{", "");
                    cid = Integer.valueOf(cidstr.replace("}", ""));
                }
                chatrooms.add(new ChatRoom(cid));
                eventContainer.mChatRoomContainer = chatRommcontainer;
                eventContainer.mChatRoomContainer.mChatRooms = chatrooms;

            }
        } catch (JSONException e) {
            String cid = msg.replace("{", "");
            cid = cid.replace("}", "");
            try {
                chatrooms.add(new ChatRoom(Integer.valueOf(cid)));
                eventContainer.mChatRoomContainer = chatRommcontainer;
                eventContainer.mChatRoomContainer.mChatRooms = chatrooms;
            } catch (Exception r) {
                r.printStackTrace();
            }

        }


    }

    /**
     * 把消息设置到contaienr中
     *
     * @param eventContainer
     * @param gson
     * @param content
     * @throws JSONException
     */
    private void setMessageContianer(EventContainer eventContainer, Gson gson, String content) {
        MessageContainer messageContainer = new MessageContainer();
        messageContainer.mEvent = eventContainer.mEvent;
        try {
            messageContainer.msg = gson.fromJson(content, Message.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        eventContainer.mMessageContainer = messageContainer;
        if (messageContainer.msg != null)
            eventContainer.err = messageContainer.msg.err;
    }


    /**
     * 把MsgPack数据解析为可用的对象
     *
     * @param data
     * @param eventContainer
     * @return
     */
    private EventContainer parseMsgpackData(byte[] data, EventContainer eventContainer, int title_type) {

        switch (title_type) {
            case MessageHelper.TITLE_TYPE_MESSAGE:
                return parseMsgpackDataMessage(data, eventContainer);
            case MessageHelper.TITLE_TYPE_MESSAGE_ACK:
                return parseMsgpackDataMessageACK(data, eventContainer);
            case MessageHelper.TITLE_TYPE_MESSAGE_ERR_ACK:
                return parseMsgpackDataMessageErrACK(data, eventContainer);
            default:
                return eventContainer;

        }


    }

    /**
     * 解析收到的消息（msgpack）
     *
     * @param data
     * @param eventContainer
     * @return
     */
    private EventContainer parseMsgpackDataMessage(byte[] data, EventContainer eventContainer) {
        List<Value> dst1 = null;
        System.out.println("data ----------= " + new String(data));
        try {
            dst1 = new MessagePack().read(MessageHelper.getRecievedBodyByte(data), Templates.tList(Templates.TValue));
            LogUtils.debugInfo(TAG, "------value----" + dst1.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return eventContainer;
        } catch (NullPointerException r) {
            r.printStackTrace();
            return eventContainer;
        }
        LogUtils.debugInfo(TAG, "----0---" + dst1.get(0).toString());
        LogUtils.debugInfo(TAG, "-------1------" + dst1.get(1).toString());
        eventContainer.mEvent = dst1.get(0).toString();
        eventContainer.mEvent = eventContainer.mEvent.replace("\"", "");
        String msg = dst1.get(1).toString();
        if (dst1.size() >= 3)
            cancleTimeoutListen(dst1.get(2).toString());
        else
            cancleTimeoutListen(0 + "");

        switch (eventContainer.mEvent) {
            /**
             * 收到消息
             */
            case ImService.MSG:
                setMessageContianer(eventContainer, new Gson(), msg);
                /**
                 * 消息去重
                 */
                if (checkDuplicateMessages(eventContainer))
                    return null;
                break;
            /**
             * 会话结束
             */
            case ImService.CONVR_END:
                Conver conver = new Conver();
                try {
                    conver.cid = Integer.valueOf(msg);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                eventContainer.mConver = conver;
                break;

        }
        return eventContainer;
    }


    /**
     * 解析发送消息的回调msgpack）
     *
     * @param data
     * @param eventContainer
     * @return
     */
    private EventContainer parseMsgpackDataMessageACK(byte[] data, EventContainer eventContainer) {
        List<Value> dst1 = null;
        System.out.println("data ----------= " + new String(data));
        try {
            dst1 = new MessagePack().read(MessageHelper.getRecievedBodyByte(data), Templates.tList(Templates.TValue));
            LogUtils.debugInfo(TAG, "------value----" + dst1.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return eventContainer;
        } catch (NullPointerException r) {
            r.printStackTrace();
            return eventContainer;
        }
        Gson gson = new Gson();
        try {
            LogUtils.debugInfo(TAG, "----0---" + dst1.get(0).toString());
            LogUtils.debugInfo(TAG, "-------1------" + dst1.get(1).toString());
            eventContainer.mEvent = dst1.get(0).toString();
            eventContainer.mEvent = eventContainer.mEvent.replace("\"", "");
            String msg = dst1.get(1).toString();
            MessageContainer messageContainer = null;
            if (dst1.size() >= 3) {
                messageContainer = cancleTimeoutListen(dst1.get(2).toString());
                System.out.println("messageContainer = " + messageContainer);
            }
            else
                messageContainer = cancleTimeoutListen(0 + "");
            switch (eventContainer.mEvent) {
                /**
                 * 查看对话成员回调
                 */
                case ImService.CHATROOM_MC_ACK:
                    setMcEventContainer(eventContainer, gson, msg);
                    break;
                /**
                 * 加入对话回调
                 */
                case ImService.CONVERSATION_JOIN:
                    eventContainer.mEvent = ImService.CONVERSATION_JOIN_ACK;
                    setJoinEventContainer(eventContainer, new JSONObject(msg));
                    break;
                /**
                 * 离开对话回调
                 */
                case ImService.CONVERSATION_LEAVE:
                    eventContainer.mEvent = ImService.CONVERSATION_LEAVE_ACK;
                    setLeaveEventContainer(eventContainer, msg);
                    break;
                /**
                 * 发送消息回调
                 */
                case ImService.MSG:
                    eventContainer.mEvent = ImService.MSG_ACK;
                    setMessageContianer(eventContainer, gson, msg);
                    InsertSendMessage2DB(eventContainer, messageContainer);
                    break;
                /**
                 * 获取对话信息回调
                 */
                case ImService.GET_CONVERSATON_INFO:
                    eventContainer = dealConversation(eventContainer, gson, msg);
                    break;
                /**
                 * 获取对话信息回调
                 */
                case ImService.CONVR_MSG_SYNC:
                    eventContainer = dealPluck(eventContainer, gson, msg);


                    break;

            }
        } catch (JSONException json) {
            json.printStackTrace();
        }


        return eventContainer;
    }

    private EventContainer dealConversation(EventContainer eventContainer, Gson gson, String msg) {
        List<Conversation> conversations = new Gson().fromJson(msg, new TypeToken<List<Conversation>>() {
        }.getType());
        eventContainer.mConversations = conversations;
        if (conversations != null && conversations.size() > 0) {
            Conversation tmp = conversations.get(0);
            if (mEventContainerCache.containsKey(tmp.getCid())) {
                eventContainer = mEventContainerCache.get(tmp.getCid());
                tmp.setLast_message_time((eventContainer.mMessageContainer.msg.mid >> 23) + BaseDao.TIME_DEFAULT_ADD);
                tmp.setUsids(String.valueOf(eventContainer.mMessageContainer.msg.uid));
                tmp.setLast_message_text(eventContainer.mMessageContainer.msg.getTxt());
                ConversationDao.getInstance(getApplicationContext()).insertConversation(tmp);
                mEventContainerCache.remove(tmp.getCid());
            }
        }
        return eventContainer;
    }

    private void InsertSendMessage2DB(EventContainer eventContainer, MessageContainer messageContainer) {
        if (messageContainer != null && messageContainer.msg != null && eventContainer != null && eventContainer.mMessageContainer != null & eventContainer.mMessageContainer.msg != null) {
            messageContainer.msg.mid = eventContainer.mMessageContainer.msg.mid;
            messageContainer.msg.seq = eventContainer.mMessageContainer.msg.seq;
            eventContainer.mMessageContainer.msg = messageContainer.msg;
            //发送的消息插入数据库
            checkDuplicateMessages(eventContainer);
        }
    }

    /**
     * 解析收到的错误消息（msgpack）
     *
     * @param data
     * @param eventContainer
     * @return
     */
    private EventContainer parseMsgpackDataMessageErrACK(byte[] data, EventContainer eventContainer) {
        List<Value> dst1 = null;
        System.out.println("data ----------= " + new String(data));
        try {
            dst1 = new MessagePack().read(MessageHelper.getRecievedBodyByte(data), Templates.tList(Templates.TValue));
            LogUtils.debugInfo(TAG, "------value----" + dst1.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return eventContainer;
        } catch (NullPointerException r) {
            r.printStackTrace();
            return eventContainer;
        }
        LogUtils.debugInfo(TAG, "----0---" + dst1.get(0).toString());
        LogUtils.debugInfo(TAG, "-------1------" + dst1.get(1).toString());
        eventContainer.mEvent = dst1.get(0).toString();
        eventContainer.mEvent = eventContainer.mEvent.replace("\"", "");
        MessageContainer messageContainer = new MessageContainer();
        Message message = new Message();
        messageContainer.msg = message;
        eventContainer.mMessageContainer = messageContainer;
        try {
            JSONObject jsonObject = new JSONObject(dst1.get(1).toString());
            if (jsonObject.has("code")) {
                eventContainer.err = jsonObject.getInt("code");
                eventContainer.mMessageContainer.msg.err = eventContainer.err;
            }
            if (jsonObject.has("msg"))
                eventContainer.errMsg = jsonObject.getString("msg");
            if (jsonObject.has("blk"))
                eventContainer.blk = jsonObject.getBoolean("blk");
            if (jsonObject.has("expire")) {
                eventContainer.expire = jsonObject.getInt("expire");
                eventContainer.mMessageContainer.msg.expire = eventContainer.expire;
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (dst1.size() >= 3)
            cancleTimeoutListen(dst1.get(2).toString());
        else
            cancleTimeoutListen(0 + "");
        if (eventContainer != null && eventContainer.mEvent != null && eventContainer.mEvent.equals(ImService.MSG)) {
            eventContainer.mEvent = ImService.MSG_ACK;

        }

        return eventContainer;
    }


    /**
     * 取消超时监听
     *
     * @param str_id
     * @return
     */
    private MessageContainer cancleTimeoutListen(String str_id) {
        int id = Integer.valueOf(str_id);
        MessageContainer messageConteainer = null;
        if (timeTasks.containsKey(id)) {
            timeTasks.get(id).setEnd(true);
            messageConteainer = timeTasks.get(id).getMessageContainer();
            timeTasks.remove(id);
        }
        return messageConteainer;

    }

    /**
     * 把收到的消息发送到Im接收器
     *
     * @param eventContainer
     */
    private void sendImBroadCast(EventContainer eventContainer) {
        if (eventContainer == null || eventContainer.mEvent == null) return;
        Bundle bundle = new Bundle();
        bundle.putSerializable(ZBIMClient.KEY_EVENTCONTAINER, eventContainer);
        sendImStatusBroadCast(bundle);
    }

    /**
     * 消息去重
     *
     * @param eventContainer
     */
    private boolean checkDuplicateMessages(EventContainer eventContainer) {
        System.out.println("eventContainer = " + eventContainer.toString());
        if ((eventContainer.mEvent.equals(ImService.MSG) || eventContainer.mEvent.equals(ImService.MSG_ACK)) && eventContainer.mMessageContainer != null && eventContainer.mMessageContainer.msg != null) {
            if (!MessageDao.getInstance(getApplicationContext()).hasMessage(eventContainer.mMessageContainer.msg.mid)) {
                Conversation conversation = ConversationDao.getInstance(getApplicationContext()).getConversationByCid(eventContainer.mMessageContainer.msg.cid);
                if (conversation == null) {//创建本地对话信息
//                    Conversation newConversation = new Conversation();
//                    conversation.setCid(eventContainer.mMessageContainer.msg.cid);
//                    conversation.setLast_message_time((eventContainer.mMessageContainer.msg.mid >> 23) + ConversationDao.TIME_DEFAULT_ADD);
//                    ConversationDao.getInstance(getApplicationContext()).insertConversation(newConversation);
//                    MessageDao.getInstance(getApplicationContext()).insertMessage(eventContainer.mMessageContainer.msg);
                    mEventContainerCache.put(eventContainer.mMessageContainer.msg.cid, eventContainer);
                    /**
                     * 获取对话信息
                     */
                    mService.sendGetConversatonInfo(eventContainer.mMessageContainer.msg.cid, "");
                    return true;
                }
                else {
                    if (conversation.getType() != Conversation.CONVERSATION_TYPE_CHAROOM)
                        MessageDao.getInstance(getApplicationContext()).insertMessage(eventContainer.mMessageContainer.msg);
                }
                return false;
            }
        }
        return true;
    }

    /**
     * 把收到的消息发送到Im接收器
     *
     * @param bundle
     */
    private void sendImStatusBroadCast(Bundle bundle) {
        EventBus.getDefault().post(bundle
                , SocketService.EVENT_SOCKET_RECEIVE_MESSAGE);//通过eventbus发送
    }


    /**
     * 连接断开
     *
     * @param code
     * @param reason
     */
    @Override
    public void onDisconnect(int code, String reason) {
        LogUtils.debugInfo(TAG, "----------onDisconnect------");
        if (connected) sendDisconnectedMsg(code, reason);
        connected = false;
        isDisconnecting = false;

        switch (code) {
            /**
             * 主动断开
             */
            case WebSocket.ConnectionHandler.CLOSE_NORMAL:

                break;
            /**
             * 无法连接到服务器（主要是网络太差出现）
             */
            case WebSocket.ConnectionHandler.CLOSE_CANNOT_CONNECT:
                socketReconnect();
                break;
            /**
             * 意外的失去了先前建立的连接
             */
            case WebSocket.ConnectionHandler.CLOSE_CONNECTION_LOST:
                socketReconnect();

                break;
            /**
             * 违反协议导致连接被关闭
             */
            case WebSocket.ConnectionHandler.CLOSE_PROTOCOL_ERROR:

                break;
            /**
             * 内部错误
             */
            case WebSocket.ConnectionHandler.CLOSE_INTERNAL_ERROR:

                break;
            /**
             * 在连接的时候服务器返回错误
             */
            case WebSocket.ConnectionHandler.CLOSE_SERVER_ERROR:

                break;
            /**
             * 服务器连接丢失,重新连接
             */
            case WebSocket.ConnectionHandler.CLOSE_RECONNECT:

                break;


        }
        LogUtils.debugInfo(TAG, code + "");
        LogUtils.debugInfo(TAG, reason);

    }

    /**
     * 连接断开消息通知分发
     *
     * @param code
     * @param reason
     */
    private void sendDisconnectedMsg(int code, String reason) {
        if (System.currentTimeMillis() - disconnect_start_time > DISCONNECT_NOTIFY_TIME) {
            EventContainer eventContainer = new EventContainer();
            eventContainer.mEvent = ImService.WEBSOCKET_DISCONNECTED;
            Bundle bundle = new Bundle();
            bundle.putSerializable(ZBIMClient.KEY_EVENTCONTAINER, eventContainer);
            bundle.putInt(ZBIMClient.KEY_DISCONNECTED_CODE, code);
            bundle.putString(ZBIMClient.KEY_DISCONNECTED_REASON, reason);
            sendImStatusBroadCast(bundle);
        }
    }


    /**
     * 连接错误
     *
     * @param error
     */
    @Override
    public void onError(Exception error) {
        LogUtils.debugInfo(TAG, "----------onError------");
        sendConnectedErrMsg(error);
        error.printStackTrace();
        connected = false;
    }

    /**
     * 连接错误消息通知分发
     *
     * @param error
     */
    private void sendConnectedErrMsg(Exception error) {
        EventContainer eventContainer = new EventContainer();
        eventContainer.mEvent = ImService.WEBSOCKET_DISCONNECTED;
        Bundle bundle = new Bundle();
        bundle.putSerializable(ZBIMClient.KEY_EVENTCONTAINER, eventContainer);
        bundle.putSerializable(ZBIMClient.KEY_CONNECTED_ERR, error);
        sendImStatusBroadCast(bundle);
    }

    /**
     * socket重连
     */
    private void socketReconnect() {

        if (DeviceUtils.netIsConnected(getApplicationContext()) && isNeedReConnected) {
            if (mService.isConnected()) {
                if (!isDisconnecting) {
                    mService.disconnect();
                    isDisconnecting = true;
                }
            }
            else {
                LogUtils.debugInfo(TAG, "----------socketReconnect------");
                changeHeartBeatRateByReconnect();
                mService.connect();
                resetTime();
                if (disconnect_start_time == 0)
                    disconnect_start_time = System.currentTimeMillis();
            }
        }

    }


    /**
     * 断开连接
     */
    private void disConnect() {
        isNeedReConnected = false;
        mService.disconnect();
        isDisconnecting = true;
    }

    /**
     * 开启连接
     */
    private void connect() {

        isNeedReConnected = true;
        socketReconnect();
    }

    /**
     * 网络变化重连socket
     */
    public class SocketRetryReceiver extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {
            changeHeartBeatRate();
            socketReconnect();

        }
    }

}
