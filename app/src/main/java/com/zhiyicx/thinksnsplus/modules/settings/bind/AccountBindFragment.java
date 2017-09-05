package com.zhiyicx.thinksnsplus.modules.settings.bind;

import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.widget.button.LoadingButton;
import com.zhiyicx.baseproject.widget.edittext.DeleteEditText;
import com.zhiyicx.baseproject.widget.edittext.PasswordEditText;
import com.zhiyicx.common.utils.RegexUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;
import static com.zhiyicx.common.config.ConstantConfig.MOBILE_PHONE_NUMBER_LENGHT;
import static com.zhiyicx.thinksnsplus.modules.settings.bind.AccountBindActivity.BUNDLE_BIND_DATA;
import static com.zhiyicx.thinksnsplus.modules.settings.bind.AccountBindActivity.BUNDLE_BIND_STATE;
import static com.zhiyicx.thinksnsplus.modules.settings.bind.AccountBindActivity.BUNDLE_BIND_TYPE;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/28
 * @contact email:648129313@qq.com
 */

public class AccountBindFragment extends TSFragment<AccountBindContract.Presenter>
        implements AccountBindContract.View {

    public static final int DEAL_TYPE_PHONE = 0; // 处理手机绑定
    public static final int DEAL_TYPE_EMAIL = 1; // 处理邮箱绑定

    @BindView(R.id.et_phone)
    DeleteEditText mEtPhone;
    @BindView(R.id.ll_bind_phone)
    LinearLayout mLlBindPhone;
    @BindView(R.id.et_email)
    DeleteEditText mEtEmail;
    @BindView(R.id.ll_bind_email)
    LinearLayout mLlBindEmail;
    @BindView(R.id.et_verify_code)
    DeleteEditText mEtVerifyCode;
    @BindView(R.id.et_password)
    PasswordEditText mEtPassword;
    @BindView(R.id.tv_error_tip)
    TextView mTvErrorTip;
    @BindView(R.id.bt_sure)
    LoadingButton mBtSure;
    @BindView(R.id.bt_send_verify_code)
    TextView mBtSendVerifyCode;
    @BindView(R.id.iv_verify_loading)
    ImageView mIvVerifyLoading;
    @BindView(R.id.rl_send_verify_code_container)
    RelativeLayout mRlSendVerifyCodeContainer;

    private int mCurrentType = DEAL_TYPE_PHONE;
    private boolean mIsBind = false; // 是否已经绑定

    private boolean isPhoneEdited;
    private boolean isEmailEdited;
    private boolean isCodeEdited;
    private boolean isPassEdited;
    private boolean mIsVerifyCodeEnable = true;
    private Animatable mVerifyAnimationDrawable;
    private boolean isSureLoading;
    private UserInfoBean mUserInfoBean;

    public AccountBindFragment instance(Bundle bundle) {
        AccountBindFragment fragment = new AccountBindFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initView(View rootView) {
        Bundle bundle = getArguments();
        mCurrentType = bundle.getInt(BUNDLE_BIND_TYPE);
        mIsBind = bundle.getBoolean(BUNDLE_BIND_STATE);
        mUserInfoBean = bundle.getParcelable(BUNDLE_BIND_DATA);
        mVerifyAnimationDrawable = (Animatable) mIvVerifyLoading.getDrawable();
        setBindType();
        setConfirmEnable();
        initListener();

        if (mUserInfoBean != null && mIsBind) {
            if (mCurrentType == DEAL_TYPE_PHONE) {
                mEtPhone.setText(mUserInfoBean.getPhone());
                mEtPhone.setSelection(mUserInfoBean.getPhone().length());
            } else {
                mEtEmail.setText(mUserInfoBean.getEmail());
                mEtEmail.setSelection(mUserInfoBean.getEmail().length());
            }

        }
    }

    @Override
    protected void initData() {
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_account_bind;
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
    public void setSureBtEnabled(boolean isEnable) {
        mBtSure.handleAnimation(!isEnable);
        isSureLoading = !isEnable;
    }

    @Override
    public void setVerifyCodeBtEnabled(boolean isEnable) {
        mIsVerifyCodeEnable = isEnable;
        mBtSendVerifyCode.setEnabled(isEnable);
    }

    @Override
    public void setVerifyCodeLoading(boolean isEnable) {
        if (isEnable) {
            mIvVerifyLoading.setVisibility(View.VISIBLE);
            mBtSendVerifyCode.setVisibility(View.INVISIBLE);
            mVerifyAnimationDrawable.start();
        } else {
            mVerifyAnimationDrawable.stop();
            mIvVerifyLoading.setVisibility(View.INVISIBLE);
            mBtSendVerifyCode.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setVerifyCodeBtText(String text) {
        mBtSendVerifyCode.setText(text);
    }


    @Override
    public void unBindPhoneOrEmailSuccess(boolean isPhone) {
        getActivity().finish();
    }

    @Override
    public void BindPhoneOrEmailSuccess(boolean isPhone) {
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

    private void initListener() {
        // 电话号码观察
        RxTextView.textChanges(mEtPhone)
                .compose(this.<CharSequence>bindToLifecycle())
                .subscribe(charSequence -> {
                    if (mIsVerifyCodeEnable) {
                        mBtSendVerifyCode.setEnabled(charSequence.length() == MOBILE_PHONE_NUMBER_LENGHT);
                    }
                    isPhoneEdited = !TextUtils.isEmpty(charSequence.toString());
                    setConfirmEnable();
                });
        // 邮箱输入观察
        RxTextView.textChanges(mEtEmail)
                .compose(this.<CharSequence>bindToLifecycle())
                .subscribe(charSequence -> {
                    if (mIsVerifyCodeEnable) {
                        mBtSendVerifyCode.setEnabled(RegexUtils.isEmail(charSequence));
                    }
                    isEmailEdited = !TextUtils.isEmpty(charSequence.toString());
                    setConfirmEnable();
                });
        // 验证码观察
        RxTextView.textChanges(mEtVerifyCode)
                .compose(this.<CharSequence>bindToLifecycle())
                .subscribe(charSequence -> {
                    isCodeEdited = !TextUtils.isEmpty(charSequence.toString());
                    setConfirmEnable();
                });
        // 密码观察
        RxTextView.textChanges(mEtPassword)
                .compose(this.<CharSequence>bindToLifecycle())
                .subscribe(charSequence -> {
                    isPassEdited = !TextUtils.isEmpty(charSequence.toString());
                    setConfirmEnable();
                    Editable editable = mEtPassword.getText();
                    int len = editable.length();
                    if (len > getResources().getInteger(R.integer.password_maxlenght)) {
                        int selEndIndex = Selection.getSelectionEnd(editable);
                        String str = editable.toString();
                        //截取新字符串
                        String newStr = str.substring(0, getResources().getInteger(R.integer.password_maxlenght));
                        mEtPassword.setText(newStr);
                        editable = mEtPassword.getText();
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
        RxView.clicks(mBtSendVerifyCode)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .compose(this.<Void>bindToLifecycle())
                .subscribe(aVoid -> {
                    if (mCurrentType == DEAL_TYPE_PHONE) {
                        mPresenter.getVertifyCode(mEtPhone.getText().toString().trim(), mIsBind);
                    } else {
                        mPresenter.getVerifyCodeByEmail(mEtEmail.getText().toString().trim(), mIsBind);
                    }
                });
        // 点击绑定/解除按钮
        RxView.clicks(mBtSure)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.<Void>bindToLifecycle())
                .subscribe(aVoid -> {
                    if (mIsBind) {// 解绑
                        mPresenter.unBindPhoneOrEmail(mEtPassword.getText().toString(), mEtVerifyCode.getText().toString(), mCurrentType == DEAL_TYPE_PHONE);
                    } else {// 绑定
                        mPresenter.bindPhoneOrEmail(mEtPhone.getText().toString(), mEtEmail.getText().toString(), mEtVerifyCode.getText().toString(), mCurrentType == DEAL_TYPE_PHONE);
                    }


                });
    }

    /**
     * 处理操作的类型 0-手机 1-邮箱
     */
    private void setBindType() {
        boolean isPhone = mCurrentType == DEAL_TYPE_PHONE;
        mLlBindPhone.setVisibility(isPhone ? View.VISIBLE : View.GONE);
        mLlBindEmail.setVisibility(!isPhone ? View.VISIBLE : View.GONE);
        if (mIsBind) {
            setCenterText(isPhone ? getString(R.string.remove_binding) + getString(R.string.phone_number) :
                    getString(R.string.remove_binding) + getString(R.string.email_address));
            mBtSure.setText(getString(R.string.remove_binding));
        } else {
            setCenterText(isPhone ? getString(R.string.binding) + getString(R.string.phone_number) :
                    getString(R.string.binding) + getString(R.string.email_address));
            mBtSure.setText(getString(R.string.binding));
        }

    }

    /**
     * 设置绑定按钮是否可点击
     */
    private void setConfirmEnable() {
        if (isCodeEdited && isPassEdited && !isSureLoading) {
            if ((mCurrentType == DEAL_TYPE_PHONE && isPhoneEdited)
                    || (mCurrentType == DEAL_TYPE_EMAIL && isEmailEdited)) {
                mBtSure.setEnabled(true);
            } else {
                mBtSure.setEnabled(false);
            }
        } else {
            mBtSure.setEnabled(false);
        }
    }
}
