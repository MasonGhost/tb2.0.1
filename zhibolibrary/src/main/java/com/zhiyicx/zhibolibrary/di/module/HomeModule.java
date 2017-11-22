package com.zhiyicx.zhibolibrary.di.module;


import com.zhiyicx.zhibolibrary.di.ActivityScope;
import com.zhiyicx.zhibolibrary.model.HomeModel;
import com.zhiyicx.zhibolibrary.model.api.service.ServiceManager;
import com.zhiyicx.zhibolibrary.model.impl.HomeModelImpl;
import com.zhiyicx.zhibolibrary.ui.view.HomeView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by zhiyicx on 2016/3/14.
 */
@Module
public class HomeModule {
    private HomeView mHomeView;

    public HomeModule(HomeView homeView) {
        this.mHomeView = homeView;
    }

    @ActivityScope
    @Provides
    HomeView provideHomeView() {
        return this.mHomeView;
    }

    @ActivityScope
    @Provides
    HomeModel provideHomeModel(ServiceManager manager) {
        return new HomeModelImpl(manager);
    }
}
