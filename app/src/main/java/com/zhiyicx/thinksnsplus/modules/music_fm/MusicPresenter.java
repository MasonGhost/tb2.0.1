package com.zhiyicx.thinksnsplus.modules.music_fm;

import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.thinksnsplus.data.source.repository.MusicRepository;

import java.util.List;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2017/02/13
 * @Email Jliuer@aliyun.com
 * @Description
 */

public class MusicPresenter extends BasePresenter<MusicContract.Repository, MusicContract.View>
        implements MusicContract.Presenter {

    @Inject
    MusicRepository mMusicRepository;

    @Inject
    public MusicPresenter(MusicContract.Repository repository, MusicContract.View rootView) {
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
    public void getMusicList() {

    }

    @Override
    public void requestNetData(int maxId, boolean isLoadMore) {

    }

    @Override
    public List requestCacheData(int maxId, boolean isLoadMore) {
        return null;
    }
}
