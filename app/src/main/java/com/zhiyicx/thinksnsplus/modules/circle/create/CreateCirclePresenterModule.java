package com.zhiyicx.thinksnsplus.modules.circle.create;

import dagger.Module;
import dagger.Provides;

/**
 * @Author Jliuer
 * @Date 2017/11/21/17:01
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Module
public class CreateCirclePresenterModule {

    CreateCircleContract.View mView;

    public CreateCirclePresenterModule(CreateCircleContract.View view) {
        mView = view;
    }

    @Provides
    CreateCircleContract.View provideCreateCircleContractView() {
        return mView;
    }

}
