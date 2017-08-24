package com.zhiyicx.thinksnsplus.modules.music_fm.paided_music.single_music;

import android.support.v7.widget.RecyclerView;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.MusicDetaisBean;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2017/08/24/15:06
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class MySingleMusicListFragment extends TSListFragment<SingleMusicListContract.Presenter, MusicDetaisBean>
        implements SingleMusicListContract.View {

    @Inject
    SingleMusicListPresenter mSingleMusicListPresenter;

    public static MySingleMusicListFragment getInstance() {
        MySingleMusicListFragment mySingleMusicListFragment = new MySingleMusicListFragment();
        return mySingleMusicListFragment;
    }

    @Override
    protected void initData() {
        DaggerSingleMusicLIstComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .singleMusicPresenterModule(new SingleMusicPresenterModule(this))
                .build().inject(this);
        super.initData();
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return null;
    }
}
