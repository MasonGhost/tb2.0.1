package com.zhiyicx.thinksnsplus.modules.wallet.integration.mine

import com.zhiyicx.common.dagger.scope.FragmentScoped
import com.zhiyicx.thinksnsplus.base.AppComponent
import com.zhiyicx.thinksnsplus.base.InjectComponent

import dagger.Component

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/05/22
 * @Contact master.jungle68@gmail.com
 */
@FragmentScoped
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(MineIntegrationPresenterModule::class))
interface MineIntegrationComponent : InjectComponent<MineIntegrationActivity>
