package com.zhiyicx.thinksnsplus.modules.chat.select;

import com.zhiyicx.thinksnsplus.data.source.repository.SelectFriendsRepository;

import dagger.Module;
import dagger.Provides;

/**
 * @author Catherine
 * @describe
 * @date 2018/1/11
 * @contact email:648129313@qq.com
 */
@Module
public class SelectFriendsPresenterModule {

    private SelectFriendsContract.View mView;

    public SelectFriendsPresenterModule(SelectFriendsContract.View mView) {
        this.mView = mView;
    }

    @Provides
    public SelectFriendsContract.View provideSelectFriendsContractView(){
        return mView;
    }
}
