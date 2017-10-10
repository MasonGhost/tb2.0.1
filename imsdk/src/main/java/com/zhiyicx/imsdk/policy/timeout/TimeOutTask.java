package com.zhiyicx.imsdk.policy.timeout;

import com.zhiyicx.imsdk.entity.MessageContainer;

/**
 * 消息超时任务器
 * Created by jungle on 16/8/9.
 * com.zhiyicx.imsdk.policy
 * zhibo_android
 * email:335891510@qq.com
 */
public class TimeOutTask implements Runnable {

    private MessageContainer mMessageContainer;
    private long begin_time;
    public static final long OUT_TIME = 5_000;// 超时时间
    public static final long SLEEP_TIME = 1_000;// 超线程沉睡时间
    private boolean isEnd;
    private TimeOutListener mListener;


    public TimeOutTask(MessageContainer mMessageContainer, long begin_time, TimeOutListener l) {
        this.mMessageContainer = mMessageContainer;
        this.begin_time = begin_time;
        this.mListener = l;
        mMessageContainer.reSendCounts++;

    }

    @Override
    public void run() {

        while (!isEnd) {
            //防止cpu占用过高
            try {
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (System.currentTimeMillis() - begin_time >= OUT_TIME) {
                timeout();
                isEnd = true;
            }

        }
    }

    /**
     * 获取任务内容
     *
     * @return
     */
    public MessageContainer getMessageContainer() {
        return mMessageContainer;
    }

    /**
     * 结束任务
     */
    public void end() {
        this.isEnd = true;
    }

    private void timeout() {
        if (mListener != null)
            mListener.timeOut(mMessageContainer);
    }

}