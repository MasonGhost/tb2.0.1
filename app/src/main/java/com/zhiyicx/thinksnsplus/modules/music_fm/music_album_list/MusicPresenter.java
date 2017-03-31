package com.zhiyicx.thinksnsplus.modules.music_fm.music_album_list;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribe;
import com.zhiyicx.thinksnsplus.data.beans.MusicAlbumListBean;
import com.zhiyicx.thinksnsplus.data.source.repository.MusicRepository;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;

/**
 * @Author Jliuer
 * @Date 2017/02/13
 * @Email Jliuer@aliyun.com
 * @Description
 */
@FragmentScoped
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
    public void requestNetData(Long maxId, final boolean isLoadMore) {
        Subscription subscription = mMusicRepository.getMusicAblumList(maxId)
                .compose(mSchedulersTransformer)
                .subscribe(new BaseSubscribe<List<MusicAlbumListBean>>() {
                    @Override
                    protected void onSuccess(List<MusicAlbumListBean> data) {
                        mRootView.onNetResponseSuccess(data, isLoadMore);
                    }

                    @Override
                    protected void onFailure(String message) {
                        mRootView.showMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        mRootView.onResponseError(throwable, isLoadMore);
                    }


                });
        addSubscrebe(subscription);
    }

    @Override
    public List requestCacheData(Long maxId, boolean isLoadMore) {
        return new ArrayList();
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<MusicAlbumListBean> data) {
        return false;
    }
}
