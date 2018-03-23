package com.zhiyicx.thinksnsplus.modules.tb.contract;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

@FragmentScoped
@Component(dependencies = AppComponent.class, modules = ContractListPresenterModule.class)
public interface ContractListComponent extends InjectComponent<ContractListFragment> {
}
