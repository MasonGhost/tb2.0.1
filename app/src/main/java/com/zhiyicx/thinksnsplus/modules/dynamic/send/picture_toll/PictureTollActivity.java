package com.zhiyicx.thinksnsplus.modules.dynamic.send.picture_toll;

import android.support.v4.app.Fragment;

import com.zhiyicx.baseproject.base.TSActivity;

public class PictureTollActivity extends TSActivity {

    @Override
    protected Fragment getFragment() {
        return PictureTollFragment.newInstance();
    }

    @Override
    protected void componentInject() {

    }
}
