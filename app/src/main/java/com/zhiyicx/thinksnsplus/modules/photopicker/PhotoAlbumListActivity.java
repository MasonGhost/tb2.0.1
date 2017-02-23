package com.zhiyicx.thinksnsplus.modules.photopicker;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.R;

public class PhotoAlbumListActivity extends TSActivity {

    @Override
    protected void componentInject() {

    }

    @Override
    protected Fragment getFragment() {
        return PhotoAlbumListFragment.initFragment(getIntent().getExtras());
    }
}
