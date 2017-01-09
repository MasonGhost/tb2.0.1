package com.zhiyicx.thinksnsplus.modules.edit_userinfo;

import com.zhiyicx.common.mvp.BasePresenter;

import javax.inject.Inject;

/**
 * @author LiuChao
 * @describe
 * @date 2017/1/9
 * @contact email:450127106@qq.com
 */

public class UserInfoPresenter extends BasePresenter<UserInfoContract.Repository, UserInfoContract.View> implements UserInfoContract.Presenter {

    @Inject
    public UserInfoPresenter(UserInfoContract.Repository repository, UserInfoContract.View rootView) {
        super(repository, rootView);
    }

    /**
     * 将Presenter从传入fragment
     */
    @Inject
    void setupListeners() {
        mRootView.setPresenter(this);
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onDestroy() {

    }
}
