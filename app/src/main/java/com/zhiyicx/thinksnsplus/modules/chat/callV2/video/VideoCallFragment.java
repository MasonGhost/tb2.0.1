package com.zhiyicx.thinksnsplus.modules.chat.callV2.video;

import android.media.AudioManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMVideoCallHelper;
import com.hyphenate.exceptions.EMNoActiveCallException;
import com.hyphenate.exceptions.EMServiceNotReadyException;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.media.EMCallSurfaceView;
import com.superrtc.sdk.VideoView;
import com.zhiyicx.baseproject.em.manager.TSEMCallStatus;
import com.zhiyicx.baseproject.em.manager.TSEMCameraDataProcessor;
import com.zhiyicx.baseproject.em.manager.control.TSEMConstants;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.modules.chat.call.TSEMHyphenate;
import com.zhiyicx.thinksnsplus.modules.chat.callV2.BaseCallFragment;
import com.zhiyicx.thinksnsplus.widget.chat.MyChronometer;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @Author Jliuer
 * @Date 2018/02/01/16:10
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class VideoCallFragment extends BaseCallFragment {

    @BindView(R.id.iv_video_bg)
    View mIvBg;
    @BindView(R.id.iv_exit_full_screen)
    ImageView mIvExitFullScreen;
    @BindView(R.id.opposite_surface)
    EMCallSurfaceView mOppositeSurface;
    @BindView(R.id.local_surface)
    EMCallSurfaceView mLocalSurface;
    @BindView(R.id.layout_surface_container)
    RelativeLayout mLayoutSurfaceContainer;
    @BindView(R.id.tv_nick)
    TextView mTvNick;
    @BindView(R.id.tv_call_state)
    TextView mTvCallState;
    @BindView(R.id.chronometer)
    MyChronometer mChronometer;
    @BindView(R.id.ll_top_container)
    LinearLayout mLlTopContainer;
    @BindView(R.id.iv_mute)
    ImageView mIvMute;
    @BindView(R.id.ll_mute)
    LinearLayout mLlMute;
    @BindView(R.id.btn_refuse_call)
    ImageView mBtnRefuseCall;
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
    @BindView(R.id.iv_switch_camera)
    ImageView mIvSwitchCamera;
    @BindView(R.id.ll_switch_camera)
    LinearLayout mLlSwitchCamera;
    @BindView(R.id.ll_bottom_container)
    LinearLayout mLlBottomContainer;
    @BindView(R.id.tv_network_status)
    TextView mTvNetworkStatus;
    @BindView(R.id.rl_btn_container)
    RelativeLayout mRlBtnContainer;

    /**
     * 视频通话帮助类
     */
    private EMVideoCallHelper mVideoCallHelper;

    /**
     * 摄像头数据处理器
     */
    private TSEMCameraDataProcessor mCameraDataProcessor;

    /**
     * 切换通话界面，这里就是交换本地和远端画面控件设置，以达到通话大小画面的切换
     */
    private int mSurfaceState;

    public static VideoCallFragment getInstance(Bundle bundle) {
        VideoCallFragment videoCallFragment = new VideoCallFragment();
        videoCallFragment.setArguments(bundle);
        return videoCallFragment;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        mCallType = CALL_TYPE_VIDEO;

        // 设置通话最大帧率，SDK 最大支持(30)，默认(20)
        EMClient.getInstance().callManager().getCallOptions().setMaxVideoFrameRate(30);

        // 初始化视频通话帮助类
        mVideoCallHelper = EMClient.getInstance().callManager().getVideoCallHelper();

        // 设置本地预览图像显示在最上层，一定要提前设置，否则无效
        mLocalSurface.setZOrderMediaOverlay(true);
        mLocalSurface.setZOrderOnTop(true);
        mOppositeSurface.setScaleMode(VideoView.EMCallViewScaleMode.EMCallViewScaleModeAspectFill);

        EMClient.getInstance().callManager().setSurfaceView(mLocalSurface, mOppositeSurface);

        // 初始化视频数据处理器
        mCameraDataProcessor = new TSEMCameraDataProcessor();
        // 设置视频通话数据处理类
        EMClient.getInstance().callManager().setCameraDataProcessor(mCameraDataProcessor);
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
                mTvCallState.setText(R.string.video_call_in);

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
        } catch (NumberFormatException e) {
            e.printStackTrace();
            LogUtils.d("user miss");
        }
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_video_call;
    }

    @OnClick({R.id.iv_exit_full_screen, R.id.opposite_surface,
            R.id.iv_mute, R.id.rl_btn_container, R.id.iv_video_bg,
            R.id.local_surface, R.id.btn_refuse_call, R.id.btn_hangup_call,
            R.id.btn_answer_call, R.id.iv_handsfree, R.id.iv_switch_camera})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_btn_container:
            case R.id.opposite_surface:
                onControlLayout();
                break;
            case R.id.local_surface:
                changeCallView();
                break;
            case R.id.iv_exit_full_screen:
                // 预留功能，退出全屏通话
//                exitFullScreen();
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
                // 免提
                onSpeaker();
                break;
            case R.id.iv_switch_camera:
                // 切换摄像头
                changeCamera();
                break;
            case R.id.iv_mute:
                // 麦克风
                onMicrophone();
                break;
            default:
        }
    }

    /**
     * 设置底部几个按钮的状态 如果是来店 那么隐藏静音公放等
     *
     * @param isComingCall 是否来电
     */
    private void setButtonState(boolean isComingCall) {
        if (isComingCall) {
            mLlMute.setVisibility(View.GONE);
            mLlSwitchCamera.setVisibility(View.GONE);
            mLlRefuseCall.setVisibility(View.VISIBLE);
            mLlAnswerCall.setVisibility(View.VISIBLE);
        } else {
            mLlMute.setVisibility(View.VISIBLE);
            mLlSwitchCamera.setVisibility(View.VISIBLE);
            mLlRefuseCall.setVisibility(View.GONE);
            mLlAnswerCall.setVisibility(View.GONE);
        }
    }

    /**
     * @author Jliuer
     * @Date 18/02/01 16:53
     * @Email Jliuer@aliyun.com
     * @Description 开始呼叫对方
     */
    private void makeCall() {
        try {
            EMClient.getInstance().callManager().makeVideoCall(mChatId, AppApplication
                    .getmCurrentLoginAuth().getUser().getName());
        } catch (EMServiceNotReadyException e) {
            e.printStackTrace();
        }
    }

    /**
     * @author Jliuer
     * @Date 18/02/01 17:11
     * @Email Jliuer@aliyun.com
     * @Description 退出全屏通话界面
     */
    private void exitFullScreen() {
        vibrate();
        mActivity.finish();
    }

    /**
     * @author Jliuer
     * @Date 18/02/01 17:16
     * @Email Jliuer@aliyun.com
     * @Description 切换摄像头
     */
    private void changeCamera() {
        // 振动反馈
        vibrate();
        // 根据切换摄像头开关是否被激活确定当前是前置还是后置摄像头
        if (mIvSwitchCamera.isActivated()) {
            EMClient.getInstance().callManager().switchCamera();
            // 设置按钮状态
            mIvSwitchCamera.setActivated(false);
        } else {
            EMClient.getInstance().callManager().switchCamera();
            // 设置按钮状态
            mIvSwitchCamera.setActivated(true);
        }
    }

    /**
     * @author Jliuer
     * @Date 18/02/01 17:56
     * @Email Jliuer@aliyun.com
     * @Description 静音
     * 麦克风开关，主要调用环信语音数据传输方法
     */
    private void onMicrophone() {
        // 振动反馈
        vibrate();
        // 根据麦克风开关是否被激活来进行判断麦克风状态，然后进行下一步操作
        if (mIvMute.isActivated()) {
            mIvMute.setImageResource(R.mipmap.btn_chat_mute_on);
            try {
                EMClient.getInstance().callManager().resumeVoiceTransfer();
            } catch (HyphenateException e) {
                e.printStackTrace();
            }
            // 设置按钮状态
            mIvMute.setActivated(false);
            TSEMCallStatus.getInstance().setMic(false);
        } else {
            // 恢复语音数据的传输
            // 暂停语音数据的传输
            mIvMute.setImageResource(R.mipmap.btn_chat_mute);
            try {
                EMClient.getInstance().callManager().pauseVoiceTransfer();
            } catch (HyphenateException e) {
                e.printStackTrace();
            }
            // 设置按钮状态
            mIvMute.setActivated(true);
            TSEMCallStatus.getInstance().setMic(true);
        }
    }

    /**
     * @author Jliuer
     * @Date 18/02/01 17:59
     * @Email Jliuer@aliyun.com
     * @Description 扬声器开关
     */
    private void onSpeaker() {
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

    /**
     * 打开扬声器
     * 主要是通过扬声器的开关以及设置音频播放模式来实现
     * 1、MODE_NORMAL：是正常模式，一般用于外放音频
     * 2、MODE_IN_CALL：
     * 3、MODE_IN_COMMUNICATION：这个和 CALL 都表示通讯模式，不过 CALL 在华为上不好使，故使用 COMMUNICATION
     * 4、MODE_RINGTONE：铃声模式
     */
    private void openSpeaker() {
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

    /**
     * 关闭扬声器，即开启听筒播放模式
     * 同上边{@link #openSpeaker()}
     */
    private void closeSpeaker() {
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

    /**
     * 控制界面的显示与隐藏
     */
    private void onControlLayout() {
        if (mRlBtnContainer.isShown()) {
            mRlBtnContainer.setVisibility(View.GONE);
        } else {
            mRlBtnContainer.setVisibility(View.VISIBLE);
        }
    }

    /**
     * @Author Jliuer
     * @Date 2018/2/1/20:44
     * @Email Jliuer@aliyun.com
     * @Description 切换通话界面，这里就是交换本地和远端画面控件设置，以达到通话大小画面的切换
     */
    private void changeCallView() {
        if (mSurfaceState == 0) {
            mSurfaceState = 1;
            EMClient.getInstance().callManager().setSurfaceView(mOppositeSurface, mLocalSurface);
        } else {
            mSurfaceState = 0;
            EMClient.getInstance().callManager().setSurfaceView(mLocalSurface, mOppositeSurface);
        }
    }

    /**
     * @Author Jliuer
     * @Date 2018/2/1/20:59
     * @Email Jliuer@aliyun.com
     * @Description 拒绝通话
     */
    private void rejectCall() {
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

    /**
     * @Author Jliuer
     * @Date 2018/2/1/21:03
     * @Email Jliuer@aliyun.com
     * @Description 结束通话
     */
    private void endCall() {
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

    /**
     * @Author Jliuer
     * @Date 2018/2/1/21:04
     * @Email Jliuer@aliyun.com
     * @Description 接听通话
     */
    private void answerCall() {
        // 振动反馈
        vibrate();
        // 做一些接听时的操作，比如隐藏按钮
        mLlRefuseCall.setVisibility(View.GONE);
        mLlAnswerCall.setVisibility(View.GONE);
        mLlHangupCall.setVisibility(View.VISIBLE);
        mLlMute.setVisibility(View.VISIBLE);
        mLlSwitchCamera.setVisibility(View.VISIBLE);
        openSpeaker();
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
    protected void surfaceViewProcessor() {
        mChronometer.setVisibility(View.VISIBLE);
        mChronometer.setBase(SystemClock.elapsedRealtime());
        mChronometer.start();
        mOppositeSurface.setVisibility(View.VISIBLE);
//        mLocalSurface.setVisibility(View.VISIBLE);
//        EMClient.getInstance().callManager().setSurfaceView(mLocalSurface, mOppositeSurface);
    }

    @Override
    protected void onFinish() {
        // 结束通话要把 SurfaceView 释放 重置为 null
        mLocalSurface = null;
        mOppositeSurface = null;
        super.onFinish();
    }

    @Override
    protected void saveCallMessage() {
        mChronometer.stop();
        mCallDruationText = mChronometer.getText().toString();
        super.saveCallMessage();
    }

    @Override
    protected void setCallStatusText(String status) {
        super.setCallStatusText(status);
        mTvCallState.setText(getString(R.string.video_calling, status));
    }
}
