package com.zhiyicx.thinksnsplus.modules.report;

import com.zhiyicx.thinksnsplus.data.source.repository.ReportRepository;

import dagger.Module;
import dagger.Provides;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/12/11
 * @Contact master.jungle68@gmail.com
 */
@Module
public class ReportPresenterModule {

    ReportContract.View mView;

    public ReportPresenterModule(ReportContract.View view) {
        mView = view;
    }

    @Provides
    ReportContract.View provideFeedBackContractView() {
        return mView;
    }

    @Provides
    ReportContract.Repository provideFeedBackContractRepository(ReportRepository repository) {
        return repository;
    }
}
