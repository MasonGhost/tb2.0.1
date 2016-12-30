package com.zhiyicx.thinksnsplus.modules.login;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.common.utils.ManyEdittextContentWatcher;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author LiuChao
 * @describe
 * @date 2016/12/23
 * @contact email:450127106@qq.com
 */

public class LoginFragment extends TSFragment<LoginContract.Presenter> implements LoginContract.View {
    @BindView(R.id.et_login_phone)
    EditText mEtLoginPhone;
    @BindView(R.id.et_login_password)
    EditText mEtLoginPassword;
    @BindView(R.id.bt_login_login)
    Button mBtLoginLogin;

    @Override
    protected void initView(View rootView) {
        new ManyEdittextContentWatcher(new ManyEdittextContentWatcher.ContentWatcher() {
            @Override
            public void allHasContent(boolean hasContent) {
                mBtLoginLogin.setClickable(hasContent);
            }
        }, mEtLoginPhone, mEtLoginPassword);
    }

    @Override
    protected void initData() {

    }

    @OnClick(R.id.bt_login_login)
    public void onClick() {
        LogUtils.i("lalallalalalla");
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    protected String setLeftTitle() {
        return "登陆";
    }

    @Override
    public void setLogining() {

    }

    @Override
    public void setLoginSuccess() {

    }

    @Override
    public void setLoginFailure() {

    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(String message) {

    }
}
