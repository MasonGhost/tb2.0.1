package com.zhiyicx.thinksnsplus.modules.dynamic.list;

import dagger.Module;
import dagger.Provides;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017//13
 * @Contact master.jungle68@gmail.com
 */
@Module
public class DynamicPresenterModule {
    private final DynamicContract.View mView;

    public DynamicPresenterModule(DynamicContract.View view) {
        mView = view;
    }

    @Provides
    DynamicContract.View provideDynamicContractView() {
        return mView;
    }
}
