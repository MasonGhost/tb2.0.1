package com.zhiyicx.thinksnsplus.modules.dynamic.send.dynamic_type;

import com.zhiyicx.thinksnsplus.data.source.repository.SelectDynamicTypeRepository;

import dagger.Module;
import dagger.Provides;

/**
 * @author Catherine
 * @describe
 * @date 2017/9/19
 * @contact email:648129313@qq.com
 */
@Module
public class SelectDynamicTypePresenterModule {

    private SelectDynamicTypeContract.View mView;

    public SelectDynamicTypePresenterModule(SelectDynamicTypeContract.View mView) {
        this.mView = mView;
    }

    @Provides
    public SelectDynamicTypeContract.View provideSelectDynamicTypeContractView(){
        return mView;
    }

    @Provides
    public SelectDynamicTypeContract.Repository provideSelectDynamicTypeContractRepository(SelectDynamicTypeRepository repository){
        return repository;
    }
}
