package com.zhiyicx.thinksnsplus.modules.wallet.integration.recharge;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.modules.wallet.integration.mine.MineIntegrationFragment;
import com.zhiyicx.thinksnsplus.modules.wallet.integration.mine.MineIntegrationPresenter;
import com.zhiyicx.thinksnsplus.modules.wallet.integration.mine.MineIntegrationPresenterModule;

/**
 * @Describe 我的积分
 * @Author Jungle68
 * @Date 2018/1/18
 * @Contact master.jungle68@gmail.com
 */
public class IntegrationRechargeActivity extends TSActivity<IntegrationRechargePresenter, IntegrationRechargeFragment> {


    @Override
    protected void componentInject() {
        DaggerIntegrationRechargeComponent
                .builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .integrationRechargePresenterModule(new IntegrationRechargePresenterModule(mContanierFragment))
                .build()
                .inject(this);

    }

    @Override
    protected IntegrationRechargeFragment getFragment() {
        return IntegrationRechargeFragment.newInstance();
    }

}
