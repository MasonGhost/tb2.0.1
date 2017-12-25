package com.zhiyicx.thinksnsplus.modules.circle.create.rule;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.modules.circle.create.CreateCirclePresenter;
import com.zhiyicx.thinksnsplus.modules.circle.create.CreateCirclePresenterModule;
import com.zhiyicx.thinksnsplus.modules.circle.create.DaggerCreateCircleComponent;

public class RuleForCreateCircleActivity extends TSActivity<CreateCirclePresenter, RuleForCreateCircleFragment> {

    @Override
    protected RuleForCreateCircleFragment getFragment() {
        return new RuleForCreateCircleFragment();
    }

    @Override
    protected void componentInject() {
        DaggerCreateCircleComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .createCirclePresenterModule(new CreateCirclePresenterModule(mContanierFragment))
                .build().inject(this);
    }
}
