package com.zhiyicx.thinksnsplus.modules.circle.create.location;

import com.zhiyicx.thinksnsplus.data.source.repository.SystemRepository;

import dagger.Module;
import dagger.Provides;

/**
 * @Author Jliuer
 * @Date 2017/11/21/16:33
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Module
public class CircleLocationPresenterModule {
    CircleLocationContract.View mView;

    public CircleLocationPresenterModule(CircleLocationContract.View view) {
        mView = view;
    }

    @Provides
    CircleLocationContract.View provideCircleLocationContractView() {
        return mView;
    }

    @Provides
    CircleLocationContract.Repository provideCircleLocationContractRepository() {
        return new NotNull();
    }

    class NotNull implements CircleLocationContract.Repository {
    }
}
