package com.zhiyicx.zhibolibrary.di.module;

import com.zhiyicx.zhibolibrary.di.ActivityScope;
import com.zhiyicx.zhibolibrary.model.RankingModel;
import com.zhiyicx.zhibolibrary.model.api.service.ServiceManager;
import com.zhiyicx.zhibolibrary.model.impl.RankingModeImpl;
import com.zhiyicx.zhibolibrary.ui.view.RankingView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by zhiyicx on 2016/3/14.
 */
@Module
public class RankingModule {
    private RankingView mView;

    public RankingModule(RankingView view) {
        this.mView = view;
    }

    @ActivityScope
    @Provides
    RankingView provideRankingView() {
        return this.mView;
    }

    @ActivityScope
    @Provides
    RankingModel provideRankingModel(ServiceManager manager) {
        return new RankingModeImpl(manager);
    }
}
