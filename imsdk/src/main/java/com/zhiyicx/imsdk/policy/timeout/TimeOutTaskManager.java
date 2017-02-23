package com.zhiyicx.imsdk.policy.timeout;

/**
 * Created by jungle on 16/8/9.
 * com.zhiyicx.imsdk.policy.soupport
 * zhibo_android
 * email:335891510@qq.com
 */

import com.zhiyicx.imsdk.entity.MessageContainer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class TimeOutTaskManager {
    private static final String TAG = "TimeOutTaskManager";
    // 请求队列
    private LinkedList<TimeOutTask> mTimeOutTasks;

    private Map<Integer, TimeOutTask> mOutTaskHashMapCache = new HashMap<>();
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
     * @param timeOutTask
     */
    public void addTimeoutTask(TimeOutTask timeOutTask) {
        mOutTaskHashMapCache.put(timeOutTask.getMessageContainer().msg.id, timeOutTask);
        synchronized (mTimeOutTasks) {
            // 增加任务
            mTimeOutTasks.addLast(timeOutTask);
        }

    }

    public TimeOutTask getTimeoutTask() {
        synchronized (mTimeOutTasks) {
            if (mTimeOutTasks.size() > 0) {
                TimeOutTask timeOutTask = mTimeOutTasks.removeFirst();
                return timeOutTask;
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
    public MessageContainer cancleTimeoutTask(String str_id){
        int id = Integer.valueOf(str_id);
        MessageContainer messageConteainer = null;
        if (mOutTaskHashMapCache.containsKey(id)) {
            mOutTaskHashMapCache.get(id).end();
            messageConteainer = mOutTaskHashMapCache.get(id).getMessageContainer();
            mOutTaskHashMapCache.remove(id);
        }
        return messageConteainer;
    }
}