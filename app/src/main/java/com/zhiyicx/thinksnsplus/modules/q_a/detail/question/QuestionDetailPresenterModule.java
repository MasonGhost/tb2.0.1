package com.zhiyicx.thinksnsplus.modules.q_a.detail.question;

import com.zhiyicx.thinksnsplus.data.source.repository.QuestionDetailRepository;

import dagger.Module;
import dagger.Provides;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/15
 * @contact email:648129313@qq.com
 */
@Module
public class QuestionDetailPresenterModule {

    private QuestionDetailContract.View mView;

    public QuestionDetailPresenterModule(QuestionDetailContract.View mView) {
        this.mView = mView;
    }

    @Provides
    public QuestionDetailContract.View provideQuestionDetailContractView(){
        return mView;
    }

    @Provides
    public QuestionDetailContract.Repository provideQuestionDetailContractRepository(QuestionDetailRepository repository){
        return repository;
    }
}
