package com.zhiyicx.thinksnsplus.modules.channel.mine;

import com.zhiyicx.thinksnsplus.data.source.repository.MyGroupRepository;

import dagger.Module;
import dagger.Provides;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/25
 * @contact email:648129313@qq.com
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

    @Provides
    public MyGroupContract.Repository provideMyGroupContractRepository(MyGroupRepository repository){
        return repository;
    }
}
