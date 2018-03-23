package com.zhiyicx.thinksnsplus.modules.tb.detail;

import dagger.Module;
import dagger.Provides;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2018/3/23
 * @Contact master.jungle68@gmail.com
 */
@Module
public class MerchianMessageListPresenterModule {
    MerchainMessageListContract.View mView;

    public MerchianMessageListPresenterModule(MerchainMessageListContract.View view) {
        mView = view;
    }

    @Provides
    MerchainMessageListContract.View provideContractView() {
        return mView;
    }
}
