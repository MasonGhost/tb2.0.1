package com.zhiyicx.thinksnsplus.modules.circle.search.container;

import com.zhiyicx.thinksnsplus.data.source.repository.CircleMainRepository;
import com.zhiyicx.thinksnsplus.modules.q_a.search.container.QASearchContainerContract;

import dagger.Module;
import dagger.Provides;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/8/11
 * @Contact master.jungle68@gmail.com
 */
@Module
public class CircleSearchContainerPresenterModule {
    CircleSearchContainerContract.View mView;

    public CircleSearchContainerPresenterModule(CircleSearchContainerContract.View view) {
        mView = view;
    }

    @Provides
    CircleSearchContainerContract.View providesView() {
        return mView;
    }

    @Provides
    CircleSearchContainerContract.Repository providesRepository(CircleMainRepository circleMainRepository) {
        return circleMainRepository;
    }
}
