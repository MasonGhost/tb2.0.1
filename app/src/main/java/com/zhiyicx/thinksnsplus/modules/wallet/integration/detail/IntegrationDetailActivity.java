package com.zhiyicx.thinksnsplus.modules.wallet.integration.detail;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

/**
 * @Describe 积分详情
 * @Author Jungle68
 * @Date 2018/1/23
 * @Contact master.jungle68@gmail.com
 */
public class IntegrationDetailActivity extends TSActivity<IntegrationDetailPresenter, IntegrationDetailListFragment> {

    @Override
    protected IntegrationDetailListFragment getFragment() {
        return IntegrationDetailListFragment.newInstance(null);
    }

    @Override
    protected void componentInject() {
        DaggerIntegrationDetailComponent
                .builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .integrationDetailPresenterModule(new IntegrationDetailPresenterModule(mContanierFragment))
                .build().inject(this);
    }
}
