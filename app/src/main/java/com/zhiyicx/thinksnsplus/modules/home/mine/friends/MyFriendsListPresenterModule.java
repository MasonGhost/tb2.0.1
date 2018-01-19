package com.zhiyicx.thinksnsplus.modules.home.mine.friends;

import dagger.Module;
import dagger.Provides;

/**
 * @author Catherine
 * @describe
 * @date 2017/12/22
 * @contact email:648129313@qq.com
 */
@Module
public class MyFriendsListPresenterModule {

    private MyFriendsListContract.View mView;

    public MyFriendsListPresenterModule(MyFriendsListContract.View mView) {
        this.mView = mView;
    }

    @Provides
    public MyFriendsListContract.View provideMyFriendsListContractView(){
        return mView;
    }
}
