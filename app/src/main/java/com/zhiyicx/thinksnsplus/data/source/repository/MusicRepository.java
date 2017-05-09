package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.MusicAlbumListBean;
import com.zhiyicx.thinksnsplus.data.source.local.MusicAlbumListBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.remote.MusicClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.music_fm.music_album_list.MusicContract;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * @Author Jliuer
 * @Date 2017/02/13
 * @Email Jliuer@aliyun.com
 * @Description
 */

public class MusicRepository implements MusicContract.Repository {
    protected MusicClient mMusicClient;
    protected MusicAlbumListBeanGreenDaoImpl mMusicAlbumListDao;

    @Inject
    public MusicRepository(ServiceManager serviceManager) {
        mMusicClient = serviceManager.getMusicClient();
        mMusicAlbumListDao = AppApplication.AppComponentHolder.getAppComponent().musicAlbumListBeanGreenDaoImpl();
    }

    @Override
    public Observable<BaseJson<List<MusicAlbumListBean>>> getMusicAblumList(long max_id) {
        return mMusicClient.getMusicList(max_id, Long.valueOf(TSListFragment.DEFAULT_PAGE_SIZE));
    }

    public List<MusicAlbumListBean> getMusicAlbumFromCache(long maxId) {
        return mMusicAlbumListDao.getMultiDataFromCache();
    }
}
