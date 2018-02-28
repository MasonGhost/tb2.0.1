package com.zhiyicx.thinksnsplus.modules.rank.tb.contribution;

import android.support.v4.app.Fragment;

import com.zhiyicx.baseproject.base.TSViewPagerFragment;
import com.zhiyicx.thinksnsplus.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.zhiyicx.thinksnsplus.modules.rank.tb.contribution.ContributionListFragment.TYPE_TODAY;
import static com.zhiyicx.thinksnsplus.modules.rank.tb.contribution.ContributionListFragment.TYPE_TOTAL;

/**
 * @Author Jliuer
 * @Date 2018/02/28/14:27
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class ContributionContainerFragment extends TSViewPagerFragment {

    @Override
    protected List<String> initTitles() {
        return Arrays.asList(mActivity.getResources().getStringArray(R.array
                .integration_contribution_type));
    }

    @Override
    protected List<Fragment> initFragments() {
        if (mFragmentList == null) {
            mFragmentList = new ArrayList<>();
            mFragmentList.add(ContributionListFragment.newInstance(TYPE_TOTAL));
            mFragmentList.add(ContributionListFragment.newInstance(TYPE_TODAY));
        }
        return mFragmentList;
    }

    @Override
    protected void initData() {

    }
}
