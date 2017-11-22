package com.zhiyicx.zhibolibrary.di.module;



import com.zhiyicx.zhibolibrary.di.ActivityScope;
import com.zhiyicx.zhibolibrary.model.GoldHistoryModel;
import com.zhiyicx.zhibolibrary.model.api.service.ServiceManager;
import com.zhiyicx.zhibolibrary.model.impl.GoldHistoryModelImpl;
import com.zhiyicx.zhibolibrary.ui.view.GoldHistoryView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by zhiyicx on 2016/3/14.
 */
@Module
public class GoldHistoryModule {
    private GoldHistoryView mView;

    public GoldHistoryModule(GoldHistoryView view) {
        this.mView = view;
    }

    @ActivityScope
    @Provides
    GoldHistoryView provideGoldHistoryView() {
        return this.mView;
    }

    @ActivityScope
    @Provides
    GoldHistoryModel provideGoldHistoryModel(ServiceManager manager) {
        return new GoldHistoryModelImpl(manager);
    }
}
