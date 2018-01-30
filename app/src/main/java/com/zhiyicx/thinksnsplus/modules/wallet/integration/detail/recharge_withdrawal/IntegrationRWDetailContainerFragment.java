package com.zhiyicx.thinksnsplus.modules.wallet.integration.detail.recharge_withdrawal;

import android.os.Bundle;
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
    public static final String BUNDLE_DEFAULT_POSITION = "default_postion";

    public static final int POSITION_RECHARGE_RECORD = 0;
    public static final int POSITION_WITHDRAWASL_RECORD = 1;

    private int mDefaultPosition;

    public static IntegrationRWDetailContainerFragment newInstance(int default_position) {

        Bundle args = new Bundle();
        IntegrationRWDetailContainerFragment fragment = new IntegrationRWDetailContainerFragment();
        args.putInt(BUNDLE_DEFAULT_POSITION, default_position);
        fragment.setArguments(args);
        return fragment;
    }

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
        mDefaultPosition = getArguments().getInt(BUNDLE_DEFAULT_POSITION);
        if (mDefaultPosition == POSITION_WITHDRAWASL_RECORD) {
            mVpFragment.setCurrentItem(mDefaultPosition);
        }

    }

    @Override
    protected void initData() {

    }
}
