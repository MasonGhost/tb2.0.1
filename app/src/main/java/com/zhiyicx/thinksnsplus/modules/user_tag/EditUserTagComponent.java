package com.zhiyicx.thinksnsplus.modules.user_tag;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/7/28
 * @Contact master.jungle68@gmail.com
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = EditUserTagPresenterModule.class)
public interface EditUserTagComponent extends InjectComponent<EditUserTagActivity> {
}
