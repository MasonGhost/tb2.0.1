package com.zhiyicx.thinksnsplus.modules.q_a.publish.add_topic;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;


/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/7/25
 * @Contact master.jungle68@gmail.com
 */

@FragmentScoped
@Component(dependencies = AppComponent.class, modules = AddTopicPresenterModule.class)
public interface AddTopicComponent extends InjectComponent<AddTopicActivity>{
}
