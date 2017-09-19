package com.zhiyicx.zhibosdk.di.module;


import com.zhiyicx.zhibosdk.di.ActivityScope;
import com.zhiyicx.zhibosdk.model.SplashModel;
import com.zhiyicx.zhibosdk.model.api.service.ZBServiceManager;
import com.zhiyicx.zhibosdk.model.imp.SplashModelImpl;

import dagger.Module;
import dagger.Provides;

/**
 * Created by zhiyicx on 2016/3/14.
 */
@Module
public class SplashModule {



    public SplashModule() {
    }


    @ActivityScope
    @Provides
    SplashModel provideSplashModel(ZBServiceManager manager) {
        return new SplashModelImpl(manager);
    }
}
