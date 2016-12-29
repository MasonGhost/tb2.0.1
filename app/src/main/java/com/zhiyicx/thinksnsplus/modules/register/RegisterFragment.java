package com.zhiyicx.thinksnsplus.modules.register;

import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxCompoundButton;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.thinksnsplus.R;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

public class RegisterFragment extends TSFragment<RegisterContract.Presenter> implements RegisterContract.View {
    public static final int PASSWORD_MAXLENGHT = 16;
    public static final int JITTER_SPACING_TIME = 2; // 抖动间隔时间，单位 s
    public static final int MOBILE_PHONE_NUMBER_LENGHT = 11; // 手机号码长度
    public static final int VERTIRY_CODE_LENGHT = 4; // 验证码长度

    @BindView(R.id.et_regist_nickname)
    EditText mEtRegistNickname;
    @BindView(R.id.et_regist_phone)
    EditText mEtRegistPhone;
    @BindView(R.id.bt_regist_send_vertify_code)
    Button mBtRegistSendVertifyCode;
    @BindView(R.id.pb_regist_loading)
    ImageView mPbRegistLoading;
    @BindView(R.id.et_regist_vertify_code)
    EditText mEtRegistVertifyCode;
    @BindView(R.id.et_regist_password)
    EditText mEtRegistPassword;
    @BindView(R.id.cb_login)
    CheckBox mCbLogin;
    @BindView(R.id.bt_regist_regist)
    Button mBtRegistRegist;

    private boolean isNameEdited;
    private boolean isPhoneEdited;
    private boolean isCodeEdited;
    private boolean isPassEdited;
    private boolean mIsVertifyCodeEnalbe = true;

    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
        return fragment;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_register;
    }

    @Override
    protected void initView(View rootView) {
        // 用户名观察
        RxTextView.textChanges(mEtRegistNickname)
                .compose(this.<CharSequence>bindToLifecycle())
                .subscribe(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence charSequence) {
                        isNameEdited = !TextUtils.isEmpty(charSequence.toString());
                        setConfirmEnable();
                    }
                });
        // 电话号码观察
        RxTextView.textChanges(mEtRegistPhone)
                .compose(this.<CharSequence>bindToLifecycle())
                .subscribe(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence charSequence) {
                        if (mIsVertifyCodeEnalbe) {
                            mBtRegistSendVertifyCode.setEnabled(charSequence.length() == MOBILE_PHONE_NUMBER_LENGHT);
                        }
                        isPhoneEdited = charSequence.length() == MOBILE_PHONE_NUMBER_LENGHT;
                        setConfirmEnable();
                    }
                });
        // 验证码观察
        RxTextView.textChanges(mEtRegistVertifyCode)
                .compose(this.<CharSequence>bindToLifecycle())
                .subscribe(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence charSequence) {
                        isCodeEdited = !TextUtils.isEmpty(charSequence.toString()) && charSequence.length() == VERTIRY_CODE_LENGHT;
                        setConfirmEnable();
                    }
                });
        // 密码观察
        RxTextView.textChanges(mEtRegistPassword)
                .compose(this.<CharSequence>bindToLifecycle())
                .subscribe(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence charSequence) {
                        isPassEdited = !TextUtils.isEmpty(charSequence.toString());
                        setConfirmEnable();
                        Editable editable = mEtRegistPassword.getText();
                        int len = editable.length();

                        if (len > PASSWORD_MAXLENGHT) {
                            int selEndIndex = Selection.getSelectionEnd(editable);
                            String str = editable.toString();
                            //截取新字符串
                            String newStr = str.substring(0, PASSWORD_MAXLENGHT);
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
                    }
                });


        // 是否显示密码
        RxCompoundButton.checkedChanges(mCbLogin)
                .compose(this.<Boolean>bindToLifecycle())
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (aBoolean) {
                            //如果选中，显示密码
                            mEtRegistPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            mEtRegistPassword.setSelection(mEtRegistPassword.getText().toString().length());
                        } else {
                            //否则隐藏密码
                            mEtRegistPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            mEtRegistPassword.setSelection(mEtRegistPassword.getText().toString().length());
                        }
                    }
                });
        // 点击发送验证码
        RxView.clicks(mBtRegistSendVertifyCode)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .compose(this.<Void>bindToLifecycle())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        mPresenter.getVertifyCode(mEtRegistPhone.getText().toString().trim());
                    }
                });
        // 点击注册按钮
        RxView.clicks(mBtRegistRegist)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.<Void>bindToLifecycle())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        mPresenter.register(mEtRegistPhone.getText().toString().trim()
                                , mEtRegistPhone.getText().toString().trim()
                                , mEtRegistVertifyCode.getText().toString().trim()
                                , mEtRegistPassword.getText().toString().trim()
                        );
                    }
                });
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void setPresenter(RegisterContract.Presenter presenter) {
        mPresenter = presenter;
    }


    @Override
    public void setVertifyCodeBtEnabled(boolean isEnable) {
        mIsVertifyCodeEnalbe = isEnable;
        mBtRegistSendVertifyCode.setEnabled(isEnable);
    }

    @Override
    public void setVertifyCodeBtText(String text) {
        mBtRegistSendVertifyCode.setText(text);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.immediate_regist);
    }

    /**
     * 设置确定按钮是否可点击
     */
    private void setConfirmEnable() {
        if (isNameEdited && isPhoneEdited && isCodeEdited && isPassEdited) {
            mBtRegistRegist.setEnabled(true);
        } else {
            mBtRegistRegist.setEnabled(false);
        }
    }


}
