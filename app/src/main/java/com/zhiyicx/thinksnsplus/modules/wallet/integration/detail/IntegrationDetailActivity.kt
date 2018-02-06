package com.zhiyicx.thinksnsplus.modules.wallet.integration.detail

import com.zhiyicx.baseproject.base.TSActivity
import com.zhiyicx.thinksnsplus.data.beans.integration.IntegrationConfigBean

/**
 * @Describe 积分详情
 * @Author Jungle68
 * @Date 2018/1/23
 * @Contact master.jungle68@gmail.com
 */
class IntegrationDetailActivity : TSActivity<IntegrationDetailPresenter, IntegrationDetailListFragment>() {

    override fun getFragment(): IntegrationDetailListFragment {
        return IntegrationDetailListFragment.newInstance(intent.extras!!.getSerializable(IntegrationDetailListFragment.BUNDLE_INTEGRATION_CONFIG) as IntegrationConfigBean)
    }

    override fun componentInject() {

    }
}
