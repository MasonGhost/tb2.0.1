package com.zhiyicx.thinksnsplus.modules.tb.tbmark_detail;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

/**
 * @Author MasonGhost
 * @Date 2018/04/17/14:55
 * @Email lx1993m@gmail.com
 * @Description TBMark详情列表
 */

public class TBMarkDetailActivity extends TSActivity<TBMarkDetailPresenter, TBMarkDetailFragment> {

    @Override
    protected void componentInject() {
        DaggerTBMarkDetailComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .tBMarkDetailPresenterModule(new TBMarkDetailPresenterModule(mContanierFragment))
                .build().inject(this);
    }

    @Override
    protected TBMarkDetailFragment getFragment() {
        return TBMarkDetailFragment.initFragment(getIntent().getExtras());
    }
}