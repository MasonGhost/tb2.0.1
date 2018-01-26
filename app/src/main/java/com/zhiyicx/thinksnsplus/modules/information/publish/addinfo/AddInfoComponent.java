package com.zhiyicx.thinksnsplus.modules.information.publish.addinfo;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/8/7
 * @Contact master.jungle68@gmail.com
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = AddInfoPresenterModule.class)
public interface AddInfoComponent extends InjectComponent<AddInfoActivity> {

    void inject(AddInfoCategoryActivity addInfoCategoryActivity);
}
