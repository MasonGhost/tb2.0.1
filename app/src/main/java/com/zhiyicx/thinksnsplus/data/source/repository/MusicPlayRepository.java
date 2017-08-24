package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.music_fm.music_play.MusicPlayContract;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2017/02/13
 * @Email Jliuer@aliyun.com
 * @Description
 */

public class MusicPlayRepository extends BaseMusicRepository implements MusicPlayContract.Repository {

    @Inject
    public MusicPlayRepository(ServiceManager serviceManager) {
        super(serviceManager);
    }
}
