package com.zhiyicx.imsdk.policy.timeout;

/**
 * Created by jungle on 16/8/9.
 * com.zhiyicx.imsdk.policy.soupport
 * zhibo_android
 * email:335891510@qq.com
 */

import android.util.SparseArray;

import com.zhiyicx.imsdk.entity.MessageContainer;

import java.util.LinkedList;

public class TimeOutTaskManager {
    private static final String TAG = "TimeOutTaskManager";
    // 请求队列
    private LinkedList<TimeOutTask> mTimeOutTasks;
    private SparseArray<TimeOutTask> mOutTaskHashMapCache = new SparseArray<>();
    private static TimeOutTaskManager sTimeOutTaskManager;

    private TimeOutTaskManager() {
        mTimeOutTasks = new LinkedList<>();
    }

    public static synchronized TimeOutTaskManager getInstance() {
        if (sTimeOutTaskManager == null) {
            sTimeOutTaskManager = new TimeOutTaskManager();
        }
        return sTimeOutTaskManager;
    }

    /**
     * 加入队列
     *
     * @param timeOutTask
     */
    public void addTimeoutTask(TimeOutTask timeOutTask) {
        try {
            mOutTaskHashMapCache.put(timeOutTask.getMessageContainer().msg.id, timeOutTask);
            synchronized (mTimeOutTasks) {
                // 增加任务
                mTimeOutTasks.addLast(timeOutTask);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TimeOutTask getTimeoutTask() {
        synchronized (mTimeOutTasks) {
            if (mTimeOutTasks.size() > 0) {
                return mTimeOutTasks.removeFirst();
            }
        }
        return null;
    }

    /**
     * 取消超时监听
     *
     * @param str_id
     * @return
     */
    public MessageContainer cancleTimeoutTask(String str_id) {
        int id = Integer.valueOf(str_id);
        MessageContainer messageConteainer = null;
        if (mOutTaskHashMapCache.get(id) != null) {
            messageConteainer = mOutTaskHashMapCache.get(id).getMessageContainer();
            mOutTaskHashMapCache.get(id).end();
            mOutTaskHashMapCache.remove(id);
        }
        return messageConteainer;
    }
}