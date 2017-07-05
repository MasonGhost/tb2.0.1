package com.zhiyicx.thinksnsplus.modules.dynamic.topdynamic_comment;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

import static com.zhiyicx.thinksnsplus.modules.dynamic.tollcomment.DynamicCommentTollFragment.TOLL_DYNAMIC_COMMENT;

public class DynamicCommentTopActivity extends TSActivity<DynamicCommentTopPresenter, DynamicCommentTopFragment> {

    @Override
    protected DynamicCommentTopFragment getFragment() {
        return DynamicCommentTopFragment.newInstance(getIntent().getBundleExtra(TOLL_DYNAMIC_COMMENT));
    }

    @Override
    protected void componentInject() {
        DaggerDynamicCommentTopComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .dynamicCommentTopPresenterModule(new DynamicCommentTopPresenterModule(mContanierFragment))
                .build().inject(this);
    }
}
