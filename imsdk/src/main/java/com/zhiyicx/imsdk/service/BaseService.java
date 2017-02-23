package com.zhiyicx.imsdk.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import org.simple.eventbus.EventBus;

/**
 * Created by junle on 16/7/6.
 */
public abstract class BaseService extends Service {

    protected final String TAG = this.getClass().getSimpleName();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

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
    }

    /**
     * 初始化
     */
    abstract public void init();
}
