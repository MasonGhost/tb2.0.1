package com.zhiyicx.imsdk.service;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.SparseArray;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.zhiyicx.imsdk.core.ImService;
import com.zhiyicx.imsdk.core.autobahn.WebSocket;
import com.zhiyicx.imsdk.db.base.BaseDao;
import com.zhiyicx.imsdk.db.dao.ConversationDao;
import com.zhiyicx.imsdk.db.dao.MessageDao;
import com.zhiyicx.imsdk.entity.AuthData;
import com.zhiyicx.imsdk.entity.ChatRoom;
import com.zhiyicx.imsdk.entity.ChatRoomContainer;
import com.zhiyicx.imsdk.entity.ChatRoomErr;
import com.zhiyicx.imsdk.entity.Conversation;
import com.zhiyicx.imsdk.entity.EventContainer;
import com.zhiyicx.imsdk.entity.IMConfig;
import com.zhiyicx.imsdk.entity.Message;
import com.zhiyicx.imsdk.entity.MessageContainer;
import com.zhiyicx.imsdk.entity.MessageExt;
import com.zhiyicx.imsdk.entity.MessageStatus;
import com.zhiyicx.imsdk.entity.MessageType;
import com.zhiyicx.imsdk.manage.ZBIMClient;
import com.zhiyicx.imsdk.policy.timeout.TimeOutListener;
import com.zhiyicx.imsdk.policy.timeout.TimeOutTask;
import com.zhiyicx.imsdk.policy.timeout.TimeOutTaskManager;
import com.zhiyicx.imsdk.policy.timeout.TimeOutTaskPool;
import com.zhiyicx.imsdk.utils.CustomMessageExtGsonDeserializer;
import com.zhiyicx.imsdk.utils.MessageHelper;
import com.zhiyicx.imsdk.utils.common.DeviceUtils;
import com.zhiyicx.imsdk.utils.common.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.msgpack.MessagePack;
import org.msgpack.template.Templates;
import org.msgpack.type.Value;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.zhiyicx.imsdk.core.ErroCode.AUTH_FAILED_ERR_UID_OR_PWD;
import static com.zhiyicx.imsdk.core.ErroCode.AUTH_FAILED_NO_UID_OR_PWD;
import static com.zhiyicx.imsdk.core.ErroCode.PACKET_EXCEPTION_ERR_BODY_TYPE;
import static com.zhiyicx.imsdk.core.ErroCode.PACKET_EXCEPTION_ERR_DATA;
import static com.zhiyicx.imsdk.core.ErroCode.PACKET_EXCEPTION_ERR_KEY_TYPE;
import static com.zhiyicx.imsdk.core.ErroCode.PACKET_EXCEPTION_ERR_PACKET_TYPE;
import static com.zhiyicx.imsdk.core.ErroCode.PACKET_EXCEPTION_ERR_SERILIZE_TYPE;
import static com.zhiyicx.imsdk.core.ErroCode.SERVER_EXCEPTION;
import static com.zhiyicx.imsdk.db.base.BaseDao.TIME_DEFAULT_ADD;

/**
 * 聊天服务
 * Created by jungle68 on 16/7/6.
 */
public class SocketService extends BaseService implements ImService.ImListener {
    private final String TAG = this.getClass().getSimpleName();

    /**
     * IM 操作类型
     */
    /**
     * 登录im
     */
    public static final int TAG_IM_LOGIN = 10000;
    /**
     * 断开im
     */
    public static final int TAG_IM_LOGINOUT = 10001;
    /**
     * 进入房间
     */
    public static final int TAG_IM_JOIN_CONVERSATION = 10002;
    /**
     * 离开房间
     */
    public static final int TAG_IM_LEAVE_CONVERSATION = 10003;
    /**
     * 发送消息
     */
    public static final int TAG_IM_SEND_MESSAGE = 10004;
    /**
     * 查看房间人数
     */
    public static final int TAG_IM_MC = 10005;
    /**
     * 尝试重连
     */
    public static final int TAG_IM_RECONNECT = 10006;
    /**
     * 获取指定序号消息
     */
    public static final int TAG_IM_PLUCK = 10007;
    /**
     * 获取指定序号消息
     */
    public static final int TAG_IM_SYNC = 10008;
    /**
     * 获取最新的几条消息
     */
    public static final int TAG_IM_SYNCLASTMESSAGE = 10009;

    /**
     * 和 IMClient 通信的 intent key
     */
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
    public static final String BUNDLE_MSG_ORDER = "order";


    public static final String SOCKET_RETRY_CONNECT = "com.zhiyicx.zhibo.socket_retry_connect";
    /**
     * 分发处理消息
     */
    public static final String EVENT_SOCKET_DEAL_MESSAGE = "event_socket_deal_message";
    /**
     * 分发处理消息
     */
    public static final String EVENT_SOCKET_RECEIVE_MESSAGE = "event_socket_receive_message";
    public static final String EVENT_SOCKET_TAG = "event_socket_tag";

    /**
     * 心跳维持时常
     */
    private long HEART_BEAT_RATE = HEART_BEAT_RATE_MIDDLE;
    /**
     * 最大心跳间隔时间
     */
    private static final long HEART_BEAT_RATE_MAX_PING = 250 * 1000;
    private static final long HEART_BEAT_RATE_MAX = 70 * 1000;
    private static final long HEART_BEAT_RATE_HEIGHT = 110 * 1000;
    private static final long HEART_BEAT_RATE_MIDDLE = 170 * 1000;
    private static final long HEART_BEAT_RATE_LOW = 230 * 1000;
    private static final long HEART_BEAT_RATE_HEIGHT_BACKGROUND = 130 * 1000;
    private static final long HEART_BEAT_RATE_MIDDLE_BACKGROUND = 190 * 1000;
    private static final long HEART_BEAT_RATE_LOW_BACKGROUND = HEART_BEAT_RATE_MAX_PING;
    /**
     * 心跳频率递减时间
     */
    private static final long HEART_BEAT_REDUCE_RATE = 10 * 1000;
    /**
     * 间隔10s重连,10秒没有收到服务器回应
     */
    private static final long HEART_PING_PONG_RATE = 10 * 1000;
    /**
     * IM超过10s没有连上，通知下发
     */
    private static final long DISCONNECT_NOTIFY_TIME = 10 * 1000;
    /**
     * 心跳，防止cpu占用过高
     */
    private static final long HEART_BEAT_RATE_INTERVAL_FOR_CPU = 500;
    /**
     * 消息发送间隔时间，防止cpu占用过高
     */
    private static final long MESSAGE_SEND_INTERVAL_FOR_CPU = 100;
    /**
     * 消息发送间隔时间，防止cpu占用过高
     */
    private static final long DELAY_RECONNECT_TIME = 5000;
    /**
     * 重连开始时间
     */
    private long mDisconnectStartTime = 0;
    /**
     * 最大的重发次数
     */
    private static final int MAX_RESEND_COUNT = 3;


    private ImService mService;
    private Context mContext;
    private long sendTime = 0L;
    private long responsTime = 0L;
    /**
     * 判断应用处于前台还是后台
     */
    private boolean isBackground = false;
    private boolean isDisconnecting = false;
    private boolean isConnecting = false;

    /**
     * For heart Beat
     */
    private Thread mThread;

    private Thread mSendMessageThread;
    /**
     * 终止线程
     */
    public volatile boolean exit = false;
    private SocketRetryReceiver mSocketRetryReceiver;
    private TimeOutTaskPool timeOutTaskPool;

    private IMConfig mIMConfig;
    /**
     * 是否需要重连
     */
    private boolean isNeedReConnected = true;
    /**
     * 当前连接状态
     */
    private boolean connected = false;
    /**
     * 向服务器发送的数据类型
     */
    private int mSendSerilizeType = ImService.BIN_MSGPACK;
    /**
     * 线程安全的队列
     */
    private Queue<MessageContainer> mMessageContainers = new ConcurrentLinkedQueue<>();
    private SparseArray<EventContainer> mEventContainerCache = new SparseArray<>();

    private Subscription mSubscription;


    private Runnable heartBeatRunnable = new Runnable() {

        @Override
        public void run() {
            while (!exit) {
                if (isNeedReConnected) {
                    /**
                     * ping后或者发送普通消息{@Link HEART_PING_PONG_RATE}s收不到回应则重连
                     */
                    if (!isConnecting && System.currentTimeMillis() - sendTime > HEART_PING_PONG_RATE && sendTime > responsTime) {
                        socketReconnect();
                        heartRateThreadSleep(HEART_BEAT_RATE_INTERVAL_FOR_CPU);
                        continue;
                    }
                    /**
                     * 心跳时间大于设定时间，&&接收到消息的时间超过了心跳时间，则发送新跳包
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
                        } else {
                            socketReconnect();
                        }
                    }
                }
                heartRateThreadSleep(HEART_BEAT_RATE_INTERVAL_FOR_CPU);
            }
        }
    };

    private void heartRateThreadSleep(long heartBeatRateIntervalForCpu) {
        /**
         * 防止cpu占用过高卡顿
         */
        try {
            Thread.sleep(heartBeatRateIntervalForCpu);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据软件在前台还是后台，以及当前的网络状况，修改心跳的频率
     */
    private long changeHeartBeatRate() {
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
        } else {
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
        return HEART_BEAT_RATE;
    }

    /**
     * 智能心跳
     * 每重连一次，重连间隔时间-10s
     */
    private void changeHeartBeatRateByReconnect() {

        if (HEART_BEAT_RATE > HEART_BEAT_RATE_MAX) {
            HEART_BEAT_RATE = HEART_BEAT_RATE - HEART_BEAT_REDUCE_RATE;
        }
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
                        if (messageContainer.msg.ext == null || messageContainer.msg.ext.customID != MessageType.MESSAGE_CUSTOM_ID_ZAN)//点赞不处理超时
                        {
                            addTimeoutTask(messageContainer);
                        }
                        sendMessage(messageContainer);
                    }
                }
                //防止cpu占用过高
                heartRateThreadSleep(MESSAGE_SEND_INTERVAL_FOR_CPU);
            }

        }
    };

    /**
     * 加入超时队列
     *
     * @param messageContainer
     */
    private void addTimeoutTask(MessageContainer messageContainer) {
        TimeOutTask timeOutTask = new TimeOutTask(messageContainer, System.currentTimeMillis(), new TimeOutListener() {
            @Override
            public void timeOut(MessageContainer messageContainer) {
                if (messageContainer.reSendCounts > MAX_RESEND_COUNT) {
                    sendTimeOutMsg(messageContainer);
                } else {
                    mMessageContainers.add(messageContainer);
                }
            }
        });
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
        if (eventContainer.mMessageContainer.msg != null) { // 更新状态
            eventContainer.mMessageContainer.msg.setSend_status(MessageStatus.SEND_FAIL);
            MessageDao.getInstance(getApplicationContext()).insertOrUpdateMessage(eventContainer.mMessageContainer.msg);
        }
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

    public void setService(ImService service) {
        mService = service;
    }


    @Override
    public void init() {
        mContext = getApplicationContext();
        mService = new ImService();
        initSocketListener();
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
    private boolean login(IMConfig imConfig) {
        if (imConfig == null || TextUtils.isEmpty(imConfig.getToken())) {
            return false;
        }
        mIMConfig = imConfig;
        isNeedReConnected = true;
        mService.setParams(imConfig.getWeb_socket_authority(), imConfig.getToken(),
                imConfig.getSerial(), imConfig.getComprs());
        LogUtils.debugInfo(TAG, "---init----" + imConfig.toString());
        isConnecting = true;
        mService.connect();
        resetTime();
        if (mDisconnectStartTime == 0) {
            mDisconnectStartTime = System.currentTimeMillis();
        }
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
        return true;
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.CHANGE_NETWORK_STATE)
                    != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.WRITE_SETTINGS)
                    != PackageManager.PERMISSION_GRANTED) {
                // 申请权限

            } else {
                // 有权限
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                if (connectivityManager != null) {
                    connectivityManager.requestNetwork(new NetworkRequest.Builder().build(), new ConnectivityManager.NetworkCallback() {
                        @Override
                        public void onAvailable(Network network) {
                            super.onAvailable(network);
                            if (!isConnecting) {
                                socketReconnect();
                            }
                        }
                    });
                }
            }

        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mService != null) {
            mService.disconnect();
        }
        isDisconnecting = true;
        if (timeOutTaskPool != null) {
            timeOutTaskPool.setStop(true);
        }
        mService = null;
        exit = true;
        if (mSocketRetryReceiver != null) {
            unregisterReceiver(mSocketRetryReceiver);
        }

        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }

    }

    @Override
    public void onConnected() {
        mDisconnectStartTime = 0;
        responseTime();
        sendConnectedMsg();
        connected = true;
        LogUtils.debugInfo(TAG, "--------connected---------");
    }

    public boolean isNeedReConnected() {
        return isNeedReConnected;
    }

    public void setNeedReConnected(boolean needReConnected) {
        isNeedReConnected = needReConnected;
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
        mSubscription = rx.Observable.just(data)
                .observeOn(Schedulers.io())
                .subscribe(new Action1<byte[]>() {
                    @Override
                    public void call(byte[] bytes) {
                        responseTime();
                        checkDataType(bytes);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }

    @Subscriber(tag = EVENT_SOCKET_DEAL_MESSAGE)
    public boolean dealMessage(Bundle bundle) {
        Observable.just(bundle)
                .observeOn(Schedulers.io())
                .subscribe(new Action1<Bundle>() {
                    @Override
                    public void call(Bundle bundle) {
                        dealSendMessage(bundle);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
        return true;

    }

    /**
     * 处理发送调用消息
     *
     * @param bundle
     */
    private void dealSendMessage(Bundle bundle) {
        boolean result = false;
        if (bundle == null) {
            return;
        }
        try {
            switch (bundle.getInt(EVENT_SOCKET_TAG)) {
                /**
                 * IM登录
                 */
                case TAG_IM_LOGIN:
                    if (bundle.getSerializable(BUNDLE_IMCONFIG) != null) {
                        result = login((IMConfig) bundle.getSerializable(BUNDLE_IMCONFIG));
                    }
                    break;
                /**
                 * IM登出
                 */
                case TAG_IM_LOGINOUT:
                    mService.setUrl(null);
                    result = disConnect();
                    break;
                /**
                 * 重连
                 */
                case TAG_IM_RECONNECT:
                    result = reConnect();
                    break;
                /**
                 * 加入对话
                 */
                case TAG_IM_JOIN_CONVERSATION:
                    result = join(bundle.getInt(BUNDLE_ROOMID), bundle.getInt(BUNDLE_MSG_ID), bundle.getString(BUNDLE_CONVR_PWD));
                    break;
                /**
                 * 加入消息队列
                 */
                case TAG_IM_SEND_MESSAGE:
                    MessageContainer messageContainer = (MessageContainer) bundle.getSerializable(BUNDLE_MESSAGECONTAINER);
                    result = mMessageContainers.add(messageContainer);
                    break;
                /***
                 * 离开会话
                 */
                case TAG_IM_LEAVE_CONVERSATION:
                    result = leave(bundle.getInt(BUNDLE_ROOMID), bundle.getInt(BUNDLE_MSG_ID), bundle.getString(BUNDLE_CONVR_PWD));
                    break;

                /**
                 * 查询会话消息
                 */
                case TAG_IM_MC:
                    result = mc((List<Integer>) bundle.getSerializable(BUNDLE_ROOMIDS), bundle.getInt(BUNDLE_MSG_ID), bundle.getString
                            (BUNDLE_CONVERSATION_FIELD));
                    break;
                /**
                 * 通过消息序号同步消息
                 */
                case TAG_IM_PLUCK:
                    result = sendPluckMessage(bundle.getInt(BUNDLE_ROOMID), (List<Integer>) bundle.getSerializable(BUNDLE_MSG_SEQ), bundle.getInt
                            (BUNDLE_MSG_ID));
                    break;
                /**
                 * 通过消息序号同步消息
                 */
                case TAG_IM_SYNC:
                    result = sendSyncMessage(bundle.getInt(BUNDLE_ROOMID), bundle.getInt(BUNDLE_MSG_GT), bundle.getInt(BUNDLE_MSG_LT, 0), bundle
                            .getInt(BUNDLE_MSG_ORDER, ZBIMClient.SYN_ASC), bundle.getInt(BUNDLE_MSG_ID));
                    break;
                /**
                 * 获取房间中最新的几条消息
                 */
                case TAG_IM_SYNCLASTMESSAGE:
                    result = sendSyncLastMessage(bundle.getInt(BUNDLE_ROOMID), bundle.getInt(BUNDLE_MSG_LIMIT, 0), bundle.getInt(BUNDLE_MSG_ID));
                    break;
                default:
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 刷新发送消息时间
        if (result) {
            resetTime();
        }
    }

    /**
     * 向IM服务器发送msgpack类型数据
     */
    public boolean sendMessage(MessageContainer messageContainer) {
        if (!messageContainer.msg.rt) {
            resetTime();
        }
        if (!DeviceUtils.netIsConnected(getApplicationContext()) || !connected) {
            return false;
        }
        checkMessageType(messageContainer);
        switch (mSendSerilizeType) {

            case ImService.BIN_JSON:

                return mService.sendJsonData(new Gson().toJson(messageContainer.msg), messageContainer.mEvent, messageContainer.msg.id);

            case ImService.BIN_MSGPACK:

                return mService.sendMsgpackData(messageContainer);
            default:

        }
        return false;

    }

    /**
     * 检测消息类型，并设置消息文本默认值
     *
     * @param messageContainer 消息内容
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
     * @param messageContainer 消息内容
     * @param txt              默认文本
     */
    private void setDefaultTxt(MessageContainer messageContainer, String txt) {
        if (TextUtils.isEmpty(messageContainer.msg.txt)) {
            messageContainer.msg.txt = txt;
        }
    }

    /**
     * 加入会话
     *
     * @param cid   房间号
     * @param msgid 本条消息的 id
     * @param pwd   密码
     */
    private boolean join(int cid, int msgid, String pwd) {
        if (cid == 0) {
            return false;
        }
        addTimeoutTask(new MessageContainer(ImService.CONVERSATION_JOIN, new Message(msgid), cid, null));
        return mService.joinConversation(cid, msgid, pwd);

    }

    /**
     * 离开会话
     *
     * @param cid   房间号
     * @param msgid 本条消息的 id
     * @param pwd   密码
     */
    private boolean leave(int cid, int msgid, String pwd) {
        if (cid == 0) {
            return false;
        }
        addTimeoutTask(new MessageContainer(ImService.CONVERSATION_LEAVE, new Message(msgid), cid, null));
        return mService.leaveConversation(cid, msgid, pwd);
    }

    /**
     * 查看对话信息
     *
     * @param cids  房间号
     * @param msgid 本条消息的 id
     * @param field 需要的字段
     * @return
     */
    private boolean mc(List<Integer> cids, int msgid, String field) {
        if (cids == null) {
            return false;
        }
        addTimeoutTask(new MessageContainer(ImService.GET_CONVERSATON_INFO_TIMEOUT, new Message(msgid), 0, cids));
        return mService.sendGetConversatonInfo(cids, field);
    }

    /**
     * 同步丢失的消息
     *
     * @param cid   房间号
     * @param seq   序列号
     * @param msgid 本条消息的 id
     * @return
     */
    private boolean sendPluckMessage(int cid, List<Integer> seq, int msgid) {
        if (cid == 0) {
            return false;
        }
        addTimeoutTask(new MessageContainer(ImService.CONVR_MSG_PLUCK, new Message(msgid), cid, null));
        return mService.sendPluckMessage(cid, seq, msgid);
    }

    /**
     * 同步丢失的消息
     *
     * @param cid   房间号
     * @param gt    开始的 seq
     * @param lt    结束的 seq
     * @param msgid 本条消息的 id
     * @return
     */
    private boolean sendSyncMessage(int cid, int gt, int lt, int order, int msgid) {
        if (cid == 0) {
            return false;
        }
        addTimeoutTask(new MessageContainer(ImService.CONVR_MSG_SYNC, new Message(msgid), cid, null));
        return mService.sendSyncMessage(cid, gt, lt, order, ImService.SEQ_LIMIT, msgid);
    }

    /**
     * 同步丢失的消息
     *
     * @param cid   房间号
     * @param limit 获取的数量
     * @param msgid 本条消息的 id
     * @return
     */
    private boolean sendSyncLastMessage(int cid, int limit, int msgid) {
        if (cid == 0) {
            return false;
        }
        addTimeoutTask(new MessageContainer(ImService.CONVR_MSG_SYNC, new Message(msgid), cid, null));
        return mService.sendSyncMessage(cid, 0, 0, ZBIMClient.SYN_ASC, limit, msgid);
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
        if (checkData(eventContainer)) {
            sendImBroadCast(eventContainer);
        }
    }

    /**
     * 检查socket错误信息
     *
     * @param eventContainer
     * @return
     */
    private boolean checkData(EventContainer eventContainer) {
        if (eventContainer == null) {
            return false;
        }
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
            // 认证失败，不需要重连
            case AUTH_FAILED_NO_UID_OR_PWD:
                setNeedReConnected(false);
                break;
            // 认证失败，不需要重连
            case AUTH_FAILED_ERR_UID_OR_PWD:
                setNeedReConnected(false);
                break;
            default:
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
            if (jsonArray.length() >= 3) {
                cancleTimeoutListen(jsonArray.get(2).toString());
            } else {
                cancleTimeoutListen(0 + "");
            }

            switch (eventContainer.mEvent) {
                /**
                 * 收到消息回调
                 */
                case ImService.MSG:
                    setMessageContianer(eventContainer, gson, jsonArray.get(1).toString());
                    /**
                     * 消息去重
                     */
                    if (checkDuplicateMessages(eventContainer)) {
                        return null;
                    }
                    break;
                /**
                 * 会话结束
                 */
                case ImService.CONVR_END:
                    Conversation conver = new Conversation();
                    try {
                        conver.setCid(Integer.valueOf(jsonArray.get(1).toString()));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    eventContainer.mConver = conver;

                    break;
                default:
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
            if (jsonArray.length() >= 3)// 是否包含消息 id
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
                /**
                 * 登录认证
                 */
                case ImService.AUTH:
                    eventContainer = dealAuth(eventContainer, gson, jsonArray.get(1).toString());
                    break;
                default:

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
     * 处理认证数据
     * ["auth",{"ping":200}] 成功
     * ["auth", {"code":1020,"msg":"Auth failed"}] // token无效
     * ["auth", {"code":1021,"msg":"User disabled","disa":1234567890}] // 用户被禁用，disa为自动解禁时间
     *
     * @param eventContainer 事件内容器
     * @param gson           Gosn 对象
     * @param body           消息内容 对应 {"code":1020,"msg":"Auth failed"}
     * @return
     */
    private EventContainer dealAuth(EventContainer eventContainer, Gson gson, String body) throws JSONException {
        JSONObject jsonObject = new JSONObject(body);
        if (jsonObject.has("code")) {
            int code = jsonObject.getInt("code");
            eventContainer.errMsg = jsonObject.getString("msg");
            eventContainer.err = code;
            switch (code) {
                case AUTH_FAILED_ERR_UID_OR_PWD: // 1021 User disabled 被禁用
                    eventContainer.disa = jsonObject.getLong("disa");

                    break;
                case AUTH_FAILED_NO_UID_OR_PWD:// 1020 Auth failed  认证失败

                    break;
                default:
            }
        } else if (jsonObject.has("ping")) {
            eventContainer.mAuthData = gson.fromJson(jsonObject.toString(), AuthData.class);
            connected = true;
            isConnecting = false;
        }
        return eventContainer;
    }

    private EventContainer dealPluck(EventContainer eventContainer, Gson gson, String content) {
        LogUtils.debugInfo("------------dealPluck---------- = " + content);
        List<Message> messages = null;
        try {
            Gson gsonDs = new GsonBuilder()
                    .registerTypeAdapter(MessageExt.class, new CustomMessageExtGsonDeserializer())
                    .create();
            messages = gsonDs.fromJson(content, new TypeToken<List<Message>>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (messages != null && messages.size() > 0) {
            int size = messages.size();
            for (int i = 0; i < size; i++) {
                EventContainer tmp = new EventContainer();
                MessageContainer messageContainer = new MessageContainer();
                messages.get(i).setIs_read(true);
                messageContainer.msg = messages.get(i);
                messageContainer.mEvent = ImService.MSG;
                tmp.mEvent = ImService.MSG;
                tmp.mMessageContainer = messageContainer;
                //消息去重
                if (checkData(tmp) && !checkDuplicateMessages(tmp)) {
                    sendImBroadCast(tmp);
                }

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
            messageContainer.msg = new Message();
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
        } else {
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
            } else {

                int cid = 0;
                if (leaveack.has("cid")) {
                    cid = leaveack.getInt("cid");
                } else {
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
            messageContainer.msg.setCreate_time((messageContainer.msg.mid >> 23) + TIME_DEFAULT_ADD); //  消息的MID，`(mid >> 23) + 1451577600000` 为毫秒时间戳
            messageContainer.msg.setSend_status(MessageStatus.SEND_SUCCESS);
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
        LogUtils.debugInfo(TAG, "data ----------= " + new String(data));
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
        if (dst1.size() >= 3) {
            cancleTimeoutListen(dst1.get(2).toString());
        } else {
            cancleTimeoutListen(0 + "");
        }

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
                Conversation conver = new Conversation();
                try {
                    conver.setCid(Integer.valueOf(msg));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                eventContainer.mConver = conver;
                break;
            default:
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
        LogUtils.debugInfo(TAG, "data ----------= " + new String(data));
        try {
            dst1 = new MessagePack().read(MessageHelper.getRecievedBodyByte(data), Templates.tList(Templates.TValue));
            LogUtils.debugInfo("------value----" + dst1.toString());
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
                LogUtils.debugInfo("messageContainer = " + messageContainer);
            } else
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
                /**
                 * 登录认证
                 */
                case ImService.AUTH:
                    eventContainer = dealAuth(eventContainer, gson, msg);
                    break;
                default:


            }
        } catch (JSONException json) {
            json.printStackTrace();
        }

        return eventContainer;
    }

    /**
     * 处理对话信息
     *
     * @param eventContainer
     * @param gson
     * @param msg
     * @return
     */
    private EventContainer dealConversation(EventContainer eventContainer, Gson gson, String msg) {
        List<Conversation> conversations = gson.fromJson(msg, new TypeToken<List<Conversation>>() {
        }.getType());
        eventContainer.mConversations = conversations;
        if (conversations != null && conversations.size() > 0) {
            Conversation tmp = conversations.get(0);
            if (mEventContainerCache.get(tmp.getCid()) != null) {
                eventContainer = mEventContainerCache.get(tmp.getCid());
                tmp.setLast_message_time((eventContainer.mMessageContainer.msg.mid >> 23) + BaseDao.TIME_DEFAULT_ADD);
                if (mIMConfig != null) {
                    tmp.setIm_uid(mIMConfig.getImUid());
                }
                tmp.setUsids(String.valueOf(eventContainer.mMessageContainer.msg.uid));
                tmp.setLast_message(eventContainer.mMessageContainer.msg);
                ConversationDao.getInstance(getApplicationContext()).insertConversation(tmp);
                mEventContainerCache.remove(tmp.getCid());
            }
        }
        return eventContainer;
    }

    /**
     * 消息插入数据库
     *
     * @param eventContainer
     * @param messageContainer
     */
    private void InsertSendMessage2DB(EventContainer eventContainer, MessageContainer messageContainer) {
        if (messageContainer != null && messageContainer.msg != null && eventContainer != null && eventContainer.mMessageContainer != null &
                eventContainer.mMessageContainer.msg != null) {
            messageContainer.msg.mid = eventContainer.mMessageContainer.msg.mid;
            messageContainer.msg.seq = eventContainer.mMessageContainer.msg.seq;
            messageContainer.msg.setSend_status(eventContainer.mMessageContainer.msg.send_status);
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
        LogUtils.debugInfo(TAG, "data ----------= " + new String(data));
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
        MessageContainer messageContainer = null;
        if (dst1.size() >= 3) {
            messageContainer = cancleTimeoutListen(dst1.get(2).toString());
            LogUtils.debugInfo("messageContainer = " + messageContainer);
        } else {
            messageContainer = cancleTimeoutListen(0 + "");
        }
        if (messageContainer != null) {
            eventContainer.mMessageContainer = messageContainer;
        } else {
            eventContainer.mMessageContainer = new MessageContainer();
            eventContainer.mMessageContainer.msg = new Message();
        }
        eventContainer.mMessageContainer.msg.setSend_status(MessageStatus.SEND_FAIL);// 标记发送失败
        try {
            JSONObject jsonObject = new JSONObject(dst1.get(1).toString());
            if (jsonObject.has("code")) {
                eventContainer.err = jsonObject.getInt("code");
                eventContainer.mMessageContainer.msg.err = eventContainer.err;
            }
            if (jsonObject.has("msg")) {
                eventContainer.errMsg = jsonObject.getString("msg");
            }
            if (jsonObject.has("blk")) {
                eventContainer.blk = jsonObject.getBoolean("blk");
            }
            if (jsonObject.has("expire")) {
                eventContainer.expire = jsonObject.getInt("expire");
                eventContainer.mMessageContainer.msg.expire = eventContainer.expire;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (dst1.size() >= 3) {
            cancleTimeoutListen(dst1.get(2).toString());
        } else {
            cancleTimeoutListen(0 + "");
        }
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
        return TimeOutTaskManager.getInstance().cancleTimeoutTask(str_id);
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
    public boolean checkDuplicateMessages(EventContainer eventContainer) {
        LogUtils.debugInfo("eventContainer = " + eventContainer.toString());
        if ((eventContainer.mEvent.equals(ImService.MSG)
                || eventContainer.mEvent.equals(ImService.MSG_ACK))
                && eventContainer.mMessageContainer != null
                && eventContainer.mMessageContainer.msg != null) {
            if (!MessageDao.getInstance(getApplicationContext()).hasMessage(eventContainer.mMessageContainer.msg.mid)) {
                Conversation conversation = ConversationDao.getInstance(getApplicationContext()).getConversationByCid(eventContainer
                        .mMessageContainer.msg.cid);
                if (conversation == null) {// 获取服务器对话信息
                    mEventContainerCache.put(eventContainer.mMessageContainer.msg.cid, eventContainer);
                    /**
                     * 获取对话信息
                     */
                    mService.sendGetConversatonInfo(eventContainer.mMessageContainer.msg.cid, "");
                }
                MessageDao.getInstance(getApplicationContext()).insertOrUpdateMessage(eventContainer.mMessageContainer.msg);
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
        if (connected) {
            sendDisconnectedMsg(code, reason);
        }
        connected = false;
        isDisconnecting = false;

        switch (code) {
            /**
             * 主动断开
             */
            case WebSocket.ConnectionHandler.CLOSE_NORMAL:
                break;
            /**
             * 无法连接到服务器（主要是网络太差出现）,或者服务器拒绝
             */
            case WebSocket.ConnectionHandler.CLOSE_CANNOT_CONNECT:
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        socketReconnect();
                    }
                }, DELAY_RECONNECT_TIME);
                break;
            /**
             * 意外的失去了先前建立的连接
             */
            case WebSocket.ConnectionHandler.CLOSE_CONNECTION_LOST:
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        socketReconnect();
                    }
                }, DELAY_RECONNECT_TIME);

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
            default:

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
        if (System.currentTimeMillis() - mDisconnectStartTime > DISCONNECT_NOTIFY_TIME) {
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
    private boolean socketReconnect() {
        try {
            if (isNeedReConnected && mIMConfig != null) {
                if (mService.isConnected()) {
                    if (!isDisconnecting) {
                        LogUtils.debugInfo(TAG, "----------socketReconnect---by own---");
                        mService.disconnect();
                        isDisconnecting = true;
                    }
                } else {
                    if (DeviceUtils.netIsConnected(getApplicationContext())) {
                        LogUtils.debugInfo(TAG, "----------socketReconnect------");
                        changeHeartBeatRateByReconnect();
                        isConnecting = true;
                        mService.reconnect();
                        resetTime();
                        if (mDisconnectStartTime == 0) {
                            mDisconnectStartTime = System.currentTimeMillis();
                        }
                        return true;
                    }
                }
            }

        } catch (IllegalStateException ignored) {
        }
        return false;
    }

    /**
     * 断开连接
     */
    private boolean disConnect() {
        isNeedReConnected = false;
        mService.disconnect();
        isDisconnecting = true;
        return true;
    }

    /**
     * 开启连接
     */
    private boolean reConnect() {
        isNeedReConnected = true;
        return socketReconnect();
    }

    /**
     * 网络变化重连socket
     */
    public class SocketRetryReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            long nowRate = changeHeartBeatRate();
            LogUtils.debugInfo(TAG, "...nowRate..." + nowRate);
            socketReconnect();

        }
    }

}