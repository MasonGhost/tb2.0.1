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

import android.hardware.Camera;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMCallManager.EMCameraDataProcessor;
import com.hyphenate.chat.EMCallStateChangeListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMVideoCallHelper;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.media.EMCallSurfaceView;
import com.hyphenate.util.EMLog;
import com.superrtc.sdk.VideoView;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.widget.chat.MyChronometer;


import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;


public class VideoCallActivity extends CallActivity implements OnClickListener {

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
    @BindView(R.id.tv_is_p2p)
    TextView mTvIsP2p;
    @BindView(R.id.ll_top_container)
    LinearLayout mLlTopContainer;
    @BindView(R.id.tv_call_monitor)
    TextView mTvCallMonitor;
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
    @BindView(R.id.ll_btns)
    RelativeLayout mLlBtns;
    @BindView(R.id.tv_network_status)
    TextView mTvNetworkStatus;
    @BindView(R.id.root_layout)
    RelativeLayout mRootLayout;
    @BindView(R.id.ll_bottom_container)
    LinearLayout mBottomContainer;
    private boolean isMuteState;
    private boolean isHandsfreeState;
    private boolean isAnswered;
    private boolean endCallTriggerByMe = false;
    private boolean monitor = true;

    private int surfaceState = -1;

    private Handler uiHandler;

    private boolean isInCalling;
    boolean isRecording = false;
    //    private Button recordBtn;
    private EMVideoCallHelper callHelper;
    private Button toggleVideoBtn;

    private BrightnessDataProcess dataProcessor = new BrightnessDataProcess();

    // dynamic adjust brightness
    class BrightnessDataProcess implements EMCameraDataProcessor {
        byte yDelta = 0;

        synchronized void setYDelta(byte yDelta) {
            LogUtils.d("VideoCallActivity", "brigntness uDelta:" + yDelta);
            this.yDelta = yDelta;
        }

        // data size is width*height*2
        // the first width*height is Y, second part is UV
        // the storage layout detailed please refer 2.x demo CameraHelper.onPreviewFrame
        @Override
        public synchronized void onProcessData(byte[] data, Camera camera, final int width, final int height, final int rotateAngel) {
            int wh = width * height;
            for (int i = 0; i < wh; i++) {
                int d = (data[i] & 0xFF) + yDelta;
                d = d < 16 ? 16 : d;
                d = d > 235 ? 235 : d;
                data[i] = (byte) d;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            finish();
            return;
        }
        setContentView(R.layout.activity_video_call);
        ButterKnife.bind(this);

//        DemoHelper.getInstance().isVideoCalling = true;
        callType = 1;

        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        uiHandler = new Handler();
        RelativeLayout btnsContainer = (RelativeLayout) findViewById(R.id.ll_btns);

        mBtnRefuseCall.setOnClickListener(this);
        mBtnAnswerCall.setOnClickListener(this);
        mBtnHangupCall.setOnClickListener(this);
        mIvMute.setOnClickListener(this);
        mIvHandsfree.setOnClickListener(this);
        mRootLayout.setOnClickListener(this);

        msgid = UUID.randomUUID().toString();
        isInComingCall = getIntent().getBooleanExtra("isComingCall", false);
        username = getIntent().getStringExtra("username");

        mTvNick.setText(username);

        // local surfaceview
        mLocalSurface.setOnClickListener(this);
        mLocalSurface.setZOrderMediaOverlay(true);
        mLocalSurface.setZOrderOnTop(true);

        // set call state listener
        addCallStateListener();
        setButtonState(isInComingCall);
        if (!isInComingCall) {// outgoing call
            soundPool = new SoundPool(1, AudioManager.STREAM_RING, 0);
            outgoing = soundPool.load(this, R.raw.em_outgoing, 1);
            mLlHangupCall.setVisibility(View.VISIBLE);
            String st = getResources().getString(R.string.Are_connected_to_each_other);
            mTvCallState.setText(st);
            EMClient.getInstance().callManager().setSurfaceView(mLocalSurface, mOppositeSurface);
            handler.sendEmptyMessage(MSG_CALL_MAKE_VIDEO);
            handler.postDelayed(() -> streamID = playMakeCallSounds(), 300);
        } else { // incoming call
            mTvCallState.setText("Ringing");
            if (EMClient.getInstance().callManager().getCallState() == EMCallStateChangeListener.CallState.IDLE
                    || EMClient.getInstance().callManager().getCallState() == EMCallStateChangeListener.CallState.DISCONNECTED) {
                // the call has ended
                finish();
                return;
            }
            mLocalSurface.setVisibility(View.INVISIBLE);
            Uri ringUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            audioManager.setMode(AudioManager.MODE_RINGTONE);
            audioManager.setSpeakerphoneOn(true);
            ringtone = RingtoneManager.getRingtone(this, ringUri);
            ringtone.play();
            EMClient.getInstance().callManager().setSurfaceView(mLocalSurface, mOppositeSurface);
        }

        final int MAKE_CALL_TIMEOUT = 50 * 1000;
        handler.removeCallbacks(timeoutHangup);
        handler.postDelayed(timeoutHangup, MAKE_CALL_TIMEOUT);

        // get instance of call helper, should be called after setSurfaceView was called
        callHelper = EMClient.getInstance().callManager().getVideoCallHelper();

        EMClient.getInstance().callManager().setCameraDataProcessor(dataProcessor);
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

    /**
     * 切换通话界面，这里就是交换本地和远端画面控件设置，以达到通话大小画面的切换
     */
    private void changeCallView() {
        if (surfaceState == 0) {
            surfaceState = 1;
            EMClient.getInstance().callManager().setSurfaceView(mOppositeSurface, mLocalSurface);
        } else {
            surfaceState = 0;
            EMClient.getInstance().callManager().setSurfaceView(mLocalSurface, mOppositeSurface);
        }
    }

    /**
     * set call state listener
     */
    void addCallStateListener() {
        callStateListener = (callState, error) -> {
            switch (callState) {

                case CONNECTING: // is connecting
                    runOnUiThread(() -> mTvCallState.setText(R.string.Are_connected_to_each_other));
                    break;
                case CONNECTED: // connected
                    runOnUiThread(() -> {
//                            mTvCallState.setText(R.string.have_connected_with);
                    });
                    break;

                case ACCEPTED: // call is accepted
                    surfaceState = 0;
                    handler.removeCallbacks(timeoutHangup);
                    runOnUiThread(() -> {
                        try {
                            if (soundPool != null) {
                                soundPool.stop(streamID);
                            }
                            EMLog.d("EMCallManager", "soundPool stop ACCEPTED");
                        } catch (Exception e) {
                        }
                        openSpeakerOn();
                        ((TextView) findViewById(R.id.tv_is_p2p)).setText(EMClient.getInstance().callManager().isDirectCall()
                                ? R.string.direct_call : R.string.relay_call);
                        mIvHandsfree.setImageResource(R.mipmap.btn_chat_handsfree_on);
                        isHandsfreeState = true;
                        isInCalling = true;
                        mChronometer.setVisibility(View.VISIBLE);
                        mChronometer.setBase(SystemClock.elapsedRealtime());
                        // call durations start
                        mChronometer.start();
                        mTvNick.setVisibility(View.INVISIBLE);
                        mTvCallState.setText(R.string.In_the_call);
//                            recordBtn.setVisibility(View.VISIBLE);
                        callingState = CallingState.NORMAL;
                        startMonitor();
                    });
                    break;
                case NETWORK_DISCONNECTED:
                    runOnUiThread(() -> {
                        mTvNetworkStatus.setVisibility(View.VISIBLE);
                        mTvNetworkStatus.setText(R.string.network_unavailable);
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
                case VIDEO_PAUSE:
                    runOnUiThread(() -> Toast.makeText(getApplicationContext(), "VIDEO_PAUSE", Toast.LENGTH_SHORT).show());
                    break;
                case VIDEO_RESUME:
                    runOnUiThread(() -> Toast.makeText(getApplicationContext(), "VIDEO_RESUME", Toast.LENGTH_SHORT).show());
                    break;
                case VOICE_PAUSE:
                    runOnUiThread(() -> Toast.makeText(getApplicationContext(), "VOICE_PAUSE", Toast.LENGTH_SHORT).show());
                    break;
                case VOICE_RESUME:
                    runOnUiThread(() -> Toast.makeText(getApplicationContext(), "VOICE_RESUME", Toast.LENGTH_SHORT).show());
                    break;
                case DISCONNECTED: // call is disconnected
                    handler.removeCallbacks(timeoutHangup);
                    @SuppressWarnings("UnnecessaryLocalVariable") final EMCallStateChangeListener.CallError fError = error;
                    runOnUiThread(new Runnable() {
                        private void postDelayedCloseMsg() {
                            uiHandler.postDelayed(() -> {
                                removeCallStateListener();
                                saveCallRecord();
                                Animation animation = new AlphaAnimation(1.0f, 0.0f);
                                animation.setDuration(1200);
                                mRootLayout.startAnimation(animation);
                                finish();
                            }, 200);
                        }

                        @Override
                        public void run() {
                            mChronometer.stop();
                            callDruationText = mChronometer.getText().toString();
                            String s1 = getResources().getString(R.string.The_other_party_refused_to_accept);
                            String s2 = getResources().getString(R.string.Connection_failure);
                            String s3 = getResources().getString(R.string.The_other_party_is_not_online);
                            String s4 = getResources().getString(R.string.The_other_is_on_the_phone_please);
                            String s5 = getResources().getString(R.string.The_other_party_did_not_answer);

                            String s6 = getResources().getString(R.string.hang_up);
                            String s7 = getResources().getString(R.string.The_other_is_hang_up);
                            String s8 = getResources().getString(R.string.did_not_answer);
                            String s9 = getResources().getString(R.string.Has_been_cancelled);
                            String s10 = getResources().getString(R.string.Refused);

                            if (fError == EMCallStateChangeListener.CallError.REJECTED) {
                                callingState = CallingState.BEREFUSED;
                                mTvCallState.setText(s1);
                            } else if (fError == EMCallStateChangeListener.CallError.ERROR_TRANSPORT) {
                                mTvCallState.setText(s2);
                            } else if (fError == EMCallStateChangeListener.CallError.ERROR_UNAVAILABLE) {
                                callingState = CallingState.OFFLINE;
                                mTvCallState.setText(s3);
                            } else if (fError == EMCallStateChangeListener.CallError.ERROR_BUSY) {
                                callingState = CallingState.BUSY;
                                mTvCallState.setText(s4);
                            } else if (fError == EMCallStateChangeListener.CallError.ERROR_NORESPONSE) {
                                callingState = CallingState.NO_RESPONSE;
                                mTvCallState.setText(s5);
                            } else if (fError == EMCallStateChangeListener.CallError.ERROR_LOCAL_SDK_VERSION_OUTDATED || fError == EMCallStateChangeListener.CallError.ERROR_REMOTE_SDK_VERSION_OUTDATED) {
                                callingState = CallingState.VERSION_NOT_SAME;
                                mTvCallState.setText(R.string.call_version_inconsistent);
                            } else {
                                if (isRefused) {
                                    callingState = CallingState.REFUSED;
                                    mTvCallState.setText(s10);
                                } else if (isAnswered) {
                                    callingState = CallingState.NORMAL;
                                    if (endCallTriggerByMe) {
//                                        mTvCallState.setText(s6);
                                    } else {
                                        mTvCallState.setText(s7);
                                    }
                                } else {
                                    if (isInComingCall) {
                                        callingState = CallingState.UNANSWERED;
                                        mTvCallState.setText(s8);
                                    } else {
                                        if (callingState != CallingState.NORMAL) {
                                            callingState = CallingState.CANCELLED;
                                            mTvCallState.setText(s9);
                                        } else {
                                            mTvCallState.setText(s6);
                                        }
                                    }
                                }
                            }
                            Toast.makeText(VideoCallActivity.this, mTvCallState.getText(), Toast.LENGTH_SHORT).show();
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
            case R.id.local_surface:
                changeCallView();
                break;
            case R.id.btn_refuse_call: // decline the call
                isRefused = true;
                mBtnRefuseCall.setEnabled(false);
                handler.sendEmptyMessage(MSG_CALL_REJECT);
                break;

            case R.id.btn_answer_call: // answer the call
                EMLog.d(TAG, "btn_answer_call clicked");
                mBtnAnswerCall.setEnabled(false);
                openSpeakerOn();
                if (ringtone != null)
                {ringtone.stop();}

                mTvCallState.setText("answering...");
                handler.sendEmptyMessage(MSG_CALL_ANSWER);
                mIvHandsfree.setImageResource(R.mipmap.btn_chat_handsfree_on);
                isAnswered = true;
                isHandsfreeState = true;
                setButtonState(false);
                mIvHandsfree.setVisibility(View.VISIBLE);
                mLocalSurface.setVisibility(View.VISIBLE);
                break;

            case R.id.btn_hangup_call: // hangup
                mIvHandsfree.setEnabled(false);
                mChronometer.stop();
                endCallTriggerByMe = true;
                mTvCallState.setText(getResources().getString(R.string.hanging_up));
                if (isRecording) {
                    callHelper.stopVideoRecord();
                }
                EMLog.d(TAG, "btn_hangup_call");
                handler.sendEmptyMessage(MSG_CALL_END);
                break;

            case R.id.iv_mute: // mute
                if (isMuteState) {
                    // resume voice transfer
                    mIvMute.setImageResource(R.mipmap.btn_chat_mute);
                    try {
                        EMClient.getInstance().callManager().resumeVoiceTransfer();
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                    }
                    isMuteState = false;
                } else {
                    // pause voice transfer
                    mIvMute.setImageResource(R.mipmap.btn_chat_mute_on);
                    try {
                        EMClient.getInstance().callManager().pauseVoiceTransfer();
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                    }
                    isMuteState = true;
                }
                break;
            case R.id.iv_handsfree: // handsfree
                if (isHandsfreeState) {
                    // turn off speaker
                    mIvHandsfree.setImageResource(R.mipmap.btn_chat_handsfree);
                    closeSpeakerOn();
                    isHandsfreeState = false;
                } else {
                    mIvHandsfree.setImageResource(R.mipmap.btn_chat_handsfree_on);
                    openSpeakerOn();
                    isHandsfreeState = true;
                }
                break;
            case R.id.root_layout:
                if (callingState == CallingState.NORMAL) {
                    if (mBottomContainer.getVisibility() == View.VISIBLE) {
                        mBottomContainer.setVisibility(View.GONE);
                        mLlTopContainer.setVisibility(View.GONE);
                        mOppositeSurface.setScaleMode(VideoView.EMCallViewScaleMode.EMCallViewScaleModeAspectFill);

                    } else {
                        mBottomContainer.setVisibility(View.VISIBLE);
                        mLlTopContainer.setVisibility(View.VISIBLE);
                        mOppositeSurface.setScaleMode(VideoView.EMCallViewScaleMode.EMCallViewScaleModeAspectFit);
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
//        DemoHelper.getInstance().isVideoCalling = false;
        stopMonitor();
        if (isRecording) {
            callHelper.stopVideoRecord();
            isRecording = false;
        }
        mLocalSurface.getRenderer().dispose();
        mLocalSurface = null;
        mOppositeSurface.getRenderer().dispose();
        mOppositeSurface = null;
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        callDruationText = mChronometer.getText().toString();
        super.onBackPressed();
    }

    /**
     * for debug & testing, you can remove this when release
     */
    void startMonitor() {
        monitor = true;
        new Thread(() -> {
            while (monitor) {
                runOnUiThread(() -> {
                    mTvCallMonitor.setText("WidthxHeight：" + callHelper.getVideoWidth() + "x" + callHelper.getVideoHeight()
                            + "\nDelay：" + callHelper.getVideoLatency()
                            + "\nFramerate：" + callHelper.getVideoFrameRate()
                            + "\nLost：" + callHelper.getVideoLostRate()
                            + "\nLocalBitrate：" + callHelper.getLocalBitrate()
                            + "\nRemoteBitrate：" + callHelper.getRemoteBitrate());

                    ((TextView) findViewById(R.id.tv_is_p2p)).setText(EMClient.getInstance().callManager().isDirectCall()
                            ? R.string.direct_call : R.string.relay_call);
                });
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                }
            }
        }, "CallMonitor").start();
    }

    void stopMonitor() {
        monitor = false;
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        if (isInCalling) {
            try {
                EMClient.getInstance().callManager().pauseVideoTransfer();
            } catch (HyphenateException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isInCalling) {
            try {
                EMClient.getInstance().callManager().resumeVideoTransfer();
            } catch (HyphenateException e) {
                e.printStackTrace();
            }
        }
    }

}
