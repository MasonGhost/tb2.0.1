/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zhiyicx.thinksnsplus.modules.chat.call;

import android.app.Application;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMCallStateChangeListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.bean.ChatUserInfoBean;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.EMLog;
import com.zhiyicx.baseproject.widget.UserAvatarView;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhiyicx.thinksnsplus.widget.chat.MyChronometer;

import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 语音通话页面
 */
public class VoiceCallActivity extends CallActivity implements OnClickListener {

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

    private boolean isMuteState;
    private boolean isHandsfreeState;

    private boolean endCallTriggerByMe = false;
    String st1;
    private boolean monitor = false;

    private UserInfoBean userInfoBean;
    private UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            finish();
            return;
        }
        setContentView(R.layout.activity_voice_call);
        ButterKnife.bind(this);
//        DemoHelper.getInstance().isVoiceCalling = true;
        callType = 0;

        mBtnRefuseCall.setOnClickListener(this);
        mBtnAnswerCall.setOnClickListener(this);
        mBtnHangupCall.setOnClickListener(this);
        mIvMute.setOnClickListener(this);
        mIvHandsfree.setOnClickListener(this);

        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        addCallStateListener();
        msgid = UUID.randomUUID().toString();

        username = getIntent().getStringExtra("username");
        isInComingCall = getIntent().getBooleanExtra("isComingCall", false);
        userInfoBean = getIntent().getParcelableExtra("user_info");
        initUserInfo();
        setButtonState(isInComingCall);
        if (!isInComingCall) {// outgoing call
            soundPool = new SoundPool(1, AudioManager.STREAM_RING, 0);
            outgoing = soundPool.load(this, R.raw.em_outgoing, 1);
            mLlHangupCall.setVisibility(View.VISIBLE);
            st1 = getResources().getString(R.string.Are_connected_to_each_other);
            mTvCallState.setText(st1);
            handler.sendEmptyMessage(MSG_CALL_MAKE_VOICE);
            handler.postDelayed(() -> streamID = playMakeCallSounds(), 300);
        } else { // incoming call
            Uri ringUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            audioManager.setMode(AudioManager.MODE_RINGTONE);
            audioManager.setSpeakerphoneOn(true);
            ringtone = RingtoneManager.getRingtone(this, ringUri);
            ringtone.play();
        }
        final int MAKE_CALL_TIMEOUT = 50 * 1000;
        handler.removeCallbacks(timeoutHangup);
        handler.postDelayed(timeoutHangup, MAKE_CALL_TIMEOUT);
    }

    private void initUserInfo(){
        if (mUserInfoBeanGreenDao == null){
            mUserInfoBeanGreenDao = new UserInfoBeanGreenDaoImpl((Application) AppApplication.getContext());
        }
        userInfoBean = mUserInfoBeanGreenDao.getSingleDataFromCache(Long.parseLong(username));
        if (userInfoBean != null){
            ImageUtils.loadUserHead(userInfoBean, mSwingCard, false);
            mTvNick.setText(userInfoBean.getName());
        }
    }

    /**
     * set call state listener
     */
    void addCallStateListener() {
        callStateListener = (callState, error) -> {
            // Message msg = handler.obtainMessage();
            EMLog.d("EMCallManager", "onCallStateChanged:" + callState);
            switch (callState) {

                case CONNECTING:
                    runOnUiThread(() -> mTvCallState.setText(st1));
                    break;
                case CONNECTED:
                    runOnUiThread(() -> {
                        String st3 = getResources().getString(R.string.have_connected_with);
                        mTvCallState.setText(st3);
                    });
                    break;

                case ACCEPTED:
                    handler.removeCallbacks(timeoutHangup);
                    runOnUiThread(() -> {
                        try {
                            if (soundPool != null) {
                                soundPool.stop(streamID);
                            }
                        } catch (Exception e) {
                        }
                        if (!isHandsfreeState) {
                            closeSpeakerOn();
                        }
                        //show relay or direct call, for testing purpose
                        ((TextView) findViewById(R.id.tv_is_p2p)).setText(EMClient.getInstance().callManager().isDirectCall()
                                ? R.string.direct_call : R.string.relay_call);
                        mChronometer.setVisibility(View.VISIBLE);
                        mChronometer.setBase(SystemClock.elapsedRealtime());
                        // duration start
                        mChronometer.start();
                        String str4 = getResources().getString(R.string.In_the_call);
                        mTvCallState.setText(str4);
                        callingState = CallingState.NORMAL;
                        startMonitor();
                    });
                    break;
                case NETWORK_UNSTABLE:
                    runOnUiThread(() -> {
                        mTvNetworkStatus.setVisibility(View.VISIBLE);
                        if (error == EMCallStateChangeListener.CallError.ERROR_NO_DATA) {
                            mTvNetworkStatus.setText(R.string.no_call_data);
                        } else {
                            mTvNetworkStatus.setText(R.string.network_unstable);
                        }
                    });
                    break;
                case NETWORK_NORMAL:
                    runOnUiThread(() -> mTvNetworkStatus.setVisibility(View.INVISIBLE));
                    break;
                case VOICE_PAUSE:
                    runOnUiThread(() -> ToastUtils.showToast(getApplicationContext(), "VOICE_PAUSE"));
                    break;
                case VOICE_RESUME:
                    runOnUiThread(() -> ToastUtils.showToast(getApplicationContext(), "VOICE_RESUME"));
                    break;
                case DISCONNECTED:
                    handler.removeCallbacks(timeoutHangup);
                    @SuppressWarnings("UnnecessaryLocalVariable") final EMCallStateChangeListener.CallError fError = error;
                    runOnUiThread(new Runnable() {
                        private void postDelayedCloseMsg() {
                            handler.postDelayed(() -> runOnUiThread(() -> {
                                Log.d("AAA", "CALL DISCONNETED");
                                removeCallStateListener();
                                saveCallRecord();
                                Animation animation = new AlphaAnimation(1.0f, 0.0f);
                                animation.setDuration(800);
                                findViewById(R.id.root_layout).startAnimation(animation);
                                finish();
                            }), 200);
                        }

                        @Override
                        public void run() {
                            mChronometer.stop();
                            callDruationText = mChronometer.getText().toString();
                            String st1 = getResources().getString(R.string.Refused);
                            String st2 = getResources().getString(R.string.The_other_party_refused_to_accept);
                            String st3 = getResources().getString(R.string.Connection_failure);
                            String st4 = getResources().getString(R.string.The_other_party_is_not_online);
                            String st5 = getResources().getString(R.string.The_other_is_on_the_phone_please);

                            String st6 = getResources().getString(R.string.The_other_party_did_not_answer_new);
                            String st7 = getResources().getString(R.string.hang_up);
                            String st8 = getResources().getString(R.string.The_other_is_hang_up);

                            String st9 = getResources().getString(R.string.did_not_answer);
                            String st10 = getResources().getString(R.string.Has_been_cancelled);
                            String st11 = getResources().getString(R.string.hang_up);

                            if (fError == EMCallStateChangeListener.CallError.REJECTED) {
                                callingState = CallingState.BEREFUSED;
                                mTvCallState.setText(st2);
                            } else if (fError == EMCallStateChangeListener.CallError.ERROR_TRANSPORT) {
                                mTvCallState.setText(st3);
                            } else if (fError == EMCallStateChangeListener.CallError.ERROR_UNAVAILABLE) {
                                callingState = CallingState.OFFLINE;
                                mTvCallState.setText(st4);
                            } else if (fError == EMCallStateChangeListener.CallError.ERROR_BUSY) {
                                callingState = CallingState.BUSY;
                                mTvCallState.setText(st5);
                            } else if (fError == EMCallStateChangeListener.CallError.ERROR_NORESPONSE) {
                                callingState = CallingState.NO_RESPONSE;
                                mTvCallState.setText(st6);
                            } else if (fError == EMCallStateChangeListener.CallError.ERROR_LOCAL_SDK_VERSION_OUTDATED || fError == EMCallStateChangeListener.CallError.ERROR_REMOTE_SDK_VERSION_OUTDATED) {
                                callingState = CallingState.VERSION_NOT_SAME;
                                mTvCallState.setText(R.string.call_version_inconsistent);
                            } else {
                                if (isRefused) {
                                    callingState = CallingState.REFUSED;
                                    mTvCallState.setText(st1);
                                } else if (isAnswered) {
                                    callingState = CallingState.NORMAL;
                                    if (endCallTriggerByMe) {
                                        mTvCallState.setText(st7);
                                    } else {
                                        mTvCallState.setText(st8);
                                    }
                                } else {
                                    if (isInComingCall) {
                                        callingState = CallingState.UNANSWERED;
                                        mTvCallState.setText(st9);
                                    } else {
                                        if (callingState != CallingState.NORMAL) {
                                            callingState = CallingState.CANCELLED;
                                            mTvCallState.setText(st10);
                                        } else {
                                            mTvCallState.setText(st11);
                                        }
                                    }
                                }
                            }
                            postDelayedCloseMsg();
                        }

                    });

                    break;

                default:
                    break;
            }

        };
        EMClient.getInstance().callManager().addCallStateChangeListener(callStateListener);
    }

    void removeCallStateListener() {
        EMClient.getInstance().callManager().removeCallStateChangeListener(callStateListener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_refuse_call:
                isRefused = true;
                mBtnRefuseCall.setEnabled(false);
                handler.sendEmptyMessage(MSG_CALL_REJECT);
                break;

            case R.id.btn_answer_call:
                mIvHandsfree.setEnabled(false);
                closeSpeakerOn();
                mTvCallState.setText("正在接听...");
                setButtonState(false);
                mLlHangupCall.setVisibility(View.VISIBLE);
                handler.sendEmptyMessage(MSG_CALL_ANSWER);
                break;

            case R.id.btn_hangup_call:
                mBtnHangupCall.setEnabled(false);
                mChronometer.stop();
                endCallTriggerByMe = true;
                mTvCallState.setText(getResources().getString(R.string.hanging_up));
                handler.sendEmptyMessage(MSG_CALL_END);
                break;

            case R.id.iv_mute:
                if (isMuteState) {
                    mIvMute.setImageResource(R.mipmap.btn_chat_mute);
                    try {
                        EMClient.getInstance().callManager().resumeVoiceTransfer();
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                    }
                    isMuteState = false;
                } else {
                    mIvMute.setImageResource(R.mipmap.btn_chat_mute_on);
                    try {
                        EMClient.getInstance().callManager().pauseVoiceTransfer();
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                    }
                    isMuteState = true;
                }
                break;
            case R.id.iv_handsfree:
                if (isHandsfreeState) {
                    mIvHandsfree.setImageResource(R.mipmap.btn_chat_handsfree);
                    closeSpeakerOn();
                    isHandsfreeState = false;
                } else {
                    mIvHandsfree.setImageResource(R.mipmap.btn_chat_handsfree_on);
                    openSpeakerOn();
                    isHandsfreeState = true;
                }
                break;
            default:
                break;
        }
    }

    /**
     * 设置底部几个按钮的状态 如果是来店 那么隐藏静音公放等
     *
     * @param isComingCall 是否来电
     */
    private void setButtonState(boolean isComingCall) {
        if (isComingCall){
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
    protected void onDestroy() {
//        DemoHelper.getInstance().isVoiceCalling = false;
        stopMonitor();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        callDruationText = mChronometer.getText().toString();
    }

    /**
     * for debug & testing, you can remove this when release
     */
    void startMonitor() {
        monitor = true;
        new Thread(() -> {
            runOnUiThread(() -> ((TextView) findViewById(R.id.tv_is_p2p)).setText(EMClient.getInstance().callManager().isDirectCall()
                    ? R.string.direct_call : R.string.relay_call));
            while (monitor) {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                }
            }
        }, "CallMonitor").start();
    }

    void stopMonitor() {

    }

}
