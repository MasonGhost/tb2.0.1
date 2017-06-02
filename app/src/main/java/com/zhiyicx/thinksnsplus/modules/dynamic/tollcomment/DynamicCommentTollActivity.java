package com.zhiyicx.thinksnsplus.modules.dynamic.tollcomment;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

public class DynamicCommentTollActivity extends TSActivity<DynamicCommentTollPresenter, DynamicCommentTollFragment> {

    @Override
    protected DynamicCommentTollFragment getFragment() {
        return DynamicCommentTollFragment.newInstance();
    }

    @Override
    protected void componentInject() {
        DaggerDynamicCommentTollComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .dynamicCommentTollPresenterModule(new DynamicCommentTollPresenterModule(mContanierFragment))
                .build().inject(this);
    }
}
