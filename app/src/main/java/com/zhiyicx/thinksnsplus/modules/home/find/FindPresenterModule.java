package com.zhiyicx.thinksnsplus.modules.home.find;


import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2018/4/18.
 */
@Module
public class FindPresenterModule {
    private final FindContract.View mView;

    public FindPresenterModule(FindContract.View view) {
        mView = view;
    }

    @Provides
    FindContract.View provideFindContractView() {
        return mView;
    }
}
