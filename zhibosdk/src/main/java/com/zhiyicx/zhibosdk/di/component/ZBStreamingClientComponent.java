package com.zhiyicx.zhibosdk.di.component;

import com.zhiyicx.zhibosdk.di.ActivityScope;
import com.zhiyicx.zhibosdk.di.module.ZBStremingModule;
import com.zhiyicx.zhibosdk.manage.ZBStreamingClient;

import dagger.Component;

/**
 * Created by zhiyicx on 2016/3/14.
 */
@ActivityScope
@Component(modules = ZBStremingModule.class, dependencies = ClientComponent.class)
public interface ZBStreamingClientComponent {
    void inject(ZBStreamingClient zbStreamingClient);
}
