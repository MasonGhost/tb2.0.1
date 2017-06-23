package com.zhiyicx.thinksnsplus.modules.login;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.widget.button.LoadingButton;
import com.zhiyicx.common.utils.ActivityHandler;
import com.zhiyicx.imsdk.utils.common.DeviceUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.modules.home.HomeActivity;
import com.zhiyicx.thinksnsplus.modules.password.findpassword.FindPasswordActivity;
import com.zhiyicx.thinksnsplus.modules.register.RegisterActivity;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import rx.functions.Action1;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;
import static com.zhiyicx.thinksnsplus.modules.login.LoginActivity.BUNDLE_TOURIST_LOGIN;

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
    LoadingButton mBtLoginLogin;
    @BindView(R.id.tv_error_tip)
    TextView mTvErrorTip;
    @BindView(R.id.tv_look_around)
    TextView mTvLookAround;
    @BindView(R.id.tv_forget_password)
    TextView mTvForgetPassword;

    private boolean mIsPhoneEdited;
    private boolean mIsPasswordEdited;

    private boolean mIsToourist;

    public static LoginFragment newInstance(boolean isTourist) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putBoolean(BUNDLE_TOURIST_LOGIN, isTourist);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            mIsToourist = getArguments().getBoolean(BUNDLE_TOURIST_LOGIN);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView(View rootView) {
        initListenter();
        // 游客判断
        mTvLookAround.setVisibility((!mIsToourist && mPresenter.istourist()) ? View.VISIBLE : View.GONE);
        if (mIsToourist || !mPresenter.istourist()) {
            setLeftTextColor(R.color.themeColor);
        }
    }

    private void initListenter() {
        // 手机号码输入框观察
        RxTextView.textChanges(mEtLoginPhone)
                .compose(this.<CharSequence>bindToLifecycle())
                .subscribe(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence charSequence) {
                        mIsPhoneEdited = !TextUtils.isEmpty(charSequence.toString());
                        setConfirmEnable();
                    }
                });
        // 密码输入框观察
        RxTextView.textChanges(mEtLoginPassword)
                .compose(this.<CharSequence>bindToLifecycle())
                .subscribe(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence charSequence) {
                        mIsPasswordEdited = !TextUtils.isEmpty(charSequence.toString());
                        setConfirmEnable();
                    }
                });
        // 点击登录按钮
        RxView.clicks(mBtLoginLogin)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.<Void>bindToLifecycle())
                .compose(mRxPermissions.ensure(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE))
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (aBoolean) {// 获取到了权限
                            mPresenter.login(mEtLoginPhone.getText().toString().trim(), mEtLoginPassword.getText().toString().trim());
                        } else {// 拒绝权限，但是可以再次请求
                            showErrorTips(getString(R.string.permisson_refused));
                        }
                    }
                });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected boolean usePermisson() {
        return true;
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
        return getString(R.string.bt_login);
    }

    @Override
    protected String setRightTitle() {
        return getString(R.string.regist);
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected void setRightClick() {
        super.setRightClick();
        Intent intent = new Intent(getActivity(), RegisterActivity.class);
        intent.putExtra(BUNDLE_TOURIST_LOGIN, mIsToourist);
        startActivity(intent);
    }

    @Override
    protected int setLeftImg() {
        return 0;
    }

    @Override
    protected String setLeftTitle() {
        return mIsToourist ? getString(R.string.cancel) : "";
    }


    @Override
    public void setLogining() {
        mBtLoginLogin.handleAnimation(true);
        mBtLoginLogin.setEnabled(false);
    }

    @Override
    public void setLoginState(boolean loginState) {
        mBtLoginLogin.handleAnimation(false);
        mBtLoginLogin.setEnabled(true);
        if (loginState) {
            mTvErrorTip.setVisibility(View.INVISIBLE);
            mTvErrorTip.setText("");
            mEtLoginPassword.setText("");
            mEtLoginPhone.setText("");
            mEtLoginPhone.requestFocus();
            DeviceUtils.hideSoftKeyboard(getContext(), mEtLoginPassword);
            if (mIsToourist) {
                getActivity().finish();
            } else {
                goHome();
            }
        }
    }

    @Override
    public void showErrorTips(String message) {
        if (TextUtils.isEmpty(message)) {
            mTvErrorTip.setVisibility(View.INVISIBLE);
        } else {
            mTvErrorTip.setVisibility(View.VISIBLE);
            mTvErrorTip.setText(message);
        }
    }

    /**
     * 设置登录按钮是否可点击
     */
    private void setConfirmEnable() {
        if (mIsPhoneEdited && mIsPasswordEdited) {
            mBtLoginLogin.setEnabled(true);
        } else {
            mBtLoginLogin.setEnabled(false);
        }
    }

    @OnClick({R.id.tv_look_around, R.id.tv_forget_password})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_look_around:
                goHome();
                break;
            case R.id.tv_forget_password:
                startActivity(new Intent(getActivity(), FindPasswordActivity.class));
                break;
            default:
        }
    }

    private void goHome() {
        ActivityHandler.getInstance().finishAllActivityEcepteCurrent();// 清除 homeAcitivity 重新加载
        startActivity(new Intent(getActivity(), HomeActivity.class));
        getActivity().finish();
    }

}
