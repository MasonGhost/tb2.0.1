package com.zhiyicx.thinksnsplus.modules.q_a.publish.detail;

import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.modules.markdown_editor.BaseMarkdownActivity;
import com.zhiyicx.thinksnsplus.modules.markdown_editor.DaggerMarkdownComponent;
import com.zhiyicx.thinksnsplus.modules.markdown_editor.MarkdownPresenterModule;

/**
 * @Author Jliuer
 * @Date 2018/01/19/11:30
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class EditeQuestionDetailActivity extends BaseMarkdownActivity<EditeQuestionDetailFragment> {

    @Override
    protected EditeQuestionDetailFragment getYourFragment() {
        return EditeQuestionDetailFragment.newInstance(getIntent().getExtras());
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
