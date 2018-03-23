package com.zhiyicx.thinksnsplus.modules.tb.dynamic.comment;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.baseproject.impl.share.ShareModule;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.modules.dynamic.detail.DaggerDynamicDetailPresenterCompnent;
import com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailPresenterModule;

import android.view.ViewGroup.LayoutParams;

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
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mContanierFragment.onBackPressed();
    }
}
