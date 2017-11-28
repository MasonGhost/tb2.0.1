package com.zhiyicx.thinksnsplus.modules.circle.create;

import android.content.Intent;

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mContanierFragment.onActivityResult(requestCode,resultCode,data);
    }
}
