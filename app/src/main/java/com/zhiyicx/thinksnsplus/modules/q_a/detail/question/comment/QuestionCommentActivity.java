package com.zhiyicx.thinksnsplus.modules.q_a.detail.question.comment;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

/**
 * @author Catherine
 * @describe 问题的评论页面
 * @date 2017/8/18
 * @contact email:648129313@qq.com
 */

public class QuestionCommentActivity extends TSActivity<QuestionCommentPresenter, QuestionCommentFragment>{

    public static final String BUNDLE_QUESTION_BEAN = "bundle_question_bean";

    @Override
    protected QuestionCommentFragment getFragment() {
        return new QuestionCommentFragment().instance(getIntent().getBundleExtra(BUNDLE_QUESTION_BEAN));
    }

    @Override
    protected void componentInject() {
        DaggerQuestionCommentComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .questionCommentPresenterModule(new QuestionCommentPresenterModule(mContanierFragment))
                .build()
                .inject(this);
    }
}
