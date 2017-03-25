package com.zhiyicx.thinksnsplus.modules.information.infosearch;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

/**
 * @Author Jliuer
 * @Date 2017/03/06
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class SearchActivity extends TSActivity<InfoSearchPresenter, SearchFragment> {
    @Override
    protected SearchFragment getFragment() {
        return new SearchFragment();
    }

    @Override
    protected void componentInject() {
        DaggerInfoSearchComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .infoSearchPresenterMudule(new InfoSearchPresenterMudule(mContanierFragment))
                .build()
                .inject(this);
    }
}
