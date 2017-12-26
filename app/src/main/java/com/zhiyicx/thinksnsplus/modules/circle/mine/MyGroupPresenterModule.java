package com.zhiyicx.thinksnsplus.modules.circle.mine;

import dagger.Module;
import dagger.Provides;

/**
 * @Describe 我的圈子列表
 * @Author Jungle68
 * @Date 2017/12/6
 * @Contact master.jungle68@gmail.com
 */
@Module
public class MyGroupPresenterModule {

    private MyGroupContract.View mView;

    public MyGroupPresenterModule(MyGroupContract.View mView) {
        this.mView = mView;
    }

    @Provides
    public MyGroupContract.View provideMyGroupContractView(){
        return mView;
    }


}
