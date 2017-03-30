package com.zhiyicx.thinksnsplus.modules.personal_center;

import com.zhiyicx.baseproject.impl.share.ShareModule;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @author LiuChao
 * @describe
 * @date 2017/3/7
 * @contact email:450127106@qq.com
 */
@FragmentScoped
@Component(modules = {PersonalCenterPresenterModule.class, ShareModule.class}, dependencies = AppComponent.class)
public interface PersonalCenterPresenterComponent extends InjectComponent<PersonalCenterActivity> {
}
