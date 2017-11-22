package com.zhiyicx.zhibolibrary.di.component;


import com.zhiyicx.zhibolibrary.di.ActivityScope;
import com.zhiyicx.zhibolibrary.di.module.StarExchangeModule;
import com.zhiyicx.zhibolibrary.ui.activity.ZBLStarExchangeActivity;

import dagger.Component;

/**
 * Created by zhiyicx on 2016/3/14.
 */
@ActivityScope
@Component(modules = StarExchangeModule.class, dependencies = ClientComponent.class)
public interface StarExchangeComponent {
    void inject(ZBLStarExchangeActivity activity);
}
