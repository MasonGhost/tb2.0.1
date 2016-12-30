package com.zhiyicx.thinksnsplus.dagger;

import com.zhiyicx.thinksnsplus.data.source.local.UserCacheImpl;

import dagger.Component;

/**
 * @author LiuChao
 * @describe
 * @date 2016/12/30
 * @contact email:450127106@qq.com
 */
@Component(modules = {GreenDaoModule.class})
public interface GreenDaoComponent {
    UserCacheImpl provideUserCacheImpl();
}
