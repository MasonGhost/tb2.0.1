package com.zhiyicx.thinksnsplus.modules.wallet.reward;

import com.zhiyicx.thinksnsplus.data.source.repository.RechargeRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.RewardRepository;
import com.zhiyicx.thinksnsplus.modules.wallet.recharge.RechargeContract;

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

    @Provides
    RewardContract.Repository provideRewardContractRepository(RewardRepository repository) {
        return repository;
    }
}
