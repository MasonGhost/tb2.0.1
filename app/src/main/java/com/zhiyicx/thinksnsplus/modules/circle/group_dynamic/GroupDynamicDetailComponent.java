package com.zhiyicx.thinksnsplus.modules.circle.group_dynamic;

import com.zhiyicx.baseproject.impl.share.ShareModule;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/19
 * @contact email:648129313@qq.com
 */
@FragmentScoped
@Component(modules = {GroupDynamicDetailPresenterModule.class, ShareModule.class}, dependencies = AppComponent.class)
public interface GroupDynamicDetailComponent extends InjectComponent<GroupDynamicDetailActivity> {
}
