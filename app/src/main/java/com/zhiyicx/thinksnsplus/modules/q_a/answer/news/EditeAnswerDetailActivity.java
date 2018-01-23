package com.zhiyicx.thinksnsplus.modules.q_a.answer.news;

import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.modules.markdown_editor.DaggerMarkdownComponent;
import com.zhiyicx.thinksnsplus.modules.markdown_editor.MarkdownPresenterModule;
import com.zhiyicx.thinksnsplus.modules.q_a.publish.detail.EditeQuestionDetailActivity;
import com.zhiyicx.thinksnsplus.modules.q_a.publish.detail.EditeQuestionDetailFragment;

/**
 * @Author Jliuer
 * @Date 2018/01/22/11:36
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class EditeAnswerDetailActivity extends EditeQuestionDetailActivity {

    @Override
    protected EditeQuestionDetailFragment getYourFragment() {
        return EditeAnswerDetailFragment.newInstance(getIntent().getExtras());
    }

    @Override
    protected void componentInject() {
        DaggerMarkdownComponent
                .builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .markdownPresenterModule(new MarkdownPresenterModule(mContanierFragment))
                .build().inject(this);
    }


}
