package com.zhiyicx.thinksnsplus.modules.wallet.integration.recharge;


import dagger.Module;
import dagger.Provides;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/05/22
 * @Contact master.jungle68@gmail.com
 */
@Module
public class IntegrationRechargePresenterModule {
    private final IntegrationRechargeContract.View mView;

    public IntegrationRechargePresenterModule(IntegrationRechargeContract.View view) {
        mView = view;
    }

    @Provides
    IntegrationRechargeContract.View provideContractView() {
        return mView;
    }

}
