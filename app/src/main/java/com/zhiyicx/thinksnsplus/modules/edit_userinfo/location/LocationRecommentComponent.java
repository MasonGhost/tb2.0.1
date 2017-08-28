package com.zhiyicx.thinksnsplus.modules.edit_userinfo.location;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;
import com.zhiyicx.thinksnsplus.modules.edit_userinfo.UserInfoActivity;
import com.zhiyicx.thinksnsplus.modules.edit_userinfo.UserInfoPresenterModule;

import dagger.Component;

/**
 * @Describe 推荐位置
 * @Author Jungle68
 * @Date 2017/8/14
 * @Contact master.jungle68@gmail.com
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = LocationRecommentPresenterModule.class)
public interface LocationRecommentComponent extends InjectComponent<LocationRecommentActivity>{
}
