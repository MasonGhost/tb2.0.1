package com.zhiyicx.thinksnsplus.modules.wallet.integration.withdrawal;


import dagger.Module;
import dagger.Provides;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/05/22
 * @Contact master.jungle68@gmail.com
 */
@Module
public class IntegrationWithdrawalsPresenterModule {
    private final IntegrationWithdrawalsContract.View mView;

    public IntegrationWithdrawalsPresenterModule(IntegrationWithdrawalsContract.View view) {
        mView = view;
    }

    @Provides
    IntegrationWithdrawalsContract.View provideContractView() {
        return mView;
    }

}
