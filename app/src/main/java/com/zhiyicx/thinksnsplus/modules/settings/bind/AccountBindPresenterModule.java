package com.zhiyicx.thinksnsplus.modules.settings.bind;

import com.zhiyicx.thinksnsplus.data.source.repository.AccountBindRepository;

import dagger.Module;
import dagger.Provides;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/28
 * @contact email:648129313@qq.com
 */
@Module
public class AccountBindPresenterModule {

    private AccountBindContract.View mView;

    public AccountBindPresenterModule(AccountBindContract.View mView) {
        this.mView = mView;
    }

    @Provides
    public AccountBindContract.View provideAccountBindContractView(){
        return mView;
    }

    @Provides
    public AccountBindContract.Repository provideAccountBindContractRepository(AccountBindRepository repository){
        return repository;
    }

}
