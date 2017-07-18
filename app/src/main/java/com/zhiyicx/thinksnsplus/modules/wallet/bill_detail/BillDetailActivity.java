package com.zhiyicx.thinksnsplus.modules.wallet.bill_detail;

import com.zhiyicx.baseproject.base.TSActivity;

import static com.zhiyicx.thinksnsplus.modules.wallet.bill.BillListFragment.BILL_INFO;

public class BillDetailActivity extends TSActivity {

    @Override
    protected BillDetailFragment getFragment() {
        return BillDetailFragment.getInstance(getIntent().getBundleExtra(BILL_INFO));
    }

    @Override
    protected void componentInject() {

    }
}
