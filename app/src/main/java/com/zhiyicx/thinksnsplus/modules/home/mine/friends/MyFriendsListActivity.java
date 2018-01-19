package com.zhiyicx.thinksnsplus.modules.home.mine.friends;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

/**
 * @author Catherine
 * @describe 我的好友页面
 * @date 2017/12/22
 * @contact email:648129313@qq.com
 */

public class MyFriendsListActivity extends TSActivity<MyFriendsListPresenter, MyFriendsListFragment>{

    @Override
    protected MyFriendsListFragment getFragment() {
        return new MyFriendsListFragment();
    }

    @Override
    protected void componentInject() {
        DaggerMyFriendsListComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .myFriendsListPresenterModule(new MyFriendsListPresenterModule(mContanierFragment))
                .build()
                .inject(this);
    }
}
