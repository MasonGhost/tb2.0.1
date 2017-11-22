package com.zhiyicx.zhibolibrary.di.component;

import com.zhiyicx.zhibolibrary.di.ActivityScope;
import com.zhiyicx.zhibolibrary.di.module.PublishCoreModule;
import com.zhiyicx.zhibolibrary.di.module.UserHomeModule;
import com.zhiyicx.zhibolibrary.ui.fragment.ZBLPublishCoreFragment;

import dagger.Component;

/**
 * Created by zhiyicx on 2016/3/14.
 */
@ActivityScope
@Component(modules = {PublishCoreModule.class,UserHomeModule.class}, dependencies = {ClientComponent.class})
public interface PublishCoreComponent {
    void inject(ZBLPublishCoreFragment fragment);
}
