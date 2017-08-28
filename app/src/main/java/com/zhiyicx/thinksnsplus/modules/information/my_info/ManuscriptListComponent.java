package com.zhiyicx.thinksnsplus.modules.information.my_info;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @Author Jliuer
 * @Date 2017/08/28/14:49
 * @Email Jliuer@aliyun.com
 * @Description
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = ManuscripListPresenterModule.class)
public interface ManuscriptListComponent extends InjectComponent<ManuscriptListFragment> {
}
