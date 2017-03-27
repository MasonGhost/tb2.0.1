package com.zhiyicx.thinksnsplus.modules.information.infochannel;

import android.app.Application;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.repository.InfoChannelRepository;

import dagger.Module;
import dagger.Provides;

/**
 * @Author Jliuer
 * @Date 2017/03/15
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Module
public class InfoChannelPresenterModule {

    InfoChannelConstract.View mView;

    public InfoChannelPresenterModule(InfoChannelConstract.View view) {
        mView = view;
    }

    @Provides
    InfoChannelConstract.View provideInfoChannelView() {
        return mView;
    }

    @Provides
    InfoChannelConstract.Reppsitory provideInfoChannelReppsitory(ServiceManager serviceManager,
                                                                 Application application) {
        return new InfoChannelRepository(serviceManager,application);
    }
}
