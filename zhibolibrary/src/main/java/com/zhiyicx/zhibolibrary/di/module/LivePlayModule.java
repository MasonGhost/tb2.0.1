package com.zhiyicx.zhibolibrary.di.module;

import com.zhiyicx.zhibolibrary.di.ActivityScope;
import com.zhiyicx.zhibolibrary.model.LivePlayModel;
import com.zhiyicx.zhibolibrary.model.api.service.ServiceManager;
import com.zhiyicx.zhibolibrary.model.impl.LivePlayModelImpl;
import com.zhiyicx.zhibolibrary.ui.common.ZBLBaseFragment;
import com.zhiyicx.zhibolibrary.ui.fragment.PublishCoreFragment;
import com.zhiyicx.zhibolibrary.ui.fragment.ScrollClearnFragment;
import com.zhiyicx.zhibolibrary.ui.view.LivePlayView;
import com.zhiyicx.zhibolibrary.ui.view.PublishCoreView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by zhiyicx on 2016/3/14.
 */
@Module
public class LivePlayModule {
    private LivePlayView mView;

    public LivePlayModule(LivePlayView view) {
        this.mView = view;
    }

    @ActivityScope
    @Provides
    LivePlayView provideLivePlayView() {
        return this.mView;
    }

    @ActivityScope
    @Provides
    LivePlayModel provideLivePlayModel(ServiceManager manager) {
        return new LivePlayModelImpl(manager);
    }

    @ActivityScope
    @Provides
    PublishCoreView providePublishCoreView() {
        return PublishCoreFragment.newInstance(PublishCoreFragment.LIVE_VIEW);
    }
    @ActivityScope
    @Provides
    ZBLBaseFragment provideBaseFragment(){
        return ScrollClearnFragment.newInstance(PublishCoreFragment.LIVE_VIEW);
    }

}
