package com.zhiyicx.thinksnsplus.modules.wallet.reward;

import dagger.Module;
import dagger.Provides;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/05/22
 * @Contact master.jungle68@gmail.com
 */
@Module
public class RewardPresenterModule {
    private final RewardContract.View mView;

    public RewardPresenterModule(RewardContract.View view) {
        mView = view;
    }

    @Provides
    RewardContract.View provideRewardContractView() {
        return mView;
    }

}
