package com.zhiyicx.thinksnsplus.modules.photopicker;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zhiyicx.baseproject.base.TSActivity;

public class PhotoAlbumDetailsActivity extends TSActivity {

    @Override
    protected void componentInject() {

    }

    @Override
    protected Fragment getFragment() {
        return PhotoAlbumDetailsFragment.initFragment(getIntent().getExtras());
    }
}
