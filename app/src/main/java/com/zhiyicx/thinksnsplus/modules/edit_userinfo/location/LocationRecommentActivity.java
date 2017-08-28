package com.zhiyicx.thinksnsplus.modules.edit_userinfo.location;

import android.content.Intent;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

/**
 * @Describe 推荐位置
 * @Author Jungle68
 * @Date 2017/8/14
 * @Contact master.jungle68@gmail.com
 */
public class LocationRecommentActivity extends TSActivity<LocationRecommentPresenter, LocationRecommentFragment> {


    @Override
    protected void componentInject() {
        DaggerLocationRecommentComponent
                .builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .locationRecommentPresenterModule(new LocationRecommentPresenterModule(mContanierFragment))
                .build().inject(this);
    }

    @Override
    protected LocationRecommentFragment getFragment() {
        return LocationRecommentFragment.newInstance(getIntent().getExtras());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mContanierFragment.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    public void onBackPressed() {
        mContanierFragment.onBackPressed();
    }
}
