package com.zhiyicx.thinksnsplus.base;

import com.zhiyicx.baseproject.base.IBaseTouristPresenter;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.source.repository.AuthRepository;

import javax.inject.Inject;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/5/16
 * @Contact master.jungle68@gmail.com
 */

public abstract class AppBasePresenter<R, V extends IBaseView> extends BasePresenter<R, V> implements IBaseTouristPresenter {

    @Inject
    protected AuthRepository mAuthRepository;

    public AppBasePresenter(R repository, V rootView) {
        super(repository, rootView);
    }

    public boolean istourist() {
        return mAuthRepository.isTourist();
    }

    @Override
    public boolean isLogin() {
        return mAuthRepository.isLogin();
    }

    @Override
    public boolean handleTouristControl() {
        if (isLogin()) {
            return false;
        } else {
            mRootView.showLoginPop();
            return true;
        }
    }
}
