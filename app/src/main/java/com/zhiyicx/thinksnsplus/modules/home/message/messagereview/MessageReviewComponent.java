package com.zhiyicx.thinksnsplus.modules.home.message.messagereview;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @Author Jliuer
 * @Date 2017/7/5/20:14
 * @Email Jliuer@aliyun.com
 * @Description
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = MessageReviewPresenterModule.class)
public interface MessageReviewComponent extends InjectComponent<MessageReviewActivity>{
}
