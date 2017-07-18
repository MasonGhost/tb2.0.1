package com.zhiyicx.thinksnsplus.modules.dynamic.topdynamic_comment;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

import static com.zhiyicx.thinksnsplus.modules.dynamic.topdynamic_comment.DynamicCommentTopFragment.TOP_DYNAMIC_COMMENT_ID;
import static com.zhiyicx.thinksnsplus.modules.dynamic.topdynamic_comment.DynamicCommentTopFragment.TOP_DYNAMIC_ID;

public class DynamicCommentTopActivity extends TSActivity<DynamicCommentTopPresenter, DynamicCommentTopFragment> {

    @Override
    protected DynamicCommentTopFragment getFragment() {
        return DynamicCommentTopFragment.newInstance(getIntent().getLongExtra(TOP_DYNAMIC_ID,-1L),
                getIntent().getLongExtra(TOP_DYNAMIC_COMMENT_ID,-1L));
    }

    @Override
    protected void componentInject() {
        DaggerDynamicCommentTopComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .dynamicCommentTopPresenterModule(new DynamicCommentTopPresenterModule(mContanierFragment))
                .build().inject(this);
    }
}
