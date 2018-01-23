package com.zhiyicx.thinksnsplus.modules.wallet.integration.detail;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;
import com.zhiyicx.thinksnsplus.modules.wallet.bill.BillActivity;
import com.zhiyicx.thinksnsplus.modules.wallet.bill.BillPresenterModule;

import dagger.Component;

/**
 * @Author Jliuer
 * @Date 2017/06/05/10:12
 * @Email Jliuer@aliyun.com
 * @Description
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = BillPresenterModule.class)
public interface IntegrationDetailComponent extends InjectComponent<BillActivity> {
}
