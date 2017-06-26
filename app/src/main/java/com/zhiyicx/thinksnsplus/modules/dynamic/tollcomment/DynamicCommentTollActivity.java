package com.zhiyicx.thinksnsplus.modules.dynamic.tollcomment;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

import static com.zhiyicx.thinksnsplus.modules.dynamic.tollcomment.DynamicCommentTollFragment.TOLL_DYNAMIC_COMMENT;

public class DynamicCommentTollActivity extends TSActivity<DynamicCommentTollPresenter, DynamicCommentTollFragment> {

    @Override
    protected DynamicCommentTollFragment getFragment() {
        return DynamicCommentTollFragment.newInstance(getIntent().getBundleExtra(TOLL_DYNAMIC_COMMENT));
    }

    @Override
    protected void componentInject() {
        DaggerDynamicCommentTollComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .dynamicCommentTollPresenterModule(new DynamicCommentTollPresenterModule(mContanierFragment))
                .build().inject(this);
    }
}
