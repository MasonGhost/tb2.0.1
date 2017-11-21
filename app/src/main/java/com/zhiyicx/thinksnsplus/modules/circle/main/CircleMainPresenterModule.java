package com.zhiyicx.thinksnsplus.modules.circle.main;

import com.zhiyicx.thinksnsplus.data.source.repository.CircleMainRepository;

import dagger.Module;
import dagger.Provides;

/**
 * @Author Jliuer
 * @Date 2017/11/14/11:37
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Module
public class CircleMainPresenterModule {
    CircleMainContract.View mView;

    public CircleMainPresenterModule(CircleMainContract.View view) {
        mView = view;
    }

    @Provides
    CircleMainContract.View providesCircleMainContractView() {
        return mView;
    }

    @Provides
    CircleMainContract.Repository providesCircleMainContractRepository(CircleMainRepository circleMainRepository) {
        return circleMainRepository;
    }
}
