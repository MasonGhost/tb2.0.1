package com.zhiyicx.thinksnsplus.modules.wallet.reward.list;

import dagger.Module;
import dagger.Provides;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/4/10
 * @Contact master.jungle68@gmail.com
 */
@Module
public class RewardListPresenterModule {

    private final RewardListContract.View mView;

    public RewardListPresenterModule(RewardListContract.View view) {
        mView = view;
    }

    @Provides
    RewardListContract.View provideRRewardListContractView() {
        return mView;
    }

}
