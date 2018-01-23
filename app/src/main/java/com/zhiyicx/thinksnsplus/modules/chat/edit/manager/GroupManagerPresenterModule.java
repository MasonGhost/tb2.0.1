package com.zhiyicx.thinksnsplus.modules.chat.edit.manager;

import com.zhiyicx.thinksnsplus.data.source.repository.GroupManagerRepository;

import dagger.Module;
import dagger.Provides;

/**
 * @author Catherine
 * @describe
 * @date 2018/1/23
 * @contact email:648129313@qq.com
 */
@Module
public class GroupManagerPresenterModule {

    private GroupManagerContract.View mView;

    public GroupManagerPresenterModule(GroupManagerContract.View mView) {
        this.mView = mView;
    }

    @Provides
    public GroupManagerContract.View provideGroupManagerContractView(){
        return mView;
    }

    @Provides
    public GroupManagerContract.Repository provideGroupManagerContractRepository(GroupManagerRepository repository){
        return repository;
    }

}
