package com.zhiyicx.zhibosdk.di.module;


import com.zhiyicx.zhibosdk.di.ActivityScope;
import com.zhiyicx.zhibosdk.model.LivePlayModel;
import com.zhiyicx.zhibosdk.model.api.service.ZBServiceManager;
import com.zhiyicx.zhibosdk.model.imp.LivePlayModelImpl;

import dagger.Module;
import dagger.Provides;

/**
 * Created by zhiyicx on 2016/3/14.
 */
@Module
public class ZBPlayModule {



    public ZBPlayModule() {
    }

    @ActivityScope
    @Provides
    LivePlayModel provideLivePlayModel(ZBServiceManager manager) {
        return new LivePlayModelImpl(manager);
    }
}
