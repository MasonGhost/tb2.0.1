package com.zhiyicx.thinksnsplus.modules.diglist;

import com.zhiyicx.thinksnsplus.data.source.repository.DigListRepository;

import dagger.Module;
import dagger.Provides;

/**
 * @author LiuChao
 * @describe
 * @date 2017/3/6
 * @contact email:450127106@qq.com
 */
@Module
public class DigListPresenterModule {
    private DigListContract.View mView;

    public DigListPresenterModule(DigListContract.View view) {
        mView = view;
    }

    @Provides
    DigListContract.View provideDigListContractView() {
        return mView;
    }

    @Provides
    DigListContract.Repository provideDigListContractRepository(DigListRepository digListRepository) {
        return null;
    }
}
