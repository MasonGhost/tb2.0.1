package com.zhiyicx.thinksnsplus.modules.tb.contract;


import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.modules.tb.contribution.ContributionListFragment;
import com.zhiyicx.thinksnsplus.modules.tb.contribution.ContributionListPresenter;

import javax.inject.Inject;

public class ContractListFragment extends TSListFragment<ContractListContract.Presenter, ContractData>
        implements ContractListContract.View{

    /**
     * bundle key
     */
    public static final String TYPE = "type";

    @Inject
    ContractListPresenter mContributionListPresenter;

    private String mType;

    public static ContributionListFragment newInstance(String type) {
        ContributionListFragment contributionListFragment = new ContributionListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TYPE, type);
        contributionListFragment.setArguments(bundle);
        return contributionListFragment;
    }

    @Override
    public String getType() {
        return null;
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return null;
    }
}
