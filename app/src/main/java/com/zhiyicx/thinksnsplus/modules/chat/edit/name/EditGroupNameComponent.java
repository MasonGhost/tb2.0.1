package com.zhiyicx.thinksnsplus.modules.chat.edit.name;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * Created by Catherine on 2018/1/22.
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = EditGroupNamePresenterModule.class)
public interface EditGroupNameComponent extends InjectComponent<EditGroupNameActivity>{
}
