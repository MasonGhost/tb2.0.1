package com.zhiyicx.thinksnsplus.modules.wallet.recharge;

import android.app.Application;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.repository.RechargeRepository;

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

    @Provides
    RechargeContract.Repository provideRechargeContractRepository(RechargeRepository rechargeRepository) {
        return rechargeRepository;
    }
}
