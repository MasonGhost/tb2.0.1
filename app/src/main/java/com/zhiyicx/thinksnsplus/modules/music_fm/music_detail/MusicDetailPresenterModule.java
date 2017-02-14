package com.zhiyicx.thinksnsplus.modules.music_fm.music_detail;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.repository.MusicDetailRepository;

import javax.inject.Inject;

import dagger.Module;
import dagger.Provides;

/**
 * @Author Jliuer
 * @Date 2017/02/14
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Module
class MusicDetailPresenterModule {
    private  MusicDetailContract.View mView;

    public MusicDetailPresenterModule(MusicDetailContract.View view) {
        mView = view;
    }

    @Provides
    MusicDetailContract.View provideMusicContractView() {
        return mView;
    }

    @Provides
    MusicDetailContract.Repository provideMusicRepository(ServiceManager serviceManager) {
        return new MusicDetailRepository(serviceManager);
    }

}
