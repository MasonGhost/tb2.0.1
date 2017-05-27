package com.zhiyicx.thinksnsplus.modules.dynamic.send.picture_toll;


import com.zhiyicx.baseproject.base.TSActivity;

import static com.zhiyicx.thinksnsplus.modules.photopicker.PhotoViewFragment.OLDTOLL;

public class PictureTollActivity extends TSActivity {

    @Override
    protected PictureTollFragment getFragment() {
        return PictureTollFragment.newInstance(getIntent().getBundleExtra(OLDTOLL));
    }

    @Override
    protected void componentInject() {

    }

    @Override
    public void onBackPressed() {
        PictureTollFragment fragment = (PictureTollFragment) mContanierFragment;
        fragment.onBackPressed();
    }
}
