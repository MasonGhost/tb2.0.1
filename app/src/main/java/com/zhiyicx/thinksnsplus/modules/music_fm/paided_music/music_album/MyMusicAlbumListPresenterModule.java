package com.zhiyicx.thinksnsplus.modules.music_fm.paided_music.music_album;

import com.zhiyicx.thinksnsplus.data.source.repository.MusicPaidsAlbumListRepository;

import dagger.Module;
import dagger.Provides;

/**
 * @Author Jliuer
 * @Date 2017/08/24/17:52
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Module
public class MyMusicAlbumListPresenterModule {
    MyMusicAblumListContract.View mView;

    public MyMusicAlbumListPresenterModule(MyMusicAblumListContract.View view) {
        mView = view;
    }

    @Provides
    MyMusicAblumListContract.View provideMyMusicAblumListContractView() {
        return mView;
    }

    @Provides
    MyMusicAblumListContract.Repository provideMyMusicAblumListContractRepository(MusicPaidsAlbumListRepository repository) {
        return repository;
    }
}
