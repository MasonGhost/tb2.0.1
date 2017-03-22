package com.zhiyicx.thinksnsplus.modules.music_fm.music_comment;


import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

import static com.zhiyicx.thinksnsplus.modules.music_fm.music_comment.MusicCommentFragment
        .CURRENT_MUSIC;

public class MusicCommentActivity extends TSActivity<MusicCommentPresenter,MusicCommentFragment> {

    @Override
    protected MusicCommentFragment getFragment() {
        return MusicCommentFragment.newInstance(getIntent().getBundleExtra(CURRENT_MUSIC));
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
