package com.zhiyicx.thinksnsplus.modules.q_a.detail.topic.list;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import javax.inject.Inject;

import dagger.Component;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/31
 * @contact email:648129313@qq.com
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = TopicDetailListPresenterModule.class)
public interface TopicDetailListComponent extends InjectComponent<TopicDetailListFragment>{
}
