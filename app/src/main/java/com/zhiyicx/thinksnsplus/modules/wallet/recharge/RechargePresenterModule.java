package com.zhiyicx.thinksnsplus.modules.wallet.recharge;

import dagger.Module;
import dagger.Provides;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/05/22
 * @Contact master.jungle68@gmail.com
 */
@Module
public class RechargePresenterModule {
    private final RechargeContract.View mView;

    public RechargePresenterModule(RechargeContract.View view) {
        mView = view;
    }

    @Provides
    RechargeContract.View provideRechargeContractView() {
        return mView;
    }

}
