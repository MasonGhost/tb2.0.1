package com.zhiyicx.thinksnsplus.modules.password.findpassword;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.common.utils.RegexUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.modules.password.changepassword.ChangePasswordContract;

import javax.inject.Inject;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/10
 * @Contact master.jungle68@gmail.com
 */
@FragmentScoped
public class FindPasswordPresenter extends BasePresenter<ChangePasswordContract.Repository, ChangePasswordContract.View> implements ChangePasswordContract.Presenter {


    @Inject
    public FindPasswordPresenter(ChangePasswordContract.Repository repository, ChangePasswordContract.View rootView) {
        super(repository, rootView);
    }


    @Override
    public void onStart() {

    }

    @Override
    public void onDestroy() {
        unSubscribe();
    }

    /**
     * 检测手机号码是否正确
     *
     * @param phone
     * @return
     */
    private boolean checkPhone(String phone) {
        if (!RegexUtils.isMobileExact(phone)) {
            mRootView.showMessage(mContext.getString(R.string.phone_number_toast_hint));
            return true;
        }
        return false;
    }

    /**
     * 检查用户名是否小于最小长度,不能以数字开头
     *
     * @param name
     * @return
     */
    private boolean checkUsername(String name) {
        if (!RegexUtils.isUsernameLength(name, mContext.getResources().getInteger(R.integer.username_min_length))) {
            mRootView.showMessage(mContext.getString(R.string.username_toast_hint));
            return true;
        }
        if (RegexUtils.isUsernameNoNumberStart(name)) {// 数字开头
            mRootView.showMessage(mContext.getString(R.string.username_toast_not_number_start_hint));
            return true;
        }
        if (!RegexUtils.isUsername(name)) {// 用户名只能包含数字、字母和下划线
            mRootView.showMessage(mContext.getString(R.string.username_toast_not_symbol_hint));
            return true;
        }
        return false;
    }

    /**
     * 检查密码是否是最小长度
     *
     * @param password
     * @return
     */
    private boolean checkPassword(String password) {
        if (password.length() < mContext.getResources().getInteger(R.integer.password_min_length)) {
            mRootView.showMessage(mContext.getString(R.string.password_toast_hint));
            return true;
        }
        return false;
    }

    @Override
    public void changePassword(String oldPassword, String newPassword, String sureNewPassword) {

    }
}
