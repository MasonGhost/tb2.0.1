package com.zhiyicx.thinksnsplus.modules.findsomeone.list;

import com.zhiyicx.thinksnsplus.data.source.repository.FindSomeRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.FollowFansListRepository;
import com.zhiyicx.thinksnsplus.modules.follow_fans.FollowFansListContract;

import dagger.Module;
import dagger.Provides;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/8/10
 * @Contact master.jungle68@gmail.com
 */
@Module
public class FindSomeOneListPresenterModule {
    private FindSomeOneListContract.View mView;

    public FindSomeOneListPresenterModule(FindSomeOneListContract.View view) {
        mView = view;
    }

    @Provides
    FindSomeOneListContract.View provideFindSomeOneListContractView() {
        return mView;
    }

    @Provides
    FindSomeOneListContract.Repository provideFindSomeOneListContractRepository(FindSomeRepository repository) {
        return repository;
    }

}
