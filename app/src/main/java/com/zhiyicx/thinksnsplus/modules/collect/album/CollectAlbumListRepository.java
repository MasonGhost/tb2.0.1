package com.zhiyicx.thinksnsplus.modules.collect.album;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.beans.MusicAlbumListBean;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.repository.MusicRepository;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author LiuChao
 * @describe
 * @date 2017/4/13
 * @contact email:450127106@qq.com
 */

public class CollectAlbumListRepository extends MusicRepository {
    @Inject
    public CollectAlbumListRepository(ServiceManager serviceManager) {
        super(serviceManager);
    }

    @Override
    public Observable<BaseJson<List<MusicAlbumListBean>>> getMusicAblumList(long max_id) {
        return mMusicClient.getCollectMusicList(max_id, Long.valueOf(TSListFragment.DEFAULT_PAGE_SIZE));
    }

    public List<MusicAlbumListBean> getMusicAlbumFromCache(long maxId) {
        return mMusicAlbumListDao.getMyCollectAlbum();
    }
}
