package com.zhiyicx.thinksnsplus.modules.register;

import com.zhiyicx.common.mvp.BasePresenter;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/28
 * @Contact master.jungle68@gmail.com
 */

public class RegisterPresenter extends BasePresenter<RegisterContract.Repository,RegisterContract.View> implements RegisterContract.Presenter {


    public RegisterPresenter(RegisterContract.Repository repository, RegisterContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void getVertifyCode(String phone) {

    }

    @Override
    public void login() {

    }
}
