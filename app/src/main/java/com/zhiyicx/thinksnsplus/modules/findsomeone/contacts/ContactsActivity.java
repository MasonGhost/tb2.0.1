package com.zhiyicx.thinksnsplus.modules.findsomeone.contacts;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.modules.usertag.DaggerEditUserTagComponent;
import com.zhiyicx.thinksnsplus.modules.usertag.EditUserTagFragment;
import com.zhiyicx.thinksnsplus.modules.usertag.EditUserTagPresenter;
import com.zhiyicx.thinksnsplus.modules.usertag.EditUserTagPresenterModule;

/**
 * @Describe 用户标签
 * @Author Jungle68
 * @Date 2017/7/29
 * @Contact master.jungle68@gmail.com
 */

public class ContactsActivity extends TSActivity<ContactsPresenter, ContactsFragment> {

    @Override
    protected void componentInject() {
        DaggerContactsComponent
                .builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .contactsPresenterModule(new ContactsPresenterModule(mContanierFragment))
                .build()
                .inject(this);

    }

    @Override
    protected ContactsFragment getFragment() {
        return ContactsFragment.newInstance(getIntent().getExtras());
    }

}
