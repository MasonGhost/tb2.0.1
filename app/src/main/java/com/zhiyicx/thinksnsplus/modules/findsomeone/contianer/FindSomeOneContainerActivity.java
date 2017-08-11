package com.zhiyicx.thinksnsplus.modules.findsomeone.contianer;

import android.os.Bundle;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.modules.findsomeone.list.FindSomeOneListPresenter;
import com.zhiyicx.thinksnsplus.modules.follow_fans.FollowFansListPresenter;

public class FindSomeOneContainerActivity extends TSActivity<FindSomeOneListPresenter, FindSomeOneContainerFragment> {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void componentInject() {
    }

    @Override
    protected FindSomeOneContainerFragment getFragment() {
        return FindSomeOneContainerFragment.newInstance(getIntent().getExtras());
    }
}
