package com.zhiyicx.thinksnsplus.modules.wallet.withdrawals.list_detail;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

public class WithdrawalsDetailActivity extends TSActivity<WithdrawalsDetailPresenter, WithdrawalsDetailFragment> {

    @Override
    protected WithdrawalsDetailFragment getFragment() {
        return WithdrawalsDetailFragment.newInstance();
    }

    @Override
    protected void componentInject() {
        DaggerWithdrawalsDetailComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .withdrawalsDetailPresenterModule(new WithdrawalsDetailPresenterModule(mContanierFragment))
                .build().inject(this);
    }
}
