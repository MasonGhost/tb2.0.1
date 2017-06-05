package com.zhiyicx.thinksnsplus.modules.wallet.bill;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

public class BillActivity extends TSActivity<BillPresenter, BillListFragment> {

    @Override
    protected BillListFragment getFragment() {
        return BillListFragment.newInstance();
    }

    @Override
    protected void componentInject() {
        DaggerBillComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .billPresenterModule(new BillPresenterModule(mContanierFragment))
                .build().inject(this);
    }
}
