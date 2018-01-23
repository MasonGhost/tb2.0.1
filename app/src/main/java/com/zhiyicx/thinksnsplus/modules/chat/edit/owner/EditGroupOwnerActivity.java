package com.zhiyicx.thinksnsplus.modules.chat.edit.owner;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

/**
 * Created by Catherine on 2018/1/22.
 */

public class EditGroupOwnerActivity extends TSActivity<EditGroupOwnerPresenter, EditGroupOwnerFragment>{

    @Override
    protected EditGroupOwnerFragment getFragment() {
        return new EditGroupOwnerFragment().instance(getIntent().getExtras());
    }

    @Override
    protected void componentInject() {
        DaggerEditGroupOwnerComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .editGroupOwnerPresenterModule(new EditGroupOwnerPresenterModule(mContanierFragment))
                .build()
                .inject(this);
    }
}
