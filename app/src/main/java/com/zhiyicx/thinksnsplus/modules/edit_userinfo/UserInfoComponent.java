package com.zhiyicx.thinksnsplus.modules.edit_userinfo;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @author LiuChao
 * @describe
 * @date 2017/1/9
 * @contact email:450127106@qq.com
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = UserInfoPresenterModule.class)
public interface UserInfoComponent extends InjectComponent<UserInfoActivity>{
}
