package com.zhiyicx.thinksnsplus.modules.q_a.publish.detail;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @Author Jliuer
 * @Date 2017/07/26/16:08
 * @Email Jliuer@aliyun.com
 * @Description
 */
@FragmentScoped
@Component(dependencies = AppComponent.class,modules = PublishContentPresenterModule.class)
public interface PublishContentComponent extends InjectComponent<PublishContentActivity>{
}
