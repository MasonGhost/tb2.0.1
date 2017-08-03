package com.zhiyicx.thinksnsplus.modules.third_platform.complete;

import com.zhiyicx.thinksnsplus.data.source.repository.CompleteAccountRepository;

import dagger.Module;
import dagger.Provides;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/31
 * @contact email:648129313@qq.com
 */
@Module
public class CompleteAccountPresenterModule {

    private CompleteAccountContract.View mView;

    public CompleteAccountPresenterModule(CompleteAccountContract.View mView) {
        this.mView = mView;
    }

    @Provides
    public CompleteAccountContract.View provideCompleteAccountContractView(){
        return mView;
    }

    @Provides
    public CompleteAccountContract.Repository provideCompleteAccountContractRepository(CompleteAccountRepository repository){
        return repository;
    }

}
