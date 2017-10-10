package com.zhiyicx.thinksnsplus.modules.home.message.messagecomment;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/10
 * @Contact master.jungle68@gmail.com
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = MessageCommentPresenterModule.class)
public interface MessageCommentComponent extends InjectComponent<MessageCommentActivity>{
}

