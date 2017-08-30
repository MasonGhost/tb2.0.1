package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.music_fm.music_album_list.MusicContract;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2017/02/13
 * @Email Jliuer@aliyun.com
 * @Description
 */

public class MusicRepository extends BaseMusicRepository implements MusicContract.Repository {

    @Inject
    public MusicRepository(ServiceManager serviceManager) {
        super(serviceManager);
    }

}
