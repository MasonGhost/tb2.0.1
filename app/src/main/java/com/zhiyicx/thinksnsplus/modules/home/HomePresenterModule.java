package com.zhiyicx.thinksnsplus.modules.home;

import dagger.Module;
import dagger.Provides;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017//13
 * @Contact master.jungle68@gmail.com
 */
@Module
public class HomePresenterModule {
    private final HomeContract.View mView;

    public HomePresenterModule(HomeContract.View view) {
        mView = view;
    }

    @Provides
    HomeContract.View provideMessageContractView() {
        return mView;
    }

}
