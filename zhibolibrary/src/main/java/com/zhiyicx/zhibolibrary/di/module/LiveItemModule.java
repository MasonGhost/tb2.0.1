package com.zhiyicx.zhibolibrary.di.module;


import com.zhiyicx.zhibolibrary.di.ActivityScope;
import com.zhiyicx.zhibolibrary.model.LiveItemModel;
import com.zhiyicx.zhibolibrary.model.api.service.ServiceManager;
import com.zhiyicx.zhibolibrary.model.impl.LiveItemModelImpl;
import com.zhiyicx.zhibolibrary.ui.view.LiveItemView;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

/**
 * Created by zhiyicx on 2016/3/14.
 */
@Module
public class LiveItemModule {
    private LiveItemView mView;

    public LiveItemModule(LiveItemView view) {
        this.mView = view;
    }

    @ActivityScope
    @Provides
    LiveItemView provideLiveItemView() {
        return this.mView;
    }

    @ActivityScope
    @Provides
    LiveItemModel provideLiveItemModel(ServiceManager manager, OkHttpClient client) {
        return new LiveItemModelImpl(manager,client);
    }
}
