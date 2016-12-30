package com.zhiyicx.imsdk.policy.timeout;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by jungle on 16/8/9.
 * com.zhiyicx.imsdk.policy
 * zhibo_android
 * email:335891510@qq.com
 */
public class TimeOutTaskPool implements Runnable {
    private static final long INTERVAL_FOR_CPU = 100;
    private TimeOutTaskManager timeOutTaskManager;

    // 创建一个可重用固定线程数的线程池
    private ExecutorService pool;
    // 线程池大小
    private final int POOL_SIZE = 5;

    // 是否停止
    private boolean isStop = false;

    public TimeOutTaskPool() {
        timeOutTaskManager = TimeOutTaskManager.getInstance();
        pool = Executors.newFixedThreadPool(POOL_SIZE);

    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        while (!isStop) {
            TimeOutTask timeOutTask = timeOutTaskManager.getTimeoutTask();
            if (timeOutTask != null) {
                pool.execute(timeOutTask);
            } else {  //如果当前未有downloadTask在任务队列中
            }
            //防止cpu占用过高
            try {
                Thread.sleep(INTERVAL_FOR_CPU);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (isStop) {
            pool.shutdown();
        }

    }

    /**
     * @param isStop the isStop to set
     */
    public void setStop(boolean isStop) {
        this.isStop = isStop;
    }

}


