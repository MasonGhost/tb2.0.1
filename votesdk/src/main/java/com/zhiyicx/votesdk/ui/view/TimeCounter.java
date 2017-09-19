package com.zhiyicx.votesdk.ui.view;

import android.os.CountDownTimer;

/**
 * Created by lei on 2016/8/19.
 */
public class TimeCounter extends CountDownTimer {
    private OnTimeCounterListener listener;
    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */
    public TimeCounter(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    public void setOnTimeCounterListener(OnTimeCounterListener listener){
        this.listener = listener;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        if (listener!=null){
            listener.onTick(millisUntilFinished);
        }

    }

    @Override
    public void onFinish() {
        if (listener!=null){
            listener.onFinish();
        }
    }


    interface OnTimeCounterListener{

        void onTick(long millisUntilFinished);

        void onFinish();
    }
}
