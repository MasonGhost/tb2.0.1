package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.data.beans.MusicDetaisBean;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.music_fm.paided_music.single_music.SingleMusicListContract;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @Author Jliuer
 * @Date 2017/08/24/17:34
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class MusicPaidsListRepository extends BaseMusicRepository implements SingleMusicListContract.Repository {

    @Inject
    public MusicPaidsListRepository(ServiceManager serviceManager) {
        super(serviceManager);
    }

    @Override
    public Observable<List<MusicDetaisBean>> getMyPaidsMusicList(long max_id) {
        return mMusicClient.getMyPaidsMusicList(max_id, (long) TSListFragment.DEFAULT_PAGE_SIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
