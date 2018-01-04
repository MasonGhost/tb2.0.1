package com.zhiyicx.thinksnsplus.modules.circle.publish;

import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.modules.markdown_editor.BaseMarkdownActivity;
import com.zhiyicx.thinksnsplus.modules.markdown_editor.DaggerMarkdownComponent;
import com.zhiyicx.thinksnsplus.modules.markdown_editor.MarkdownPresenterModule;

public class PublishPostActivity extends BaseMarkdownActivity<PublishPostFragment> {

    @Override
    protected PublishPostFragment getYourFragment() {
        return PublishPostFragment.newInstance(getIntent().getExtras());
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
