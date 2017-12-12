package com.zhiyicx.thinksnsplus.modules.circle.earning;

import com.zhiyicx.thinksnsplus.data.source.repository.CircleEarningRepository;

import dagger.Module;
import dagger.Provides;

/**
 * @Author Jliuer
 * @Date 2017/12/12/13:48
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Module
public class CircleEarningPresenterModule {
    CircleEarningContract.View mView;

    public CircleEarningPresenterModule(CircleEarningContract.View view) {
        mView = view;
    }

    @Provides
    CircleEarningContract.View provideCircleEarningView() {
        return mView;
    }

    @Provides
    CircleEarningContract.Repository provideCircleEarningRepository(CircleEarningRepository repository) {
        return repository;
    }
}
