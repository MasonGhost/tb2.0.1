package com.zhiyicx.thinksnsplus.modules.findsomeone.contacts;

import dagger.Module;
import dagger.Provides;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/10
 * @Contact master.jungle68@gmail.com
 */
@Module
public class ContactsPresenterModule {
    private final ContactsContract.View mView;

    public ContactsPresenterModule(ContactsContract.View view) {
        mView = view;
    }

    @Provides
    ContactsContract.View provideView() {
        return mView;
    }

}
