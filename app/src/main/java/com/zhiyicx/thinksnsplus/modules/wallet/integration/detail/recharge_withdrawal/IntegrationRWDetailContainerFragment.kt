package com.zhiyicx.thinksnsplus.modules.wallet.integration.detail.recharge_withdrawal

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View

import com.zhiyicx.baseproject.base.TSViewPagerFragment
import com.zhiyicx.thinksnsplus.R
import com.zhiyicx.thinksnsplus.modules.wallet.integration.detail.IntegrationDetailListFragment
import com.zhiyicx.thinksnsplus.modules.wallet.integration.detail.IntegrationDetailPresenter

import java.util.ArrayList
import java.util.Arrays

/**
 * @Describe
 * @Author Jungle68
 * @Date 2018/1/24
 * @Contact master.jungle68@gmail.com
 */

class IntegrationRWDetailContainerFragment : TSViewPagerFragment<IntegrationDetailPresenter>() {

    override fun setUseSatusbar() = true

    override fun setUseStatusView() = true

    override fun initTitles() = Arrays.asList(*resources.getStringArray(R.array.integration_recharge_withdrawal_type))

    override fun initFragments(): List<Fragment> {
        if (mFragmentList == null) {
            mFragmentList = ArrayList()
            mFragmentList.add(IntegrationDetailListFragment.newInstance(IntegrationDetailListFragment.CHOOSE_TYPE_RECHARGE))
            mFragmentList.add(IntegrationDetailListFragment.newInstance(IntegrationDetailListFragment.CHOOSE_TYPE_CASH))
        }
        return mFragmentList
    }

    override fun initViewPager(rootView: View) {
        super.initViewPager(rootView)
        mTsvToolbar.setDefaultTabLeftMargin(resources.getDimensionPixelOffset(R.dimen.spacing_big_large))
        mTsvToolbar.setDefaultTabRightMargin(resources.getDimensionPixelOffset(R.dimen.spacing_big_large))
        val defaultPosition = arguments.getInt(BUNDLE_DEFAULT_POSITION)
        if (defaultPosition == POSITION_WITHDRAWASL_RECORD) {
            mVpFragment.currentItem = defaultPosition
        }

    }

    override fun initData(){}

    companion object {
        const val BUNDLE_DEFAULT_POSITION = "default_postion"

        const val POSITION_RECHARGE_RECORD = 0
        const val POSITION_WITHDRAWASL_RECORD = 1

        fun newInstance(defaultPosition: Int): IntegrationRWDetailContainerFragment {

            val args = Bundle()
            val fragment = IntegrationRWDetailContainerFragment()
            args.putInt(BUNDLE_DEFAULT_POSITION, defaultPosition)
            fragment.arguments = args
            return fragment
        }
    }
}
