package com.zhiyicx.thinksnsplus.modules.wallet.bill;

import com.zhiyicx.baseproject.base.TSActivity;

public class BillActivity extends TSActivity {

    @Override
    protected BillListFragment getFragment() {
        return BillListFragment.newInstance();
    }

    @Override
    protected void componentInject() {

    }
}
