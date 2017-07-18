package com.zhiyicx.thinksnsplus.modules.feedback;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @Author Jliuer
 * @Date 2017/06/02/17:26
 * @Email Jliuer@aliyun.com
 * @Description
 */
@FragmentScoped
@Component(dependencies = AppComponent.class,modules = FeedBackPresenterModule.class)
public interface FeedBackComponent extends InjectComponent<FeedBackActivity>{
}
