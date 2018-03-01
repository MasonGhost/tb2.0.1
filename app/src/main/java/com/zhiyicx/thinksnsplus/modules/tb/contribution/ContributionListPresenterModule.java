package com.zhiyicx.thinksnsplus.modules.tb.contribution;

import dagger.Module;
import dagger.Provides;

/**
 * @Author Jliuer
 * @Date 2018/03/01/14:56
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Module
public class ContributionListPresenterModule {
    ContributionListContract.View mView;

    public ContributionListPresenterModule(ContributionListContract.View view) {
        mView = view;
    }

    @Provides
    ContributionListContract.View provideContributionListContractView() {
        return mView;
    }
}
