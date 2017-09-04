package com.zhiyicx.thinksnsplus.data.source.repository;


import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.data.beans.MusicAlbumListBean;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.music_fm.paided_music.music_album.MyMusicAblumListContract;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @Author Jliuer
 * @Date 2017/08/24/17:53
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class MusicPaidsAlbumListRepository extends BaseMusicRepository implements MyMusicAblumListContract.Repository {

    @Inject
    public MusicPaidsAlbumListRepository(ServiceManager serviceManager) {
        super(serviceManager);
    }

    @Override
    public Observable<List<MusicAlbumListBean>> getMyPaidsMusicAlbumList(long max_id) {
        return mMusicClient.getMyPaidsMusicAlbumList(max_id, (long) TSListFragment.DEFAULT_PAGE_SIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
