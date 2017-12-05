package com.zhiyicx.thinksnsplus.modules.circle.detailv2.post;

import com.zhiyicx.baseproject.impl.share.ShareModule;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @author Jliuer
 * @Date 2017/12/05/10:53
 * @Email Jliuer@aliyun.com
 * @Description
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = {CirclePostDetailPresenterModule.class, ShareModule.class})
public interface CirclePostDetailComponent extends InjectComponent<CirclePostDetailActivity> {

}
