package com.zhiyicx.thinksnsplus.modules.wallet.integration.detail

import com.zhiyicx.common.dagger.scope.FragmentScoped
import com.zhiyicx.thinksnsplus.base.AppComponent
import com.zhiyicx.thinksnsplus.base.InjectComponent

import dagger.Component

/**
 * @Describe
 * @Author Jungle68
 * @Date 2018/1/23
 * @Contact master.jungle68@gmail.com
 */
@FragmentScoped
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(IntegrationDetailPresenterModule::class))
interface IntegrationDetailComponent : InjectComponent<IntegrationDetailListFragment>
