package com.zhiyicx.zhibosdk.manage;

import android.content.Context;
import android.hardware.Camera;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.ViewGroup;

import com.qiniu.pili.droid.streaming.AVCodecType;
import com.qiniu.pili.droid.streaming.CameraStreamingSetting;
import com.qiniu.pili.droid.streaming.MediaStreamingManager;
import com.qiniu.pili.droid.streaming.MicrophoneStreamingSetting;
import com.qiniu.pili.droid.streaming.StreamStatusCallback;
import com.qiniu.pili.droid.streaming.StreamingPreviewCallback;
import com.qiniu.pili.droid.streaming.StreamingProfile;
import com.qiniu.pili.droid.streaming.StreamingState;
import com.qiniu.pili.droid.streaming.StreamingStateChangedListener;
import com.qiniu.pili.droid.streaming.SurfaceTextureCallback;
import com.qiniu.pili.droid.streaming.WatermarkSetting;
import com.qiniu.pili.droid.streaming.widget.AspectFrameLayout;
import com.zhiyicx.old.imsdk.db.dao.ConversationDao;
import com.zhiyicx.old.imsdk.entity.ChatRoomContainer;
import com.zhiyicx.old.imsdk.entity.ChatRoomDataCount;
import com.zhiyicx.old.imsdk.entity.Conversation;
import com.zhiyicx.old.imsdk.entity.MessageType;
import com.zhiyicx.old.imsdk.manage.ChatRoomClient;
import com.zhiyicx.old.imsdk.manage.listener.ImMsgReceveListener;
import com.zhiyicx.old.imsdk.manage.listener.ImStatusListener;
import com.zhiyicx.old.imsdk.manage.listener.ImTimeoutListener;
import com.zhiyicx.old.imsdk.service.SocketService;
import com.zhiyicx.zhibosdk.ZBSmartLiveSDK;
import com.zhiyicx.zhibosdk.di.component.DaggerZBStreamingClientComponent;
import com.zhiyicx.zhibosdk.di.module.ZBStremingModule;
import com.zhiyicx.zhibosdk.manage.listener.OnCloseStatusListener;
import com.zhiyicx.zhibosdk.manage.listener.OnCommonCallbackListener;
import com.zhiyicx.zhibosdk.manage.listener.OnIMMessageTimeOutListener;
import com.zhiyicx.zhibosdk.manage.listener.OnImListener;
import com.zhiyicx.zhibosdk.manage.listener.OnImStatusListener;
import com.zhiyicx.zhibosdk.manage.listener.OnLiveStartPlayListener;
import com.zhiyicx.zhibosdk.manage.listener.OncheckSteamStatusListener;
import com.zhiyicx.zhibosdk.manage.listener.ZBFrameCapturedCallback;
import com.zhiyicx.zhibosdk.manage.listener.ZBStreamingPreviewListener;
import com.zhiyicx.zhibosdk.manage.listener.ZBSurfaceTextureListener;
import com.zhiyicx.zhibosdk.manage.soupport.StreamingSoupport;
import com.zhiyicx.zhibosdk.model.PublishModel;
import com.zhiyicx.zhibosdk.model.api.ZBApi;
import com.zhiyicx.zhibosdk.model.api.ZBContants;
import com.zhiyicx.zhibosdk.model.entity.ZBApiImInfo;
import com.zhiyicx.zhibosdk.model.entity.ZBApiJson;
import com.zhiyicx.zhibosdk.model.entity.ZBApiStream;
import com.zhiyicx.zhibosdk.model.entity.ZBBaseJson;
import com.zhiyicx.zhibosdk.model.entity.ZBCheckStreamPullJson;
import com.zhiyicx.zhibosdk.model.entity.ZBEndStreamJson;
import com.zhiyicx.zhibosdk.model.entity.ZBIconInfo;
import com.zhiyicx.zhibosdk.model.entity.ZBUserAuth;
import com.zhiyicx.zhibosdk.policy.OnNetworkJitterListener;
import com.zhiyicx.zhibosdk.policy.OnReconnetListener;
import com.zhiyicx.zhibosdk.policy.ReconnetPolicy;
import com.zhiyicx.zhibosdk.policy.impl.ReconnectPolicyImpl;
import com.zhiyicx.zhibosdk.receiver.NetChangeReceiver;
import com.zhiyicx.zhibosdk.utils.CommonUtils;
import com.zhiyicx.zhibosdk.utils.LogUtils;
import com.zhiyicx.zhibosdk.widget.ZBAspectFrameLayout;

import org.json.JSONException;
import org.json.JSONObject;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.io.File;
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
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * Created by jungle on 16/7/5.
 * 推流配置 480p 最大 25 fps
 *
 * com.zhiyicx.zhibo.manage
 * zhibo_android
 * email:335891510@qq.com
 */
public class ZBStreamingClient implements StreamingSoupport, ImMsgReceveListener, ImStatusListener, ImTimeoutListener {
    private static final String TAG = ZBStreamingClient.class.getSimpleName();

    private static volatile ZBStreamingClient sZBStreamingClient;

    public static final int HANDLER_NORMAL = 2;
    public static final int HANDLER_WEAK = 0;
    public static final int HANDLER_BAD = 1;
    public static final int SWITCH_CAMEA = 3;
    private Subscription mStartSubscription;
    private Subscription mUploadSubscription;
    private ReconnetPolicy mReconnetPolicy;
    private MediaStreamingManager mCameraStreamingManager;
    private StreamingProfile mProfile;

    private boolean isReady;
    public boolean isStreaming;
    private boolean isStart;

    private Status currentStatus = Status.normal;
    private final int maxFps = 30;
    private final int checkTime = 15 * 1000;
    private int weakTimes;
    private int BadTimes;
    private int normalTimes;
    private boolean isBackCamera;

    private String mStream;
    private ChatRoomClient mChatRoomClient;//聊天室

    private ChatRoomDataCount mChatRoomDataCount = new ChatRoomDataCount();//聊天室人数 浏览量 统计
    private final static long CHATROOM_MC_TIME = 1000 * 30;//获取房间人数间隔时间


    private Timer mcTimer;
    private ZBStreamingPreviewListener mZBStreamingPreviewListener;
    private ZBSurfaceTextureListener mZBSurfaceTextureListener;

    /**
     * 重连状态通知
     */
    private OnReconnetListener mOnReconnetListener;
    /**
     * 网络抖动监听
     */
    private OnNetworkJitterListener mNetworkJitterListener;
    @Inject
    PublishModel mModel;
    @Inject
    Context mContext;
    private String mStreamId;
    private ZBApiImInfo mImInfo = new ZBApiImInfo();

    public void setOnImListener(OnImListener onImListener) {
        mOnImListener = onImListener;
    }

    private OnImListener mOnImListener;

    public void setOnImStatusListener(OnImStatusListener onImStatusListener) {
        mOnImStatusListener = onImStatusListener;
    }

    private OnImStatusListener mOnImStatusListener;

    public void setOnIMMessageTimeOutListener(OnIMMessageTimeOutListener onIMMessageTimeOutListener) {
        mOnIMMessageTimeOutListener = onIMMessageTimeOutListener;
    }

    private OnIMMessageTimeOutListener mOnIMMessageTimeOutListener;


    public void setReconnetListener(OnReconnetListener onReconnetListener) {
        mOnReconnetListener = onReconnetListener;
    }

    public void setZBSurfaceTextureListener(ZBSurfaceTextureListener ZBSurfaceTextureListener) {
        mZBSurfaceTextureListener = ZBSurfaceTextureListener;
    }

    public void setZBStreamingPreviewListener(ZBStreamingPreviewListener ZBStreamingPreviewListener) {
        mZBStreamingPreviewListener = ZBStreamingPreviewListener;
    }


    public void setNetworkJitterListener(OnNetworkJitterListener networkJitterListener) {
        mNetworkJitterListener = networkJitterListener;
    }

    @Override
    public void onMessageReceived(com.zhiyicx.old.imsdk.entity.Message message) {
        if (message.cid != mImInfo.cid) return;//丢去不是当前房间的消息
        if (message.ext != null) {
            switch (message.ext.customID) {
                case MessageType.MESSAGE_CUSTOM_ID_JOIN_CHATROOM:
                    mChatRoomDataCount.setReviewCount(mChatRoomDataCount.getReviewCount() + 1);
                    mChatRoomDataCount.setViererCount(mChatRoomDataCount.getViererCount() + 1);
                    if (mOnImListener != null)
                        mOnImListener.onSomeBodyJoinMessageReceived(message.ext.ZBUSID);
                    break;

                case MessageType.MESSAGE_CUSTOM_ID_LEAVE_CHATROOM:
                    mChatRoomDataCount.setViererCount(mChatRoomDataCount.getViererCount() - 1);
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
        } else {
            if (mOnImListener != null)
                mOnImListener.onMessageReceived(message);
        }
    }

    @Override
    public void onMessageACKReceived(com.zhiyicx.old.imsdk.entity.Message message) {

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
                LogUtils.debugInfo(TAG, message.err + "");
                break;


        }
    }

    @Override
    public void onConversationJoinACKReceived(ChatRoomContainer chatRoomContainer) {
        if (chatRoomContainer.mChatRooms.get(0).cid != mImInfo.cid)
            return;//丢去不是当前房间的消息
        if (chatRoomContainer.err == SocketService.SUCCESS_CODE) {//没有发生错误
            if (mOnImListener != null)
                mOnImListener.onJoinRoomSuccessed();
            if (chatRoomContainer.mChatRooms.get(0).expire != -1) {//被禁言
                if (mOnImListener != null)
                    mOnImListener.onBanned(chatRoomContainer.mChatRooms.get(0).expire);
            }
        } else {
            LogUtils.debugInfo(TAG, chatRoomContainer.err + "");
        }
    }

    @Override
    public void onConversationLeaveACKReceived(ChatRoomContainer chatRoomContainer) {
        if (chatRoomContainer.mChatRooms.get(0).cid != mImInfo.cid)
            return;//丢去不是当前房间的消息
        if (chatRoomContainer.err == SocketService.SUCCESS_CODE) {//没有发生错误

        } else {
            LogUtils.debugInfo(TAG, chatRoomContainer.err + "");
        }
    }

    @Override
    public void onConversationMCACKReceived(List<Conversation> conversations) {
        if (conversations.get(0).getCid() != mImInfo.cid)
            return;//丢去不是当前房间的消息
        mChatRoomDataCount.setViererCount(conversations.get(0).getMc() - 1);//减去主播自己
        if (mChatRoomClient != null)
            mChatRoomClient.sendDataCountMessage(mChatRoomDataCount);
        if (mOnImListener != null)
            mOnImListener.onChatRoomDataCountReceived(mChatRoomDataCount);

    }


    @Override
    public void onConnected() {
        LogUtils.debugInfo("------ZBstreaming-----------onConnected------------" + mImInfo.toString());
        if (mChatRoomClient != null && mImInfo.cid != 0)
            mChatRoomClient.joinRoom();
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
    public void sendTextMsg(String text) {
        if (mChatRoomClient != null)
            mChatRoomClient.sendTextMsg(text);
    }

    @Override
    public void sendGiftMessage(Map jsonstr, String gift_code, String count, OnCommonCallbackListener l) {
        if (mChatRoomClient != null)
            mChatRoomClient.sendGiftMessage(jsonstr);
    }

    @Override
    public void sendZan(int type) {
        if (mChatRoomClient != null)
            mChatRoomClient.sendZan(type);
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
    public void sendMessage(com.zhiyicx.old.imsdk.entity.Message message) {
        if (mChatRoomClient != null)
            mChatRoomClient.sendMessage(message);
    }

    @Override
    public void mc() {
        if (mChatRoomClient != null)
            mChatRoomClient.mc();
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

    StreamStatusCallback streamStatusCallback = new StreamStatusCallback() {
        @Override
        public void notifyStreamStatusChanged(StreamingProfile.StreamStatus streamStatus) {
            LogUtils.debugInfo(TAG, "videoFps:" + streamStatus.videoFps + "--------------bitRate:" + streamStatus.totalAVBitrate);
            configPolicy(streamStatus);//推流配置策略
        }
    };

    enum Status {
        shutdown,
        normal,
        weakNet,
        badNet
    }


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLER_WEAK:
                    if (weakTimes >= 5) {
                        reduceFps();//降低fps
                        LogUtils.errroInfo(TAG, "HANDLER_WEAK");
                    }
                    break;
                case HANDLER_BAD:
                    if (BadTimes >= 5) {
                        reduceFps();
                        if (mNetworkJitterListener != null)
                            mNetworkJitterListener.onNetworkJitter();
                        LogUtils.errroInfo(TAG, "HANDLER_BAD");
                    }
                    break;
                case HANDLER_NORMAL:
                    if (normalTimes >= 5) {
                        improveFps();//提高fps
                        LogUtils.errroInfo(TAG, "HANDLER_NORMAL");
                    }
                    break;
                case SWITCH_CAMEA:
                    LogUtils.errroInfo(TAG, isBackCamera + "");
                    if (isBackCamera) switchCamera();//如果开始直播前是后置摄像头,则切换到前置摄像头
                    break;
                default:

                    break;
            }
        }
    };

    private ZBStreamingClient() {
        initDagger();
    }

    public static ZBStreamingClient getInstance() {

        if (sZBStreamingClient == null) {
            synchronized (ZBStreamingClient.class) {
                if (sZBStreamingClient == null) {
                    sZBStreamingClient = new ZBStreamingClient();
                }
            }
        }
        return sZBStreamingClient;
    }

    @Override
    public void initConfig(ZBAspectFrameLayout aspectFrameLayout, GLSurfaceView gLSurfaceView, WatermarkSetting watermarkSetting) throws JSONException, IllegalAccessException {
        if (mImInfo.cid == 0) {
            throw new IllegalAccessException("还没校验流信息，请先调用ZBStreamingClien.checkStrem()");
        }
        this.mReconnetPolicy = new ReconnectPolicyImpl(mContext);
        StreamingProfile.Stream stream = new StreamingProfile.Stream(new JSONObject(mStream));
        mProfile = new StreamingProfile();

        int videoQuality = 0;
        int audioQuality = 0;
        int encodingLevel = 0;

        videoQuality = StreamingProfile.VIDEO_QUALITY_MEDIUM2;//视频质量
        audioQuality = StreamingProfile.AUDIO_QUALITY_MEDIUM2;
        encodingLevel = StreamingProfile.VIDEO_ENCODING_HEIGHT_720;

        mProfile.setVideoQuality(videoQuality)//流信息
                .setAudioQuality(audioQuality)
                .setEncodingSizeLevel(encodingLevel)
                .setEncoderRCMode(StreamingProfile.EncoderRCModes.QUALITY_PRIORITY)
                .setEncodingOrientation(StreamingProfile.ENCODING_ORIENTATION.PORT)
                .setStream(stream)
                .setStreamStatusConfig(new StreamingProfile.StreamStatusConfig(3))
                .setSendingBufferProfile(new StreamingProfile.SendingBufferProfile(0.2f, 0.8f, 3.0f, 20 * 1000));

        CameraStreamingSetting setting = new CameraStreamingSetting();//相机的信息
        setting.setCameraId(Camera.CameraInfo.CAMERA_FACING_FRONT)
                .setBuiltInFaceBeautyEnabled(false)
                .setVideoFilter(CameraStreamingSetting.VIDEO_FILTER_TYPE.VIDEO_FILTER_BEAUTY)
                .setFaceBeautySetting(new CameraStreamingSetting.FaceBeautySetting(0.8f, 0.6f, 0.8f))
                .setContinuousFocusModeEnabled(true)
                .setCameraPrvSizeLevel(CameraStreamingSetting.PREVIEW_SIZE_LEVEL.LARGE)
                .setCameraPrvSizeRatio(CameraStreamingSetting.PREVIEW_SIZE_RATIO.RATIO_16_9)
                .setResetTouchFocusDelayInMs(3000); // 单位毫秒

        aspectFrameLayout.setShowMode(AspectFrameLayout.SHOW_MODE.FULL);//设置全屏

        mCameraStreamingManager = new MediaStreamingManager(gLSurfaceView.getContext(),//生成七牛的管理类
                aspectFrameLayout,
                gLSurfaceView,
                AVCodecType.HW_VIDEO_WITH_HW_AUDIO_CODEC);
        MicrophoneStreamingSetting mMicrophoneStreamingSetting = new MicrophoneStreamingSetting();
        mMicrophoneStreamingSetting.setBluetoothSCOEnabled(true);
        mCameraStreamingManager.prepare(setting, mMicrophoneStreamingSetting, watermarkSetting, mProfile);//开始准备
        mCameraStreamingManager.setNativeLoggingEnabled(true);//Log 管理
//        mCameraStreamingManager.setStreamStatusCallback(new StreamStatusCallback() {
//            @Override
//            public void notifyStreamStatusChanged(StreamingProfile.StreamStatus streamStatus) {
//                LogUtils.debugInfo(TAG, "videoFps:" + streamStatus.videoFps + "--------------bitRate:" + streamStatus.totalAVBitrate);
//                configPolicy(streamStatus);//推流配置策略
//            }
//        });
        mCameraStreamingManager.setStreamStatusCallback(streamStatusCallback);
        mCameraStreamingManager.setStreamingStateListener(new StreamingStateChangedListener() {

            @Override
            public void onStateChanged(StreamingState streamingState, Object o) {//回调
                switch (streamingState) {
                    case PREPARING:
                        LogUtils.debugInfo(TAG, "PREPARING");
                        break;
                    case READY:
                        isReady = true;
                        if (isStreaming) {//拉流成功后,遇到其他情况比如关屏后重新连接推流
                            reconnectOfShutdown(true);
                        }
                        LogUtils.debugInfo(TAG, "READY");
                        break;
                    case CONNECTING:
                        LogUtils.debugInfo(TAG, "CONNECTING");
                        break;
                    case STREAMING:
                        LogUtils.debugInfo(TAG, "STREAMING");
                        break;
                    case SHUTDOWN:
                        LogUtils.debugInfo(TAG, "SHUTDOWN");
                        markShutdown();
                        break;
                    case IOERROR:
                        markShutdown();
                        LogUtils.debugInfo(TAG, "IOERROR");
                        break;
//                    case NETBLOCKING:
//                        markShutdown();
//                        LogUtils.debugInfo(TAG, "NETBLOCKING");
//                        break;
//                    case CONNECTION_TIMEOUT:
//                        markShutdown();
//                        LogUtils.debugInfo(TAG, "CONNECTION_TIMEOUT");
//                        break;
                    case UNKNOWN:
                        markShutdown();
                        LogUtils.debugInfo(TAG, "UNKNOWN");
                        break;
                    case DISCONNECTED://网络断开
                        markShutdown();
                        LogUtils.debugInfo(TAG, "DISCONNECTED");
                        break;
                    case CAMERA_SWITCHED:
                        LogUtils.debugInfo(TAG, "CAMERA_SWITCHED");
                        isBackCamera = !isBackCamera;
                        break;
                    case SENDING_BUFFER_EMPTY:
                        break;
                    case SENDING_BUFFER_FULL:
                        break;
                    case AUDIO_RECORDING_FAIL:
                        break;
                        default:
                }
            }

//            @Override
//            public boolean onStateHandled(int i, Object o) {
//                switch (i) {
//                    case CameraStreamingManager.STATE.SENDING_BUFFER_HAS_FEW_ITEMS:
//                        LogUtils.debugInfo(TAG, "SENDING_BUFFER_HAS_FEW_ITEMS");
//                        mProfile.improveVideoQuality(1);
//                        mCameraStreamingManager.setStreamingProfile(mProfile);
//                        return true;
//                    case CameraStreamingManager.STATE.SENDING_BUFFER_HAS_MANY_ITEMS:
//                        LogUtils.debugInfo(TAG, "SENDING_BUFFER_HAS_MANY_ITEMS");
//                        mProfile.reduceVideoQuality(1);
//                        mCameraStreamingManager.setStreamingProfile(mProfile);
//                        return true;
//                    default:
//                        return false;
//                }
//            }
        });


        mCameraStreamingManager.setStreamingPreviewCallback(new StreamingPreviewCallback() {

            @Override
            public boolean onPreviewFrame(byte[] var1, int var2, int var3, int var4, int var5, long var6) {
                if (mZBStreamingPreviewListener != null)
                    return mZBStreamingPreviewListener.onPreviewFrame(var1, var2, var3);
                return false;
            }
        });


        mCameraStreamingManager.setSurfaceTextureCallback(new SurfaceTextureCallback() {
            @Override
            public void onSurfaceCreated() {
                if (mZBSurfaceTextureListener != null)
                    mZBSurfaceTextureListener.onSurfaceCreated();
            }

            @Override
            public void onSurfaceChanged(int i, int i1) {
                if (mZBSurfaceTextureListener != null)
                    mZBSurfaceTextureListener.onSurfaceChanged(i, i1);
            }

            @Override
            public void onSurfaceDestroyed() {
                if (mZBSurfaceTextureListener != null)
                    mZBSurfaceTextureListener.onSurfaceDestroyed();
            }

            @Override
            public int onDrawFrame(int i, int i1, int i2, float[] floats) {
                if (mZBSurfaceTextureListener != null)
                    return mZBSurfaceTextureListener.onDrawFrame(i, i1, i2, floats);
                return i;
            }
        });
    }

    @Override
    public ZBApiImInfo getPresenterImInfo() {
        if (mImInfo == null) throw new IllegalAccessError("checkStrem not call");
        return mImInfo;
    }

    /**
     * 设置Im监听
     */
    private void initImlistener() {
        mChatRoomClient.setImMsgReceveListener(this);
        mChatRoomClient.setImStatusListener(this);
        mChatRoomClient.setImTimeoutListener(this);

    }

    private void initDagger() {
        DaggerZBStreamingClientComponent
                .builder()
                .clientComponent(ZBSmartLiveSDK.getClientComponent())
                .zBStremingModule(new ZBStremingModule())
                .build()
                .inject(this);

    }

    private void markShutdown() {
        currentStatus = Status.shutdown;//标记当前播放是否中断
        reconnect();
    }

    @org.simple.eventbus.Subscriber(tag = NetChangeReceiver.RECONNECT_STREAM_OF_SHUTDOWN, mode = ThreadMode.ASYNC)
    private boolean reconnectOfShutdown(boolean b) {
        if (currentStatus == Status.shutdown) {//如果当前为断开状态并且软件在前台,则重新连接
            boolean res = false;
            try {
                res = mCameraStreamingManager.startStreaming();
            } catch (Exception e) {
                e.printStackTrace();
                return res;
            }
            currentStatus = res ? Status.normal : Status.shutdown;
            return res;
        } else {
            LogUtils.debugInfo(TAG, "reconnect.....");
            return true;
        }
    }


    /**
     * 使用重连策略重连
     */
    @Override
    public void reconnect() {
        LogUtils.debugInfo(TAG, "isStreaming....."+isStreaming);
        if (isStreaming) mReconnetPolicy.shutDown();//拉流开始后才开始重连
    }

    /**
     * 断开连接
     */
    @org.simple.eventbus.Subscriber(tag = NetChangeReceiver.NETWORK_DISCONNECT, mode = ThreadMode.POST)
    private void netDisconnect(boolean b) {
        currentStatus = Status.shutdown;//标记当前播放是否中断
    }

    /**
     * 非wifi情况,提示用户
     */

    @Subscriber(tag = NetChangeReceiver.NETWORK_NOT_WIFI, mode = ThreadMode.MAIN)
    private void notWifiOpen(boolean b) {
        mNetworkJitterListener.onNetInData();
    }


    private void initListenler() {
        mReconnetPolicy.setCallBack(new ReconnetPolicy.ReconnectPolicyCallback() {
            @Override
            public boolean onReConnect() {
                boolean b = reconnectOfShutdown(true);//重新连接
                LogUtils.debugInfo(TAG, "reconnect is " + b + "//" + currentStatus);
                if (b && mOnReconnetListener != null) {
                    LogUtils.debugInfo(TAG," ZBStreamingClient.getInstance() =-------------reconnectScuccess ");
                    mOnReconnetListener.reconnectScuccess();
                }
                return b;
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

    /**
     * 转换摄像头
     */
    @Override
    public boolean switchCamera() {
        if (mCameraStreamingManager == null) return false;
        LogUtils.errroInfo(TAG, "switchCamera");
        return mCameraStreamingManager.switchCamera();//转换摄像头
    }


    /**
     * 强制显示前置摄像头
     */
    @Override
    public void showCameraFacingFront() {
        if (mHandler == null) return;
        mHandler.sendEmptyMessageDelayed(SWITCH_CAMEA, 1000);//是否切换摄像头
    }

    /**
     * 开始推流
     *
     * @param title     直播间名称
     * @param mLocation 直播间地址，经纬度用","隔开
     * @param mfile     直播间封面
     * @return 推流是否成功
     */
    @Override
    public void startPlay(String title, String mLocation, final File mfile, final OnLiveStartPlayListener mOnLiveStartPlayListener) {
        ZBUserAuth userAuth = ZBContants.getmUserAuth(CommonUtils.MD5encode(ZBApi.getContantsSeckret()));
        if (userAuth == null) {
            LogUtils.errroInfo("ticket验证失败！");

            return;
        }
        if (title == null)
            title = "";
        if (mLocation == null)
            mLocation = "";
        mStartSubscription = mModel.startStream(userAuth.getAk(), title
                , mLocation)//经纬度，默认为空
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        //请求开始直播时不可点击选择封面
                        if (mOnLiveStartPlayListener != null)
                            mOnLiveStartPlayListener.onStartPre();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ZBApiJson>() {
                    @Override
                    public void call(ZBApiJson body) {
                        if (body.code.equals(ZBApi.REQUEST_SUCESS)) {//成功
                            if (isReady) {//准备拉流成功
                                isStart = true;
                                if (mOnLiveStartPlayListener != null)
                                    mOnLiveStartPlayListener.onStartReady();
                                try {
                                    if (mOnLiveStartPlayListener != null) {
                                        mOnLiveStartPlayListener.onStartReady();
                                        uploadFile(mfile, mOnLiveStartPlayListener);//开始上传剪切图片
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    startFail(mOnLiveStartPlayListener);
                                }
                            } else {//准备拉流失败
                                startFail(mOnLiveStartPlayListener);
                            }
                        } else {//失败
                            LogUtils.debugInfo("startplay", "failure/" + body.message);
                            startFail(mOnLiveStartPlayListener);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {//错误
                        startFail(mOnLiveStartPlayListener);
                        throwable.printStackTrace();
                    }
                });
    }


    private void uploadFile(File mFile, final OnLiveStartPlayListener mOnLiveStartPlayListener) {
        if (mFile == null) {
            startStreaming(mOnLiveStartPlayListener);
        } else {
            mUploadSubscription = mModel.upload(ZBContants.getmUserAuth(CommonUtils.MD5encode(ZBApi.getContantsSeckret())).getAk()
                    , ""
                    , mFile)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<ZBBaseJson<ZBIconInfo[]>>() {
                        @Override
                        public void call(ZBBaseJson<ZBIconInfo[]> json) {
                            if (json.code.equals(ZBApi.REQUEST_SUCESS)) {
                                startStreaming(mOnLiveStartPlayListener);
                            } else {
                                Log.w(TAG, json.message);
                                startFail(mOnLiveStartPlayListener);
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            throwable.printStackTrace();
                            startFail(mOnLiveStartPlayListener);
                        }
                    });
        }
    }

    private void startStreaming(OnLiveStartPlayListener mOnLiveStartPlayListener) {
        boolean isScucees = mCameraStreamingManager.startStreaming();
        if (mOnLiveStartPlayListener != null) {
            if (isScucees) {
                isStreaming = true;
                mOnLiveStartPlayListener.onStartSuccess();
                /**
                 * 推流成功，加入im聊天室
                 */
                mChatRoomClient.joinRoom();
                initListenler();
                refreshMc();
            } else {
                mOnLiveStartPlayListener.onStartFail();
            }
        }
    }

    private void startFail(OnLiveStartPlayListener mOnLiveStartPlayListener) {
        if (mOnLiveStartPlayListener != null) {
            mOnLiveStartPlayListener.onStartFail();
        }
    }


    @Override
    public boolean stopStreaming() {
        return mCameraStreamingManager.stopStreaming();
    }

    /**
     * Log 管理
     * 当 enabled 设置为 true ，SDK Native 层的 log 将会被打开；当设置为 false，SDK Native 层的 log 将会被关闭。默认处于打开状态
     *
     * @param isEnable
     */
    @Override
    public void setNativeLoggingEnabled(boolean isEnable) {
        mCameraStreamingManager.setNativeLoggingEnabled(isEnable);
    }

    /**
     * 在调用 captureFrame 的时候，您需要传入 width 和 height，
     * 以及 FrameCapturedCallback，如果传入的 width 或者 height 小于等于 0，
     * SDK 返回的 Bitmap 将会是预览的尺寸 。SDK 完成截帧之后，会回调 onFrameCaptured，
     * 并将结果以参数的形式返回给调用者
     *
     * @param with
     * @param height
     * @param frameCapturedCallback
     */
    @Override
    public void captureFrame(int with, int height, ZBFrameCapturedCallback frameCapturedCallback) {
        mCameraStreamingManager.captureFrame(with, height, frameCapturedCallback);

    }

    /**
     * 推流配置策略
     *
     * @param streamStatus
     */
    private void configPolicy(StreamingProfile.StreamStatus streamStatus) {
        if (mHandler == null) return;
        //如果为断开模式,有流推送则调整为普通模式
        if (currentStatus == Status.shutdown) {
            currentStatus = Status.normal;
        }
        if (streamStatus.videoFps < (maxFps * 0.2f)) {
            LogUtils.debugInfo(TAG, "check--------------BAD");
            if (currentStatus == Status.normal || currentStatus == Status.weakNet) {
                mHandler.sendEmptyMessageDelayed(HANDLER_BAD, checkTime);
                currentStatus = Status.badNet;
                BadTimes = 1;
            } else if (currentStatus == Status.badNet) {
                BadTimes++;
            }
        } else if (streamStatus.videoFps < (maxFps * 0.4f)) {
            LogUtils.debugInfo(TAG, "check--------------WEAK");
            if (currentStatus == Status.weakNet) {//弱网模式
                weakTimes++;
            } else if (currentStatus == Status.normal || currentStatus == Status.badNet) {//正常模式
                mHandler.sendEmptyMessageDelayed(HANDLER_WEAK, checkTime);
                currentStatus = Status.weakNet;
                weakTimes = 1;
            }
        } else if (streamStatus.videoFps >= (maxFps * 0.85f)) {
            LogUtils.debugInfo(TAG, "check--------------NORMAL");
            if (currentStatus == Status.normal) {
                normalTimes++;
            } else if (currentStatus == Status.badNet || currentStatus == Status.weakNet) {
                mHandler.sendEmptyMessageDelayed(HANDLER_NORMAL, checkTime);
                currentStatus = Status.normal;
                normalTimes = 1;
            }
        }
    }

    /**
     * 降低fps
     */
    private void reduceFps() {
        mProfile.setVideoQuality(StreamingProfile.VIDEO_QUALITY_MEDIUM3)//流信息
                .setAudioQuality(StreamingProfile.AUDIO_QUALITY_MEDIUM1);
        mCameraStreamingManager.setStreamingProfile(mProfile);//改变配置
    }

    /**
     * 提高fps
     */
    private void improveFps() {
        mProfile.setVideoQuality(StreamingProfile.AUDIO_QUALITY_HIGH2)//流信息
                .setAudioQuality(StreamingProfile.AUDIO_QUALITY_MEDIUM2);
        mCameraStreamingManager.setStreamingProfile(mProfile);//改变配置
    }


    /**
     * 是否静音
     *
     * @param isMute
     */
    @Override
    public void mute(boolean isMute) {
        mCameraStreamingManager.mute(isMute);
    }

    /**
     * 手动对焦
     *
     * @param v   手动对焦的触碰布局
     * @param res 对焦时显示的视图，位于v之中
     */
    @Override
    public void setFocusAreaIndicator(ViewGroup v, int res) {
        mCameraStreamingManager.setFocusAreaIndicator(v, v.findViewById(res));
        mCameraStreamingManager.getZoom();
    }

    /**
     * 对焦坐标
     *
     * @param x
     * @param y
     */
    @Override
    public void doSingleTapUp(int x, int y) {
        mCameraStreamingManager.doSingleTapUp(x, y);

    }

    /**
     * 设置当前zoom值
     *
     * @param mCurrentZoom
     */
    @Override
    public void setZoomValue(int mCurrentZoom) {
        mCameraStreamingManager.setZoomValue(mCurrentZoom);
    }

    /**
     * 获取当前zoom值
     *
     * @return
     */
    @Override
    public int getZoom() {
        return mCameraStreamingManager.getZoom();
    }

    /**
     * 获取支持的最大zoom值
     *
     * @return
     */
    @Override
    public int getMaxZoom() {
        return mCameraStreamingManager.getMaxZoom();
    }

    /**
     * 是否支持zoom
     *
     * @return
     */
    @Override
    public boolean isZoomSupported() {
        return mCameraStreamingManager.isZoomSupported();
    }

    @Override
    public boolean turnLightOn() {
        return mCameraStreamingManager.turnLightOn();
    }

    @Override
    public boolean turnLightOff() {
        return mCameraStreamingManager.turnLightOff();
    }


    @Override
    public void onResume() {
        if (mCameraStreamingManager != null) {
            mCameraStreamingManager.resume();
        }
    }

    @Override
    public void onPause() {
        mCameraStreamingManager.pause();
    }

    @Override
    public void onDestroy() {

        mCameraStreamingManager.destroy();
        mReconnetPolicy.stop();
        mHandler.removeCallbacksAndMessages(null);//取消所有消息
        mHandler = null;
        streamStatusCallback=null;
        if (mcTimer != null) {
            mcTimer.cancel();
            mcTimer = null;
        }
        unSubscribe(mStartSubscription);
        unSubscribe(mUploadSubscription);
        if (mChatRoomClient != null) {
            mChatRoomClient.leaveRoom();
            mChatRoomClient.onDestroy();
        }
        ConversationDao.getInstance(mContext).delConversation(mImInfo.cid, Conversation.CONVERSATION_TYPE_CHAROOM);
        sZBStreamingClient = null;
        try {
            System.gc();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void unSubscribe(Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();//保证activity结束时取消所有正在执行的订阅
        }
    }

    /**
     * 校验推流
     */
    public static void checkStrem(final OncheckSteamStatusListener l) {
        getInstance().creatAndCheckStrem(l);

    }

    private void creatAndCheckStrem(final OncheckSteamStatusListener l) {
        final ZBUserAuth userAuth = getUserAuth();
        if (userAuth == null) {
            return;
        }

        //创建直播间
        mModel.createStream(userAuth.getAk())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        if (l != null) {
                            l.onStartCheck();
                        }
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())

                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<ZBApiStream, String>() {
                    @Override
                    public String call(ZBApiStream apiStream) {
                        if (apiStream.code.equals(ZBApi.REQUEST_SUCESS)) {//如果成功创建则返回streamid
                            mImInfo = apiStream.data.im;
                            // TODO: 16/8/17 测试cid
//                            mImInfo.cid = 4;
                            saveConversation();

                            return apiStream.data.id;
                        } else {
                            if (l != null) {
                                l.onFial(apiStream.code, apiStream.message);
                            }
                        }
                        return null;
                    }
                }).observeOn(Schedulers.io())
                .flatMap(new Func1<String, Observable<ZBCheckStreamPullJson>>() {
                    @Override
                    public Observable<ZBCheckStreamPullJson> call(String s) {
                        if (s == null) {
                            return Observable.create(new Observable.OnSubscribe<ZBCheckStreamPullJson>() {
                                @Override
                                public void call(rx.Subscriber<? super ZBCheckStreamPullJson> subscriber) {
                                    subscriber.onNext(null);//继续返回空
                                    if (l != null) {
                                        l.onFial("-1", "请求失败");
                                    }

                                }
                            });
                        }
                        mStreamId = CommonUtils.MD5encode(s);//加密streamid
                        return mModel.checkStream(userAuth.getAk(),
                                mStreamId);
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ZBCheckStreamPullJson>() {
                    @Override
                    public void call(ZBCheckStreamPullJson json) {
                        if (json == null) {//请求创建房间时没成功，在这里处理
                            if (l != null) {
                                l.onFial("-1", "请求失败");
                            }
                        } else {
                            if (json.code.equals(ZBApi.REQUEST_SUCESS)) {//效验成功
                                //跳转到直播间页面
                                if (json.data.code != null && json.data.code.equals(ZBApi.REQUEST_LIMIT_PLAY)) {//禁播状态提示用户
                                    if (l != null) {
                                        l.onDisable(json.data.time);
                                    }
                                    return;
                                }
                                mStream = json.data.stream;
                                mChatRoomClient = new ChatRoomClient(mImInfo.cid, userAuth.getUsid(), mContext);
                                initImlistener();
                                if (l != null) {
                                    l.onSuccess();
                                }
                            } else {//效验失败
                                if (l != null) {
                                    l.onFial(json.code, json.message);
                                }
                            }
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (l != null) {
                            l.onError(throwable);
                        }
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

    @Nullable
    private ZBUserAuth getUserAuth() {
        final ZBUserAuth userAuth = ZBContants.getmUserAuth(CommonUtils.MD5encode(ZBApi.getContantsSeckret()));
        if (userAuth == null) {
            LogUtils.errroInfo("ticket验证失败！");
            return null;
        }
        return userAuth;
    }

    @Override
    public void closePlay(final OnCloseStatusListener l) {
        LogUtils.errroInfo("closePlay--------------------！");

        mModel.endStream(ZBContants.getmUserAuth(CommonUtils.MD5encode(ZBApi.getContantsSeckret())).getAk(),//结束流

                mStreamId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ZBEndStreamJson>() {
                    @Override
                    public void call(ZBEndStreamJson endStreamJson) {
                        if (endStreamJson.code.equals(ZBApi.REQUEST_SUCESS)) {

                            mImInfo.cid = 0;
                            mStreamId = null;
                            mStream = null;
                            if (l != null) {
                                l.onSuccess(endStreamJson);
                            }

                        } else {
                            if (l != null) {
                                l.onFial(endStreamJson.code, endStreamJson.message);
                            }
                            LogUtils.errroInfo(TAG, endStreamJson.message);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (l != null) {
                            l.onError(throwable);
                        }
                    }
                });
    }

    /**
     * 禁言
     *
     * @param usid
     * @param time
     * @param l
     */
    @Override
    public void imDisable(String usid, int time, final OnCommonCallbackListener l) {
        final ZBUserAuth userAuth = getUserAuth();
        if (userAuth == null) {
            return;
        }
        if (time < 0) {
            throw new IllegalArgumentException("time must >=0 !");
        }
        long tmpTime = time;
        if (time > 0) {
            tmpTime = System.currentTimeMillis() / 1000 + time * 60;
        }
        mModel.imDisable(usid, mImInfo.cid, tmpTime, userAuth.getAk()
        ).subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ZBApiJson>() {
                    @Override
                    public void call(ZBApiJson result) {
                        if (result.code.equals(ZBApi.REQUEST_SUCESS)) {
                            l.onSuccess();
                        } else {
                            l.onFail(result.code, result.message);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        l.onError(throwable);

                    }
                });


    }

    /**
     * 解除禁言
     *
     * @param l
     */
    @Override
    public void imEnable(String usid, final OnCommonCallbackListener l) {
        final ZBUserAuth userAuth = getUserAuth();
        if (userAuth == null) {
            return;
        }
        mModel.imEnable(usid, mImInfo.cid, userAuth.getAk()
        ).subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ZBApiJson>() {
                    @Override
                    public void call(ZBApiJson result) {
                        if (result.code.equals(ZBApi.REQUEST_SUCESS)) {
                            l.onSuccess();
                        } else {
                            l.onFail(result.code, result.message);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        l.onError(throwable);

                    }
                });


    }

    /**
     * 刷新房间人数
     */
    private void refreshMc() {
        mcTimer = new Timer(true);
        mcTimer = new Timer(true);
        mcTimer.schedule(new ChatrommMcTimerTask(), CHATROOM_MC_TIME, CHATROOM_MC_TIME);
    }

    public class ChatrommMcTimerTask extends TimerTask {

        @Override
        public void run() {
            if (mChatRoomClient != null) {
                mChatRoomClient.mc();
            }
        }
    }

    public boolean isBackCamera() {
        return isBackCamera;
    }
}
