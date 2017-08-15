package com.zhiyicx.thinksnsplus.modules.q_a.detail.topic;

import com.zhiyicx.baseproject.impl.share.ShareModule;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/14
 * @contact email:648129313@qq.com
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = {TopicDetailPresenterModule.class, ShareModule.class})
public interface TopicDetailComponent extends InjectComponent<TopicDetailActivity>{
}
