package com.zhiyicx.thinksnsplus.modules.wallet.withdrawals.list_detail;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @Author Jliuer
 * @Date 2017/05/24/9:41
 * @Email Jliuer@aliyun.com
 * @Description
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = WithdrawalsDetailPresenterModule.class)
public interface WithdrawalsDetailComponent extends InjectComponent<WithdrawalsDetailActivity> {
}
