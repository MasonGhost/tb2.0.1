package com.zhiyicx.zhibolibrary.di.component;


import com.zhiyicx.zhibolibrary.di.ActivityScope;
import com.zhiyicx.zhibolibrary.di.module.ConfigManagerModule;
import com.zhiyicx.zhibolibrary.manager.ConfigManager;

import dagger.Component;

/**
 * Created by jungle on 2016/10/14.
 */
@ActivityScope
@Component(modules = ConfigManagerModule.class, dependencies = ClientComponent.class)
public interface ConfigManagerComponent {
    void inject(ConfigManager configManager);
}
