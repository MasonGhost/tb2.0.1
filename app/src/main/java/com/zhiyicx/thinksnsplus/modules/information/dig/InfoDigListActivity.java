package com.zhiyicx.thinksnsplus.modules.information.dig;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/11
 * @contact email:648129313@qq.com
 */

public class InfoDigListActivity extends TSActivity<InfoDigListPresenter, InfoDigListFragment>{

    public static final String BUNDLE_INFO_DIG = "bundle_info_dig";

    @Override
    protected InfoDigListFragment getFragment() {
        return new InfoDigListFragment().instance(getIntent().getBundleExtra(BUNDLE_INFO_DIG));
    }

    @Override
    protected void componentInject() {
        DaggerInfoDigListComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .infoDigListPresenterModule(new InfoDigListPresenterModule(mContanierFragment))
                .build()
                .inject(this);
    }
}
