package com.zhiyicx.thinksnsplus.modules.rank.tb.rank;

import com.zhiyicx.baseproject.base.TSActivity;

/**
 * @Author Jliuer
 * @Date 2018/02/28/13:41
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class RankListActivity extends TSActivity<RankListPresenter, RankListFragment> {

    @Override
    protected RankListFragment getFragment() {
        return new RankListFragment();
    }

    @Override
    protected void componentInject() {

    }
}
