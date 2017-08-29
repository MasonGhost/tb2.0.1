package com.zhiyicx.thinksnsplus.modules.rank.type_list;

import com.zhiyicx.thinksnsplus.data.source.repository.RankTypeListRepository;

import dagger.Module;
import dagger.Provides;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/23
 * @contact email:648129313@qq.com
 */
@Module
public class RankTypeListPresenterModule {

    private RankTypeListContract.View mView;

    public RankTypeListPresenterModule(RankTypeListContract.View mView) {
        this.mView = mView;
    }

    @Provides
    public RankTypeListContract.View provideRankTypeListContractView(){
        return mView;
    }

    @Provides
    public RankTypeListContract.Repository provideRankTypeListContractRepository(RankTypeListRepository repository){
        return repository;
    }
}
