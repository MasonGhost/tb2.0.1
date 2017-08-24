package com.zhiyicx.thinksnsplus.modules.music_fm.paided_music.single_music;

import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.MusicDetaisBean;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2017/08/24/17:04
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class SingleMusicListPresenter extends AppBasePresenter<SingleMusicListContract.Repository,SingleMusicListContract.View>
        implements SingleMusicListContract.Presenter {

    @Inject
    public SingleMusicListPresenter(SingleMusicListContract.Repository repository, SingleMusicListContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        mRepository.getMyPaidsMusicList(maxId).subscribe(new BaseSubscribeForV2<List<MusicDetaisBean>>() {
            @Override
            protected void onSuccess(List<MusicDetaisBean> data) {
                mRootView.onNetResponseSuccess(data,isLoadMore);
            }
        });
    }

    @Override
    public List<MusicDetaisBean> requestCacheData(Long max_Id, boolean isLoadMore) {
        return null;
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<MusicDetaisBean> data, boolean isLoadMore) {
        return false;
    }
}
