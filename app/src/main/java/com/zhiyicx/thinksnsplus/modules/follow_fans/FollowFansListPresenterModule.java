package com.zhiyicx.thinksnsplus.modules.follow_fans;

import com.zhiyicx.thinksnsplus.data.source.repository.FollowFansListRepository;

import dagger.Module;
import dagger.Provides;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/14
 * @contact email:450127106@qq.com
 */
@Module
public class FollowFansListPresenterModule {
    private FollowFansListContract.View mView;

    public FollowFansListPresenterModule(FollowFansListContract.View view) {
        mView = view;
    }

    @Provides
    FollowFansListContract.View provideFollowFansListContractView() {
        return mView;
    }

    @Provides
    FollowFansListContract.Repository provideFollowFansListContractRepository(FollowFansListRepository followFansListRepository) {
        return followFansListRepository;
    }

}
