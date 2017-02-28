package com.zhiyicx.thinksnsplus.modules.dynamic.detail;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

public class DynamicDetailActivity extends TSActivity<DynamicDetailPresenter, DynamicDetailFragment> {


    @Override
    protected DynamicDetailFragment getFragment() {
        return DynamicDetailFragment.initFragment(null);
    }

    @Override
    protected void componentInject() {
        DaggerDynamicDetailPresenterCompnent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .dynamicDetailPresenterModule(new DynamicDetailPresenterModule(mContanierFragment))
                .build().inject(this);
    }
}
