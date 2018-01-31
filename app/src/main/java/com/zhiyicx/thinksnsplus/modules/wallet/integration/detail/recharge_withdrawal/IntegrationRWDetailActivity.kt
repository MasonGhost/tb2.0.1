package com.zhiyicx.thinksnsplus.modules.wallet.integration.detail.recharge_withdrawal

import com.zhiyicx.baseproject.base.TSActivity
import com.zhiyicx.thinksnsplus.modules.wallet.integration.detail.IntegrationDetailPresenter


/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/11/14
 * @Contact master.jungle68@gmail.com
 */
class IntegrationRWDetailActivity : TSActivity<IntegrationDetailPresenter, IntegrationRWDetailContainerFragment>() {

    override fun getFragment(): IntegrationRWDetailContainerFragment {
        return IntegrationRWDetailContainerFragment.newInstance(intent.getIntExtra(IntegrationRWDetailContainerFragment
                .BUNDLE_DEFAULT_POSITION, IntegrationRWDetailContainerFragment.POSITION_RECHARGE_RECORD))
    }

    override fun componentInject() {

    }
}
