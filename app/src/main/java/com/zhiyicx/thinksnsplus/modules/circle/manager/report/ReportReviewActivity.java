package com.zhiyicx.thinksnsplus.modules.circle.manager.report;

import android.os.Bundle;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

public class ReportReviewActivity extends TSActivity<ReportReviewPresenter, ReporReviewFragment> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected ReporReviewFragment getFragment() {
        return ReporReviewFragment.newInstance(getIntent().getExtras());
    }

    @Override
    protected void componentInject() {
        DaggerReportReviewComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .reportReviewPresenterModule(new ReportReviewPresenterModule(mContanierFragment))
                .build().inject(this);
    }
}
