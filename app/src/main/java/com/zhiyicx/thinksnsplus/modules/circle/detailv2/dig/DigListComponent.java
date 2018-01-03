package com.zhiyicx.thinksnsplus.modules.circle.detailv2.dig;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @author Jliuer
 * @Date 17/12/11 15:59
 * @Email Jliuer@aliyun.com
 * @Description
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = DigListPresenterModule.class)
public interface DigListComponent extends InjectComponent<DigListActivity> {
}
