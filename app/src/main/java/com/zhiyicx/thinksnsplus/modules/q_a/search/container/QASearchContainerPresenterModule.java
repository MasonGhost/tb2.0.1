package com.zhiyicx.thinksnsplus.modules.q_a.search.container;

import dagger.Module;
import dagger.Provides;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/8/11
 * @Contact master.jungle68@gmail.com
 */
@Module
public class QASearchContainerPresenterModule {
    QASearchContainerContract.View mView;

    public QASearchContainerPresenterModule(QASearchContainerContract.View view) {
        mView = view;
    }

    @Provides
    QASearchContainerContract.View providesView() {
        return mView;
    }


}
