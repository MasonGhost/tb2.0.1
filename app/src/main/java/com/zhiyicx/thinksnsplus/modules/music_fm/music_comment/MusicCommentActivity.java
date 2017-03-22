package com.zhiyicx.thinksnsplus.modules.music_fm.music_comment;


import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

public class MusicCommentActivity extends TSActivity<MusicCommentPresenter,MusicCommentFragment> {

    @Override
    protected MusicCommentFragment getFragment() {
        return MusicCommentFragment.newInstance(null);
    }

    @Override
    protected void componentInject() {
        DaggerMusicCommentComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .musicCommentPresenterModule(new MusicCommentPresenterModule(mContanierFragment))
                .build()
                .inject(this);
    }
}
