package com.zhiyicx.thinksnsplus.modules.q_a.publish.question;

import com.zhiyicx.thinksnsplus.data.source.repository.PublishPublishQuestionRepository;

import dagger.Module;
import dagger.Provides;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/24
 * @contact email:648129313@qq.com
 */
@Module
public class PublishQuestionPresenterModule {

    private PublishQuestionContract.View mView;

    public PublishQuestionPresenterModule(PublishQuestionContract.View mView) {
        this.mView = mView;
    }

    @Provides
    public PublishQuestionContract.View provideSendQuizContractView(){
        return mView;
    }

    @Provides
    public PublishQuestionContract.Repository provideSendQuizContractRepository(PublishPublishQuestionRepository repository){
        return repository;
    }
}
