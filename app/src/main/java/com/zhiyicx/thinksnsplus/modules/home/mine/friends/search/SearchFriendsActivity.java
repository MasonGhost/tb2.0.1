package com.zhiyicx.thinksnsplus.modules.home.mine.friends.search;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

/**
 * @author Catherine
 * @describe 搜索好友页面
 * @date 2017/12/25
 * @contact email:648129313@qq.com
 */

public class SearchFriendsActivity extends TSActivity<SearchFriendsPresenter, SearchFriendsFragment>{


    @Override
    protected SearchFriendsFragment getFragment() {
        return new SearchFriendsFragment();
    }

    @Override
    protected void componentInject() {
        DaggerSearchFriendsComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .searchFriendsPresenterModule(new SearchFriendsPresenterModule(mContanierFragment))
                .build()
                .inject(this);
    }
}
