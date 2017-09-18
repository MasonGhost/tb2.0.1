package com.zhiyicx.thinksnsplus.modules.q_a.detail.topic.list;

import com.zhiyicx.thinksnsplus.data.source.repository.TopicDetailListRepository;

import dagger.Module;
import dagger.Provides;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/31
 * @contact email:648129313@qq.com
 */
@Module
public class TopicDetailListPresenterModule {

    private TopicDetailListContract.View mView;

    public TopicDetailListPresenterModule(TopicDetailListContract.View mView) {
        this.mView = mView;
    }

    @Provides
    public TopicDetailListContract.View provideTopicDetailListContractView(){
        return mView;
    }

    @Provides
    public TopicDetailListContract.Repository provideTopicDetailListContractRpository(TopicDetailListRepository repository){
        return repository;
    }
}
