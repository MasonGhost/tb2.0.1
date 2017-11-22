package com.zhiyicx.thinksnsplus.modules.circle.create;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

public class CreateCircleActivity extends TSActivity<CreateCirclePresenter, CreateCircleFragment> {

    @Override
    protected CreateCircleFragment getFragment() {
        return new CreateCircleFragment();
    }

    @Override
    protected void componentInject() {
        DaggerCreateCircleComponent
                .builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .createCirclePresenterModule(new CreateCirclePresenterModule(mContanierFragment))
                .build().inject(this);
    }
}
