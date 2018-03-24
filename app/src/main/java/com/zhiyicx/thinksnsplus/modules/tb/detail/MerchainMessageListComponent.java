package com.zhiyicx.thinksnsplus.modules.tb.detail;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;
import com.zhiyicx.thinksnsplus.modules.tb.contribution.ContributionListFragment;
import com.zhiyicx.thinksnsplus.modules.tb.contribution.ContributionListPresenterModule;

import dagger.Component;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2018/3/23
 * @Contact master.jungle68@gmail.com
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = MerchianMessageListPresenterModule.class)
public interface MerchainMessageListComponent extends InjectComponent<MerchainMessageListFragment> {
}
