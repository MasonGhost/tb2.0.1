package com.zhiyicx.zhibolibrary.di.module;


import com.zhiyicx.zhibolibrary.di.ActivityScope;
import com.zhiyicx.zhibolibrary.model.EndStreamModel;
import com.zhiyicx.zhibolibrary.model.api.service.ServiceManager;
import com.zhiyicx.zhibolibrary.model.impl.EndStreamModelImpl;
import com.zhiyicx.zhibolibrary.ui.view.EndStreamView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by zhiyicx on 2016/3/14.
 */
@Module
public class EndStreamModule {
    private EndStreamView mView;

    public EndStreamModule(EndStreamView view) {
        this.mView = view;
    }

    @ActivityScope
    @Provides
    EndStreamView provideEndStreamView() {
        return this.mView;
    }

    @ActivityScope
    @Provides
    EndStreamModel provideEndStreamModel(ServiceManager manager) {
        return new EndStreamModelImpl(manager);
    }


}
