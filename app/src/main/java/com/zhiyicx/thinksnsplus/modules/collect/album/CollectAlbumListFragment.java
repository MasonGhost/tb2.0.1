package com.zhiyicx.thinksnsplus.modules.collect.album;

import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.MusicAlbumListBean;
import com.zhiyicx.thinksnsplus.modules.music_fm.music_album_list.MusicListFragment;
import com.zhiyicx.thinksnsplus.modules.music_fm.music_album_list.MusicPresenter;
import com.zhiyicx.thinksnsplus.modules.music_fm.music_album_list.MusicPresenterModule;

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
    protected void initData() {
       /* DaggerCollectAlbumListPresenterComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .musicPresenterModule(new MusicPresenterModule(this))
                .build().inject(this);*/
        DaggerCollectAlbumListPresenterComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .collectAlbumPresenterModule(new CollectAlbumPresenterModule(this))
                .build().inject(this);
        super.initData();
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected boolean showToolBarDivider() {
        return false;
    }

    public static CollectAlbumListFragment newInstance() {
        CollectAlbumListFragment collectAlbumListFragment = new CollectAlbumListFragment();
        return collectAlbumListFragment;
    }

    @Override
    public void onCollectCountUpdate(MusicAlbumListBean e_albumListBean) {
        mMusicAlbumListBean = e_albumListBean;
        Observable.from(mListDatas).filter(new Func1<MusicAlbumListBean, Boolean>() {
            @Override
            public Boolean call(MusicAlbumListBean albumListBean) {
                return mMusicAlbumListBean.getId() == albumListBean.getId();
            }
        }).subscribe(new Action1<MusicAlbumListBean>() {
            @Override
            public void call(MusicAlbumListBean albumListBean_same) {
                albumListBean_same.setCollect_count(mMusicAlbumListBean.getCollect_count());
                albumListBean_same.setShare_count(mMusicAlbumListBean.getShare_count());
                albumListBean_same.setIs_collection(mMusicAlbumListBean.getIs_collection());
                albumListBean_same.setComment_count(mMusicAlbumListBean.getComment_count());
                albumListBean_same.setTaste_count(mMusicAlbumListBean.getTaste_count());
                mPresenter.updateOneMusic(albumListBean_same);
                // 为什么收藏是0不是1吗，问Jliuer@aliyun.com
                // 回答：因为是本地的收藏状态改变，如果操作前是 1 ，操作手动修改为 0
                if (albumListBean_same.getIs_collection() == 0) {
                    if (!mListDatas.contains(albumListBean_same)) {
                        mListDatas.add(albumListBean_same);
                    }
                } else {
                    mListDatas.remove(albumListBean_same);
                }
               refreshData();
            }
        });
        LogUtils.d("CollectAlbumListFragment-->EVENT_ABLUM_COLLECT");
    }
}
