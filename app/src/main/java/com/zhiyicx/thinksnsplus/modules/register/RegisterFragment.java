package com.zhiyicx.thinksnsplus.modules.register;

import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.widget.edittext.DeleteEditText;
import com.zhiyicx.baseproject.widget.edittext.PasswordEditText;
import com.zhiyicx.thinksnsplus.R;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;
import static com.zhiyicx.common.config.ConstantConfig.MOBILE_PHONE_NUMBER_LENGHT;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/4
 * @Contact master.jungle68@gmail.com
 */
public class RegisterFragment extends TSFragment<RegisterContract.Presenter> implements RegisterContract.View {
    @BindView(R.id.et_regist_username)
    DeleteEditText mEtRegistUsername;
    @BindView(R.id.et_regist_phone)
    DeleteEditText mEtRegistPhone;
    @BindView(R.id.bt_regist_send_vertify_code)
    Button mBtRegistSendVertifyCode;
    @BindView(R.id.pb_regist_loading)
    ImageView mPbRegistLoading;
    @BindView(R.id.et_regist_vertify_code)
    DeleteEditText mEtRegistVertifyCode;
    @BindView(R.id.et_regist_password)
    PasswordEditText mEtRegistPassword;
    @BindView(R.id.bt_regist_regist)
    Button mBtRegistRegist;
    @BindView(R.id.tv_error_tip)
    TextView mTvErrorTip;
    @BindView(R.id.tv_look_around)
    TextView mTvLookAround;

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
    protected String setCenterTitle() {
        return getString(R.string.immediate_regist);
    }

    @Override
    protected void initView(View rootView) {
        // 用户名观察
        RxTextView.textChanges(mEtRegistUsername)
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
                        isPhoneEdited = !TextUtils.isEmpty(charSequence.toString());
                        setConfirmEnable();
                    }
                });
        // 验证码观察
        RxTextView.textChanges(mEtRegistVertifyCode)
                .compose(this.<CharSequence>bindToLifecycle())
                .subscribe(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence charSequence) {
                        isCodeEdited = !TextUtils.isEmpty(charSequence.toString());
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

                        if (len > getResources().getInteger(R.integer.password_maxlenght)) {
                            int selEndIndex = Selection.getSelectionEnd(editable);
                            String str = editable.toString();
                            //截取新字符串
                            String newStr = str.substring(0, getResources().getInteger(R.integer.password_maxlenght));
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
                        mPresenter.register(mEtRegistUsername.getText().toString().trim()
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
        if (TextUtils.isEmpty(message)) {
            mTvErrorTip.setVisibility(View.INVISIBLE);
            System.out.println(" =INVISIBLE ");
        } else {
            mTvErrorTip.setVisibility(View.VISIBLE);
            mTvErrorTip.setText(message);
            System.out.println(" =VISIBLE " + message);
        }
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
