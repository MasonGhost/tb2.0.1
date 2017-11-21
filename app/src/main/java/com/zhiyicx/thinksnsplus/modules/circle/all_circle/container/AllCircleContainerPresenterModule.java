package com.zhiyicx.thinksnsplus.modules.circle.all_circle.container;

import com.zhiyicx.thinksnsplus.data.source.repository.AllCircleContainerRepository;

import dagger.Module;
import dagger.Provides;

/**
 * @Author Jliuer
 * @Date 2017/11/21/15:48
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Module
public class AllCircleContainerPresenterModule {

    AllCircleContainerContract.View mView;

    public AllCircleContainerPresenterModule(AllCircleContainerContract.View view) {
        mView = view;
    }

    @Provides
    AllCircleContainerContract.View providesAllCircleContainerContractView() {
        return mView;
    }

    @Provides
    AllCircleContainerContract.Repository providesAllCircleContainerContractRepository(AllCircleContainerRepository allCircleContainerRepository) {
        return allCircleContainerRepository;
    }
}
