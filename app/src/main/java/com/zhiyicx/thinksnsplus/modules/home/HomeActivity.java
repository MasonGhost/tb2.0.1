package com.zhiyicx.thinksnsplus.modules.home;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.common.utils.ActivityUtils;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/22
 * @Contact master.jungle68@gmail.com
 */

public class HomeActivity extends TSActivity {
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void componentInject() {
    }

    @Override
    protected Fragment getFragment() {
        return HomeFragment.newInstance();
    }

    @Override
    public void onBackPressed() {
        ActivityUtils.goHome(this);
    }

}
