package com.zhiyicx.thinksnsplus.modules.q_a.search.list.qa;

import dagger.Module;
import dagger.Provides;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/8/10
 * @Contact master.jungle68@gmail.com
 */
@Module
public class QASearchListPresenterModule {
    private QASearchListContract.View mView;

    public QASearchListPresenterModule(QASearchListContract.View view) {
        mView = view;
    }

    @Provides
    QASearchListContract.View provideView() {
        return mView;
    }

}
