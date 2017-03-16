package com.zhiyicx.thinksnsplus.modules.information.infochannel;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @Author Jliuer
 * @Date 2017/03/15
 * @Email Jliuer@aliyun.com
 * @Description
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = InfoChannelPresenterModule.class)
public interface InfoChannelComponent extends InjectComponent<ChannelActivity>{
}
