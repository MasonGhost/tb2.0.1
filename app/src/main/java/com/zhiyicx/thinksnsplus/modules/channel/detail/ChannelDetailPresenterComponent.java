package com.zhiyicx.thinksnsplus.modules.channel.detail;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @author LiuChao
 * @describe
 * @date 2017/4/11
 * @contact email:450127106@qq.com
 */
@FragmentScoped
@Component(modules = ChannelDetailPresenterModule.class, dependencies = AppComponent.class)
public interface ChannelDetailPresenterComponent extends InjectComponent<ChannelDetailActivity> {
}
