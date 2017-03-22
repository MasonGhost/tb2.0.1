package com.zhiyicx.thinksnsplus.modules.music_fm.music_comment;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribe;
import com.zhiyicx.thinksnsplus.data.beans.MusicCommentListBean;
import com.zhiyicx.thinksnsplus.data.source.repository.MusicCommentRepositroty;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;

/**
 * @Author Jliuer
 * @Date 2017/03/22
 * @Email Jliuer@aliyun.com
 * @Description
 */
@FragmentScoped
public class MusicCommentPresenter extends BasePresenter<MusicCommentContract.Repository,
        MusicCommentContract.View> implements MusicCommentContract.Presenter {

    @Inject
    MusicCommentRepositroty mMusicCommentRepositroty;

    @Inject
    public MusicCommentPresenter(MusicCommentContract.Repository repository, MusicCommentContract
            .View rootView) {
        super(repository, rootView);
    }

    @Inject
    void setupListeners() {
        mRootView.setPresenter(this);
    }

    @Override
    public void requestNetData(String music_id, Long maxId,final boolean isLoadMore) {
        Subscription subscription =mMusicCommentRepositroty.getMusicCommentList(music_id,maxId)
                .compose(mSchedulersTransformer)
                .subscribe(new BaseSubscribe<List<MusicCommentListBean>>() {
                    @Override
                    protected void onSuccess(List<MusicCommentListBean> data) {
                        mRootView.onNetResponseSuccess(data, isLoadMore);
                    }

                    @Override
                    protected void onFailure(String message) {
                        mRootView.showMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        mRootView.onResponseError(throwable,isLoadMore);
                    }
                });

        addSubscrebe(subscription);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public List<MusicCommentListBean> requestCacheData(Long max_Id, boolean isLoadMore) {
        return new ArrayList();
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<MusicCommentListBean> data) {
        return false;
    }

}
