package com.zhiyicx.thinksnsplus.widget;

import android.os.CountDownTimer;
import android.text.TextUtils;
import android.widget.TextView;

/**
 * @Author Jliuer
 * @Date 2017/5/16/14:18
 * @Email Jliuer@aliyun.com
 * @Description 倒计时
 */
public class TCountTimer extends CountDownTimer {

    private int mTime_count = 6000;
    private int mTime_interval = 1000;
    private TextView mBtn;
    private String mEndStr;
    private int mNormalColor, mTimingColor;
    private String mDurStr;
    private boolean canUseOntick;
    private boolean canUseListener;
    private boolean durStrBehindTime;
    private OnTimeListener mOnTimeListener;

    public static final class Builder {
        private int time_count = 6000;
        private int time_interval = 1000;
        private TextView btn;
        private String endStr;
        private int normalColor, timingColor;
        private String durStr;
        private boolean canUseOntick;
        private boolean canUseListener;
        private boolean durStrBehindTime;
        private OnTimeListener onTimeListener;

        private Builder(TCountTimer timer) {
            this.time_count = timer.mTime_count;
            this.time_interval = timer.mTime_interval;
            this.btn = timer.mBtn;
            this.endStr = timer.mEndStr;
            this.normalColor = timer.mNormalColor;
            this.timingColor = timer.mTimingColor;
            this.durStr = timer.mDurStr;
            this.canUseOntick = timer.canUseOntick;
            this.canUseListener = timer.canUseListener;
            this.durStrBehindTime = timer.durStrBehindTime;
            this.onTimeListener = timer.mOnTimeListener;
        }

        private Builder() {
        }

        public Builder buildTimeCount(int time_count) {
            this.time_count = time_count;
            return this;
        }

        public Builder buildTimeInterval(int time_interval) {
            this.time_interval = time_interval;
            return this;
        }

        public Builder buildBtn(TextView btn) {
            this.btn = btn;
            return this;
        }

        public Builder buildEndStr(String endStr) {
            this.endStr = endStr;
            return this;
        }

        public Builder buildDurText(String durStr) {
            this.durStr = durStr;
            return this;
        }

        public Builder buildNormalColor(int normalColor) {
            this.normalColor = normalColor;
            return this;
        }

        public Builder buildTimingColor(int timingColor) {
            this.timingColor = timingColor;
            return this;
        }

        public Builder buildCanUseOntick(boolean canUseOntick) {
            this.canUseOntick = canUseOntick;
            return this;
        }

        public Builder buildCanUseListener(boolean canUseListener) {
            this.canUseListener = canUseListener;
            return this;
        }

        public Builder buildDurStrBehindTime(boolean durStrBehindTime) {
            this.durStrBehindTime = durStrBehindTime;
            return this;
        }

        public Builder buildOnTimeListener(OnTimeListener onTimeListener) {
            this.onTimeListener = onTimeListener;
            return this;
        }

        public TCountTimer build() {
            if (btn == null)
                throw new IllegalArgumentException("view can not be null");
            return new TCountTimer(this);
        }
    }

    public TCountTimer(Builder builder) {
        super(builder.time_count, builder.time_interval);
        this.mBtn = builder.btn;
        this.mDurStr = builder.durStr;
        this.mEndStr = builder.endStr;
        this.mNormalColor = builder.normalColor;
        this.mTimingColor = builder.timingColor;
        this.mTime_interval = builder.time_interval;
        this.mTime_count = builder.time_count;
        this.canUseOntick = builder.canUseOntick;
        this.canUseListener = builder.canUseListener;
        this.durStrBehindTime = builder.durStrBehindTime;
        this.mOnTimeListener = builder.onTimeListener;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Builder newBuilder() {
        return new Builder(this);
    }


    // 计时完毕时触发
    @Override
    public void onFinish() {
        if (mNormalColor > 0) {
            mBtn.setTextColor(mNormalColor);
        }
        mBtn.setText(mEndStr);
        mBtn.setEnabled(true);
        if (mOnTimeListener != null && canUseListener) {
            mOnTimeListener.onFinish();
        }
    }

    // 计时过程显示
    @Override
    public void onTick(long millisUntilFinished) {
        if (mBtn == null) {
            replease();
            return;
        }
        if (mTimingColor > 0) {
            mBtn.setTextColor(mTimingColor);
        }
        mBtn.setEnabled(canUseOntick);
        if (!TextUtils.isEmpty(mDurStr)) {
            String str = mDurStr + " " + millisUntilFinished / 1000 + "s";
            if (durStrBehindTime) {
                str = millisUntilFinished / 1000 + "s" + " " + mDurStr;
            }
            mBtn.setText(str);
        } else {
            mBtn.setText(millisUntilFinished / 1000 + "s");
        }

        if (mOnTimeListener != null && canUseListener) {
            mOnTimeListener.onTick();
        }

    }

    public void rest() {
        cancel();
        if (mNormalColor > 0) {
            mBtn.setTextColor(mNormalColor);
        }
        mBtn.setText(mEndStr);
        mBtn.setEnabled(true);
    }

    private void replease() {
        cancel();
    }

    public interface OnTimeListener {
        void onTick();

        void onFinish();
    }
}