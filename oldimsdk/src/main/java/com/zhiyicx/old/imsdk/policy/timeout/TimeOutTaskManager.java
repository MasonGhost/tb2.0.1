package com.zhiyicx.old.imsdk.policy.timeout;

/**
 * Created by jungle on 16/8/9.
 * com.zhiyicx.old.imsdk.policy.soupport
 * zhibo_android
 * email:335891510@qq.com
 */

import java.util.LinkedList;

public class TimeOutTaskManager {
    private static final String TAG = "TimeOutTaskManager";
    // UI请求队列
    private LinkedList<TimeOutTask> timeOutTasks;
    // 任务不能重复

    private static TimeOutTaskManager timeOutTaskManager;

    private TimeOutTaskManager() {

        timeOutTasks = new LinkedList<>();

    }

    public static synchronized TimeOutTaskManager getInstance() {
        if (timeOutTaskManager == null) {
            timeOutTaskManager = new TimeOutTaskManager();
        }
        return timeOutTaskManager;
    }

    //1.先执行
    public void addTimeoutTask(TimeOutTask downloadTask) {
        synchronized (timeOutTasks) {
            // 增加下载任务
            timeOutTasks.addLast(downloadTask);
        }

    }


    public TimeOutTask getTimeoutTask() {
        synchronized (timeOutTasks) {
            if (timeOutTasks.size() > 0) {
                TimeOutTask timeOutTask = timeOutTasks.removeFirst();
                return timeOutTask;
            }
        }
        return null;
    }
}