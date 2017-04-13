package com.zhiyicx.thinksnsplus.modules.collect.album;

import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.modules.music_fm.music_album_list.MusicListFragment;
import com.zhiyicx.thinksnsplus.modules.music_fm.music_album_list.MusicPresenter;
import com.zhiyicx.thinksnsplus.modules.music_fm.music_album_list.MusicPresenterModule;

import javax.inject.Inject;

/**
 * @author LiuChao
 * @describe
 * @date 2017/4/13
 * @contact email:450127106@qq.com
 */

public class CollectAlbumListFragment extends MusicListFragment {

    @Inject
    MusicPresenter mMusicPresenter;

    @Override
    protected void initData() {
       /* DaggerCollectAlbumListPresenterComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .musicPresenterModule(new MusicPresenterModule(this))
                .build().inject(this);*/
        DaggerCollectAlbumListPresenterComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .collectAlbumPresenterModule(new CollectAlbumPresenterModule(this))
                .build().inject(this);
        super.initData();
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected boolean showToolBarDivider() {
        return false;
    }

    public static CollectAlbumListFragment newInstance() {
        CollectAlbumListFragment collectAlbumListFragment = new CollectAlbumListFragment();
        return collectAlbumListFragment;
    }
}
