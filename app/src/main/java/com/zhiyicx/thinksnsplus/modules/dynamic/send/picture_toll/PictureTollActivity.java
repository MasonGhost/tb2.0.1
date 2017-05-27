package com.zhiyicx.thinksnsplus.modules.dynamic.send.picture_toll;

import android.support.v4.app.Fragment;

import com.zhiyicx.baseproject.base.TSActivity;

import static com.zhiyicx.thinksnsplus.modules.photopicker.PhotoViewFragment.OLDTOLL;

public class PictureTollActivity extends TSActivity {

    @Override
    protected Fragment getFragment() {
        return PictureTollFragment.newInstance(getIntent().getBundleExtra(OLDTOLL));
    }

    @Override
    protected void componentInject() {

    }
}
