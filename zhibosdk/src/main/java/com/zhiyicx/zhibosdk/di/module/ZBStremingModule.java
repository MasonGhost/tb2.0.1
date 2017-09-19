package com.zhiyicx.zhibosdk.di.module;


import com.zhiyicx.zhibosdk.di.ActivityScope;
import com.zhiyicx.zhibosdk.model.PublishModel;
import com.zhiyicx.zhibosdk.model.api.service.ZBServiceManager;
import com.zhiyicx.zhibosdk.model.imp.PublishModelImpl;

import dagger.Module;
import dagger.Provides;

/**
 * Created by zhiyicx on 2016/3/14.
 */
@Module
public class ZBStremingModule {



    public ZBStremingModule() {
    }

    @ActivityScope
    @Provides
    PublishModel providePublishModel(ZBServiceManager manager) {
        return new PublishModelImpl(manager);
    }
}
