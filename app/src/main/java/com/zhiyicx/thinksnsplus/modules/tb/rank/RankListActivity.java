package com.zhiyicx.thinksnsplus.modules.tb.rank;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

/**
 * @Author Jliuer
 * @Date 2018/02/28/13:41
 * @Email Jliuer@aliyun.com
 * @Description 排行榜
 */
public class RankListActivity extends TSActivity<RankListPresenter, RankListFragment> {

    @Override
    protected RankListFragment getFragment() {
        return new RankListFragment();
    }

    @Override
    protected void componentInject() {
        DaggerRankListComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .rankListPresenterModule(new RankListPresenterModule(mContanierFragment))
                .build().inject(this);
    }
}
