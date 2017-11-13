package com.zhiyicx.thinksnsplus.modules.music_fm.music_play;

import android.content.Intent;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.baseproject.impl.share.ShareModule;
import com.zhiyicx.baseproject.impl.share.UmengSharePolicyImpl;
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
                .shareModule(new ShareModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UmengSharePolicyImpl.onActivityResult(requestCode, resultCode, data, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UmengSharePolicyImpl.onDestroy(this);
    }
}
