package com.zhiyicx.thinksnsplus.modules.circle.all_circle;

import dagger.Module;
import dagger.Provides;

/**
 * @Author Jliuer
 * @Date 2017/11/21/16:33
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Module
public class CircleListPresenterModule {
    CircleListContract.View mView;

    public CircleListPresenterModule(CircleListContract.View view) {
        mView = view;
    }

    @Provides
    CircleListContract.View provideCircleListContractView(){
        return mView;
    }

}
