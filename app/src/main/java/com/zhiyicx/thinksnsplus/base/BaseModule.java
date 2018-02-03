package com.zhiyicx.thinksnsplus.base;


import com.zhiyicx.common.mvp.i.IBaseView;

import dagger.Module;
import dagger.Provides;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/2/10
 * @Contact master.jungle68@gmail.com
 */
@Module
public abstract class BaseModule<T extends IBaseView> {

    private final T mView;

    public BaseModule(T view) {
        mView = view;
    }

    @Provides
    T provideGuideContractView() {
        return mView;
    }
}
