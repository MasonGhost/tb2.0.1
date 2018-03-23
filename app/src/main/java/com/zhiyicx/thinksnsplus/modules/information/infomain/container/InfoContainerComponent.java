package com.zhiyicx.thinksnsplus.modules.information.infomain.container;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;
import com.zhiyicx.thinksnsplus.modules.information.infomain.InfoActivity;

import dagger.Component;

/**
 * @Author Jliuer
 * @Date 2017/03/14
 * @Email Jliuer@aliyun.com
 * @Description
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = InfoContainerPresenterModule.class)
public interface InfoContainerComponent extends InjectComponent<InfoContainerFragment> {
}
