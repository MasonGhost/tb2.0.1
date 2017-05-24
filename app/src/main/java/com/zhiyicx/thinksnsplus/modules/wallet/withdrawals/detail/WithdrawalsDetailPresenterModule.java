package com.zhiyicx.thinksnsplus.modules.wallet.withdrawals.detail;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.repository.WithdrawalsDetailRepository;

import dagger.Module;
import dagger.Provides;

/**
 * @Author Jliuer
 * @Date 2017/05/24/9:38
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Module
public class WithdrawalsDetailPresenterModule {
    WithdrawalsDetailConstract.View mView;


    public WithdrawalsDetailPresenterModule(WithdrawalsDetailConstract.View view) {
        mView = view;
    }

    @Provides
    WithdrawalsDetailConstract.View provideWithdrawalsDetailConstractView() {
        return mView;
    }

    @Provides
    WithdrawalsDetailConstract.Repository provideWithdrawalsDetailRepository(ServiceManager serviceManager) {
        return new WithdrawalsDetailRepository(serviceManager);
    }
}
