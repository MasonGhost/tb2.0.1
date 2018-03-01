package com.zhiyicx.thinksnsplus.modules.mechanism.search;

import dagger.Module;
import dagger.Provides;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/8/11
 * @Contact master.jungle68@gmail.com
 */
@Module
public class SearchMechanismUserPresenterModule {
    SearchMechanismUserContract.View mView;

    public SearchMechanismUserPresenterModule(SearchMechanismUserContract.View view) {
        mView = view;
    }

    @Provides
    SearchMechanismUserContract.View providesView() {
        return mView;
    }

}
