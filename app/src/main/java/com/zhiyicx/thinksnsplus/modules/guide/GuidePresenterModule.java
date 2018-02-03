package com.zhiyicx.thinksnsplus.modules.guide;


import com.zhiyicx.thinksnsplus.base.BaseModule;

import dagger.Module;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/2/10
 * @Contact master.jungle68@gmail.com
 */
@Module
public class GuidePresenterModule extends BaseModule<GuideContract.View> {

    public GuidePresenterModule(GuideContract.View view) {
        super(view);
    }
}
