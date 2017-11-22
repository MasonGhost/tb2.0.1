package com.zhiyicx.zhibolibrary.di.module;



import com.zhiyicx.zhibolibrary.di.ActivityScope;
import com.zhiyicx.zhibolibrary.model.StarExchangeModel;
import com.zhiyicx.zhibolibrary.model.api.service.ServiceManager;
import com.zhiyicx.zhibolibrary.model.impl.StarExchangeModelImpl;
import com.zhiyicx.zhibolibrary.ui.view.StarExchangeView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by zhiyicx on 2016/3/14.
 */
@Module
public class StarExchangeModule {
    private StarExchangeView mView;

    public StarExchangeModule(StarExchangeView view) {
        this.mView = view;
    }

    @ActivityScope
    @Provides
    StarExchangeView provideStarExchangeView() {
        return this.mView;
    }

    @ActivityScope
    @Provides
    StarExchangeModel provideStarExchangeModel(ServiceManager manager) {
        return new StarExchangeModelImpl(manager);
    }
}
