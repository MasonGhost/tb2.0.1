package com.zhiyicx.thinksnsplus.modules.dynamic.topdynamic;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @Author Jliuer
 * @Date 2017/05/23/13:48
 * @Email Jliuer@aliyun.com
 * @Description
 */
@FragmentScoped
@Component(modules = DynamicTopPresenterModule.class, dependencies = AppComponent.class)
public interface DynamicTopComponent extends InjectComponent<DynamicTopActivity> {
}
