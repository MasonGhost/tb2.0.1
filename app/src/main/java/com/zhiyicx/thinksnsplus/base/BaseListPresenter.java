package com.zhiyicx.thinksnsplus.base;

import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.config.TouristConfig;
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

public abstract class BaseListPresenter<R, V extends IBaseView, T extends BaseListBean> extends BasePresenter<R, V> implements ITSListPresenter<T> {

    @Inject
    protected AuthRepository mAuthRepository;

    public BaseListPresenter(R repository, V rootView) {
        super(repository, rootView);
    }

    @Override
    public boolean isLogin() {
        return mAuthRepository.isLogin();
    }

    @Override
    public boolean handleTouristControl() {
        if (isLogin() || TouristConfig.LIST_CAN_LOAD_MORE) {
            return false;
        } else {
            mRootView.showLoginPop();
            return true;
        }
    }
}
