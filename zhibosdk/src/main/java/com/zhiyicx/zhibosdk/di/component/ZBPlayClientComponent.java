package com.zhiyicx.zhibosdk.di.component;

import com.zhiyicx.zhibosdk.di.ActivityScope;
import com.zhiyicx.zhibosdk.di.module.ZBPlayModule;
import com.zhiyicx.zhibosdk.manage.ZBPlayClient;

import dagger.Component;

/**
 * Created by zhiyicx on 2016/3/14.
 */
@ActivityScope
@Component(modules = ZBPlayModule.class, dependencies = ClientComponent.class)
public interface ZBPlayClientComponent {
    void inject(ZBPlayClient zbPlayClient);
}
