package com.zhiyicx.thinksnsplus.modules.dynamic.detail;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.baseproject.impl.share.ShareModule;
import com.zhiyicx.baseproject.impl.share.UmengSharePolicyImpl;
import com.zhiyicx.thinksnsplus.base.AppApplication;

public class DynamicDetailActivity extends TSActivity<DynamicDetailPresenter, DynamicDetailFragment> {

    @Override
    protected DynamicDetailFragment getFragment() {
        return DynamicDetailFragment.initFragment(
                getIntent().getExtras());
    }

    @Override
    protected void componentInject() {
        DaggerDynamicDetailPresenterCompnent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .shareModule(new ShareModule(DynamicDetailActivity.this))
                .dynamicDetailPresenterModule(new DynamicDetailPresenterModule(mContanierFragment))
                .build().inject(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UmengSharePolicyImpl.onDestroy(this);
    }
}
