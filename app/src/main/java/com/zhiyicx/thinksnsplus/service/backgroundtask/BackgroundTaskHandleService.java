<<<<<<< HEAD
package com.zhiyicx.thinksnsplus.service.backgroundtask;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.EVENT_BACKGROUND_TASK;
import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.EVENT_START_BACKGROUND_TASK;
import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.EVENT_STOP_BACKGROUND_TASK;

/**
 * @Describe 后台任务处理服务
 * @Author Jungle68
 * @Date 2017/1/22
 * @Contact master.jungle68@gmail.com
 */

public class BackgroundTaskHandleService extends Service {

    private BackgroundTaskHandler mBackgroundTaskHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        init();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        mBackgroundTaskHandler.stopTask();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Subscriber(tag = EVENT_BACKGROUND_TASK, mode = ThreadMode.POST)
    public boolean addBackgroundRequestTask(BackgroundRequestTaskBean backgroundRequestTaskBean) {
        return mBackgroundTaskHandler.addBackgroundRequestTask(backgroundRequestTaskBean);
    }

    @Subscriber(tag = EVENT_STOP_BACKGROUND_TASK, mode = ThreadMode.POST)
    public void stopBackgroundRequestTask() {
        mBackgroundTaskHandler.stopTask();
    }

    @Subscriber(tag = EVENT_START_BACKGROUND_TASK, mode = ThreadMode.POST)
    public void startBackgroundRequestTask() {
        init();
    }

    private void init() {
        mBackgroundTaskHandler = new BackgroundTaskHandler();
    }

}
=======
package com.zhiyicx.thinksnsplus.service.backgroundtask;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.EVENT_BACKGROUND_TASK;
import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.EVENT_START_BACKGROUND_TASK;
import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.EVENT_STOP_BACKGROUND_TASK;

/**
 * @Describe 后台任务处理服务
 * @Author Jungle68
 * @Date 2017/1/22
 * @Contact master.jungle68@gmail.com
 */

public class BackgroundTaskHandleService extends Service {

    private BackgroundTaskHandler mBackgroundTaskHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        init();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        mBackgroundTaskHandler.stopTask();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Subscriber(tag = EVENT_BACKGROUND_TASK, mode = ThreadMode.POST)
    public boolean addBackgroundRequestTask(BackgroundRequestTaskBean backgroundRequestTaskBean) {
        return mBackgroundTaskHandler.addBackgroundRequestTask(backgroundRequestTaskBean);
    }

    @Subscriber(tag = EVENT_STOP_BACKGROUND_TASK, mode = ThreadMode.POST)
    public void stopBackgroundRequestTask() {
        mBackgroundTaskHandler.stopTask();
    }

    @Subscriber(tag = EVENT_START_BACKGROUND_TASK, mode = ThreadMode.POST)
    public void startBackgroundRequestTask() {
        init();
    }

    private void init() {
        mBackgroundTaskHandler = new BackgroundTaskHandler();
    }

}
>>>>>>> 5eb1174523744bea0c0756f5af31310a1467fb94
