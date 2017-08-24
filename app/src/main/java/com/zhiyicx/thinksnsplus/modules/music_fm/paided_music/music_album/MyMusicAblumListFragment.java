package com.zhiyicx.thinksnsplus.modules.music_fm.paided_music.music_album;

import android.support.v7.widget.RecyclerView;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.MusicAlbumListBean;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2017/08/24/15:07
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class MyMusicAblumListFragment extends TSListFragment<MyMusicAblumListContract.Presenter,MusicAlbumListBean>
        implements MyMusicAblumListContract.View{

    @Inject
    MyMusicAlbumPresenter mMyMusicAlbumPresenter;

    public static MyMusicAblumListFragment getInstance() {
        MyMusicAblumListFragment myMusicAblumListFragment = new MyMusicAblumListFragment();
        return myMusicAblumListFragment;
    }

    @Override
    protected void initData() {
        DaggerMyMusicAlbumListComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .myMusicAlbumListPresenterModule(new MyMusicAlbumListPresenterModule(this))
                .build().inject(this);
        super.initData();
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return null;
    }
}
