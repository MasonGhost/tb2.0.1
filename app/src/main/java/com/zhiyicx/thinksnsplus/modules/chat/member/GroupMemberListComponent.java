package com.zhiyicx.thinksnsplus.modules.chat.member;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @author Catherine
 * @describe
 * @date 2018/1/23
 * @contact email:648129313@qq.com
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = GroupMemberListPresenterModule.class)
public interface GroupMemberListComponent extends InjectComponent<GroupMemberListActivity>{
}
