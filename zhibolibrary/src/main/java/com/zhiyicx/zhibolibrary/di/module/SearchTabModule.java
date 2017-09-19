package com.zhiyicx.zhibolibrary.di.module;


import com.zhiyicx.zhibolibrary.di.ActivityScope;
import com.zhiyicx.zhibolibrary.model.SearchTabModel;
import com.zhiyicx.zhibolibrary.model.api.service.ServiceManager;
import com.zhiyicx.zhibolibrary.model.impl.SearchTabModelImpl;
import com.zhiyicx.zhibolibrary.ui.view.SearchTabView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by zhiyicx on 2016/3/14.
 */
@Module
public class SearchTabModule {
    private SearchTabView mView;

    public SearchTabModule(SearchTabView view) {
        this.mView = view;
    }

    @ActivityScope
    @Provides
    SearchTabView provideSearchTabView() {
        return this.mView;
    }

    @ActivityScope
    @Provides
    SearchTabModel provideSearchTabModel(ServiceManager manager) {
        return new SearchTabModelImpl(manager);
    }
}
