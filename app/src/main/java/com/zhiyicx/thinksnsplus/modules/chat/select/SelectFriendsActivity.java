package com.zhiyicx.thinksnsplus.modules.chat.select;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

/**
 * @author Catherine
 * @describe 选择好友创建会话页面
 * @date 2018/1/11
 * @contact email:648129313@qq.com
 */

public class SelectFriendsActivity extends TSActivity<SelectFriendsPresenter, SelectFriendsFragment>{


    @Override
    protected SelectFriendsFragment getFragment() {
        return new SelectFriendsFragment();
    }

    @Override
    protected void componentInject() {
        DaggerSelectFriendsComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .selectFriendsPresenterModule(new SelectFriendsPresenterModule(mContanierFragment))
                .build()
                .inject(this);
    }
}
