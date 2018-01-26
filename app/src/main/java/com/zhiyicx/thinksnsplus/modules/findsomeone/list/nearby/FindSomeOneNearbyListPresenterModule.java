package com.zhiyicx.thinksnsplus.modules.findsomeone.list.nearby;

import dagger.Module;
import dagger.Provides;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/8/10
 * @Contact master.jungle68@gmail.com
 */
@Module
public class FindSomeOneNearbyListPresenterModule {
    private FindSomeOneNearbyListContract.View mView;

    public FindSomeOneNearbyListPresenterModule(FindSomeOneNearbyListContract.View view) {
        mView = view;
    }

    @Provides
    FindSomeOneNearbyListContract.View provideView() {
        return mView;
    }
}
