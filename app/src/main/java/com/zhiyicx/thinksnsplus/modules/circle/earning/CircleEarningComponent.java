package com.zhiyicx.thinksnsplus.modules.circle.earning;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @Author Jliuer
 * @Date 2017/12/12/13:51
 * @Email Jliuer@aliyun.com
 * @Description
 */
@FragmentScoped
@Component(dependencies = AppComponent.class,modules = CircleEarningPresenterModule.class)
public interface CircleEarningComponent extends InjectComponent<CircleEarningActivity> {
}
