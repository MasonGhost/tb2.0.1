package com.zhiyicx.thinksnsplus.modules.chat.edit.owner;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * Created by Catherine on 2018/1/22.
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = EditGroupOwnerPresenterModule.class)
public interface EditGroupOwnerComponent extends InjectComponent<EditGroupOwnerActivity>{
}
