<<<<<<< HEAD
package com.zhiyicx.thinksnsplus.modules.chat;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;

import dagger.Component;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/01/06
 * @Contact master.jungle68@gmail.com
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = ChatPresenterModule.class)
public interface ChatComponent {
    void inject(ChatActivity chatActivity);
}
=======
package com.zhiyicx.thinksnsplus.modules.chat;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;

import dagger.Component;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/01/06
 * @Contact master.jungle68@gmail.com
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = ChatPresenterModule.class)
public interface ChatComponent {
    void inject(ChatActivity chatActivity);
}
>>>>>>> 5eb1174523744bea0c0756f5af31310a1467fb94
