package com.zhiyicx.zhibolibrary.di.module;


import com.zhiyicx.zhibolibrary.di.ActivityScope;
import com.zhiyicx.zhibolibrary.model.GoldRankModel;
import com.zhiyicx.zhibolibrary.model.api.service.ServiceManager;
import com.zhiyicx.zhibolibrary.model.impl.GoldRankModeImpl;
import com.zhiyicx.zhibolibrary.ui.view.GoldRankView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by zhiyicx on 2016/3/14.
 */
@Module
public class GoldRankModule {
    private GoldRankView mView;

    public GoldRankModule(GoldRankView view) {
        this.mView = view;
    }

    @ActivityScope
    @Provides
    GoldRankView provideGoldRankView() {
        return this.mView;
    }

    @ActivityScope
    @Provides
    GoldRankModel provideGoldRankModel(ServiceManager manager) {
        return new GoldRankModeImpl(manager);
    }
}
