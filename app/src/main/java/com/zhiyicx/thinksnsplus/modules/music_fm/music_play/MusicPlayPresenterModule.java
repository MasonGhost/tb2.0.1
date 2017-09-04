package com.zhiyicx.thinksnsplus.modules.music_fm.music_play;

import android.app.Application;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.repository.MusicPlayRepository;

import dagger.Module;
import dagger.Provides;

/**
 * @Author Jliuer
 * @Date 2017/02/14
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Module
class MusicPlayPresenterModule {
    private  MusicPlayContract.View mView;

    public MusicPlayPresenterModule(MusicPlayContract.View view) {
        mView = view;
    }

    @Provides
    MusicPlayContract.View provideMusicContractView() {
        return mView;
    }

    @Provides
    MusicPlayContract.Repository provideMusicRepository(MusicPlayRepository musicPlayRepository) {
        return musicPlayRepository;
    }

}
