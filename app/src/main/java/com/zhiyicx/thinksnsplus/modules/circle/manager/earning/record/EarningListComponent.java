package com.zhiyicx.thinksnsplus.modules.circle.manager.earning.record;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @Author Jliuer
 * @Date 2017/06/05/10:12
 * @Email Jliuer@aliyun.com
 * @Description
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = EarningListPresenterModule.class)
public interface EarningListComponent extends InjectComponent<EarningListActivity> {
}
