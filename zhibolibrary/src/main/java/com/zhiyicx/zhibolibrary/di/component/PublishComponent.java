package com.zhiyicx.zhibolibrary.di.component;


import com.zhiyicx.zhibolibrary.di.ActivityScope;
import com.zhiyicx.zhibolibrary.di.module.PublishModule;
import com.zhiyicx.zhibolibrary.ui.activity.PublishLiveActivity;

import dagger.Component;

/**
 * Created by zhiyicx on 2016/3/14.
 */
@ActivityScope
@Component(modules = PublishModule.class, dependencies = ClientComponent.class)
public interface PublishComponent {
    void inject(PublishLiveActivity activity);
}
