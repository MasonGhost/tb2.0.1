package com.zhiyicx.thinksnsplus.modules.findsomeone.contianer;

import dagger.Module;
import dagger.Provides;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/8/11
 * @Contact master.jungle68@gmail.com
 */
@Module
public class FindSomeOneContainerPresenterModule {
    FindSomeOneContainerContract.View mView;

    public FindSomeOneContainerPresenterModule(FindSomeOneContainerContract.View view) {
        mView = view;
    }

    @Provides
    FindSomeOneContainerContract.View providesView() {
        return mView;
    }

}
