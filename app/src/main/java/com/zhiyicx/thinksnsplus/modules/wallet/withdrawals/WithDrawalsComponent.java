package com.zhiyicx.thinksnsplus.modules.wallet.withdrawals;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @Author Jliuer
 * @Date 2017/05/23/14:11
 * @Email Jliuer@aliyun.com
 * @Description
 */
@FragmentScoped
@Component(modules = WithDrawalsPresenterModule.class, dependencies = AppComponent.class)
public interface WithDrawalsComponent extends InjectComponent<WithdrawalsActivity> {
}
