package com.zhiyicx.zhibosdk.di.component;

import com.zhiyicx.zhibosdk.di.ActivityScope;
import com.zhiyicx.zhibosdk.di.module.ZBCloudApiModule;
import com.zhiyicx.zhibosdk.manage.ZBCloudApiClient;

import dagger.Component;

/**
 * Created by zhiyicx on 2016/3/14.
 */
@ActivityScope
@Component(modules = ZBCloudApiModule.class, dependencies = ClientComponent.class)
public interface ZBCloudApiClientComponent {
    void inject(ZBCloudApiClient zbCloudApiClient);
}
