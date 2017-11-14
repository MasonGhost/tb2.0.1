package com.zhiyicx.thinksnsplus.modules.circle.detail;

import com.zhiyicx.baseproject.impl.share.ShareModule;
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
@Component(modules = {ChannelDetailPresenterModule.class, ShareModule.class}, dependencies = AppComponent.class)
public interface ChannelDetailPresenterComponent extends InjectComponent<ChannelDetailActivity> {
}
