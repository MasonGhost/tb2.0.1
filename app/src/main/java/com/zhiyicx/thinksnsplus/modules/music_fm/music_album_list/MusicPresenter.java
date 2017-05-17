package com.zhiyicx.thinksnsplus.modules.music_fm.music_album_list;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribe;
import com.zhiyicx.thinksnsplus.data.beans.MusicAlbumListBean;
import com.zhiyicx.thinksnsplus.data.source.local.MusicAlbumListBeanGreenDaoImpl;

import org.jetbrains.annotations.NotNull;

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
public class MusicPresenter extends AppBasePresenter<MusicContract.Repository, MusicContract.View>
        implements MusicContract.Presenter {

    @Inject
    MusicAlbumListBeanGreenDaoImpl mMusicAlbumListDao;

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
        Subscription subscription = mRepository.getMusicAblumList(maxId)
                .compose(mSchedulersTransformer)
                .subscribe(new BaseSubscribe<List<MusicAlbumListBean>>() {
                    @Override
                    protected void onSuccess(List<MusicAlbumListBean> data) {
                        if (data.isEmpty()){
                            mMusicAlbumListDao.saveMultiData(data);
                        }else{
                            mMusicAlbumListDao.clearTable();
                        }

                        mRootView.onNetResponseSuccess(data, isLoadMore);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
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
        return mRepository.getMusicAlbumFromCache(maxId);
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<MusicAlbumListBean> data, boolean isLoadMore) {
        return false;
    }

    @Override
    public void updateOneMusic(MusicAlbumListBean albumListBean){
        mMusicAlbumListDao.updateSingleData(albumListBean);
    }
}
