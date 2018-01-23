package com.zhiyicx.thinksnsplus.modules.wallet.integration.detail;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.modules.wallet.bill.BillListFragment;
import com.zhiyicx.thinksnsplus.modules.wallet.bill.BillPresenter;
import com.zhiyicx.thinksnsplus.modules.wallet.bill.BillPresenterModule;
import com.zhiyicx.thinksnsplus.modules.wallet.bill.DaggerBillComponent;

public class IntegrationDetailActivity extends TSActivity<BillPresenter, BillListFragment> {

    @Override
    protected BillListFragment getFragment() {
        return BillListFragment.newInstance();
    }

    @Override
    protected void componentInject() {
//        DaggerBillComponent.builder()
//                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
//                .billPresenterModule(new BillPresenterModule(mContanierFragment))
//                .build().inject(this);
    }
}
