package com.zhiyicx.thinksnsplus.modules.chat.call.voice;

import android.media.AudioManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.EMNoActiveCallException;
import com.hyphenate.exceptions.EMServiceNotReadyException;
import com.hyphenate.exceptions.HyphenateException;
import com.zhiyicx.baseproject.em.manager.TSEMCallStatus;
import com.zhiyicx.baseproject.em.manager.util.TSEMConstants;
import com.zhiyicx.baseproject.widget.UserAvatarView;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.modules.chat.call.BaseCallFragment;
import com.zhiyicx.thinksnsplus.modules.chat.call.TSEMHyphenate;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhiyicx.thinksnsplus.widget.chat.MyChronometer;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @Author Jliuer
 * @Date 2018/02/06/14:26
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class VoiceCallFragment extends BaseCallFragment {

    @BindView(R.id.swing_card)
    UserAvatarView mSwingCard;
    @BindView(R.id.tv_nick)
    TextView mTvNick;
    @BindView(R.id.tv_network_status)
    TextView mTvNetworkStatus;
    @BindView(R.id.tv_call_state)
    TextView mTvCallState;
    @BindView(R.id.chronometer)
    MyChronometer mChronometer;
    @BindView(R.id.topLayout)
    LinearLayout mTopLayout;
    @BindView(R.id.iv_mute)
    ImageView mIvMute;
    @BindView(R.id.ll_mute)
    LinearLayout mLlMute;
    @BindView(R.id.btn_refuse_call)
    ImageView mBtnRefuseCall;
    @BindView(R.id.iv_exit_full_screen)
    ImageView mIvExitFullScreen;
    @BindView(R.id.ll_refuse_call)
    LinearLayout mLlRefuseCall;
    @BindView(R.id.btn_hangup_call)
    ImageView mBtnHangupCall;
    @BindView(R.id.ll_hangup_call)
    LinearLayout mLlHangupCall;
    @BindView(R.id.btn_answer_call)
    ImageView mBtnAnswerCall;
    @BindView(R.id.ll_answer_call)
    LinearLayout mLlAnswerCall;
    @BindView(R.id.iv_handsfree)
    ImageView mIvHandsfree;
    @BindView(R.id.ll_handsfree)
    LinearLayout mLlHandsfree;
    @BindView(R.id.root_layout)
    LinearLayout mRootLayout;

    public static VoiceCallFragment getInstance(Bundle bundle) {
        VoiceCallFragment videoCallFragment = new VoiceCallFragment();
        videoCallFragment.setArguments(bundle);
        return videoCallFragment;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        mCallType = CALL_TYPE_VOICE;
    }

    @Override
    protected void initData() {
        // 设置界面控件的显示
        // 判断下当前是否正在进行通话中
        if (TSEMCallStatus.getInstance().getCallState() == TSEMCallStatus.CALL_STATUS_NORMAL) {
            // 设置通话 呼入 or 呼出 状态
            TSEMCallStatus.getInstance().setInComing(isInComingCall);
            // 设置资源加载监听
            mSoundPool.setOnLoadCompleteListener((soundPool, i, i1) -> playCallSound());
            setButtonState(isInComingCall);
            if (isInComingCall) {
                // 设置通话状态为对方申请通话
                mTvCallState.setText(R.string.voice_call_in);

                mLlRefuseCall.setVisibility(View.VISIBLE);
                mLlAnswerCall.setVisibility(View.VISIBLE);
                mLlHangupCall.setVisibility(View.GONE);
            } else {
                // 设置通话状态为正在呼叫中
                mTvCallState.setText(R.string.video_call_out);

                mLlRefuseCall.setVisibility(View.VISIBLE);
                mLlAnswerCall.setVisibility(View.GONE);
                mLlHangupCall.setVisibility(View.GONE);

                // 自己是主叫方，调用呼叫方法
                makeCall();
            }
        } else if (TSEMCallStatus.getInstance().getCallState() == TSEMCallStatus
                .CALL_STATUS_CONNECTING) {
            // 设置通话呼入呼出状态
            isInComingCall = TSEMCallStatus.getInstance().isInComing();
            // 设置通话状态为正在呼叫中
            mTvCallState.setText(R.string.video_call_out);

            mLlRefuseCall.setVisibility(View.VISIBLE);
            mLlAnswerCall.setVisibility(View.GONE);
            mLlHangupCall.setVisibility(View.GONE);
        } else if (TSEMCallStatus.getInstance().getCallState() == TSEMCallStatus
                .CALL_STATUS_CONNECTING_INCOMING) {
            // 设置通话呼入呼出状态
            isInComingCall = TSEMCallStatus.getInstance().isInComing();
            // 设置通话状态为对方申请通话
            mTvCallState.setText(R.string.video_call_in);

            mLlRefuseCall.setVisibility(View.VISIBLE);
            mLlAnswerCall.setVisibility(View.VISIBLE);
            mLlHangupCall.setVisibility(View.GONE);
        } else {
            // 设置通话呼入呼出状态
            isInComingCall = TSEMCallStatus.getInstance().isInComing();
            // 再次打开要设置状态为正常通话状态
            mCallStatus = TSEMConstants.TS_CALL_ACCEPTED;
            mTvCallState.setText(R.string.video_call_in);
            mLlRefuseCall.setVisibility(View.GONE);
            mLlAnswerCall.setVisibility(View.GONE);
            mLlHangupCall.setVisibility(View.VISIBLE);
        }
        try {
            mTvNick.setText(getUserInfo(Long.parseLong(mChatId)).getName());
            ImageUtils.loadUserHead(getUserInfo(Long.parseLong(mChatId)), mSwingCard, false);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            LogUtils.d("user miss");
        }
    }

    @Override
    protected void makeCall() {
        try {
            EMClient.getInstance().callManager().makeVoiceCall(mChatId, AppApplication
                    .getmCurrentLoginAuth().getUser().getName());
        } catch (EMServiceNotReadyException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void exitFullScreen() {
        vibrate();
        mActivity.finish();
    }

    @Override
    protected void onMicrophone() {
        // 振动反馈
        vibrate();
        // 根据麦克风开关是否被激活来进行判断麦克风状态，然后进行下一步操作
        if (!mIvMute.isActivated()) {
            mIvMute.setImageResource(R.mipmap.btn_chat_mute_on);

            try {
                EMClient.getInstance().callManager().pauseVoiceTransfer();
            } catch (HyphenateException e) {
                e.printStackTrace();
            }

            // 设置按钮状态
            mIvMute.setActivated(true);
            TSEMCallStatus.getInstance().setMic(false);
        } else {
            // 恢复语音数据的传输
            // 暂停语音数据的传输
            mIvMute.setImageResource(R.mipmap.btn_chat_mute);

            try {
                EMClient.getInstance().callManager().resumeVoiceTransfer();
            } catch (HyphenateException e) {
                e.printStackTrace();
            }

            // 设置按钮状态
            mIvMute.setActivated(false);
            TSEMCallStatus.getInstance().setMic(true);
        }
    }

    @Override
    protected void rejectCall() {
        // 振动反馈
        vibrate();
        // 通话结束，重置通话状态
        TSEMCallStatus.getInstance().reset();
        // 结束通话时取消通话状态监听
        TSEMHyphenate.getInstance().removeCallStateChangeListener();
        // 拒绝通话后关闭通知铃音
        stopCallSound();
        try {
            // 调用 SDK 的拒绝通话方法
            EMClient.getInstance().callManager().rejectCall();
        } catch (EMNoActiveCallException e) {
            e.printStackTrace();
        }
        // 拒绝通话设置通话状态为自己拒绝
        mCallStatus = TSEMConstants.TS_CALL_REFUESD_IS_INCOMING;
        // 保存一条通话消息
        saveCallMessage();
        // 结束界面
        onFinish();
    }

    @Override
    protected void endCall() {
        // 振动反馈
        vibrate();
        // 通话结束，重置通话状态
        TSEMCallStatus.getInstance().reset();
        // 结束通话时取消通话状态监听
        TSEMHyphenate.getInstance().removeCallStateChangeListener();
        // 结束通话后关闭通知铃音
        stopCallSound();
        try {
            // 调用 SDK 的结束通话方法
            EMClient.getInstance().callManager().endCall();
        } catch (EMNoActiveCallException e) {
            e.printStackTrace();
        }
        // 挂断电话调用保存消息方法
        saveCallMessage();
        // 结束界面
        onFinish();
    }

    @Override
    protected void answerCall() {
        // 振动反馈
        vibrate();
        // 做一些接听时的操作，比如隐藏按钮
        mLlRefuseCall.setVisibility(View.GONE);
        mLlAnswerCall.setVisibility(View.GONE);
        mLlHangupCall.setVisibility(View.VISIBLE);
        mLlMute.setVisibility(View.VISIBLE);
        mLlHandsfree.setVisibility(View.VISIBLE);
        // 接听通话后关闭通知铃音
        stopCallSound();
        // 调用接通通话方法
        try {
            EMClient.getInstance().callManager().answerCall();
            // 设置通话状态为正常结束
            mCallStatus = TSEMConstants.TS_CALL_ACCEPTED;
            // 更新通话状态为已接通
            TSEMCallStatus.getInstance().setCallState(TSEMCallStatus.CALL_STATUS_ACCEPTED);
        } catch (EMNoActiveCallException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onSpeaker() {
        // 振动反馈
        vibrate();
        // 根据按钮状态决定打开还是关闭扬声器
        if (mIvHandsfree.isActivated()) {
            mIvHandsfree.setImageResource(R.mipmap.btn_chat_handsfree);
            closeSpeaker();
        } else {
            mIvHandsfree.setImageResource(R.mipmap.btn_chat_handsfree_on);
            openSpeaker();
        }
    }

    @Override
    protected void openSpeaker() {
        // 设置按钮状态
        mIvHandsfree.setActivated(true);
        TSEMCallStatus.getInstance().setSpeaker(true);
        // 检查是否已经开启扬声器
        if (!mAudioManager.isSpeakerphoneOn()) {
            // 打开扬声器
            mAudioManager.setSpeakerphoneOn(true);
        }
        // 设置声音模式为正常模式
        mAudioManager.setMode(AudioManager.MODE_NORMAL);
    }

    @Override
    protected void closeSpeaker() {
        // 设置按钮状态
        mIvHandsfree.setActivated(false);
        TSEMCallStatus.getInstance().setSpeaker(false);
        // 检查是否已经开启扬声器
        if (mAudioManager.isSpeakerphoneOn()) {
            // 打开扬声器
            mAudioManager.setSpeakerphoneOn(false);
        }
        // 设置声音模式为通讯模式，即使用听筒播放
        mAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
    }

    @Override
    protected void callAccept() {
        mChronometer.setVisibility(View.VISIBLE);
        mChronometer.setBase(SystemClock.elapsedRealtime());
        mChronometer.start();
    }

    @Override
    protected void saveCallMessage() {
        mChronometer.stop();
        mCallDruationText = mChronometer.getText().toString();
        super.saveCallMessage();
    }

    private void setButtonState(boolean isComingCall) {
        if (isComingCall) {
            mLlMute.setVisibility(View.GONE);
            mLlHandsfree.setVisibility(View.GONE);
            mLlRefuseCall.setVisibility(View.VISIBLE);
            mLlAnswerCall.setVisibility(View.VISIBLE);
        } else {
            mLlMute.setVisibility(View.VISIBLE);
            mLlHandsfree.setVisibility(View.VISIBLE);
            mLlRefuseCall.setVisibility(View.GONE);
            mLlAnswerCall.setVisibility(View.GONE);
        }
    }

    @Override
    protected void setCallStatusText(String status) {
        super.setCallStatusText(status);
        mTvCallState.setText(getString(R.string.video_calling, status));
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_voice_call;
    }


    @OnClick({R.id.swing_card, R.id.ll_mute, R.id.btn_refuse_call, R.id.btn_hangup_call, R.id.iv_exit_full_screen,
            R.id.btn_answer_call, R.id.iv_handsfree})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_exit_full_screen:
//                exitFullScreen();
                break;
            case R.id.swing_card:
                break;
            case R.id.ll_mute:
                // 麦克风
                onMicrophone();
                break;
            case R.id.btn_refuse_call:
                rejectCall();
                break;
            case R.id.btn_hangup_call:
                endCall();
                break;
            case R.id.btn_answer_call:
                answerCall();
                break;
            case R.id.iv_handsfree:
                onSpeaker();
                break;
            default:
        }
    }
}
