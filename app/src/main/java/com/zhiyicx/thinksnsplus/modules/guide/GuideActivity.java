package com.zhiyicx.thinksnsplus.modules.guide;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.modules.settings.SettingsPresenterModule;

public class GuideActivity extends TSActivity<GuidePresenter,GuideFragment> {

    @Override
    protected GuideFragment getFragment() {
        return new GuideFragment();
    }

    @Override
    protected void componentInject() {
        DaggerGuideComponent
                .builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .guidePresenterModule(new GuidePresenterModule(mContanierFragment))
                .build()
                .inject(this);
    }
}
