package com.zhiyicx.thinksnsplus.modules.wallet.recharge;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

/**
 * @Describe 钱包
 * @Author Jungle68
 * @Date 2017/5/22
 * @Contact master.jungle68@gmail.com
 */
public class RechargeActivity extends TSActivity<RechargePresenter, RechargeFragment> {


    @Override
    protected void componentInject() {
        DaggerRechargeComponent
                .builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .rechargePresenterModule(new RechargePresenterModule(mContanierFragment))
                .build()
                .inject(this);

    }

    @Override
    protected RechargeFragment getFragment() {
        return RechargeFragment.newInstance(getIntent().getExtras());
    }

}
