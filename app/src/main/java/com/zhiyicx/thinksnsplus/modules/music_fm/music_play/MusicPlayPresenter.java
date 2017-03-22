package com.zhiyicx.thinksnsplus.modules.music_fm.music_play;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.common.thridmanager.share.ShareContent;
import com.zhiyicx.common.thridmanager.share.SharePolicy;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.base.BaseSubscribe;
import com.zhiyicx.thinksnsplus.data.source.repository.MusicPlayRepository;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2017/02/14
 * @Email Jliuer@aliyun.com
 * @Description
 */
@FragmentScoped
public class MusicPlayPresenter extends BasePresenter<MusicPlayContract.Repository,
        MusicPlayContract.View> implements MusicPlayContract.Presenter {

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

    @Inject
    public SharePolicy mSharePolicy;

    @Override
    public void shareMusic() {
        ShareContent shareContent = new ShareContent();
        mSharePolicy.setShareContent(shareContent);
        mSharePolicy.showShare(((TSFragment) mRootView).getActivity());
    }

    @Override
    public void handleLike(boolean isLiked, String music_id) {
        mRepository.handleLike(isLiked,music_id);
    }
}
