package com.zhiyicx.thinksnsplus.modules.register;

import android.Manifest;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.config.SystemConfig;
import com.zhiyicx.baseproject.widget.button.LoadingButton;
import com.zhiyicx.baseproject.widget.edittext.DeleteEditText;
import com.zhiyicx.baseproject.widget.edittext.PasswordEditText;
import com.zhiyicx.common.utils.ActivityHandler;
import com.zhiyicx.common.utils.RegexUtils;
import com.zhiyicx.imsdk.utils.common.DeviceUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.modules.home.HomeActivity;
import com.zhiyicx.thinksnsplus.modules.register.rule.UserRuleActivity;
import com.zhiyicx.thinksnsplus.modules.usertag.EditUserTagFragment;
import com.zhiyicx.thinksnsplus.modules.usertag.TagFrom;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;
import static com.zhiyicx.common.config.ConstantConfig.MOBILE_PHONE_NUMBER_LENGHT;
import static com.zhiyicx.thinksnsplus.modules.login.LoginActivity.BUNDLE_TOURIST_LOGIN;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/4
 * @Contact master.jungle68@gmail.com
 */
public class RegisterFragment extends TSFragment<RegisterContract.Presenter> implements
        RegisterContract.View {

    private static final int REGISTER_PHONE = 0; // 手机号注册
    private static final int REGISTER_EMAIL = 1; // 邮箱注册

    @BindView(R.id.et_regist_username)
    DeleteEditText mEtRegistUsername;
    @BindView(R.id.et_regist_phone)
    DeleteEditText mEtRegistPhone;
    @BindView(R.id.et_register_email)
    DeleteEditText mEtRegisterEmail;
    @BindView(R.id.bt_regist_send_vertify_code)
    TextView mBtRegistSendVertifyCode;
    @BindView(R.id.iv_vertify_loading)
    ImageView mIvVertifyLoading;
    @BindView(R.id.et_regist_vertify_code)
    DeleteEditText mEtRegistVertifyCode;
    @BindView(R.id.et_regist_password)
    PasswordEditText mEtRegistPassword;
    @BindView(R.id.bt_regist_regist)
    LoadingButton mBtRegistRegist;
    @BindView(R.id.tv_error_tip)
    TextView mTvErrorTip;
    @BindView(R.id.tv_app_rule)
    TextView mAppRule;
    @BindView(R.id.tv_look_around)
    TextView mTvLookAround;
    @BindView(R.id.ll_register_by_phone)
    LinearLayout mLlRegisterByPhone;
    @BindView(R.id.ll_register_by_email)
    LinearLayout mLlRegisterByEmail;

    private AnimationDrawable mVertifyAnimationDrawable;
    private boolean isNameEdited;
    private boolean isPhoneEdited;
    private boolean isEmailEdited;
    private boolean isCodeEdited;
    private boolean isPassEdited;
    private boolean mIsVertifyCodeEnalbe = true;
    private boolean isRegisting = false;
    private boolean mIsToourist;

    private int mCurrentRegisterType = REGISTER_PHONE; // 默认手机注册

    public static RegisterFragment newInstance(boolean isTourist) {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        args.putBoolean(BUNDLE_TOURIST_LOGIN, isTourist);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_register;
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected int setToolBarBackgroud() {
        return R.color.white;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.register_by_phone);
    }

    @Override
    protected String setRightTitle() {
        return getString(R.string.email_address);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            mIsToourist = getArguments().getBoolean(BUNDLE_TOURIST_LOGIN);
        }
        mSystemConfigBean = mPresenter.getSystemConfigBean();
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initView(View rootView) {
        boolean isAccountAllType = mSystemConfigBean.getRegisterSettings() == null
                || mSystemConfigBean.getRegisterSettings() != null
                && SystemConfig.REGITER_ACCOUNTTYPE_ALL.equals(mSystemConfigBean
                .getRegisterSettings().getMethod());
        boolean isOnlyMobile = mSystemConfigBean.getRegisterSettings() != null
                && SystemConfig.REGITER_ACCOUNTTYPE_MOBILE_ONLY.equals(mSystemConfigBean
                .getRegisterSettings().getMethod());
        boolean isOnlyEmail = mSystemConfigBean.getRegisterSettings() != null
                && SystemConfig.REGITER_ACCOUNTTYPE_MAIL_ONLY.equals(mSystemConfigBean
                .getRegisterSettings().getMethod());

        if (isOnlyMobile) {
            mCurrentRegisterType = REGISTER_PHONE;
        } else {
            mCurrentRegisterType = REGISTER_EMAIL;
        }
        setRightText(mCurrentRegisterType == REGISTER_PHONE ?
                getString(R.string.email_address) : getString(R.string.phone_hint));
        setCenterText(mCurrentRegisterType == REGISTER_PHONE ?
                getString(R.string.register_by_phone) : getString(R.string.register_by_email));

        setRegisterType();
        mVertifyAnimationDrawable = (AnimationDrawable) mIvVertifyLoading.getDrawable();
        initListener();
        // 游客判断
//        mTvLookAround.setVisibility((!mIsToourist && mPresenter.isTourist()) ? View.VISIBLE :
// View.GONE);
        mToolbarRight.setVisibility(isAccountAllType ? View.VISIBLE : View.GONE);
    }

    private void initListener() {
        // 用户名观察
        RxTextView.textChanges(mEtRegistUsername)
                .compose(this.bindToLifecycle())
                .subscribe(charSequence -> {
                    isNameEdited = !TextUtils.isEmpty(charSequence.toString());
                    setConfirmEnable();
                });
        // 电话号码观察
        RxTextView.textChanges(mEtRegistPhone)
                .compose(this.bindToLifecycle())
                .subscribe(charSequence -> {
                    if (mIsVertifyCodeEnalbe) {
                        mBtRegistSendVertifyCode.setEnabled(charSequence.length() ==
                                MOBILE_PHONE_NUMBER_LENGHT);
                    }
                    isPhoneEdited = !TextUtils.isEmpty(charSequence.toString());
                    setConfirmEnable();
                });
        // 邮箱地址观察
        RxTextView.textChanges(mEtRegisterEmail)
                .compose(this.bindToLifecycle())
                .subscribe(charSequence -> {
                    if (mIsVertifyCodeEnalbe) {
                        mBtRegistSendVertifyCode.setEnabled(RegexUtils.isEmail(charSequence));
                    }
                    isEmailEdited = !TextUtils.isEmpty(charSequence.toString());
                    setConfirmEnable();
                });
        // 验证码观察
        RxTextView.textChanges(mEtRegistVertifyCode)
                .compose(this.bindToLifecycle())
                .subscribe(charSequence -> {
                    isCodeEdited = !TextUtils.isEmpty(charSequence.toString());
                    setConfirmEnable();
                });
        // 密码观察
        RxTextView.textChanges(mEtRegistPassword)
                .compose(this.bindToLifecycle())
                .subscribe(charSequence -> {
                    isPassEdited = !TextUtils.isEmpty(charSequence.toString());
                    setConfirmEnable();
                    Editable editable = mEtRegistPassword.getText();
                    int len = editable.length();

                    if (len > getResources().getInteger(R.integer.password_maxlenght)) {
                        int selEndIndex = Selection.getSelectionEnd(editable);
                        String str = editable.toString();
                        //截取新字符串
                        String newStr = str.substring(0, getResources().getInteger(R.integer
                                .password_maxlenght));
                        mEtRegistPassword.setText(newStr);
                        editable = mEtRegistPassword.getText();
                        //新字符串的长度
                        int newLen = editable.length();
                        //旧光标位置超过字符串长度
                        if (selEndIndex > newLen) {
                            selEndIndex = editable.length();
                        }
                        //设置新光标所在的位置
                        Selection.setSelection(editable, selEndIndex);

                    }
                });


        // 点击发送验证码
        RxView.clicks(mBtRegistSendVertifyCode)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> {

                    if (mCurrentRegisterType == REGISTER_PHONE) {
                        mPresenter.getVertifyCode(mEtRegistPhone.getText().toString().trim());
                    } else {
                        if (mPresenter
                                .getSystemConfigBean().getSite().getClient_email().contains(mEtRegisterEmail.getText().toString().trim())
                                ) {
                            showMessage(getString(R.string.can_not_use_protected_email));
                            return;
                        }
                        mPresenter.getVerifyCodeByEmail(mEtRegisterEmail.getText().toString()
                                .trim());
                    }
                });
        // 点击注册按钮
        RxView.clicks(mBtRegistRegist)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .compose(mRxPermissions.ensureEach(Manifest.permission.READ_PHONE_STATE))
                .subscribe(permission -> {
                    String userName = mEtRegistUsername.getText().toString().trim();
                    if (mPresenter
                            .getSystemConfigBean().getSite().getReserved_nickname()
                            .contains(mEtRegistUsername.getText().toString().trim())) {
                        showMessage(getString(R.string.can_not_use_protected_name));
                        return;
                    }

                    // 获取到了权限
                    if (permission.granted) {
                        // 手机号注册
                        if (mCurrentRegisterType == REGISTER_PHONE) {
                            mPresenter.register(mEtRegistUsername.getText().toString().trim()
                                    , mEtRegistPhone.getText().toString().trim()
                                    , mEtRegistVertifyCode.getText().toString().trim()
                                    , mEtRegistPassword.getText().toString().trim()
                            );
                        } else {
                            // 邮箱注册
                            mPresenter.registerByEmail(mEtRegistUsername.getText().toString().trim()
                                    , mEtRegisterEmail.getText().toString().trim()
                                    , mEtRegistVertifyCode.getText().toString().trim()
                                    , mEtRegistPassword.getText().toString().trim()
                            );
                        }

                    } else if (permission.shouldShowRequestPermissionRationale) {// 拒绝权限，但是可以再次请求
                        showMessage(getString(R.string.permisson_refused));
                    } else {//永久拒绝
                        showMessage(getString(R.string.permisson_refused_nerver_ask));
                    }
                });
        try {
            mAppRule.setVisibility(mPresenter.getSystemConfigBean().getRegisterSettings().hasShowTerms() ? View.VISIBLE : View.GONE);
        } catch (NullPointerException e) {
            mAppRule.setVisibility(View.GONE);
        }
        mAppRule.setText(getString(R.string.app_rule_register, getString(R.string.app_name)));
        RxView.clicks(mAppRule)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> UserRuleActivity.startUserRuleActivity(getActivity(),
                        mPresenter.getSystemConfigBean().getRegisterSettings().getContent()));
    }

    @Override
    protected void initData() {

    }

    @Override
    protected boolean usePermisson() {
        return true;
    }

    @Override
    public void setVertifyCodeBtEnabled(boolean isEnable) {
        mIsVertifyCodeEnalbe = isEnable;
        mBtRegistSendVertifyCode.setEnabled(isEnable);
    }

    @Override
    public void setVertifyCodeLoadin(boolean isEnable) {
        if (isEnable) {
            mIvVertifyLoading.setVisibility(View.VISIBLE);
            mBtRegistSendVertifyCode.setVisibility(View.INVISIBLE);
            mVertifyAnimationDrawable.start();
        } else {
            mVertifyAnimationDrawable.stop();
            mIvVertifyLoading.setVisibility(View.INVISIBLE);
            mBtRegistSendVertifyCode.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setVertifyCodeBtText(String text) {
        mBtRegistSendVertifyCode.setText(text);
    }

    @Override
    public void setRegisterBtEnabled(boolean isEnable) {
        mBtRegistRegist.handleAnimation(!isEnable);
        isRegisting = !isEnable;
        setConfirmEnable();
    }

    @Override
    public void goHome() {
        DeviceUtils.hideSoftKeyboard(getContext(), mEtRegistPassword);
        ActivityHandler.getInstance().finishAllActivityEcepteCurrent();// 清除 homeAcitivity 重新加载
        boolean needCompleteUserInfo = mSystemConfigBean.getRegisterSettings() == null
                || mSystemConfigBean.getRegisterSettings().isCompleteData() || "need".equals(mSystemConfigBean.getRegisterSettings().getFixed());
        if (needCompleteUserInfo) {
            EditUserTagFragment.startToEditTagActivity(getActivity(), TagFrom.REGISTER, null);
        } else {
            startActivity(new Intent(getActivity(), HomeActivity.class));
        }
        getActivity().finish();
    }

    @Override
    public void showMessage(String message) {
        if (TextUtils.isEmpty(message)) {
            mTvErrorTip.setVisibility(View.INVISIBLE);
        } else {
            mTvErrorTip.setVisibility(View.VISIBLE);
            mTvErrorTip.setText(message);
        }
    }


    /**
     * 设置确定按钮是否可点击
     */
    private void setConfirmEnable() {
        if (isNameEdited && isCodeEdited && isPassEdited && !isRegisting) {
            if ((mCurrentRegisterType == REGISTER_PHONE && isPhoneEdited)
                    || (mCurrentRegisterType == REGISTER_EMAIL && isEmailEdited)) {
                mBtRegistRegist.setEnabled(true);
            } else {
                mBtRegistRegist.setEnabled(false);
            }
        } else {
            mBtRegistRegist.setEnabled(false);
        }
    }

    @OnClick({R.id.tv_look_around})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_look_around:
                goHome();
                break;
            default:
        }
    }

    @Override
    protected void setRightClick() {
        // 修改UI
        resetUI();
        setRegisterType();
        // 清空数据
        clearAllData();
        // 重置倒计时
        mPresenter.closeTimer();
        setVertifyCodeBtEnabled(true);
        setVertifyCodeBtText(getString(R.string.send_vertify_code));
        setVertifyCodeLoadin(false);
        mBtRegistSendVertifyCode.setEnabled(false);
    }

    private void setRegisterType() {
        mLlRegisterByPhone.setVisibility(mCurrentRegisterType == REGISTER_PHONE ? View.VISIBLE :
                View.GONE);
        mLlRegisterByEmail.setVisibility(mCurrentRegisterType == REGISTER_EMAIL ? View.VISIBLE :
                View.GONE);
    }

    /**
     * 清空所有数据 在切换的时候使用
     */
    private void clearAllData() {
        mEtRegistUsername.setText("");
        mEtRegisterEmail.setText("");
        mEtRegistPhone.setText("");
        mEtRegistVertifyCode.setText("");
        mEtRegistPassword.setText("");
        isNameEdited = false;
        isEmailEdited = false;
        isPhoneEdited = false;
        isCodeEdited = false;
        isPassEdited = false;
        setConfirmEnable();
    }

    /**
     * 重置UI
     */
    private void resetUI() {
        mCurrentRegisterType = mCurrentRegisterType == REGISTER_PHONE ? REGISTER_EMAIL :
                REGISTER_PHONE;
        setRightText(mCurrentRegisterType == REGISTER_PHONE ?
                getString(R.string.email_address) : getString(R.string.phone_hint));
        setCenterText(mCurrentRegisterType == REGISTER_PHONE ?
                getString(R.string.register_by_phone) : getString(R.string.register_by_email));
        showMessage("");
    }
}
