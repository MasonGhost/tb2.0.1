package com.zhiyicx.thinksnsplus.modules.chat.callV2;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMCallManager;
import com.hyphenate.chat.EMCallStateChangeListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.util.EMLog;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.em.manager.control.TSEMConstants;
import com.zhiyicx.baseproject.em.manager.control.TSEMDateUtil;
import com.zhiyicx.baseproject.em.manager.eventbus.TSEMCallEvent;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.modules.chat.call.TSEMHyphenate;

import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

/**
 * @Author Jliuer
 * @Date 2018/02/01/14:55
 * @Email Jliuer@aliyun.com
 * @Description 音视频通话基类
 */
public abstract class BaseCallFragment extends TSFragment {

    protected final int CALL_TYPE_VIDEO = 1;
    protected final int CALL_TYPE_VOICE = 0;

    /**
     * 呼叫方名字
     */
    protected String mChatId;

    /**
     * 是否是拨打进来的电话
     */
    protected boolean isInComingCall;

    /**
     * 通话结束状态，用来保存通话结束后的消息提示
     */
    protected int mCallStatus;

    /**
     * 通话类型，用于区分语音和视频通话 0 代表语音，1 代表视频
     */
    protected int mCallType;

    /**
     * 音频管理器
     */
    protected AudioManager mAudioManager;
    protected SoundPool mSoundPool;
    protected int streamID;
    protected int loadId;

    /**
     * 振动器
     */
    protected Vibrator mVibrator;

    /**
     * 通话时长
     */
    protected String mCallDruationText;

    /**
     * 铃声播放
     */
    protected Ringtone mRingtone;

    private EMCallManager.EMCallPushProvider mEMCallPushProvider;

    /**
     * 对方的用户信息
     */
    private UserInfoBean mUserInfoBean;
    private UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected boolean showToolBarDivider() {
        return false;
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 设置通话界面属性，保持屏幕常亮，关闭输入法，以及解锁
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
    }

    @Override
    protected void initView(View rootView) {
        // 获取通话对方的username
        mChatId = getArguments().getString(TSEMConstants.TS_EXTRA_CHAT_ID);
        isInComingCall = getArguments().getBoolean(TSEMConstants.TS_EXTRA_CALL_IS_INCOMING, false);
        // 默认通话状态为自己取消
        mCallStatus = TSEMConstants.TS_CALL_CANCEL;

        // 收到呼叫或者呼叫对方时初始化通话状态监听
        TSEMHyphenate.getInstance().setCallStateChangeListener();

        // 初始化振动器
        mVibrator = (Vibrator) mActivity.getSystemService(Context.VIBRATOR_SERVICE);
        // 初始化音频管理器
        mAudioManager = (AudioManager) mActivity.getSystemService(Context.AUDIO_SERVICE);
        // 根据系统版本不同选择不同的方式初始化音频播放工具
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            createSoundPoolWithBuilder();
        } else {
            createSoundPoolWithConstructor();
        }
        // 根据通话呼叫与被呼叫加载不同的提示音效
        if (isInComingCall) {
            // TODO load diff call sound
        } else {
        }
        loadId = mSoundPool.load(mActivity, R.raw.em_outgoing, 1);

        initPushvider();
    }

    /**
     * @author Jliuer
     * @Date 18/02/01 16:07
     * @Email Jliuer@aliyun.com
     * @Description see http://www.easemob
     * .com/apidoc/android/chat3.0
     * /interfacecom_1_1hyphenate_1_1chat_1_1_e_m_call_manager_1_1_e_m_call_push_provider.html
     */
    private void initPushvider() {
        mEMCallPushProvider = new EMCallManager.EMCallPushProvider() {

            void updateMessageText(final EMMessage oldMsg, final String to) {
                // update local message text
                EMConversation conv = EMClient.getInstance().chatManager().getConversation(oldMsg
                        .getTo());
                conv.removeMessage(oldMsg.getMsgId());
            }

            @Override
            public void onRemoteOffline(final String to) {

                //this function should exposed & move to Demo
                EMLog.d(TAG, "onRemoteOffline, to:" + to);

                final EMMessage message = EMMessage.createTxtSendMessage("You have an incoming " +
                        "call", to);
                // set the user-defined extension field
                message.setAttribute("em_apns_ext", true);

                message.setAttribute("is_voice_call", mCallType == 0);

                message.setMessageStatusCallback(new EMCallBack() {

                    @Override
                    public void onSuccess() {
                        EMLog.d(TAG, "onRemoteOffline success");
                        updateMessageText(message, to);
                    }

                    @Override
                    public void onError(int code, String error) {
                        EMLog.d(TAG, "onRemoteOffline Error");
                        updateMessageText(message, to);
                    }

                    @Override
                    public void onProgress(int progress, String status) {
                    }
                });
                // send messages
                EMClient.getInstance().chatManager().sendMessage(message);
            }
        };
        EMClient.getInstance().callManager().setPushProvider(mEMCallPushProvider);
    }

    /**
     * 当系统的 SDK 版本高于21时，使用另一种方式创建 SoundPool
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected void createSoundPoolWithBuilder() {
        AudioAttributes attributes = new AudioAttributes.Builder()
                // 设置音频要用在什么地方，这里选择电话通知铃音
                .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE).setContentType
                        (AudioAttributes.CONTENT_TYPE_SONIFICATION).build();
        // 使用 build 的方式实例化 SoundPool
        mSoundPool = new SoundPool.Builder().setAudioAttributes(attributes).setMaxStreams(1)
                .build();
    }

    /**
     * 使用构造函数实例化 SoundPool
     */
    @SuppressWarnings("deprecation")
    protected void createSoundPoolWithConstructor() {
        // 老版本使用构造函数方式实例化 SoundPool，MODE 设置为铃音 MODE_RINGTONE
        mSoundPool = new SoundPool(1, AudioManager.MODE_RINGTONE, 0);
    }

    /**
     * 调用系统振动
     */
    protected void vibrate() {
        if (mVibrator == null) {
            mVibrator = (Vibrator) mActivity.getSystemService(Context.VIBRATOR_SERVICE);
        }
        if (mVibrator == null) {
            return;
        }
        mVibrator.vibrate(60);
    }

    /**
     * 销毁界面时做一些自己的操作
     */
    @Override
    public void onDestroy() {
        // 关闭音效并释放资源
        stopCallSound();
        super.onDestroy();
    }

    /**
     * 关闭音效的播放，并释放资源
     */
    protected void stopCallSound() {
        if (mSoundPool != null) {
            // 停止播放音效
            mSoundPool.stop(streamID);
            // 释放资源
            mSoundPool.release();
            mSoundPool = null;
        }
        if (mEMCallPushProvider != null) {
            EMClient.getInstance().callManager().setPushProvider(null);
            mEMCallPushProvider = null;
        }
    }

    /**
     * 播放呼叫通话提示音
     */
    protected void playCallSound() {
        if (!mAudioManager.isSpeakerphoneOn()) {
            mAudioManager.setSpeakerphoneOn(true);
        }
        // 设置音频管理器音频模式为铃音模式
        mAudioManager.setMode(AudioManager.MODE_RINGTONE);
        // 播放提示音，返回一个播放的音频id，等下停止播放需要用到
        if (mSoundPool != null) {
            streamID = mSoundPool.play(loadId, // 播放资源id；就是加载到SoundPool里的音频资源顺序，这里就是第一个，也是唯一的一个
                    0.5f,   // 左声道音量
                    0.5f,   // 右声道音量
                    1,      // 优先级，这里至于一个提示音，不需要关注
                    -1,     // 是否循环；0 不循环，-1 循环
                    1);     // 播放比率；从0.5-2，一般设置为1，表示正常播放
        }
    }

    /**
     * 通话结束，保存一条记录通话的消息
     */
    protected void saveCallMessage() {
        EMMessage message = null;
        EMTextMessageBody body = null;
        String content = null;
        if (isInComingCall) {
            message = EMMessage.createReceiveMessage(EMMessage.Type.TXT);
            message.setFrom(mChatId);
        } else {
            message = EMMessage.createSendMessage(EMMessage.Type.TXT);
            message.setTo(mChatId);
        }

        switch (mCallStatus) {
            case TSEMConstants.TS_CALL_ACCEPTED:
                // 通话正常结束，要加上通话时间
                content = mActivity.getString(R.string.call_duration) + mCallDruationText;
                break;
            case TSEMConstants.TS_CALL_CANCEL:
                // 自己取消
                content = mActivity.getString(R.string.Has_been_cancelled);
                break;
            case TSEMConstants.TS_CALL_CANCEL_IS_INCOMING:
                // 对方取消
                content = mActivity.getString(R.string.Has_been_cancelled_other_party);
                break;
            case TSEMConstants.TS_CALL_BUSY:
                // 对方正忙
                content = mActivity.getString(R.string.The_other_is_on_the_phone);
                break;
            case TSEMConstants.TS_CALL_OFFLINE:
                // 对方不在线
                content = mActivity.getString(R.string.The_other_is_not_online);
                break;
            case TSEMConstants.TS_CALL_REFUESD_IS_INCOMING:
                // 自己已拒绝
                content = mActivity.getString(R.string.Refused);
                break;
            case TSEMConstants.TS_CALL_REFUESD:
                // 对方拒绝
                content = mActivity.getString(R.string.The_other_party_has_refused_to);
                break;
            case TSEMConstants.TS_CALL_NORESPONSE:
                // 对方无响应
                content = mActivity.getString(R.string.The_other_party_did_not_answer);
                break;
            case TSEMConstants.TS_CALL_TRANSPORT:
                // 建立连接失败
                content = mActivity.getString(R.string.not_connect_to_server);
                break;
            case TSEMConstants.TS_CALL_VERSION_DIFFERENT:
                // 双方通话协议版本不同
                content = mActivity.getString(R.string.version_diff);
                break;
            default:
                // 默认为取消
                content = mActivity.getString(R.string.Has_been_cancelled);
                break;
        }
        body = new EMTextMessageBody(content);
        message.addBody(body);
        message.setStatus(EMMessage.Status.SUCCESS);
        message.setMsgId(TSEMDateUtil.getCurrentMillisecond() + "");
        if (mCallType == CALL_TYPE_VIDEO) {
            message.setAttribute(TSEMConstants.ML_ATTR_CALL_VIDEO, true);
        } else {
            message.setAttribute(TSEMConstants.ML_ATTR_CALL_VOICE, true);
        }
        message.setUnread(false);
        // 调用sdk的保存消息方法
        EMClient.getInstance().chatManager().saveMessage(message);
    }

    /**
     * @author Jliuer
     * @Date 18/02/01 17:43
     * @Email Jliuer@aliyun.com
     * @Description 实现订阅方法，订阅全局监听发来的通话状态事件
     */
    @Subscriber(mode = ThreadMode.MAIN)
    public void onTSEMCallEventBus(TSEMCallEvent event) {
        EMCallStateChangeListener.CallError callError = event.getCallError();
        EMCallStateChangeListener.CallState callState = event.getCallState();

        switch (callState) {
            case CONNECTING:
                // 正在呼叫对方
                setCallStatusText(getString(R.string.video_call_connecting));
                break;
            case CONNECTED:
                // 正在等待对方接受呼叫申请（对方申请与你进行通话）
                setCallStatusText(getString(R.string.video_call_connecting));
                break;
            case ACCEPTED:
                // 通话已接通
                setCallStatusText(getString(R.string.network_normal));
                // 电话接通，停止播放提示音
                stopCallSound();
                // 通话已接通，设置通话状态为正常状态
                mCallStatus = TSEMConstants.TS_CALL_ACCEPTED;
                // 通话接通，处理下SurfaceView的显示
                surfaceViewProcessor();
                break;
            case DISCONNECTED:
                // 通话已中断
                setCallStatusText(getString(R.string.video_call_off));
                if (callError == EMCallStateChangeListener.CallError.ERROR_UNAVAILABLE) {
                    // 设置通话状态为对方不在线
                    mCallStatus = TSEMConstants.TS_CALL_OFFLINE;
                } else if (callError == EMCallStateChangeListener.CallError.ERROR_BUSY) {
                    // 设置通话状态为对方在忙
                    mCallStatus = TSEMConstants.TS_CALL_BUSY;
                } else if (callError == EMCallStateChangeListener.CallError.REJECTED) {
                    // 设置通话状态为对方已拒绝
                    mCallStatus = TSEMConstants.TS_CALL_REFUESD;
                } else if (callError == EMCallStateChangeListener.CallError.ERROR_NORESPONSE) {
                    // 设置通话状态为对方未响应
                    mCallStatus = TSEMConstants.TS_CALL_NORESPONSE;
                } else if (callError == EMCallStateChangeListener.CallError.ERROR_TRANSPORT) {
                    // 设置通话状态为建立连接失败
                    mCallStatus = TSEMConstants.TS_CALL_TRANSPORT;
                } else if (callError == EMCallStateChangeListener.CallError
                        .ERROR_LOCAL_SDK_VERSION_OUTDATED) {
                    // 设置通话状态为双方协议不同
                    mCallStatus = TSEMConstants.TS_CALL_VERSION_DIFFERENT;
                } else {
                    // 根据当前状态判断是正常结束，还是对方取消通话
                    if (mCallStatus == TSEMConstants.TS_CALL_CANCEL) {
                        // 设置通话状态
                        mCallStatus = TSEMConstants.TS_CALL_REFUESD_IS_INCOMING;
                    }
                }
                // 通话结束保存消息
                saveCallMessage();
                // 结束通话时取消通话状态监听
                TSEMHyphenate.getInstance().removeCallStateChangeListener();
                onFinish();
                break;
            case NETWORK_UNSTABLE:
                if (callError == EMCallStateChangeListener.CallError.ERROR_NO_DATA) {
                    // 没有通话数据
                    setCallStatusText(getString(R.string.no_call_data));
                } else {
                    // 网络不稳定
                    setCallStatusText(getString(R.string.network_unstable));
                }
                break;
            case NETWORK_NORMAL:
                // 网络正常
                setCallStatusText(getString(R.string.network_normal));
                break;
            case VIDEO_PAUSE:
                // 视频传输已暂停
                break;
            case VIDEO_RESUME:
                // 视频传输已恢复
                break;
            case VOICE_PAUSE:
                // 语音传输已暂停
                break;
            case VOICE_RESUME:
                // 语音传输已恢复
                break;
            default:
                break;
        }
    }

    /**
     * @author Jliuer
     * @Date 18/02/01 17:27
     * @Email Jliuer@aliyun.com
     * @Description 接通了，设置显示对方图像控件显示
     */
    protected abstract void surfaceViewProcessor();

    /**
     * 通话状态变化
     *
     * @param status
     */
    protected void setCallStatusText(String status) {

    }

    /**
     * @author Jliuer
     * @Date 18/02/01 17:39
     * @Email Jliuer@aliyun.com
     * @Description 结束通话关闭界面
     */
    protected void onFinish() {
        stopCallSound();
        mActivity.finish();
    }

    public void onUserLeaveHint() {
    }

    public void onActivityResume() {
    }

    protected UserInfoBean getUserInfo(long id) {
        if (mUserInfoBean != null) {
            return mUserInfoBean;
        }
        if (mUserInfoBeanGreenDao == null) {
            mUserInfoBeanGreenDao = new UserInfoBeanGreenDaoImpl(mActivity.getApplication());
        }
        return mUserInfoBeanGreenDao.getSingleDataFromCache(id);
    }
}
