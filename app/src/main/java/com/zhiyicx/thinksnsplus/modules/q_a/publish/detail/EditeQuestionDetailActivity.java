package com.zhiyicx.thinksnsplus.modules.q_a.publish.detail;

import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.modules.markdown_editor.BaseMarkdownActivity;

/**
 * @Author Jliuer
 * @Date 2018/01/19/11:30
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class EditeQuestionDetailActivity extends BaseMarkdownActivity<EditeQuestionDetailPresenter, EditeQuestionDetailFragment> {

    @Override
    protected EditeQuestionDetailFragment getYourFragment() {
        return EditeQuestionDetailFragment.newInstance(getIntent().getExtras());
    }

    @Override
    protected void componentInject() {
        DaggerEditeQuestionDetailComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .editeQuestionDetailPresenterModule(new EditeQuestionDetailPresenterModule(mContanierFragment))
                .build().inject(this);
    }
}
