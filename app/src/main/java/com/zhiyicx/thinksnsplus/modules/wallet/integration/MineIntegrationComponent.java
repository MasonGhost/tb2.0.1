package com.zhiyicx.thinksnsplus.modules.wallet.integration;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;
import com.zhiyicx.thinksnsplus.modules.wallet.WalletActivity;
import com.zhiyicx.thinksnsplus.modules.wallet.WalletPresenterModule;

import dagger.Component;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/05/22
 * @Contact master.jungle68@gmail.com
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = MineIntegrationPresenterModule.class)
public interface MineIntegrationComponent extends InjectComponent<MineIntegrationActivity> {
}
