package com.zhiyicx.zhibolibrary.di.module;


import com.zhiyicx.zhibolibrary.di.ActivityScope;
import com.zhiyicx.zhibolibrary.model.ConfigManagerModel;
import com.zhiyicx.zhibolibrary.model.UserHomeModel;
import com.zhiyicx.zhibolibrary.model.api.service.ServiceManager;
import com.zhiyicx.zhibolibrary.model.impl.ConfigManagerModelImpl;
import com.zhiyicx.zhibolibrary.model.impl.UserHomeModelImpl;

import dagger.Module;
import dagger.Provides;

/**
 * Created by zhiyicx on 2016/3/14.
 */
@Module
public class ConfigManagerModule {

    public ConfigManagerModule() {
    }

    @ActivityScope
    @Provides
    UserHomeModel provideUserHomeModel(ServiceManager manager) {
        return new UserHomeModelImpl(manager);
    }
    @ActivityScope
    @Provides
    ConfigManagerModel provideConfigManagerModel(ServiceManager manager) {
        return new ConfigManagerModelImpl(manager);
    }


}
