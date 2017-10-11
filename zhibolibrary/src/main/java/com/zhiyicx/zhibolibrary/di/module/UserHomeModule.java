package com.zhiyicx.zhibolibrary.di.module;


import com.zhiyicx.zhibolibrary.di.ActivityScope;
import com.zhiyicx.zhibolibrary.model.UserHomeModel;
import com.zhiyicx.zhibolibrary.model.api.service.ServiceManager;
import com.zhiyicx.zhibolibrary.model.impl.UserHomeModelImpl;
import com.zhiyicx.zhibolibrary.ui.view.UserHomeView;
import com.zhiyicx.zhibolibrary.util.UiUtils;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by zhiyicx on 2016/3/14.
 */
@Module
public class UserHomeModule {
    private UserHomeView mView;

    public UserHomeModule(UserHomeView view) {
        this.mView = view;
    }

    @ActivityScope
    @Provides
    UserHomeView provideUserHomeView() {
        return this.mView;
    }

    @ActivityScope
    @Provides
    UserHomeModel provideUserHomeModel(ServiceManager manager) {
        return new UserHomeModelImpl(manager);
    }

}
