package com.zhiyicx.thinksnsplus.modules.q_a.detail.answer.dig_list;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

public class AnswerDigListActivity extends TSActivity<AnswerDigListPresenter, AnswerDigListFragment> {

    @Override
    protected void componentInject() {
        DaggerAnswerDigListPresenterComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .answerDigListPresenterModule(new AnswerDigListPresenterModule(mContanierFragment))
                .build().inject(this);
    }

    @Override
    protected AnswerDigListFragment getFragment() {
        return AnswerDigListFragment.initFragment(getIntent().getExtras());
    }
}
