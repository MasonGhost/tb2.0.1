package com.zhiyicx.thinksnsplus.modules.home.mine.friends.search;

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

}
