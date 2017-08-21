package com.zhiyicx.thinksnsplus.modules.q_a.detail.question.comment;

import com.zhiyicx.thinksnsplus.data.source.repository.QuestionCommentRepository;

import dagger.Module;
import dagger.Provides;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/18
 * @contact email:648129313@qq.com
 */
@Module
public class QuestionCommentPresenterModule {

    private QuestionCommentContract.View mView;

    public QuestionCommentPresenterModule(QuestionCommentContract.View mView) {
        this.mView = mView;
    }

    @Provides
    public QuestionCommentContract.View provideQuestionCommentContractView(){
        return mView;
    }

    @Provides
    public QuestionCommentContract.Repository provideQuestionCommentContractRepository(QuestionCommentRepository repository){
        return repository;
    }
}
