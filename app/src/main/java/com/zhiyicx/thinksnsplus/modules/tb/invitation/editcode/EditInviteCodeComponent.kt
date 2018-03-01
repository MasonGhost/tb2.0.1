package com.zhiyicx.thinksnsplus.modules.tb.invitation.editcode

import com.zhiyicx.common.dagger.scope.FragmentScoped
import com.zhiyicx.thinksnsplus.base.AppComponent
import com.zhiyicx.thinksnsplus.base.InjectComponent

import dagger.Component

/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/28
 * @Contact master.jungle68@gmail.com
 */
@FragmentScoped
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(EditInviteCodePresenterModule::class))
interface EditInviteCodeComponent : InjectComponent<EditInviteCodeActivity>

