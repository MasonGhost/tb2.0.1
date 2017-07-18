package com.zhiyicx.thinksnsplus.modules.gallery;

import dagger.Module;
import dagger.Provides;

/**
 * @Author Jliuer
 * @Date 2017/06/29/9:54
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Module
public class GalleryPresenterModule {

    private GalleryConstract.View mView;

    public GalleryPresenterModule(GalleryConstract.View view) {
        mView = view;
    }

    @Provides
    GalleryConstract.View provideGalleryConstractView() {
        return mView;
    }
}
