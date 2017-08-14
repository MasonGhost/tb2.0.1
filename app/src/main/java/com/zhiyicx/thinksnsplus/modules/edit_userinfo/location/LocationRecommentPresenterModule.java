package com.zhiyicx.thinksnsplus.modules.edit_userinfo.location;

import dagger.Module;
import dagger.Provides;

/**
 * @author LiuChao
 * @describe
 * @date 2017/1/9
 * @contact email:450127106@qq.com
 */
@Module
public class LocationRecommentPresenterModule {
    private LocationRecommentContract.View mView;

    public LocationRecommentPresenterModule(LocationRecommentContract.View view) {
        mView = view;
    }

    @Provides
    LocationRecommentContract.View provideView() {
        return mView;
    }

    @Provides
    LocationRecommentContract.Repository provideRepository() {
        return new LocationRecommentContract.Repository() {
        };
    }
}
