package com.zhiyicx.thinksnsplus.modules.q_a.publish.add_topic;

import com.zhiyicx.thinksnsplus.data.source.repository.AddTopicRepository;

import dagger.Module;
import dagger.Provides;


/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/7/25
 * @Contact master.jungle68@gmail.com
 */

@Module
public class AddTopicPresenterModule {

    private AddTopicContract.View mView;

    public AddTopicPresenterModule(AddTopicContract.View mView) {
        this.mView = mView;
    }

    @Provides
    public AddTopicContract.View provideSendQuizContractView(){
        return mView;
    }

    @Provides
    public AddTopicContract.Repository provideSendQuizContractRepository(AddTopicRepository repository){
        return repository;
    }
}
