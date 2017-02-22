package com.zhiyicx.thinksnsplus.modules.music_fm.music_album_detail;

import android.support.v4.media.MediaBrowserCompat;

import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.thinksnsplus.data.source.repository.MusicDetailRepository;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2017/02/14
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class MusicDetailPresenter extends BasePresenter<MusicDetailContract.Repository,
        MusicDetailContract.View> implements MusicDetailContract.Presenter {

    @Inject
    MusicDetailRepository mMusicDetailRepository;

    @Inject
    public MusicDetailPresenter(MusicDetailContract.Repository repository, MusicDetailContract
            .View rootView) {
        super(repository, rootView);
    }

    /**
     * 将Presenter从传入fragment
     */
    @Inject
    void setupListeners() {
        mRootView.setPresenter(this);
    }

    @Override
    public void getMediaList() {
        mRootView.setMediaList(null);
    }
}
