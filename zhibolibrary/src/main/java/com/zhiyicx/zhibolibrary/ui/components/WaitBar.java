package com.zhiyicx.zhibolibrary.ui.components;

import android.os.Message;
import android.text.TextUtils;
import android.widget.TextView;

/**
 * Created by jess on 7/18/16.
 */
public class WaitBar {
    private String mText;
    private TextView mTextView;
    private boolean isStart;
    public static final int DURATION_PER = 400;
    private android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    mTextView.setText(mText + ".");
                    mHandler.sendEmptyMessageDelayed(1, DURATION_PER);
                    break;
                case 1:
                    mTextView.setText(mText + "..");
                    mHandler.sendEmptyMessageDelayed(2, DURATION_PER);
                    break;
                case 2:
                    mTextView.setText(mText + "...");
                    mHandler.sendEmptyMessageDelayed(0, DURATION_PER);
                    break;
            }
        }
    };

    public void setText(TextView textView, String text) {
        this.mText = text;
        this.mTextView = textView;
        start();
    }

    public void start() {
        if (TextUtils.isEmpty(mText) || mTextView == null) return;
        this.isStart = true;
        mHandler.removeCallbacksAndMessages(null);
        mHandler.sendEmptyMessage(0);
    }

    public void stop() {
        if (!isStart) return;
        mTextView.setText("");
        mHandler.removeCallbacksAndMessages(null);
    }
}
