package com.zhiyicx.thinksnsplus.modules.chat.edit.manager;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

/**
 * @author Catherine
 * @describe
 * @date 2018/1/23
 * @contact email:648129313@qq.com
 */

public class GroupManagerActivity extends TSActivity<GroupManagerPresenter, GroupManagerFragment>{


    @Override
    protected GroupManagerFragment getFragment() {
        return new GroupManagerFragment().instance(getIntent().getExtras());
    }

    @Override
    protected void componentInject() {
        DaggerGroupManagerComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .groupManagerPresenterModule(new GroupManagerPresenterModule(mContanierFragment))
                .build()
                .inject(this);
    }
}
