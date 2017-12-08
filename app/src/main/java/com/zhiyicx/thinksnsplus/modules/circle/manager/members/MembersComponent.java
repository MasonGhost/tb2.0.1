package com.zhiyicx.thinksnsplus.modules.circle.manager.members;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @author Jliuer
 * @Date 2017/12/08/15:39
 * @Email Jliuer@aliyun.com
 * @Description
 */
@FragmentScoped
@Component(dependencies = AppComponent.class,modules = MembersPresenterModule.class)
public interface MembersComponent extends InjectComponent<MembersListActivity> {
}
