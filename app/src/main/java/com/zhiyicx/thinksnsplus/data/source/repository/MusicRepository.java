package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.beans.MusicAlbumListBean;
import com.zhiyicx.thinksnsplus.data.source.remote.MusicClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.music_fm.music_album.MusicContract;

import java.util.Map;

import javax.inject.Inject;

import rx.Observable;

/**
 * @Author Jliuer
 * @Date 2017/02/13
 * @Email Jliuer@aliyun.com
 * @Description
 */

public class MusicRepository implements MusicContract.Repository {
    MusicClient mMusicClient;

    @Inject
    public MusicRepository(ServiceManager serviceManager) {
        mMusicClient = serviceManager.getMusicClient();
    }

    @Override
    public Observable<BaseJson<MusicAlbumListBean>> getMusicList(Map map) {
        return mMusicClient.getMusicList(map);
    }
}
