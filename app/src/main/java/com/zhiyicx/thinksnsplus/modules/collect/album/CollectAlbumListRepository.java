package com.zhiyicx.thinksnsplus.modules.collect.album;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.data.beans.MusicAlbumListBean;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseMusicRepository;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author LiuChao
 * @describe
 * @date 2017/4/13
 * @contact email:450127106@qq.com
 */

public class CollectAlbumListRepository extends BaseMusicRepository {
    @Inject
    public CollectAlbumListRepository(ServiceManager serviceManager) {
        super(serviceManager);
    }

    @Override
    public Observable<List<MusicAlbumListBean>> getMusicAblumList(long max_id) {
        return mMusicClient.getCollectMusicList(max_id, (long) TSListFragment.DEFAULT_PAGE_SIZE);
    }

    @Override
    public List<MusicAlbumListBean> getMusicAlbumFromCache(long maxId) {
        return mMusicAlbumListDao.getMyCollectAlbum();
    }
}
