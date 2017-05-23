package com.zhiyicx.thinksnsplus.modules.wallet.account;

import com.zhiyicx.baseproject.base.TSActivity;

public class AccountActivity extends TSActivity {

    @Override
    protected AccountFragment getFragment() {
        return new AccountFragment();
    }

    @Override
    protected void componentInject() {

    }
}
