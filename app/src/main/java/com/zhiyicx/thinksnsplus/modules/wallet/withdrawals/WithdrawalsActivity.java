package com.zhiyicx.thinksnsplus.modules.wallet.withdrawals;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

public class WithdrawalsActivity extends TSActivity<WithDrawalsPresenter, WithdrawalsFragment> {

    @Override
    protected WithdrawalsFragment getFragment() {
        return WithdrawalsFragment.newInstance(getIntent().getExtras());
    }

    @Override
    protected void componentInject() {
        DaggerWithDrawalsComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .withDrawalsPresenterModule(new WithDrawalsPresenterModule(mContanierFragment))
                .build().inject(this);
    }
}
