package com.zhiyicx.thinksnsplus.modules.home.mine;

import dagger.Module;
import dagger.Provides;

/**
 * @author LiuChao
 * @describe
 * @date 2017/3/9
 * @contact email:450127106@qq.com
 */
@Module
public class MinePresenterModule {
    private MineContract.View mView;

    public MinePresenterModule(MineContract.View view) {
        mView = view;
    }

    @Provides
    MineContract.View provideMineContractView() {
        return mView;
    }

}
