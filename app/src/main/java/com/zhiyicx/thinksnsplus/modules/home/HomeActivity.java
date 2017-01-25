package com.zhiyicx.thinksnsplus.modules.home;

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
    protected void componentInject() {
//       DaggerRegisterComponent
//               .builder()
//               .appComponent(AppApplication.AppComponentHolder.getAppComponent())
//               .registerPresenterModule(new RegisterPresenterModule((RegisterContract.View) mContanierFragment))
//               .build()
//               .inject(this);

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
