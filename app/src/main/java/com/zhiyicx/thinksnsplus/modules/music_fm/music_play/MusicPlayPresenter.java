package com.zhiyicx.thinksnsplus.modules.music_fm.music_play;

import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribe;
import com.zhiyicx.thinksnsplus.data.source.repository.MusicPlayRepository;

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
    MusicPlayRepository mMusicPlayRepository;

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

    @Override
    public void digMusic(String music_id){
        mMusicPlayRepository.doDigg(music_id)
                .compose(mSchedulersTransformer)
                .subscribe(new BaseSubscribe<Integer>() {
                    @Override
                    protected void onSuccess(Integer data) {
                        mRootView.digMusic(true);
                    }

                    @Override
                    protected void onFailure(String message) {
                        mRootView.digMusic(false);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        mRootView.digMusic(false);
                    }
                });
    }

    @Override
    public void cancleDigMusic(String music_id){
        mMusicPlayRepository.cancleDigg(music_id).compose(mSchedulersTransformer)
                .subscribe(new BaseSubscribe<Integer>() {
                    @Override
                    protected void onSuccess(Integer data) {
                        mRootView.cancleDigMusic(true);
                    }

                    @Override
                    protected void onFailure(String message) {
                        mRootView.cancleDigMusic(false);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        mRootView.cancleDigMusic(false);
                    }
                });
    }
}
