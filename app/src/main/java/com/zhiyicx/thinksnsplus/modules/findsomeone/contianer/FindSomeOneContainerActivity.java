package com.zhiyicx.thinksnsplus.modules.findsomeone.contianer;

import android.content.Intent;
import android.os.Bundle;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.modules.findsomeone.list.FindSomeOneListPresenter;
import com.zhiyicx.thinksnsplus.modules.findsomeone.search.name.DaggerSearchSomeOneComponent;
import com.zhiyicx.thinksnsplus.modules.findsomeone.search.name.SearchSomeOnePresenterModule;
import com.zhiyicx.thinksnsplus.modules.follow_fans.FollowFansListPresenter;

public class FindSomeOneContainerActivity extends TSActivity<FindSomeOneContainerPresenter, FindSomeOneContainerFragment> {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void componentInject() {
        DaggerFindSomeOneContainerComponent
                .builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .findSomeOneContainerPresenterModule(new FindSomeOneContainerPresenterModule(mContanierFragment))
                .build().inject(this);
    }

    @Override
    protected FindSomeOneContainerFragment getFragment() {
        return FindSomeOneContainerFragment.newInstance(getIntent().getExtras());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mContanierFragment.onActivityResult(requestCode,resultCode,data);
    }
}
