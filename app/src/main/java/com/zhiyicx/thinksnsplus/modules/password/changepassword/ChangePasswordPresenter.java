package com.zhiyicx.thinksnsplus.modules.password.changepassword;

import com.zhiyicx.baseproject.cache.CacheBean;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.BaseSubscribe;

import javax.inject.Inject;

import rx.Subscription;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/10
 * @Contact master.jungle68@gmail.com
 */
@FragmentScoped
public class ChangePasswordPresenter extends BasePresenter<ChangePasswordContract.Repository, ChangePasswordContract.View> implements ChangePasswordContract.Presenter {


    @Inject
    public ChangePasswordPresenter(ChangePasswordContract.Repository repository, ChangePasswordContract.View rootView) {
        super(repository, rootView);
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
        Subscription changePasswordSub = mRepository.changePassword(oldPassword, newPassword)
                .subscribe(new BaseSubscribe<CacheBean>() {
                    @Override
                    protected void onSuccess(CacheBean data) {
                        mRootView.showMessage(mContext.getString(R.string.change_password_success));
                        mRootView.finsh();
                    }

                    @Override
                    protected void onFailure(String message) {
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
