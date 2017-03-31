package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.beans.MusicCommentListBean;
import com.zhiyicx.thinksnsplus.data.source.remote.MusicClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.music_fm.music_comment.MusicCommentContract;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * @Author Jliuer
 * @Date 2017/03/22
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class MusicCommentRepositroty implements MusicCommentContract.Repository {

    MusicClient mMusicClient;

    @Inject
    public MusicCommentRepositroty(ServiceManager serviceManager) {
        mMusicClient = serviceManager.getMusicClient();
    }

    @Override
    public Observable<BaseJson<List<MusicCommentListBean>>> getMusicCommentList(String music_id,
                                                                                long max_id) {
        return mMusicClient.getMusicCommentList(music_id,max_id, Long.valueOf(TSListFragment.DEFAULT_PAGE_SIZE));
    }
}
