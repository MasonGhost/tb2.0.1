package com.zhiyicx.thinksnsplus.modules.tb.word;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2018/4/3.
 */
@Module
public class WordPresenterModule {
    WordContract.View mView;

    public WordPresenterModule(WordContract.View view) {
        mView = view;
    }

    @Provides
    WordContract.View provideWordContractView() {
        return mView;
    }
}
