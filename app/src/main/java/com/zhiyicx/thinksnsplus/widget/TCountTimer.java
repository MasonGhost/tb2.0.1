package com.zhiyicx.thinksnsplus.widget;

import android.os.CountDownTimer;
import android.widget.TextView;

/**
 * @Author Jliuer
 * @Date 2017/5/16/14:18
 * @Email Jliuer@aliyun.com
 * @Description 倒计时
 */
public class TCountTimer extends CountDownTimer {
    public static final int TIME_COUNT = 6000;//时间防止从119s开始显示（以倒计时120s为例子）
    private TextView btn;
    private String endStrRid;
    private int normalColor, timingColor;//未计时的文字颜色，计时期间的文字颜色
    private String durText = "-1";

    /**
     * 参数 millisInFuture  倒计时总时间（如60S，120s等）
     * 参数 countDownInterval  渐变时间（每次倒计1s）
     * <p>
     * 参数 btn点击的按钮(因为Button是TextView子类，为了通用我的参数设置为TextView）
     * <p>
     * 参数 endStrRid 倒计时结束后，按钮对应显示的文字
     */
    public TCountTimer(long millisInFuture, long countDownInterval, TextView btn,
                       String endStrRid) {
        super(millisInFuture, countDownInterval);
        this.btn = btn;
        this.endStrRid = endStrRid;
    }

    public TCountTimer(TextView btn, String endStrRid) {
        super(TIME_COUNT, 1000);
        this.btn = btn;
        this.endStrRid = endStrRid;
    }

    public TCountTimer(TextView btn) {
        super(TIME_COUNT, 1000);
        this.btn = btn;
        this.endStrRid = "";
    }


    public TCountTimer(TextView tv_varify, int normalColor, int timingColor) {
        this(tv_varify);
        this.normalColor = normalColor;
        this.timingColor = timingColor;
    }

    // 计时完毕时触发
    @Override
    public void onFinish() {
        if (normalColor > 0) {
            btn.setTextColor(normalColor);
        }
        btn.setText(endStrRid);
        btn.setEnabled(true);
    }

    // 计时过程显示
    @Override
    public void onTick(long millisUntilFinished) {
        if (timingColor > 0) {
            btn.setTextColor(timingColor);
        }
//        btn.setEnabled(false);
        if (!durText.equals("-1")) {
            btn.setText(durText + " " + millisUntilFinished / 1000 + "s");
        } else {
            btn.setText(millisUntilFinished / 1000 + "s");
        }

    }

    /**
     * 倒计时 中显示的文字
     */
    public void setBtnText(String str) {
        this.durText = str;
    }

    public void rest() {
        cancel();
        if (normalColor > 0) {
            btn.setTextColor(normalColor);
        }
        btn.setText(endStrRid);
        btn.setEnabled(true);
    }
}