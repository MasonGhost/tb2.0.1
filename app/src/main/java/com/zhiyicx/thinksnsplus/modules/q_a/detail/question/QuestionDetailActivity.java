package com.zhiyicx.thinksnsplus.modules.q_a.detail.question;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

/**
 * @author Catherine
 * @describe 问题详情
 * @date 2017/8/15
 * @contact email:648129313@qq.com
 */

public class QuestionDetailActivity extends TSActivity<QuestionDetailPresenter, QuestionDetailFragment>{

    public static final String BUNDLE_QUESTION_BEAN = "bundle_question_bean";

    @Override
    protected QuestionDetailFragment getFragment() {
        return new QuestionDetailFragment().instance(getIntent().getBundleExtra(BUNDLE_QUESTION_BEAN));
    }

    @Override
    protected void componentInject() {
        DaggerQuestionDetailComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .questionDetailPresenterModule(new QuestionDetailPresenterModule(mContanierFragment))
                .build()
                .inject(this);
    }
}
