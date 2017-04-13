package com.zhiyicx.thinksnsplus.modules.collect.album;

import android.support.v4.media.MediaBrowserCompat;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.repository.MusicRepository;
import com.zhiyicx.thinksnsplus.modules.music_fm.music_album_list.MusicContract;
import com.zhiyicx.thinksnsplus.modules.music_fm.music_album_list.MusicPresenterModule;

import dagger.Module;
import dagger.Provides;

/**
 * @author LiuChao
 * @describe
 * @date 2017/4/13
 * @contact email:450127106@qq.com
 */
@Module
public class CollectAlbumPresenterModule {
    private MusicContract.View view;

    public CollectAlbumPresenterModule(MusicContract.View view) {
        this.view = view;
    }

    @Provides
    MusicContract.View provideMusicContractView() {
        return view;
    }

    @Provides
    MusicContract.Repository provideMusicRepository(ServiceManager serviceManager) {
        return new CollectAlbumListRepository(serviceManager);
    }

    @Provides
    MediaBrowserCompat provideMediaBrowserCompat() {
        return null;
    }
}
