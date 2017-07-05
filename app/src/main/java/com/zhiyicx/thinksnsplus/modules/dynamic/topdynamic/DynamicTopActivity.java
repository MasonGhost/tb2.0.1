package com.zhiyicx.thinksnsplus.modules.dynamic.topdynamic;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

import static com.zhiyicx.thinksnsplus.modules.dynamic.topdynamic.DynamicTopFragment.FEEDID;

public class DynamicTopActivity extends TSActivity<DynamicTopPresenter, DynamicTopFragment> {

    @Override
    protected DynamicTopFragment getFragment() {
        return DynamicTopFragment.newInstance(getIntent().getLongExtra(FEEDID, -1L));
    }

    @Override
    protected void componentInject() {
        DaggerDynamicTopComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .dynamicTopPresenterModule(new DynamicTopPresenterModule(mContanierFragment))
                .build().inject(this);
    }
}
