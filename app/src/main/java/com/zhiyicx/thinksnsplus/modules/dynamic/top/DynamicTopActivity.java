package com.zhiyicx.thinksnsplus.modules.dynamic.top;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

public class DynamicTopActivity extends TSActivity<DynamicTopPresenter, DynamicTopFragment> {

    @Override
    protected DynamicTopFragment getFragment() {
        return DynamicTopFragment.newInstance();
    }

    @Override
    protected void componentInject() {
        DaggerDynamicTopComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .dynamicTopPresenterModule(new DynamicTopPresenterModule(mContanierFragment))
                .build().inject(this);
    }
}
