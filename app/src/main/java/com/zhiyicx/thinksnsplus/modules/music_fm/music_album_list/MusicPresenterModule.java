package com.zhiyicx.thinksnsplus.modules.music_fm.music_album_list;

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
public class MusicPresenterModule {
    private MusicContract.View view;

    public MusicPresenterModule(MusicContract.View view) {
        this.view = view;
    }

    @Provides
    MusicContract.View provideMusicContractView() {
        return view;
    }

    @Provides
    MusicContract.Repository provideMusicRepository(MusicRepository musicRepository) {
        return musicRepository;
    }
}
