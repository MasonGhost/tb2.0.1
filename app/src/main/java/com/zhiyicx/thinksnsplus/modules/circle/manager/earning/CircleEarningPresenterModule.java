package com.zhiyicx.thinksnsplus.modules.circle.manager.earning;

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

}
