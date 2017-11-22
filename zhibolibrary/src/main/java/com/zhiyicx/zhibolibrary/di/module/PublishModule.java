package com.zhiyicx.zhibolibrary.di.module;


import com.zhiyicx.zhibolibrary.di.ActivityScope;
import com.zhiyicx.zhibolibrary.model.PublishModel;
import com.zhiyicx.zhibolibrary.model.api.service.ServiceManager;
import com.zhiyicx.zhibolibrary.model.impl.PublishModelImpl;
import com.zhiyicx.zhibolibrary.ui.common.ZBLBaseFragment;
import com.zhiyicx.zhibolibrary.ui.fragment.ZBLCameraFragment;
import com.zhiyicx.zhibolibrary.ui.fragment.ZBLPublishCoreFragment;
import com.zhiyicx.zhibolibrary.ui.fragment.ZBLScrollClearnFragment;
import com.zhiyicx.zhibolibrary.ui.view.CameraView;
import com.zhiyicx.zhibolibrary.ui.view.PublishCoreView;
import com.zhiyicx.zhibolibrary.ui.view.PublishView;
import com.zhiyicx.zhibolibrary.util.UiUtils;

import dagger.Module;
import dagger.Provides;

/**
 * Created by zhiyicx on 2016/3/14.
 */
@Module
public class PublishModule {
    private PublishView mPublishView;

    public PublishModule(PublishView publishView) {
        this.mPublishView = publishView;
    }

    @ActivityScope
    @Provides
    PublishView providePublishView() {
        return this.mPublishView;
    }

    @ActivityScope
    @Provides
    PublishModel providePublishModel(ServiceManager manager) {
        return new PublishModelImpl(manager);
    }

    @ActivityScope
    @Provides
    PublishCoreView providePublishCoreView() {
        return ZBLPublishCoreFragment.newInstance(ZBLPublishCoreFragment.PUBLISH_VIEW);
    }
    @ActivityScope
    @Provides
    ZBLBaseFragment provideBaseFragment(){
        return ZBLScrollClearnFragment.newInstance(ZBLPublishCoreFragment.LIVE_VIEW);
    }

    @ActivityScope
    @Provides
    CameraView provideCameraView(){
        return ZBLCameraFragment.newInstance();
    }
}
