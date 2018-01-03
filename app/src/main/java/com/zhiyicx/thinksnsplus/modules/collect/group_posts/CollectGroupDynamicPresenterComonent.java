package com.zhiyicx.thinksnsplus.modules.collect.group_posts;

import com.zhiyicx.baseproject.impl.share.ShareModule;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;
import com.zhiyicx.thinksnsplus.modules.circle.detail.ChannelDetailPresenterModule;

import dagger.Component;

/**
 * @Author Jliuer
 * @Date 2017/07/24/11:30
 * @Email Jliuer@aliyun.com
 * @Description
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = { ShareModule.class,ChannelDetailPresenterModule.class})
public interface CollectGroupDynamicPresenterComonent extends InjectComponent<CollectGroupDynamicListFragment> {
}
