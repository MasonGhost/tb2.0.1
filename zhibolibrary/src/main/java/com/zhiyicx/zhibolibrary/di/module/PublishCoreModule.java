package com.zhiyicx.zhibolibrary.di.module;


import com.zhiyicx.zhibolibrary.di.ActivityScope;
import com.zhiyicx.zhibolibrary.model.PublishCoreModel;
import com.zhiyicx.zhibolibrary.model.api.service.ServiceManager;
import com.zhiyicx.zhibolibrary.model.impl.PublishCoreModelImpl;
import com.zhiyicx.zhibolibrary.ui.view.PublishCoreView;
import com.zhiyicx.zhibolibrary.util.UiUtils;

import dagger.Module;
import dagger.Provides;

/**
 * Created by zhiyicx on 2016/3/14.
 */
@Module
public class PublishCoreModule {
    private PublishCoreView mView;

    public PublishCoreModule(PublishCoreView view) {
        this.mView = view;
    }

    @ActivityScope
    @Provides
    PublishCoreView providePublishCoreView() {
        return this.mView;
    }

    @ActivityScope
    @Provides
    PublishCoreModel providePublishCoreModel(ServiceManager manager) {
        return new PublishCoreModelImpl(manager);
    }

}
