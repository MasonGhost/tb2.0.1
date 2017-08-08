package com.zhiyicx.thinksnsplus.modules.edit_userinfo.location.search;


import android.content.Intent;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.modules.information.publish.addinfo.AddInfoCategoryFragment;
import com.zhiyicx.thinksnsplus.modules.information.publish.addinfo.AddInfoPresenter;
import com.zhiyicx.thinksnsplus.modules.information.publish.addinfo.AddInfoPresenterModule;
import com.zhiyicx.thinksnsplus.modules.information.publish.addinfo.DaggerAddInfoComponent;

/**
 * @Describe  地区搜索
 * @Author Jungle68
 * @Date 2017/8/7
 * @Contact master.jungle68@gmail.com
 */
public class LocationSearchActivity extends TSActivity<AddInfoPresenter, LocationSearchFragment> {

    @Override
    protected LocationSearchFragment getFragment() {
        return LocationSearchFragment.newInstance(getIntent().getExtras());
    }

    @Override
    protected void componentInject() {
        DaggerLocationSearchComponent
                .builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .locationSearchPresenterModule(new LocationSearchPresenterModule(mContanierFragment))
                .build().inject(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mContanierFragment.onActivityResult(requestCode,resultCode,data);
    }
}
