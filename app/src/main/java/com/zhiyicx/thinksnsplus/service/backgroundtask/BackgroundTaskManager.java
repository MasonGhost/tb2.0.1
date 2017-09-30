package com.zhiyicx.thinksnsplus.service.backgroundtask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;

import org.simple.eventbus.EventBus;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Action1;

import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.EVENT_BACKGROUND_TASK;

/**
 * @Describe 任务管理器，使用后台任务只需使用此类即可
 * @Author Jungle68
 * @Date 2017/1/23
 * @Contact master.jungle68@gmail.com
 */

public class BackgroundTaskManager {
    public static final int DEFAULT_SERVICE_START_TIME_WAITING = 10; // 默认Service 启动耗时时间

    @SuppressLint("StaticFieldLeak")
    private static volatile BackgroundTaskManager sBackgroundTaskManager; // context 必须使用 application 否者会造成内存泄漏
    private Context mContext;
    private boolean mIsServiceStart;// Service 是否开启

    private BackgroundTaskManager(Context context) {
        this.mContext = context.getApplicationContext();
        EventBus.getDefault().register(this);
    }

    public static BackgroundTaskManager getInstance(Context context) {

        if (sBackgroundTaskManager == null) {
            synchronized (BackgroundTaskManager.class) {
                if (sBackgroundTaskManager == null) {
                    sBackgroundTaskManager = new BackgroundTaskManager(context);
                }
            }
        }
        return sBackgroundTaskManager;
    }

    /**
     * 加入任务队列
     *
     * @param backgroundRequestTaskBean 任务
     */
    public void addBackgroundRequestTask(final BackgroundRequestTaskBean backgroundRequestTaskBean) {
        if (startBackgroundTask()) {
            Observable.timer(DEFAULT_SERVICE_START_TIME_WAITING, TimeUnit.SECONDS)
                    .subscribe(new Action1<Long>() {
                        @Override
                        public void call(Long aLong) {
                            EventBus.getDefault().post(backgroundRequestTaskBean, EVENT_BACKGROUND_TASK);
                        }
                    });

        } else {
            EventBus.getDefault().post(backgroundRequestTaskBean, EVENT_BACKGROUND_TASK);
        }
    }

    /**
     * 开启后台任务，主要处理缓存任务
     */
    public boolean startBackgroundTask() {
        boolean start = false;
        if (!mIsServiceStart) {
            mContext.startService(new Intent(mContext, BackgroundTaskHandleService.class));
            mIsServiceStart = true;
            start = true;
        }
        return start;
    }

    /**
     * 关闭后台任务
     */
    public void closeBackgroundTask() {
        if (mIsServiceStart && mContext.stopService(new Intent(mContext, BackgroundTaskHandleService.class))) {
            mIsServiceStart = false;
        }
    }
}
