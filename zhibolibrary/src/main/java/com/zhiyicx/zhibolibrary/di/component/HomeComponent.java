package com.zhiyicx.zhibolibrary.di.component;


import com.zhiyicx.zhibolibrary.di.ActivityScope;
import com.zhiyicx.zhibolibrary.di.module.HomeModule;
import com.zhiyicx.zhibolibrary.ui.activity.HomeActivity;

import dagger.Component;

/**
 * Created by zhiyicx on 2016/3/14.
 */
@ActivityScope
@Component(modules = HomeModule.class, dependencies = ClientComponent.class)
public interface HomeComponent {
    void inject(HomeActivity activity);
}
