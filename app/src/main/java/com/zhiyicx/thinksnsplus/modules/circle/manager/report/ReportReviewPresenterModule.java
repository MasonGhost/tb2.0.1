package com.zhiyicx.thinksnsplus.modules.circle.manager.report;

import com.zhiyicx.thinksnsplus.data.source.repository.ReportReviewRepository;

import dagger.Module;
import dagger.Provides;

/**
 * @Author Jliuer
 * @Date 2017/12/14/9:52
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Module
public class ReportReviewPresenterModule {
    ReporReviewContract.View mView;

    public ReportReviewPresenterModule(ReporReviewContract.View view) {
        mView = view;
    }

    @Provides
    ReporReviewContract.View provideReportReviewContractView() {
        return mView;
    }

    @Provides
    ReporReviewContract.Repository provideReportRevieContractRepository(ReportReviewRepository reportReviewRepository) {
        return reportReviewRepository;
    }
}
