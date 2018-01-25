package com.zhiyicx.thinksnsplus.modules.wallet.integration.detail;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.integration.IntegrationConfigBean;

/**
 * @Describe 积分详情
 * @Author Jungle68
 * @Date 2018/1/23
 * @Contact master.jungle68@gmail.com
 */
public class IntegrationDetailActivity extends TSActivity<IntegrationDetailPresenter, IntegrationDetailListFragment> {

    @Override
    protected IntegrationDetailListFragment getFragment() {
        return IntegrationDetailListFragment.newInstance(null, (IntegrationConfigBean) getIntent().getExtras().getSerializable
                (IntegrationDetailListFragment.BUNDLE_INTEGRATION_CONFIG));
    }

    @Override
    protected void componentInject() {

    }
}
