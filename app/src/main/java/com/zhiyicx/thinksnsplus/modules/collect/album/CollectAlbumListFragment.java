package com.zhiyicx.thinksnsplus.modules.collect.album;

import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.MusicAlbumListBean;
import com.zhiyicx.thinksnsplus.modules.music_fm.music_album_list.MusicListFragment;
import com.zhiyicx.thinksnsplus.modules.music_fm.music_album_list.MusicPresenter;
import com.zhiyicx.thinksnsplus.modules.music_fm.music_album_list.MusicPresenterModule;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * @author LiuChao
 * @describe
 * @date 2017/4/13
 * @contact email:450127106@qq.com
 */

public class CollectAlbumListFragment extends MusicListFragment {

    @Inject
    MusicPresenter mMusicPresenter;

    private MusicAlbumListBean mMusicAlbumListBean;

    @Override
    protected boolean setUseStatusView() {
        return false;
    }

    @Override
    protected void initData() {
        DaggerCollectAlbumListPresenterComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .collectAlbumPresenterModule(new CollectAlbumPresenterModule(this))
                .build().inject(this);
        super.initData();
    }

    @Override
    public void onCacheResponseSuccess(@NotNull List<MusicAlbumListBean> data, boolean isLoadMore) {
        List<MusicAlbumListBean> dealData = new ArrayList<>();
        for (MusicAlbumListBean albumListBean : data) {
            if (albumListBean.getHas_collect()) {
                dealData.add(albumListBean);
            }
        }
        super.onCacheResponseSuccess(dealData, isLoadMore);
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected boolean showToolBarDivider() {
        return false;
    }

    @Override
    public boolean isCollection() {
        return true;
    }

    public static CollectAlbumListFragment newInstance() {
        return new CollectAlbumListFragment();
    }

    @Override
    public void onCollectCountUpdate(MusicAlbumListBean e_albumListBean) {
        mMusicAlbumListBean = e_albumListBean;
        Observable.from(mListDatas).filter(albumListBean -> mMusicAlbumListBean.getId() == albumListBean.getId()).subscribe(albumListBean_same -> {
            albumListBean_same.setCollect_count(mMusicAlbumListBean.getCollect_count());
            albumListBean_same.setShare_count(mMusicAlbumListBean.getShare_count());
            albumListBean_same.setHas_collect(mMusicAlbumListBean.getHas_collect());
            albumListBean_same.setComment_count(mMusicAlbumListBean.getComment_count());
            albumListBean_same.setTaste_count(mMusicAlbumListBean.getTaste_count());
            mPresenter.updateOneMusic(albumListBean_same);

            if (albumListBean_same.getHas_collect()) {
                if (!mListDatas.contains(albumListBean_same)) {
                    mListDatas.add(albumListBean_same);
                }
            } else {
                mListDatas.remove(albumListBean_same);
            }
            if (mListDatas.isEmpty()) {
                mRvList.setBackground(null);
            }
            refreshData();
        });
        LogUtils.d("CollectAlbumListFragment-->EVENT_ABLUM_COLLECT");
    }

}
