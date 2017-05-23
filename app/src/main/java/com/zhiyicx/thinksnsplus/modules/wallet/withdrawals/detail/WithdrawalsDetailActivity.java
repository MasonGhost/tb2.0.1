package com.zhiyicx.thinksnsplus.modules.wallet.withdrawals.detail;

import com.zhiyicx.baseproject.base.TSActivity;

public class WithdrawalsDetailActivity extends TSActivity {

    @Override
    protected WithdrawalsDetailFragment getFragment() {
        return new WithdrawalsDetailFragment();
    }

    @Override
    protected void componentInject() {

    }
}
