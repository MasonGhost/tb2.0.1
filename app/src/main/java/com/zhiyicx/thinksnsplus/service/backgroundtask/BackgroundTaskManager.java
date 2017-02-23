package com.zhiyicx.thinksnsplus.service.backgroundtask;

import android.content.Context;
import android.content.Intent;

import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;

import org.simple.eventbus.EventBus;

import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.EVENT_BACKGROUND_TASK;

/**
 * @Describe 任务管理器，使用后台任务只需使用此类即可
 * @Author Jungle68
 * @Date 2017/1/23
 * @Contact master.jungle68@gmail.com
 */

public class BackgroundTaskManager {
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
    public void addBackgroundRequestTask(BackgroundRequestTaskBean backgroundRequestTaskBean) {
        startBackgroundTask();
        EventBus.getDefault().post(backgroundRequestTaskBean, EVENT_BACKGROUND_TASK);
    }

    /**
     * 开启后台任务，主要处理缓存任务
     */
    public void startBackgroundTask() {
        if (!mIsServiceStart) {
            mContext.startService(new Intent(mContext, BackgroundTaskHandleService.class));
            mIsServiceStart = true;
        }
    }

    /**
     * 关闭后台任务
     */
    public void closeBackgroundTask() {
//        if (mIsServiceStart && mContext.stopService(new Intent(mContext, BackgroundTaskHandleService.class))) {
//            mIsServiceStart = false;
//        }
    }
}
