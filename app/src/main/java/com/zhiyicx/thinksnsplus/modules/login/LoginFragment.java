package com.zhiyicx.thinksnsplus.modules.login;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.thinksnsplus.R;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.functions.Action1;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

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
    @BindView(R.id.tv_error_tip)
    TextView mTvErrorTip;
    @BindView(R.id.tv_look_around)
    TextView mTvLookAround;
    @BindView(R.id.tv_forget_password)
    TextView mTvForgetPassword;

    private boolean isPhoneEdited;
    private boolean isPasswordEdited;

    @Override
    protected void initView(View rootView) {
        mToolbarCenter.setTextColor(getResources().getColor(R.color.important_for_content));
        // 手机号码输入框观察
        RxTextView.textChanges(mEtLoginPhone)
                .compose(this.<CharSequence>bindToLifecycle())
                .subscribe(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence charSequence) {
                        isPhoneEdited = !TextUtils.isEmpty(charSequence.toString());
                        setConfirmEnable();
                    }
                });
        // 密码输入框观察
        RxTextView.textChanges(mEtLoginPassword)
                .compose(this.<CharSequence>bindToLifecycle())
                .subscribe(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence charSequence) {
                        isPasswordEdited = !TextUtils.isEmpty(charSequence.toString());
                        setConfirmEnable();
                    }
                });
        // 点击登录按钮
        RxView.clicks(mBtLoginLogin)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.<Void>bindToLifecycle())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        mPresenter.login(mEtLoginPhone.getText().toString().trim(), mEtLoginPassword.getText().toString().trim());
                    }
                });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    protected int setToolBarBackgroud() {
        return R.color.white;
    }

    @Override
    protected String setCenterTitle() {
        return getResources().getString(R.string.bt_login);
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    public void setLogining() {
    }

    @Override
    public void setLoginSuccess() {
        Toast.makeText(getActivity(), "登陆成功", Toast.LENGTH_SHORT).show();
        /*Intent it = new Intent();
        it.setClass(getActivity(), GuideActivity.class);
        startActivity(it);*/
    }

    @Override
    public void setLoginFailure() {

    }

    @Override
    public void showErrorTips(String error) {
        mTvErrorTip.setText(error);
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

    /**
     * 设置登陆按钮是否可点击
     */
    private void setConfirmEnable() {
        if (isPhoneEdited && isPasswordEdited) {
            mBtLoginLogin.setEnabled(true);
        } else {
            mBtLoginLogin.setEnabled(false);
        }
    }

    @OnClick({R.id.tv_look_around, R.id.tv_forget_password})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_look_around:
                break;
            case R.id.tv_forget_password:
                break;
        }
    }
}
