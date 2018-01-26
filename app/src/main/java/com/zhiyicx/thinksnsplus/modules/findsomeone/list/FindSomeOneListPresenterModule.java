package com.zhiyicx.thinksnsplus.modules.findsomeone.list;

import dagger.Module;
import dagger.Provides;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/8/10
 * @Contact master.jungle68@gmail.com
 */
@Module
public class FindSomeOneListPresenterModule {
    private FindSomeOneListContract.View mView;

    public FindSomeOneListPresenterModule(FindSomeOneListContract.View view) {
        mView = view;
    }

    @Provides
    FindSomeOneListContract.View provideFindSomeOneListContractView() {
        return mView;
    }

}
