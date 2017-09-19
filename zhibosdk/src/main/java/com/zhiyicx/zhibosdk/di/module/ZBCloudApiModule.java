package com.zhiyicx.zhibosdk.di.module;


import com.zhiyicx.zhibosdk.di.ActivityScope;
import com.zhiyicx.zhibosdk.model.CloudApiModel;
import com.zhiyicx.zhibosdk.model.api.service.ZBServiceManager;
import com.zhiyicx.zhibosdk.model.imp.CloudApiModelImpl;

import dagger.Module;
import dagger.Provides;

/**
 * Created by zhiyicx on 2016/3/14.
 */
@Module
public class ZBCloudApiModule {



    public ZBCloudApiModule() {
    }


    @ActivityScope
    @Provides
    CloudApiModel provideCloudApiModel(ZBServiceManager manager) {
        return new CloudApiModelImpl(manager);
    }
}
