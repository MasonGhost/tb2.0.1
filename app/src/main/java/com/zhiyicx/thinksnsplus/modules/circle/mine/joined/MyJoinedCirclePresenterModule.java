package com.zhiyicx.thinksnsplus.modules.circle.mine.joined;

import com.zhiyicx.thinksnsplus.data.source.repository.CircleListRepository;
import com.zhiyicx.thinksnsplus.modules.circle.all_circle.CircleListContract;

import dagger.Module;
import dagger.Provides;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/12/6
 * @Contact master.jungle68@gmail.com
 */
@Module
public class MyJoinedCirclePresenterModule {
    MyJoinedCircleContract.View mView;

    public MyJoinedCirclePresenterModule(MyJoinedCircleContract.View view) {
        mView = view;
    }

    @Provides
    MyJoinedCircleContract.View provideCircleListContractView() {
        return mView;
    }

    @Provides
    MyJoinedCircleContract.Repository provideCircleListContractRepository(CircleListRepository circleListRepository) {
        return circleListRepository;
    }
}
