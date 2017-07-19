package com.zhiyicx.thinksnsplus.modules.channel.group_dynamic;

import com.zhiyicx.thinksnsplus.data.source.repository.GroupDynamicDetailRepository;

import dagger.Module;
import dagger.Provides;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/18
 * @contact email:648129313@qq.com
 */
@Module
public class GroupDynamicDetailPresenterModule {

    private GroupDynamicDetailContract.View mView;

    public GroupDynamicDetailPresenterModule(GroupDynamicDetailContract.View mView) {
        this.mView = mView;
    }

    @Provides
    public GroupDynamicDetailContract.View provideGroupDynamicDetailContractView(){
        return mView;
    }

    @Provides
    public GroupDynamicDetailContract.Repository provideGroupDynamicDetailContractRepository(GroupDynamicDetailRepository repository){
        return repository;
    }

}
