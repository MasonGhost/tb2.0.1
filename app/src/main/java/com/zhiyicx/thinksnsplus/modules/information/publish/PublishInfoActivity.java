package com.zhiyicx.thinksnsplus.modules.information.publish;


import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

public class PublishInfoActivity extends TSActivity<PublishInfoPresenter, PublishInfoFragment> {

    @Override
    protected PublishInfoFragment getFragment() {
        return PublishInfoFragment.getInstance(getIntent().getExtras());
    }

    @Override
    protected void componentInject() {
        DaggerPublishInfoComponent
                .builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .publishInfoPresenterModule(new PublishInfoPresenterModule(mContanierFragment))
                .build().inject(this);
    }
}
