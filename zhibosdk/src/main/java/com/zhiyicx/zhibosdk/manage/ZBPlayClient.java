package com.zhiyicx.zhibosdk.manage;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.zhiyicx.old.imsdk.db.dao.ConversationDao;
import com.zhiyicx.old.imsdk.entity.ChatRoomContainer;
import com.zhiyicx.old.imsdk.entity.ChatRoomDataCount;
import com.zhiyicx.old.imsdk.entity.Conversation;
import com.zhiyicx.old.imsdk.entity.Message;
import com.zhiyicx.old.imsdk.entity.MessageType;
import com.zhiyicx.old.imsdk.manage.ChatRoomClient;
import com.zhiyicx.old.imsdk.manage.listener.ImMsgReceveListener;
import com.zhiyicx.old.imsdk.manage.listener.ImStatusListener;
import com.zhiyicx.old.imsdk.manage.listener.ImTimeoutListener;
import com.zhiyicx.old.imsdk.service.SocketService;
import com.zhiyicx.zhibosdk.ZBSmartLiveSDK;
import com.zhiyicx.zhibosdk.di.component.DaggerZBPlayClientComponent;
import com.zhiyicx.zhibosdk.di.module.ZBPlayModule;
import com.zhiyicx.zhibosdk.manage.listener.OnCommonCallbackListener;
import com.zhiyicx.zhibosdk.manage.listener.OnIMMessageTimeOutListener;
import com.zhiyicx.zhibosdk.manage.listener.OnImListener;
import com.zhiyicx.zhibosdk.manage.listener.OnImStatusListener;
import com.zhiyicx.zhibosdk.manage.listener.OnPlayStartListenr;
import com.zhiyicx.zhibosdk.manage.listener.OnVideoPlayCompletionListener;
import com.zhiyicx.zhibosdk.manage.listener.OnVideoStartPlayListener;
import com.zhiyicx.zhibosdk.manage.soupport.PlayClientSupport;
import com.zhiyicx.zhibosdk.model.LivePlayModel;
import com.zhiyicx.zhibosdk.model.api.ZBApi;
import com.zhiyicx.zhibosdk.model.api.ZBContants;
import com.zhiyicx.zhibosdk.model.entity.ZBApiImInfo;
import com.zhiyicx.zhibosdk.model.entity.ZBApiPlay;
import com.zhiyicx.zhibosdk.model.entity.ZBBaseJson;
import com.zhiyicx.zhibosdk.model.entity.ZBUserAuth;
import com.zhiyicx.zhibosdk.policy.OnReconnetListener;
import com.zhiyicx.zhibosdk.policy.ReconnetPolicy;
import com.zhiyicx.zhibosdk.policy.impl.ReconnectPolicyImpl;
import com.zhiyicx.zhibosdk.receiver.NetChangeReceiver;
import com.zhiyicx.zhibosdk.utils.CommonUtils;
import com.zhiyicx.zhibosdk.utils.LogUtils;
import com.zhiyicx.zhibosdk.widget.ZBMediaPlayer;
import com.zhiyicx.zhibosdk.widget.ZBVideoView;

import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by jungle on 16/7/15.
 * com.zhiyicx.zhibosdk.manage.soupport
 * zhibo_android
 * email:335891510@qq.com
 */
public class ZBPlayClient implements PlayClientSupport, ImMsgReceveListener, ImStatusListener, ImTimeoutListener {
    private static final String TAG = "ZBPlayClient";
    private volatile static ZBPlayClient sZBPlayClient;
    private ZBVideoView mZBVideoView;
    private String videoPath;
    private ReconnetPolicy mReconnetPolicy;
    private long mUpdateTime;
    private boolean isShutdown;
    private boolean isFrist = true;
    private static final long ERRO_SPAN_TIME = 3 * 1000;
    private boolean isLiving;//是否播放的是直播
    private ZBApiImInfo mImInfo = new ZBApiImInfo();
    private String uisd;//主播的id

    /**
     * 点赞处理
     */
    private long LAST_CLICK_ZAN = 0L;//记录最后一次点赞的时间
    private static final long TIME_CLICK_ZAN = 1000 * 5;//5s内没有点赞发送点赞数量；
    private int mTotalZanNums = 0;//为发送到服务器的赞数量
    private int mZanNums = 0;//当前发送的赞数量
    private Thread mZanThread;
    private boolean isExit = false;

    @Inject
    LivePlayModel mModel;
    @Inject
    Context mContext;
    private Subscription mStartPlaySubscription;
    private Subscription mStartVideoSubscription;
    private ChatRoomClient mChatRoomClient;

    public void setReconnetListener(OnReconnetListener onReconnetListener) {
        mOnReconnetListener = onReconnetListener;
    }

    /**
     * 重连状态通知
     */
    private OnReconnetListener mOnReconnetListener;

    public void setOnPlayStartListenr(OnPlayStartListenr onPlayStartListenr) {
        mOnPlayStartListenr = onPlayStartListenr;
    }

    private OnPlayStartListenr mOnPlayStartListenr;

    public void setOnVideoPlayCompletionListener(OnVideoPlayCompletionListener onVideoPlayCompletionListener) {
        mOnVideoPlayCompletionListener = onVideoPlayCompletionListener;
    }

    private OnVideoPlayCompletionListener mOnVideoPlayCompletionListener;

    public void setOnImListener(OnImListener onImListener) {
        mOnImListener = onImListener;
    }

    private OnImListener mOnImListener;

    public void setOnIMMessageTimeOutListener(OnIMMessageTimeOutListener onIMMessageTimeOutListener) {
        mOnIMMessageTimeOutListener = onIMMessageTimeOutListener;
    }

    private OnIMMessageTimeOutListener mOnIMMessageTimeOutListener;


    public void setOnImStatusListener(OnImStatusListener onImStatusListener) {
        mOnImStatusListener = onImStatusListener;
    }

    private OnImStatusListener mOnImStatusListener;
    private Handler mHandler;

    private ZBPlayClient() {
        initDagger();
        mReconnetPolicy = new ReconnectPolicyImpl(mContext);
        initReconnectHandler();
    }

    private void initReconectListener() {

        mReconnetPolicy.setCallBack(new ReconnetPolicy.ReconnectPolicyCallback() {
            @Override
            public boolean onReConnect() {
                reconnectOfShutdown(true);//重新连接
                return false;
            }

            @Override
            public void reConnentFailure() {
                LogUtils.debugInfo(TAG, "failure-------");
                if (mOnReconnetListener != null)
                    mOnReconnetListener.reConnentFailure();
            }

            @Override
            public void reconnectStart() {//重连开始执行
                LogUtils.errroInfo(TAG, "start.....");
                if (mOnReconnetListener != null)
                    mOnReconnetListener.reconnectStart();
            }

            @Override
            public void reconnectEnd() {//重连结束
                LogUtils.errroInfo(TAG, "end.....");
            }
        });
    }

    private void initReconnectHandler() {
        mHandler = new Handler(mContext.getMainLooper()) {
            @Override
            public void handleMessage(android.os.Message msg) {

                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        long spanTime = System.currentTimeMillis() - mUpdateTime;//固定时间呢buffer没有回调则认定断开连接
                        if (spanTime >= ERRO_SPAN_TIME) {//断开超过上限时间,提示用户
                            reconnect();
                        }
                        break;
                }

            }
        };
    }

    private void initDagger() {
        DaggerZBPlayClientComponent
                .builder()
                .clientComponent(ZBSmartLiveSDK.getClientComponent())
                .zBPlayModule(new ZBPlayModule())
                .build()
                .inject(this);

    }

    public static ZBPlayClient getInstance() {

        if (sZBPlayClient == null) {
            synchronized (ZBPlayClient.class) {
                if (sZBPlayClient == null) {
                    sZBPlayClient = new ZBPlayClient();
                }
            }
        }
        return sZBPlayClient;
    }


    /**
     * 设置聊天室消息监听
     */
    private void initIMListener() {
        mChatRoomClient.setImMsgReceveListener(this);
        mChatRoomClient.setImStatusListener(this);
        mChatRoomClient.setImTimeoutListener(this);

    }

    private void initListener() {

        mZBVideoView.setOnBufferingUpdateListener(mOnBufferingUpdateListener);
        mZBVideoView.setOnErrorListener(mOnErrorListener);
        mZBVideoView.setOnInfoListener(mOnInfoListener);
        mZBVideoView.setOnCompletionListener(mOnCompletionListener);
    }

    /**
     * 手动重新连接播放
     */
    @Override
    public void reStartConnect() {
        mZBVideoView.setVideoPath(videoPath);
        mZBVideoView.start();
    }

    /**
     * 开始观看直播
     *
     * @param zbVideoView
     * @param uid         主播id
     * @param streamId    流id
     */
    @Override
    public void startLive(ZBVideoView zbVideoView, String uid, String streamId, OnVideoStartPlayListener l) {
        this.mZBVideoView = zbVideoView;
        this.isLiving = true;
        initListener();
        getLiveUrl(uid, streamId, l);
    }

    /**
     * @param uid
     * @param streamId
     */
    private void getLiveUrl(final String uid, String streamId, final OnVideoStartPlayListener l) {
        final ZBUserAuth userAuth = getZBUserAuth();
        mStartPlaySubscription = mModel.getPlayUrl(userAuth.getAk()
                , uid
                , streamId)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                    }
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ZBApiPlay>() {
                    @Override
                    public void call(ZBApiPlay apiPlay) {
                        if (apiPlay.code.equals(ZBApi.REQUEST_SUCESS)) {
                            checkData(apiPlay);
                        }
                        else {
                            if (l != null)
                                l.onFail(apiPlay.code, apiPlay.message);
                        }
                    }

                    private void checkData(ZBApiPlay apiPlay) {
                        if (apiPlay.data.code == null) {//正在直播
                            mImInfo = apiPlay.data.im;
                            // TODO: 16/8/17 测试cid
//                            mImInfo.cid = 4;

                            saveConversation();
                            uisd = apiPlay.data.user.getUsid();
                            if (apiPlay.data.playurls.rtmp != null) {
                                videoPath = apiPlay.data.playurls.rtmp;
                                mZBVideoView.setVideoPath(apiPlay.data.playurls.rtmp);
                                mZBVideoView.start();

                                mChatRoomClient = new ChatRoomClient(mImInfo.cid, userAuth.getUsid(), mContext);//创建聊天室

                                mChatRoomClient.joinRoom();
                                initReconectListener();
                                initIMListener();
                                if (l != null)
                                    l.onSuccess();
                            }
                            else {
                                if (l != null)
                                    l.onFail("", "播放地址异常");
                            }
                        }
                        else if (apiPlay.data.code.equals(ZBApi.REQUEST_LIVE_END)) {//直播结束
                            if (l != null)
                                l.onLiveEnd(new Gson().toJson(apiPlay), uid);
                        }
                        else {//错误
                            if (l != null)
                                l.onFail(apiPlay.data.code, apiPlay.data.message);
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

    /**
     * 保存对话信息
     */
    private void saveConversation() {
        Conversation conversation = new Conversation();
        conversation.setCid(mImInfo.cid);
        conversation.setIm_uid(mImInfo.im_uid);
        conversation.setType(Conversation.CONVERSATION_TYPE_CHAROOM);
        ConversationDao.getInstance(mContext).insertConversation(conversation);
    }

    @Override
    public void startVedio(ZBVideoView zbVideoView, final String vid, final OnVideoStartPlayListener l) {
        this.mZBVideoView = zbVideoView;
        this.isLiving = false;
        initListener();
        getVideoUrl(vid, l);


    }

    private void getVideoUrl(String vid, final OnVideoStartPlayListener l) {
        ZBUserAuth userAuth = getZBUserAuth();

        mStartVideoSubscription = mModel.getVideoUrl(userAuth.getAk()
                , vid)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                    }
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .finallyDo(new Action0() {
                    @Override
                    public void call() {
                    }
                }).subscribe(new Action1<ZBApiPlay>() {
                    @Override
                    public void call(ZBApiPlay apiPlay) {
                        if (apiPlay.code.equals(ZBApi.REQUEST_SUCESS)) {
                            videoPath = apiPlay.data.playurls.http;
                            mZBVideoView.setVideoPath(videoPath);
                            mZBVideoView.start();
                            if (l != null)
                                l.onSuccess();

                        }
                        else {
                            if (l != null)
                                l.onFail(apiPlay.code, apiPlay.message);
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

    @Override
    public void onResume() {
        mZBVideoView.start();
    }

    @org.simple.eventbus.Subscriber(tag = NetChangeReceiver.RECONNECT_STREAM_OF_SHUTDOWN, mode = ThreadMode.ASYNC)
    public void reconnectOfShutdown(boolean b) {
        LogUtils.errroInfo(TAG, "reconnectOfShutdown.......");
        if (isShutdown) {
            Observable.just(1)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Integer>() {
                        @Override
                        public void call(Integer integer) {
                            mZBVideoView.setVideoPath(videoPath);
                            mZBVideoView.start();
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    });
        }
    }

    @Subscriber(tag = NetChangeReceiver.NETWORK_DISCONNECT, mode = ThreadMode.POST)
    public void netDisconnect(boolean b) {
        isShutdown = true;//标记当前播放是否中断
    }

    private ZBMediaPlayer.OnCompletionListener mOnCompletionListener = new ZBMediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion() {
            LogUtils.debugInfo(TAG, "Play Completed !");
            if (isLiving) {
                reconnect();//发送重连信息
            }
            else {
                if (mOnVideoPlayCompletionListener != null)
                    mOnVideoPlayCompletionListener.onVideoPlayCompleted();

            }


        }
    };

    private ZBMediaPlayer.OnBufferingUpdateListener mOnBufferingUpdateListener = new ZBMediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(int precent) {
            mUpdateTime = System.currentTimeMillis();
            LogUtils.errroInfo(TAG, "onBufferingUpdate--------->" + precent);
            if (isShutdown) {//由于此回调是频繁回调,所以在直播流断开时才做操作
                isShutdown = false;
                mOnReconnetListener.reconnectScuccess();//告诉用户重连成功,做相应的ui处理
                mReconnetPolicy.reconnectSuccess();//告诉重连机制连接成功,取消重连
            }
        }
    };


    private ZBMediaPlayer.OnInfoListener mOnInfoListener = new ZBMediaPlayer.OnInfoListener() {
        @Override
        public boolean onInfo(int what, int extra) {
            LogUtils.debugInfo(TAG, "onInfo: " + what + ", " + extra);
            if (what == ZBMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {//开始播放

                if (isFrist) {//因为此回调可能在重新播放的时候再次回调所以保证只调用一次此方法
                    isFrist = false;
                    if (mOnPlayStartListenr != null) {
                        mOnPlayStartListenr.onPlayStart();
                    }
                }
            }
            else if (what == ZBMediaPlayer.MEDIA_INFO_BUFFERING_END) {
                if (isLiving) sendReconnectMessage();//如果是直播,发送重连信息
            }

            return false;
        }
    };

    /**
     * 发送重连信息
     */
    public void sendReconnectMessage() {
        mHandler.sendEmptyMessageDelayed(0, ERRO_SPAN_TIME);
    }


    private ZBMediaPlayer.OnErrorListener mOnErrorListener = new ZBMediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(int errorCode) {
            Log.e(TAG, "Error happened, errorCode = " + errorCode);
            switch (errorCode) {
                case ZBMediaPlayer.ERROR_CODE_INVALID_URI:
                    LogUtils.debugInfo(TAG, "Invalid URL !");
                    break;
                case ZBMediaPlayer.ERROR_CODE_404_NOT_FOUND:
                    LogUtils.debugInfo(TAG, "404 resource not found !");
                    break;
                case ZBMediaPlayer.ERROR_CODE_CONNECTION_REFUSED:
                    LogUtils.debugInfo(TAG, "Connection refused !");
                    break;
                case ZBMediaPlayer.ERROR_CODE_CONNECTION_TIMEOUT:
                    LogUtils.debugInfo(TAG, "Connection timeout !");
                    break;
                case ZBMediaPlayer.ERROR_CODE_EMPTY_PLAYLIST:
                    LogUtils.debugInfo(TAG, "Empty playlist !");
                    break;
                case ZBMediaPlayer.ERROR_CODE_STREAM_DISCONNECTED:
                    LogUtils.debugInfo(TAG, "Stream disconnected !");
                    break;
                case ZBMediaPlayer.ERROR_CODE_IO_ERROR:
                    LogUtils.debugInfo(TAG, "Network IO Error !");
                    break;
                case ZBMediaPlayer.MEDIA_ERROR_UNKNOWN:
                default:
                    LogUtils.debugInfo(TAG, "unknown error !");
                    break;
            }
            reconnect();//发生错误尝试重连
            // Todo pls handle the error status here, retry or call finish()

            // If you want to retry, do like this:
            // mVideoView.setVideoPath(mVideoPath);
            // mVideoView.start();
            // Return true means the error has been handled
            // If return false, then `onCompletion` will be called
            return true;
        }
    };


    /**
     * 重连
     */
    @Override
    public void reconnect() {
        isShutdown = true;
        mReconnetPolicy.shutDown();
    }

    @Override
    public void onPause() {
        mZBVideoView.pause();
    }

    @Override
    public void onDestroy() {
        mZBVideoView.stopPlayback();

        isExit = true;
        mHandler.removeCallbacksAndMessages(null);//取消所有消息
        mReconnetPolicy.stop();
        sZBPlayClient = null;
        unSubscribe(mStartPlaySubscription);
        if (mChatRoomClient != null) {
            mChatRoomClient.sendLeaveRoomMessage();
            TimerTask task = new TimerTask() {
                public void run() {
                    mChatRoomClient.leaveRoom();
                    mChatRoomClient.onDestroy();
                }
            };
            Timer timer = new Timer();
            timer.schedule(task, 1000);
        }

    }

    @Override
    public ZBApiImInfo getPresenterImInfo() {
        if (mImInfo == null) throw new IllegalAccessError("startLive not call");
//        if (true) throw new IllegalArgumentException("timeout < 0");
        return mImInfo;
    }

    @Override
    public void sendTextMsg(String text) {
        if (mChatRoomClient != null)
            mChatRoomClient.sendTextMsg(text);
    }

    @Override
    public void sendGiftMessage(final Map jsonstr, String gift_code, String count, final OnCommonCallbackListener l) {
        dealGift(jsonstr, gift_code, count, l);
    }

    private void dealGift(final Map jsonstr, String gift_code, String count, final OnCommonCallbackListener l) {
        if (mChatRoomClient != null) {
            ZBUserAuth userAuth = getZBUserAuth();
            if (userAuth == null) return;
            mModel.sendGift(userAuth.getAk(), uisd, gift_code, count)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<ZBBaseJson<String>>() {
                        @Override
                        public void call(ZBBaseJson<String> stringZBBaseJson) {
                            if (stringZBBaseJson.code.equals(ZBApi.REQUEST_SUCESS)) {
                                mChatRoomClient.sendGiftMessage(jsonstr);
                                if (l != null)
                                    l.onSuccess();
                            }
                            else {
                                if (l != null)
                                    l.onFail(stringZBBaseJson.code, stringZBBaseJson.message);
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

    @Nullable
    private ZBUserAuth getZBUserAuth() {
        ZBUserAuth userAuth = ZBContants.getmUserAuth(CommonUtils.MD5encode(ZBApi.getContantsSeckret()));
        if (userAuth == null) {
            LogUtils.errroInfo("ticket验证失败！");
            return null;
        }
        return userAuth;
    }

    @Override
    public void sendZan(int type) {
        dealZan(type);
    }

    /**
     * 处理送赞
     *
     * @param type
     */
    private void dealZan(int type) {
        if (mChatRoomClient != null) {
            mChatRoomClient.sendZan(type);
            mTotalZanNums++;
            LAST_CLICK_ZAN = System.currentTimeMillis();
            if (mZanThread == null || !mZanThread.isAlive()) {
                mZanThread = new Thread(mZanRunnable);
                mZanThread.start();
            }
        }
    }


    /**
     * 异步放赞到服务器
     */
    private Runnable mZanRunnable = new Runnable() {
        @Override
        public void run() {
            while (!isExit) {
                if (System.currentTimeMillis() - LAST_CLICK_ZAN >= TIME_CLICK_ZAN && mTotalZanNums > 0) {
                    //发送统计的赞
                    mZanNums = mTotalZanNums;
                    sendZan2Service();
                    break;

                }
                else {
                    //防止线程占用cpu过高
                    try {
                        Thread.sleep(TIME_CLICK_ZAN);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };

    private void sendZan2Service() {
        ZBUserAuth userAuth = getZBUserAuth();
        mModel.sendZan(userAuth.getAk(), uisd, mZanNums + "")
                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<ZBBaseJson<String>>() {
                    @Override
                    public void call(ZBBaseJson<String> stringZBBaseJson) {
                        if (stringZBBaseJson.code.equals(ZBApi.REQUEST_SUCESS)) {
                            mTotalZanNums -= mZanNums;
                        }

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });

    }

    @Override
    public void sendAttention() {
        if (mChatRoomClient != null)
            mChatRoomClient.sendAttention();
    }

    @Override
    public void sendMessage(boolean isEnable, Map jsonstr, int customId) {
        if (mChatRoomClient != null)
            mChatRoomClient.sendMessage(isEnable, jsonstr, customId);
    }

    @Override
    public void sendMessage(Message message) {
        if (mChatRoomClient != null)
            mChatRoomClient.sendMessage(message);
    }

    @Override
    public void mc() {
        if (mChatRoomClient != null)
            mChatRoomClient.mc();
    }


    private void unSubscribe(Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();//保证activity结束时取消所有正在执行的订阅
        }
    }

    @Override
    public void onMessageReceived(Message message) {
        if (message.cid != mImInfo.cid) return;//丢去不是当前房间的消息


        if (message.ext != null) {
            switch (message.ext.customID) {
                case MessageType.MESSAGE_CUSTOM_ID_JOIN_CHATROOM:
                    if (mOnImListener != null)
                        mOnImListener.onSomeBodyJoinMessageReceived(message.ext.ZBUSID);
                    break;

                case MessageType.MESSAGE_CUSTOM_ID_LEAVE_CHATROOM:
                    if (mOnImListener != null)
                        mOnImListener.onSomeBodyLeaveMessageReceived(message.ext.ZBUSID);
                    break;
                case MessageType.MESSAGE_CUSTOM_ID_GIFT:
                    if (mOnImListener != null)
                        mOnImListener.onGiftReceived(message);
                    break;
                case MessageType.MESSAGE_CUSTOM_ID_ZAN:
                    if (mOnImListener != null)
                        mOnImListener.onZanReceived(message);
                    break;
                case MessageType.MESSAGE_CUSTOM_ID_FLLOW:
                    if (mOnImListener != null)
                        mOnImListener.onAttentionMessageReceived(message);
                    break;
                case MessageType.MESSAGE_CUSTOM_ID_DATACOUNT:
                    ChatRoomDataCount chatRoomDataCount = new Gson().fromJson(message.ext.custom.toString(), ChatRoomDataCount.class);
                    if (mOnImListener != null)
                        mOnImListener.onChatRoomDataCountReceived(chatRoomDataCount);
                    break;
                case MessageType.MESSAGE_CUSTOM_ID_CONVERSATION_END:
                    if (mOnImListener != null)
                        mOnImListener.onConvrEnd(message.cid);

                    break;
                case MessageType.MESSAGE_CUSTOM_ID_SYSTEM_TIP:
                    if (mOnImListener != null)
                        mOnImListener.onSystemMessageReceived(message.txt);
                    break;

                default:
                    if (mOnImListener != null)
                        mOnImListener.onMessageReceived(message);
                    break;

            }
        }
        else {
            if (mOnImListener != null)
                mOnImListener.onMessageReceived(message);
        }


    }

    @Override
    public void onMessageACKReceived(Message message) {

        switch (message.err) {
            /**
             * 成功
             */
            case SocketService.SUCCESS_CODE:
                if (mOnImListener == null) return;
                mOnImListener.onMessageACK(message);
                break;
            /**
             * 被禁言
             */
            case SocketService.CHATROOM_BANNED_WORDS:
                // TODO: 16/5/30 被禁言后的操作
                if (mOnImListener == null) return;
                mOnImListener.onBanned(message.expire);
                break;
            /**
             * 消息发送失败重新加入聊天室
             */
            case SocketService.CHATROOM_SEND_MESSAGE_FAILED:
            case SocketService.CHATROOM_NOT_JOIN_ROOM:
                if (mChatRoomClient != null && mImInfo.cid != 0)
                    mChatRoomClient.joinRoom();
            default:
                LogUtils.debugInfo(message.err + "");
                break;


        }
    }

    @Override
    public void onConversationJoinACKReceived(ChatRoomContainer chatRoomContainer) {
        if (chatRoomContainer.mChatRooms.get(0).cid != mImInfo.cid)
            return;//丢去不是当前房间的消息
        if (chatRoomContainer.err == SocketService.SUCCESS_CODE) {//没有发生错误
//            mRootView.joinedChatroom(chatRoomContainer);
            if (chatRoomContainer.mChatRooms.get(0).expire != -1) {//被禁言
                if (mOnImListener != null)
                    mOnImListener.onBanned(chatRoomContainer.mChatRooms.get(0).expire);
//                mRootView.setbanneded(true, chatRoomContainer.mChatRooms.get(0).gag);
            }
        }
        else {
            LogUtils.debugInfo(chatRoomContainer.err + "");
        }
    }

    @Override
    public void onConversationLeaveACKReceived(ChatRoomContainer chatRoomContainer) {
        if (chatRoomContainer.mChatRooms.get(0).cid != mImInfo.cid)
            return;//丢去不是当前房间的消息
        if (chatRoomContainer.err == SocketService.SUCCESS_CODE) {//没有发生错误
//            mRootView.leavedChatroom(chatRoomContainer);

        }
        else {
            LogUtils.debugInfo(chatRoomContainer.err + "");
        }
    }

    @Override
    public void onConversationMCACKReceived(List<Conversation> conversations) {
        if (conversations.get(0).getCid() != mImInfo.cid)
            return;//丢去不是当前房间的消息

    }

    @Override
    public void onConnected() {

        if (mChatRoomClient != null && mImInfo.cid != 0)
            mChatRoomClient.joinRoom();
        //加入聊天室
        if (mOnImStatusListener != null)
            mOnImStatusListener.onConnected();
    }

    @Override
    public void onDisconnect(int code, String reason) {
        if (mOnImStatusListener != null)
            mOnImStatusListener.onDisconnect(code, reason);
    }

    @Override
    public void onError(Exception error) {

    }

    @Override
    public void onMessageTimeout(com.zhiyicx.old.imsdk.entity.Message message) {
        if (mOnIMMessageTimeOutListener != null)
            mOnIMMessageTimeOutListener.onMessageTimeout(message);
    }

    @Override
    public void onConversationJoinTimeout(int roomId) {

    }

    @Override
    public void onConversationLeaveTimeout(int roomId) {

    }

    @Override
    public void onConversationMcTimeout(List<Integer> roomIds) {

    }
}
