package com.zhiyicx.thinksnsplus.modules.tb.dynamic.comment;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.baseproject.impl.share.ShareModule;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.modules.dynamic.detail.DaggerDynamicDetailPresenterCompnent;
import com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailPresenterModule;

/**
 * @Describe TB 快讯评论列表
 * @Author Jungle68
 * @Date 2018/3/21
 * @Contact master.jungle68@gmail.com
 */
public class DynamicCommentListActivity extends TSActivity<DynamicCommentListPresenter, DynamicCommentListFragment> {

    @Override
    protected DynamicCommentListFragment getFragment() {
        return DynamicCommentListFragment.initFragment(
                getIntent().getExtras());
    }

    @Override
    protected void componentInject() {
        DaggerDynamicCommentListPresenterCompnent
                .builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .dynamicCommentListPresenterModule(new DynamicCommentListPresenterModule(mContanierFragment))
                .build()
                .inject(this);
    }


}
