package com.zhiyicx.thinksnsplus.modules.wallet.recharge;

import android.content.Intent;

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mContanierFragment.onActivityResult(requestCode,resultCode,data);
    }
}
