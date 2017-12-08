package com.zhiyicx.thinksnsplus.modules.markdown_editor;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

public class MarkdownActivity extends TSActivity<MarkdownPresenter, MarkdownFragment> {

    @Override
    protected MarkdownFragment getFragment() {
        return MarkdownFragment.newInstance(getIntent().getExtras());
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
