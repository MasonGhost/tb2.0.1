package com.zhiyicx.thinksnsplus.modules.music_fm.music_comment;

import android.support.v4.app.Fragment;

import com.zhiyicx.baseproject.base.TSActivity;

public class MusicCommentActivity extends TSActivity {

    @Override
    protected Fragment getFragment() {
        return MusicCommentFragment.newInstance(null);
    }

    @Override
    protected void componentInject() {

    }
}
