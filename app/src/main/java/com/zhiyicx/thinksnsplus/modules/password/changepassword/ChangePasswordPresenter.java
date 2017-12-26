package com.zhiyicx.thinksnsplus.modules.password.changepassword;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.source.repository.PasswordRepository;

import javax.inject.Inject;

import rx.Subscription;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/10
 * @Contact master.jungle68@gmail.com
 */
@FragmentScoped
public class ChangePasswordPresenter extends BasePresenter<ChangePasswordContract.View> implements ChangePasswordContract.Presenter {

    @Inject
    PasswordRepository mPasswordRepository;

    @Inject
    public ChangePasswordPresenter(ChangePasswordContract.View rootView) {
        super(rootView);
    }


    @Override
    public void onStart() {

    }

    @Override
    public void changePassword(String oldPassword, String newPassword, String sureNessword) {
        if (checkPasswordLength(oldPassword, mContext.getString(R.string.old_password_toast_hint))) {
            return;
        }
        if (checkPasswordLength(newPassword, mContext.getString(R.string.new_password_toast_hint))) {
            return;
        }
        if (checkPasswordLength(sureNessword, mContext.getString(R.string.sure_new_password_toast_hint))) {
            return;
        }
        if (!newPassword.equals(sureNessword)) {
            mRootView.showMessage(mContext.getString(R.string.password_diffrent));
            return;
        }
        Subscription changePasswordSub = mPasswordRepository.changePasswordV2(oldPassword, newPassword)
                .subscribe(new BaseSubscribeForV2<Object>() {
                    @Override
                    protected void onSuccess(Object data) {
                        mRootView.showSnackSuccessMessage(mContext.getString(R.string.change_success));
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        mRootView.showMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        throwable.printStackTrace();
                        mRootView.showMessage(mContext.getString(R.string.err_net_not_work));
                    }
                });
        addSubscrebe(changePasswordSub);
    }

    /**
     * 检查密码是否是最小长度
     *
     * @param password
     * @return
     */
    private boolean checkPasswordLength(String password, String hint) {
        if (password.length() < mContext.getResources().getInteger(R.integer.password_min_length)) {
            mRootView.showMessage(hint);
            return true;
        }
        return false;
    }
}
