package com.zhiyicx.thinksnsplus.modules.circle.publish;

import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.modules.information.publish.DaggerPublishInfoComponent;
import com.zhiyicx.thinksnsplus.modules.markdown_editor.BaseMarkdownActivity;

public class PublishPostActivity extends BaseMarkdownActivity<PublishPostPresenter,PublishPostFragment> {

    @Override
    protected PublishPostFragment getYourFragment() {
        return PublishPostFragment.newInstance(getIntent().getExtras());
    }

    @Override
    protected void componentInject() {
        DaggerPublishPostComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .publishPostPresenterModule(new PublishPostPresenterModule(mContanierFragment))
                .build().inject(this);
    }
}
