package com.zhiyicx.thinksnsplus.modules.rank.type_list;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

/**
 * @author Catherine
 * @describe 分类详情下的排行列表
 * @date 2017/8/23
 * @contact email:648129313@qq.com
 */

public class RankTypeListActivity extends TSActivity<RankTypeListPresenter, RankTypeListFragment>{

    public static final String BUNDLE_RANK_BEAN = "bundle_rank_bean";

    @Override
    protected RankTypeListFragment getFragment() {
        return new RankTypeListFragment().instance(getIntent().getExtras());
    }

    @Override
    protected void componentInject() {
        DaggerRankTypeListComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .rankTypeListPresenterModule(new RankTypeListPresenterModule(mContanierFragment))
                .build()
                .inject(this);
    }
}
