package com.zhiyicx.thinksnsplus.modules.music_fm.music_detail;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

public class MusicDetailActivity extends TSActivity<MusicDetailPresenter, MusicDetailFragment> {

    @Override
    protected MusicDetailFragment getFragment() {
        return new MusicDetailFragment();
    }

    @Override
    protected void componentInject() {
        DaggerMusicDetailComponent.builder().appComponent(AppApplication.AppComponentHolder
                .getAppComponent()).musicDetailPresenterModule(new MusicDetailPresenterModule
                (mContanierFragment)).build();
    }
}
