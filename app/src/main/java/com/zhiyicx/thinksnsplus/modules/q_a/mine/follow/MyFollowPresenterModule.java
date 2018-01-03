package com.zhiyicx.thinksnsplus.modules.q_a.mine.follow;

import dagger.Module;
import dagger.Provides;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/28
 * @contact email:648129313@qq.com
 */
@Module
public class MyFollowPresenterModule {
    private MyFollowContract.View mView;

    public MyFollowPresenterModule(MyFollowContract.View mView) {
        this.mView = mView;
    }

    @Provides
    public MyFollowContract.View provideMyFollowContractView(){
        return mView;
    }

}
