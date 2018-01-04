package com.zhiyicx.thinksnsplus.modules.wallet.withdrawals.list_detail;

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

    WithdrawalsDetailPresenterModule(WithdrawalsDetailConstract.View view) {
        mView = view;
    }

    @Provides
    WithdrawalsDetailConstract.View provideWithdrawalsDetailConstractView() {
        return mView;
    }

}
