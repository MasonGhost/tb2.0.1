package com.zhiyicx.thinksnsplus.modules.q_a.answer.news;

import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.modules.markdown_editor.DaggerMarkdownComponent;
import com.zhiyicx.thinksnsplus.modules.markdown_editor.MarkdownPresenterModule;
import com.zhiyicx.thinksnsplus.modules.q_a.publish.news.PublishQuestionActivityV2;
import com.zhiyicx.thinksnsplus.modules.q_a.publish.news.PublishQuestionFragmentV2;

/**
 * @Author Jliuer
 * @Date 2018/01/22/11:36
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class PublishAnswerActivityV2 extends PublishQuestionActivityV2 {

    @Override
    protected PublishQuestionFragmentV2 getYourFragment() {
        return PublishAnswerFragmentV2.newInstance(getIntent().getExtras());
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
