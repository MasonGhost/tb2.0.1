package com.zhiyicx.old.imsdk.policy.timeout;

import com.zhiyicx.old.imsdk.entity.MessageContainer;

/**
 * Created by jungle on 16/8/9.
 * com.zhiyicx.old.imsdk.policy
 * zhibo_android
 * email:335891510@qq.com
 */
public class TimeOutTask implements Runnable {
    public MessageContainer getMessageContainer() {
        return mMessageContainer;
    }

    private MessageContainer mMessageContainer;
    private long begin_time;
    private long out_time = 10 * 1000;//超时时间


    private boolean isEnd;

    private TimeOutListener mListener;

    public void setEnd(boolean end) {

        this.isEnd = end;
    }

    public TimeOutTask(MessageContainer mMessageContainer, long begin_time, TimeOutListener l) {
        this.mMessageContainer = mMessageContainer;
        this.begin_time = begin_time;
        this.mListener=l;
        mMessageContainer.reSendCounts++;
    }

    @Override
    public void run() {

        while (!isEnd) {

            if (System.currentTimeMillis() - begin_time >= out_time) {
                timeout();
                isEnd = true;
            }else {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private void timeout() {
            if(mListener!=null)
                mListener.timeOut(mMessageContainer);
    }

}