package com.zhiyicx.thinksnsplus.modules.tb.contribution;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @Author Jliuer
 * @Date 2018/03/01/14:57
 * @Email Jliuer@aliyun.com
 * @Description
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = ContributionListPresenterModule.class)
public interface ContributionListComponent extends InjectComponent<ContributionListFragment> {
}
