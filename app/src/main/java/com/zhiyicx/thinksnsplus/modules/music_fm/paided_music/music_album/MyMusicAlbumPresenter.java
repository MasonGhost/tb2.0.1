package com.zhiyicx.thinksnsplus.modules.music_fm.paided_music.music_album;

import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.MusicAlbumListBean;

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
        });
    }

    @Override
    public List<MusicAlbumListBean> requestCacheData(Long max_Id, boolean isLoadMore) {
        return null;
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<MusicAlbumListBean> data, boolean isLoadMore) {
        return false;
    }
}
