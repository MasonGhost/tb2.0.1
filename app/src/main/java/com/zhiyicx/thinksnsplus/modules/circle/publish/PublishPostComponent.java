package com.zhiyicx.thinksnsplus.modules.circle.publish;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @Author Jliuer
 * @Date 2018/01/23/16:14
 * @Email Jliuer@aliyun.com
 * @Description
 */
@FragmentScoped
@Component(modules = PublishPostPresenterModule.class, dependencies = AppComponent.class)
public interface PublishPostComponent extends InjectComponent<PublishPostActivity> {
}
