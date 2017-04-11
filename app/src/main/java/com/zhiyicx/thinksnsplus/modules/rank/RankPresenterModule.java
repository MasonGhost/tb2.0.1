package com.zhiyicx.thinksnsplus.modules.rank;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.repository.RankRepository;

import dagger.Module;
import dagger.Provides;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/4/10
 * @Contact master.jungle68@gmail.com
 */
@Module
public class RankPresenterModule {

    private final RankContract.View mView;

    public RankPresenterModule(RankContract.View view) {
        mView = view;
    }

    @Provides
    RankContract.View provideRankContractView() {
        return mView;
    }


    @Provides
    RankContract.Repository provideRankContractRepository(ServiceManager serviceManager){
        return new RankRepository(serviceManager);
    }

}
