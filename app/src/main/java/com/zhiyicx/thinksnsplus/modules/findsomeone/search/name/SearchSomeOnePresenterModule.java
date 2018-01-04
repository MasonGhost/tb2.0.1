package com.zhiyicx.thinksnsplus.modules.findsomeone.search.name;

import dagger.Module;
import dagger.Provides;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/8/11
 * @Contact master.jungle68@gmail.com
 */
@Module
public class SearchSomeOnePresenterModule {
    SearchSomeOneContract.View mView;

    public SearchSomeOnePresenterModule(SearchSomeOneContract.View view) {
        mView = view;
    }

    @Provides
    SearchSomeOneContract.View providesView() {
        return mView;
    }

}
