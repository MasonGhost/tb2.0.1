package com.zhiyicx.zhibosdk.di.component;

import com.zhiyicx.zhibosdk.di.ActivityScope;
import com.zhiyicx.zhibosdk.di.module.SplashModule;
import com.zhiyicx.zhibosdk.service.ZBUpdateService;

import dagger.Component;

/**
 * Created by zhiyicx on 2016/3/14.
 */
@ActivityScope
@Component(modules = SplashModule.class, dependencies = ClientComponent.class)
public interface UpdateComponent {
    void inject(ZBUpdateService service);
}
