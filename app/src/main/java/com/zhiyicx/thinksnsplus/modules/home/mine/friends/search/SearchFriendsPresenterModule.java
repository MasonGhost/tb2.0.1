package com.zhiyicx.thinksnsplus.modules.home.mine.friends.search;

import com.zhiyicx.thinksnsplus.data.source.repository.SearchFriendsRepository;

import dagger.Module;
import dagger.Provides;

/**
 * @author Catherine
 * @describe
 * @date 2017/12/25
 * @contact email:648129313@qq.com
 */
@Module
public class SearchFriendsPresenterModule {

    private SearchFriendsContract.View mView;

    public SearchFriendsPresenterModule(SearchFriendsContract.View mView) {
        this.mView = mView;
    }

    @Provides
    public SearchFriendsContract.View provideSearchFriendsContractView(){
        return mView;
    }

    @Provides
    public SearchFriendsContract.Repository provideSearchFriendsContractRepository(SearchFriendsRepository repository){
        return repository;
    }
}
