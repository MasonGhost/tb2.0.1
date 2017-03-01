package com.zhiyicx.thinksnsplus.modules.music_fm.music_play;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

public class MusicPlayActivity extends TSActivity<MusicPlayPresenter, MusicPlayFragment> {

    private MusicPlayFragment mMusicPlayFragment;

    @Override
    protected MusicPlayFragment getFragment() {
        mMusicPlayFragment = new MusicPlayFragment();
        return mMusicPlayFragment;
    }

    @Override
    protected void componentInject() {
        DaggerMusicPlayComponent.builder().appComponent(AppApplication.AppComponentHolder
                .getAppComponent()).musicPlayPresenterModule(new MusicPlayPresenterModule
                (mContanierFragment)).build();
    }
}
