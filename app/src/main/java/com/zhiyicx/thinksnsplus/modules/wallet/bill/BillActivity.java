package com.zhiyicx.thinksnsplus.modules.wallet.bill;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;


/**
 * @Describe 钱包明细页
 * @Author Jungle68
 * @Date 2017/3/30
 * @Contact master.jungle68@gmail.com
 */
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
