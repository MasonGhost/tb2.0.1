package com.zhiyicx.thinksnsplus.modules.circle.mine;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

/**
 * @author Catherine
 * @describe 我的圈子列表
 * @date 2017/8/25
 * @contact email:648129313@qq.com
 */

public class MyGroupActivity extends TSActivity<MyGroupPresenter, MyGroupFragment>{
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
