package com.zhiyicx.thinksnsplus.modules.rank;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

public class RankActivity extends TSActivity<RankPresenter, RankFragment> {

    @Override
    protected RankFragment getFragment() {
        return RankFragment.newInstance(getIntent().getExtras());
    }

    @Override
    protected void componentInject() {
        DaggerRankComponent
                .builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .rankPresenterModule(new RankPresenterModule(mContanierFragment))
                .build()
                .inject(this);
    }
}
