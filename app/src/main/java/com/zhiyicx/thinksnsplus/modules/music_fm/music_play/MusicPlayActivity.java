package com.zhiyicx.thinksnsplus.modules.music_fm.music_play;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

import static com.zhiyicx.thinksnsplus.modules.music_fm.music_album_detail.MusicDetailFragment
        .MUSIC_INFO;

public class MusicPlayActivity extends TSActivity<MusicPlayPresenter, MusicPlayFragment> {

    @Override
    protected MusicPlayFragment getFragment() {
        return MusicPlayFragment.newInstance(getIntent().getBundleExtra(MUSIC_INFO));
    }

    @Override
    protected void componentInject() {
        DaggerMusicPlayComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .musicPlayPresenterModule(new MusicPlayPresenterModule(mContanierFragment))
                .build()
                .inject(this);
    }
}
