package com.zhiyicx.thinksnsplus.modules.information.infodetails.topinfo_comment;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.modules.dynamic.topdynamic_comment.DaggerDynamicCommentTopComponent;

import static com.zhiyicx.thinksnsplus.modules.dynamic.topdynamic_comment.DynamicCommentTopFragment.TOP_DYNAMIC_COMMENT_ID;
import static com.zhiyicx.thinksnsplus.modules.dynamic.topdynamic_comment.DynamicCommentTopFragment.TOP_DYNAMIC_ID;

public class InfoCommentTopActivity extends TSActivity<InfoCommentTopPresenter, InfoCommentTopFragment> {

    @Override
    protected InfoCommentTopFragment getFragment() {
        return InfoCommentTopFragment.newInstance(getIntent().getLongExtra(TOP_DYNAMIC_ID,-1L),
                getIntent().getLongExtra(TOP_DYNAMIC_COMMENT_ID,-1L));
    }

    @Override
    protected void componentInject() {
        DaggerInfoCommentTopComponent
                .builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .infoCommentTopPresenterModule(new InfoCommentTopPresenterModule(mContanierFragment))
                .build().inject(this);
    }
}
