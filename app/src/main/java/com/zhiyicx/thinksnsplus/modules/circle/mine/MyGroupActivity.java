package com.zhiyicx.thinksnsplus.modules.circle.mine;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

/**
 * @Describe 我的圈子列表
 * @Author Jungle68
 * @Date 2017/12/6
 * @Contact master.jungle68@gmail.com
 */
public class MyGroupActivity extends TSActivity<MyGroupPresenter, MyGroupFragment> {
    @Override
    protected MyGroupFragment getFragment() {
        return new MyGroupFragment();
    }

    @Override
    protected void componentInject() {
        DaggerMyGroupComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .myGroupPresenterModule(new MyGroupPresenterModule(mContanierFragment))
                .build()
                .inject(this);
    }
}
