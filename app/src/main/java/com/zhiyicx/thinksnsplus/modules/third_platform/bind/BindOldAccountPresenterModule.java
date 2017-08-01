package com.zhiyicx.thinksnsplus.modules.third_platform.bind;

import com.zhiyicx.thinksnsplus.data.source.repository.BindOldAccountRepository;

import dagger.Module;
import dagger.Provides;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/31
 * @contact email:648129313@qq.com
 */
@Module
public class BindOldAccountPresenterModule {

    private BindOldAccountContract.View mView;

    public BindOldAccountPresenterModule(BindOldAccountContract.View mView) {
        this.mView = mView;
    }

    @Provides
    public BindOldAccountContract.View provideBindOldAccountContractView(){
        return mView;
    }

    @Provides
    public BindOldAccountContract.Repository provideBindOldAccountContractRepository(BindOldAccountRepository repository){
        return repository;
    }
}
