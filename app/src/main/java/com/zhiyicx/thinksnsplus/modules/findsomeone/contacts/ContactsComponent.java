package com.zhiyicx.thinksnsplus.modules.findsomeone.contacts;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;
import com.zhiyicx.thinksnsplus.modules.usertag.EditUserTagActivity;
import com.zhiyicx.thinksnsplus.modules.usertag.EditUserTagPresenterModule;

import dagger.Component;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/7/28
 * @Contact master.jungle68@gmail.com
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = ContactsPresenterModule.class)
public interface ContactsComponent extends InjectComponent<ContactsActivity> {
}
