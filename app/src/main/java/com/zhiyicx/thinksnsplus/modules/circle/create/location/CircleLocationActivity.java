package com.zhiyicx.thinksnsplus.modules.circle.create.location;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

public class CircleLocationActivity extends TSActivity<CircleLocationPresenter,
        CircleLocationFragment> {

    @Override
    protected CircleLocationFragment getFragment() {
        return new CircleLocationFragment();
    }

    @Override
    protected void componentInject() {
        DaggerCircleLocationComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .circleLocationPresenterModule(new CircleLocationPresenterModule
                        (mContanierFragment))
                .build().inject(this);
    }
}
