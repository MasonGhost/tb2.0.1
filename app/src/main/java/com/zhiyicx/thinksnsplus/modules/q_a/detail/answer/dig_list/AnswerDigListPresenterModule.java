package com.zhiyicx.thinksnsplus.modules.q_a.detail.answer.dig_list;

import com.zhiyicx.thinksnsplus.data.source.repository.AnswerDigListRepository;

import dagger.Module;
import dagger.Provides;

/**
 * @author LiuChao
 * @describe
 * @date 2017/3/6
 * @contact email:450127106@qq.com
 */
@Module
public class AnswerDigListPresenterModule {
    private AnswerDigListContract.View mView;

    public AnswerDigListPresenterModule(AnswerDigListContract.View view) {
        mView = view;
    }

    @Provides
    AnswerDigListContract.View provideDigListContractView() {
        return mView;
    }

    @Provides
    AnswerDigListContract.Repository provideDigListContractRepository(AnswerDigListRepository digListRepository) {
        return digListRepository;
    }
}
