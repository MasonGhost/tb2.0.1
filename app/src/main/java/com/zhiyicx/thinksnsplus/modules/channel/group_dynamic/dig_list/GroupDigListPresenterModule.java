package com.zhiyicx.thinksnsplus.modules.channel.group_dynamic.dig_list;

import com.zhiyicx.thinksnsplus.data.source.repository.GroupDigListRepository;

import dagger.Module;
import dagger.Provides;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/20
 * @contact email:648129313@qq.com
 */
@Module
public class GroupDigListPresenterModule {

    private GroupDigListContract.View mView;

    public GroupDigListPresenterModule(GroupDigListContract.View mView) {
        this.mView = mView;
    }

    @Provides
    public GroupDigListContract.View provideGroupDigListContractView(){
        return mView;
    }

    @Provides
    public GroupDigListContract.Repository provideGroupDigListContractRepository(GroupDigListRepository repository){
        return repository;
    }
}
