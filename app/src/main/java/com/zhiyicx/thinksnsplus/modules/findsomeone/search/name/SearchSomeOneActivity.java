package com.zhiyicx.thinksnsplus.modules.findsomeone.search.name;


import android.content.Intent;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

/**
 * @Describe  用户搜索
 * @Author Jungle68
 * @Date 2017/8/7
 * @Contact master.jungle68@gmail.com
 */
public class SearchSomeOneActivity extends TSActivity<SearchSomeOnePresenter, SearchSomeOneFragment> {

    @Override
    protected SearchSomeOneFragment getFragment() {
        return SearchSomeOneFragment.newInstance(getIntent().getExtras());
    }

    @Override
    protected void componentInject() {
        DaggerSearchSomeOneComponent
                .builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .searchSomeOnePresenterModule(new SearchSomeOnePresenterModule(mContanierFragment))
                .build().inject(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mContanierFragment.onActivityResult(requestCode,resultCode,data);
    }
}
