package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.beans.MusicAlbumListBean;
import com.zhiyicx.thinksnsplus.data.source.remote.MusicClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.music_fm.music_play.MusicPlayContract;

import java.util.Map;

import javax.inject.Inject;

import rx.Observable;

/**
 * @Author Jliuer
 * @Date 2017/02/13
 * @Email Jliuer@aliyun.com
 * @Description
 */

public class MusicPlayRepository implements MusicPlayContract.Repository {

    MusicClient mMusicClient;

    @Inject
    public MusicPlayRepository(ServiceManager serviceManager) {
        mMusicClient = serviceManager.getMusicClient();
    }

    @Override
    public Observable<BaseJson<Integer>> doDigg(String music_id) {
        return mMusicClient.doDigg(music_id);
    }

    @Override
    public Observable<BaseJson<Integer>> cancleDigg(String music_id) {
        return mMusicClient.cancleDigg(music_id);
    }
}
