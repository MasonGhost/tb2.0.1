package com.zhiyicx.thinksnsplus.modules.q_a.publish.news;

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
public class PublishQuestionActivityV2 extends BaseMarkdownActivity<PublishQuestionFragmentV2> {

    @Override
    protected PublishQuestionFragmentV2 getYourFragment() {
        return PublishQuestionFragmentV2.newInstance(getIntent().getExtras());
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
