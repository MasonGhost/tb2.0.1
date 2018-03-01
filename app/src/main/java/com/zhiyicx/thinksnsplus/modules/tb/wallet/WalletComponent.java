package com.zhiyicx.thinksnsplus.modules.tb.wallet;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @Author Jliuer
 * @Date 2018/03/01/15:01
 * @Email Jliuer@aliyun.com
 * @Description
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = WalletPresenterModule.class)
public interface WalletComponent extends InjectComponent<WalletActivity> {
}
