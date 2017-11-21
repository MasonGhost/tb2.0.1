package com.zhiyicx.thinksnsplus.modules.circle.all_circle;

import com.zhiyicx.thinksnsplus.data.source.repository.CircleListRepository;

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

    @Provides
    CircleListContract.Repository provideCircleListContractRepository(CircleListRepository circleListRepository){
        return circleListRepository;
    }
}
