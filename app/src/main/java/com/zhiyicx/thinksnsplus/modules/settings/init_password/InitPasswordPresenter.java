package com.zhiyicx.thinksnsplus.modules.settings.init_password;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.repository.AuthRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.PasswordRepository;

import javax.inject.Inject;

import rx.Subscription;

/**
 * @author Catherine
 * @describe
 * @date 2017/9/18
 * @contact email:648129313@qq.com
 */
@FragmentScoped
public class InitPasswordPresenter extends AppBasePresenter<InitPasswordContract.View>
        implements InitPasswordContract.Presenter {

    @Inject
    PasswordRepository mPasswordRepository;

    @Inject
    AuthRepository mAuthRepository;

    @Inject
    public InitPasswordPresenter(InitPasswordContract.View rootView) {
        super(rootView);
    }


    @Override
    public void initPassword(String password, String confirm_password) {
        if (checkPasswordLength(password, mContext.getString(R.string.old_password_toast_hint))) {
            return;
        }
        if (checkPasswordLength(password, mContext.getString(R.string.new_password_toast_hint))) {
            return;
        }
        if (checkPasswordLength(confirm_password, mContext.getString(R.string.sure_new_password_toast_hint))) {
            return;
        }
        if (!password.equals(confirm_password)) {
            mRootView.showMessage(mContext.getString(R.string.password_diffrent));
            return;
        }
        Subscription subscription = mPasswordRepository.changePasswordV2(null, password)
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage(mContext.getString(R.string.bill_doing)))
                .subscribe(new BaseSubscribeForV2<Object>() {
                    @Override
                    protected void onSuccess(Object data) {
                        mRootView.initPasswordResult(true);
                        // 更新状态
                        UserInfoBean userInfoBean = mRootView.getCurrentUser();
                        userInfoBean.setInitial_password(true);
                        AuthBean authBean = AppApplication.getmCurrentLoginAuth();
                        authBean.setUser(userInfoBean);
                        AppApplication.getmCurrentLoginAuth().setUser(userInfoBean);
                        mAuthRepository.saveAuthBean(authBean);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        mRootView.showSnackErrorMessage(message);
                        mRootView.showMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        throwable.printStackTrace();
                        mRootView.showSnackErrorMessage(mContext.getString(R.string.err_net_not_work));
                        mRootView.showMessage(mContext.getString(R.string.err_net_not_work));
                    }
                });
        addSubscrebe(subscription);
    }

    /**
     * 检查密码是否是最小长度
     *
     * @param password 密码
     * @return 是否可用
     */
    private boolean checkPasswordLength(String password, String hint) {
        if (password.length() < mContext.getResources().getInteger(R.integer.password_min_length)) {
            mRootView.showMessage(hint);
            return true;
        }
        return false;
    }
}
