package com.zhiyicx.thinksnsplus.modules.music_fm.music_play;

import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.thinksnsplus.data.source.repository.MusicDetailRepository;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2017/02/14
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class MusicPlayPresenter extends BasePresenter<MusicPlayContract.Repository,
        MusicPlayContract.View> implements MusicPlayContract.Presenter {

    @Inject
    MusicDetailRepository mMusicDetailRepository;

    @Inject
    public MusicPlayPresenter(MusicPlayContract.Repository repository, MusicPlayContract
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
}
