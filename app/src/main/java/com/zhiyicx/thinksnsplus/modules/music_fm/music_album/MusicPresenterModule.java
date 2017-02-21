package com.zhiyicx.thinksnsplus.modules.music_fm.music_album;

import android.support.v4.media.MediaBrowserCompat;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.repository.MusicRepository;

import dagger.Module;
import dagger.Provides;

/**
 * @Author Jliuer
 * @Date 2017/02/13
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Module
class MusicPresenterModule {
    private MusicContract.View view;

    MusicPresenterModule(MusicContract.View view) {
        this.view = view;
    }

    @Provides
    MusicContract.View provideMusicContractView() {
        return view;
    }

    @Provides
    MusicContract.Repository provideMusicRepository(ServiceManager serviceManager) {
        return new MusicRepository(serviceManager);
    }

    @Provides
    MediaBrowserCompat provideMediaBrowserCompat(){
        return null;
    }
}
