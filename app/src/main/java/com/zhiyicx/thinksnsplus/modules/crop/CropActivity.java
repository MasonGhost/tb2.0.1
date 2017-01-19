package com.zhiyicx.thinksnsplus.modules.crop;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zhiyicx.baseproject.base.TSActivity;

public class CropActivity extends TSActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void componentInject() {

    }

    @Override
    protected Fragment getFragment() {
        return CropFragment.initFragment(getIntent().getExtras());
    }
}
