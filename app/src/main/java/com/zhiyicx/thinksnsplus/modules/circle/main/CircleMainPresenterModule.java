package com.zhiyicx.thinksnsplus.modules.circle.main;

import dagger.Module;
import dagger.Provides;

/**
 * @author Jliuer
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

}
