package com.zhiyicx.thinksnsplus.modules.circle.main;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @author Jliuer
 * @Date 2017/11/14/11:33
 * @Email Jliuer@aliyun.com
 * @Description
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = CircleMainPresenterModule.class)
public interface CircleMainPresenterComponent extends InjectComponent<CircleMainActivity> {
}
