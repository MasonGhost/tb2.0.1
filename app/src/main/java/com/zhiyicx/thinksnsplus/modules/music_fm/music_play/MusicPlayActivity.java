package com.zhiyicx.thinksnsplus.modules.music_fm.music_play;

import android.os.Bundle;
import android.os.PersistableBundle;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

public class MusicPlayActivity extends TSActivity<MusicPlayPresenter, MusicPlayFragment> {

    private MusicPlayFragment mMusicPlayFragment;
    private Bundle save;

    @Override
    protected void restoreData(Bundle savedInstanceState) {
        super.restoreData(savedInstanceState);
        save = savedInstanceState;
    }

    @Override
    protected MusicPlayFragment getFragment() {
        mMusicPlayFragment = new MusicPlayFragment();
        if (save == null) {
            save = getIntent().getExtras();
        }
        mMusicPlayFragment.setArguments(save);
        return mMusicPlayFragment;
    }

    @Override
    protected void componentInject() {
        DaggerMusicPlayComponent.builder().appComponent(AppApplication.AppComponentHolder
                .getAppComponent()).musicPlayPresenterModule(new MusicPlayPresenterModule
                (mContanierFragment)).build();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState = save;
    }
}
