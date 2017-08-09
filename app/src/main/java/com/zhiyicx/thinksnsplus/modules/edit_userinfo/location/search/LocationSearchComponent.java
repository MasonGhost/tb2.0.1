package com.zhiyicx.thinksnsplus.modules.edit_userinfo.location.search;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;
import com.zhiyicx.thinksnsplus.modules.information.publish.addinfo.AddInfoActivity;
import com.zhiyicx.thinksnsplus.modules.information.publish.addinfo.AddInfoCategoryActivity;
import com.zhiyicx.thinksnsplus.modules.information.publish.addinfo.AddInfoPresenterModule;

import dagger.Component;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/8/7
 * @Contact master.jungle68@gmail.com
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = LocationSearchPresenterModule.class)
public interface LocationSearchComponent extends InjectComponent<LocationSearchActivity> {

}
