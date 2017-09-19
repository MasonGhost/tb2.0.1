package com.zhiyicx.zhibolibrary.di.component;


import com.zhiyicx.zhibolibrary.di.ActivityScope;
import com.zhiyicx.zhibolibrary.di.module.LivePlayModule;
import com.zhiyicx.zhibolibrary.ui.activity.LivePlayActivity;

import dagger.Component;

/**
 * Created by zhiyicx on 2016/3/14.
 */
@ActivityScope
@Component(modules = LivePlayModule.class, dependencies = ClientComponent.class)
public interface LivePlayComponent {
    void inject(LivePlayActivity activity);
}
