package com.zhiyicx.thinksnsplus.modules.q_a.detail.answer.dig_list;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;;

import dagger.Component;

/**
 * @author LiuChao
 * @describe
 * @date 2017/3/6
 * @contact email:450127106@qq.com
 */
@FragmentScoped
@Component(modules = AnswerDigListPresenterModule.class, dependencies = AppComponent.class)
public interface AnswerDigListPresenterComponent extends InjectComponent<AnswerDigListActivity>{
}
