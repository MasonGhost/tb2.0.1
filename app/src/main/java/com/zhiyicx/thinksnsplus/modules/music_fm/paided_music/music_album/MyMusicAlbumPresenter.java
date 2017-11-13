package com.zhiyicx.thinksnsplus.modules.music_fm.paided_music.music_album;

import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.MusicAlbumListBean;
import com.zhiyicx.thinksnsplus.data.source.local.MusicAlbumListBeanGreenDaoImpl;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2017/08/24/18:01
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class MyMusicAlbumPresenter extends AppBasePresenter<MyMusicAblumListContract.Repository,MyMusicAblumListContract.View>
        implements MyMusicAblumListContract.Presenter {

    @Inject
    MusicAlbumListBeanGreenDaoImpl mMusicAlbumListDao;

    @Inject
    public MyMusicAlbumPresenter(MyMusicAblumListContract.Repository repository, MyMusicAblumListContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        mRepository.getMyPaidsMusicAlbumList(maxId).subscribe(new BaseSubscribeForV2<List<MusicAlbumListBean>>() {
            @Override
            protected void onSuccess(List<MusicAlbumListBean> data) {
                mRootView.onNetResponseSuccess(data,isLoadMore);
            }
            @Override
            protected void onFailure(String message, int code) {
                super.onFailure(message, code);
                mRootView.onResponseError(null,isLoadMore);
            }

            @Override
            protected void onException(Throwable throwable) {
                super.onException(throwable);
                mRootView.onResponseError(throwable,isLoadMore);
            }
        });
    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {
       mRootView.onCacheResponseSuccess(null,isLoadMore);
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
