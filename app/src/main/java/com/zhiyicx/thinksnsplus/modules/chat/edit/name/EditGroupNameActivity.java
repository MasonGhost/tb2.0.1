package com.zhiyicx.thinksnsplus.modules.chat.edit.name;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

/**
 * Created by Catherine on 2018/1/22.
 * @description 修改群名称页面
 */

public class EditGroupNameActivity extends TSActivity<EditGroupNamePresenter, EditGroupNameFragment>{

    @Override
    protected EditGroupNameFragment getFragment() {
        return null;
    }

    @Override
    protected void componentInject() {
        DaggerEditGroupNameComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .editGroupNamePresenterModule(new EditGroupNamePresenterModule(mContanierFragment))
                .build()
                .inject(this);
    }
}
