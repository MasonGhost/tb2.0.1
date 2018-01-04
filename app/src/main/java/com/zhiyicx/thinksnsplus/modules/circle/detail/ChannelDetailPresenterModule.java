package com.zhiyicx.thinksnsplus.modules.circle.detail;

import com.zhiyicx.thinksnsplus.data.source.repository.ChannelDetailRepository;

import dagger.Module;
import dagger.Provides;

/**
 * @author LiuChao
 * @describe
 * @date 2017/4/11
 * @contact email:450127106@qq.com
 */
@Module
public class ChannelDetailPresenterModule {
    private ChannelDetailContract.View mView;

    public ChannelDetailPresenterModule(ChannelDetailContract.View view) {
        mView = view;
    }

    @Provides
    public ChannelDetailContract.View provideChannelDetailContractView() {
        return mView;
    }

    @Provides
    public ChannelDetailContract.Repository provideChannelDetailContractRepository(ChannelDetailRepository channelDetailRepository) {
        return channelDetailRepository;
    }
}
