package com.zhiyicx.thinksnsplus.modules.circle.detailv2;

import dagger.Module;
import dagger.Provides;

/**
 * @author Jliuer
 * @Date 2017/11/22/14:38
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Module
public class CircleDetailPresenterModule {
    CircleDetailContract.View mView;

    public CircleDetailPresenterModule(CircleDetailContract.View view) {
        mView = view;
    }

    @Provides
    CircleDetailContract.View provideCircleDetailContractView() {
        return mView;
    }

}
