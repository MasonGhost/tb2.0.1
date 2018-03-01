package com.zhiyicx.thinksnsplus.modules.tb.contribution;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.zhiyicx.baseproject.base.TSListFragment;

/**
 * @Author Jliuer
 * @Date 2018/02/28/14:31
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class ContributionListFragment extends TSListFragment<ContributionListContract.Presenter, ContributionData> {

    public static final String TYPE_TOTAL = "totla";
    public static final String TYPE_TODAY = "today";
    public static final String TYPE = "type";

    public static ContributionListFragment newInstance(String type) {
        ContributionListFragment contributionListFragment = new ContributionListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TYPE, type);
        contributionListFragment.setArguments(bundle);
        return contributionListFragment;
    }

    @Override
    protected boolean showToolBarDivider() {
        return false;
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return null;
    }
}
