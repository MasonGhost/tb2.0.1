package com.zhiyicx.thinksnsplus.modules.circle.list;

import dagger.Module;
import dagger.Provides;

/**
 * @author LiuChao
 * @describe
 * @date 2017/4/8
 * @contact email:450127106@qq.com
 */
@Module
public class ChannelListPresenterModule {
    private ChannelListContract.View mView;

    public ChannelListPresenterModule(ChannelListContract.View view) {
        mView = view;
    }

    @Provides
    public ChannelListContract.View provideChannelListContractView() {
        return mView;
    }

}
