package com.zhiyicx.thinksnsplus.modules.wallet.integration.withdrawal;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;
import com.zhiyicx.thinksnsplus.modules.wallet.integration.recharge.IntegrationRechargeActivity;
import com.zhiyicx.thinksnsplus.modules.wallet.integration.recharge.IntegrationRechargePresenterModule;

import dagger.Component;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/05/22
 * @Contact master.jungle68@gmail.com
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = IntegrationWithdrawalsPresenterModule.class)
public interface IntegrationWithdrawalsComponent extends InjectComponent<IntegrationWithdrawalsActivity> {
}
