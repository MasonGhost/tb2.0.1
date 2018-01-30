package com.zhiyicx.thinksnsplus.modules.wallet.integration.detail.recharge_withdrawal;

import android.support.v4.app.Fragment;
import android.view.View;

import com.zhiyicx.baseproject.base.TSViewPagerFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.modules.wallet.integration.detail.IntegrationDetailListFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.zhiyicx.thinksnsplus.modules.wallet.integration.detail.IntegrationDetailListFragment.CHOOSE_TYPE_CASH;
import static com.zhiyicx.thinksnsplus.modules.wallet.integration.detail.IntegrationDetailListFragment.CHOOSE_TYPE_RECHARGE;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2018/1/24
 * @Contact master.jungle68@gmail.com
 */

public class IntegrationRWDetailContainerFragment extends TSViewPagerFragment {

    @Override
    protected boolean setUseSatusbar() {
        return true;
    }

    @Override
    protected boolean setUseStatusView() {
        return true;
    }


    @Override
    protected List<String> initTitles() {
        return Arrays.asList(getResources().getStringArray(R.array.integration_recharge_withdrawal_type));
    }

    @Override
    protected List<Fragment> initFragments() {
        if (mFragmentList == null) {
            mFragmentList = new ArrayList();
            mFragmentList.add(IntegrationDetailListFragment.newInstance(CHOOSE_TYPE_RECHARGE));
            mFragmentList.add(IntegrationDetailListFragment.newInstance(CHOOSE_TYPE_CASH));
        }
        return mFragmentList;
    }


    @Override
    protected void initViewPager(View rootView) {
        super.initViewPager(rootView);
        mTsvToolbar.setDefaultTabLeftMargin(getResources().getDimensionPixelOffset(R.dimen.spacing_big_large));
        mTsvToolbar.setDefaultTabRightMargin(getResources().getDimensionPixelOffset(R.dimen.spacing_big_large));
    }
    @Override
    protected void initData() {

    }
}
