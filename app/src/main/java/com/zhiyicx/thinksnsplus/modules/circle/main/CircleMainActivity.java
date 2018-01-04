package com.zhiyicx.thinksnsplus.modules.circle.main;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

public class CircleMainActivity extends TSActivity<CircleMainPresenter, CircleMainFragment> {

    @Override
    protected CircleMainFragment getFragment() {
        return CircleMainFragment.newInstance();
    }

    @Override
    protected void componentInject() {
        DaggerCircleMainPresenterComponent
                .builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .circleMainPresenterModule(new CircleMainPresenterModule(mContanierFragment))
                .build().inject(this);
    }

    @Override
    public void onBackPressed() {
        mContanierFragment.onBackPressed();
    }
}
