package com.zhiyicx.thinksnsplus.modules.password.findpassword;

import android.graphics.drawable.Animatable;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.widget.button.LoadingButton;
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
 * @Date 2017/1/11
 * @Contact master.jungle68@gmail.com
 */
public class FindPasswordFragment extends TSFragment<FindPasswordContract.Presenter> implements FindPasswordContract.View {

    @BindView(R.id.tv_error_tip)
    TextView mTvErrorTip;
    @BindView(R.id.et_phone)
    DeleteEditText mEtPhone;
    @BindView(R.id.bt_send_vertify_code)
    Button mBtSendVertifyCode;
    @BindView(R.id.iv_vertify_loading)
    ImageView mIvVertifyLoading;
    @BindView(R.id.rl_send_vertify_code_container)
    RelativeLayout mRlSendVertifyCodeContainer;
    @BindView(R.id.et_vertify_code)
    DeleteEditText mEtVertifyCode;
    @BindView(R.id.et_password)
    PasswordEditText mEtPassword;
    @BindView(R.id.bt_sure)
    LoadingButton mBtSure;

    private boolean isPhoneEdited;
    private boolean isCodeEdited;
    private boolean isPassEdited;
    private boolean mIsVertifyCodeEnalbe = true;
    private Animatable mVertifyAnimationDrawable;
    private boolean isSureLoading;

    public static FindPasswordFragment newInstance() {
        FindPasswordFragment fragment = new FindPasswordFragment();
        return fragment;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_find_password;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.find_password);
    }


    @Override
    protected int setToolBarBackgroud() {
        return R.color.white;
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected void initView(View rootView) {
        mVertifyAnimationDrawable = (Animatable) mIvVertifyLoading.getDrawable();
        // 电话号码观察
        RxTextView.textChanges(mEtPhone)
                .compose(this.<CharSequence>bindToLifecycle())
                .subscribe(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence charSequence) {
                        if (mIsVertifyCodeEnalbe) {
                            mBtSendVertifyCode.setEnabled(charSequence.length() == MOBILE_PHONE_NUMBER_LENGHT);
                        }
                        isPhoneEdited = !TextUtils.isEmpty(charSequence.toString());
                        setConfirmEnable();
                    }
                });
        // 验证码观察
        RxTextView.textChanges(mEtVertifyCode)
                .compose(this.<CharSequence>bindToLifecycle())
                .subscribe(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence charSequence) {
                        isCodeEdited = !TextUtils.isEmpty(charSequence.toString());
                        setConfirmEnable();
                    }
                });
        // 密码观察
        RxTextView.textChanges(mEtPassword)
                .compose(this.<CharSequence>bindToLifecycle())
                .subscribe(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence charSequence) {
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
                    }
                });


        // 点击发送验证码
        RxView.clicks(mBtSendVertifyCode)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .compose(this.<Void>bindToLifecycle())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        mPresenter.getVertifyCode(mEtPhone.getText().toString().trim());
                    }
                });
        // 点击注册按钮
        RxView.clicks(mBtSure)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.<Void>bindToLifecycle())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        mPresenter.findPassword(mEtPhone.getText().toString().trim()
                                , mEtVertifyCode.getText().toString().trim()
                                , mEtPassword.getText().toString().trim()
                        );
                    }
                });
    }

    @Override
    protected void initData() {
        setConfirmEnable();
    }


    @Override
    public void setPresenter(FindPasswordContract.Presenter presenter) {
        this.mPresenter = presenter;
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
        } else {
            mTvErrorTip.setVisibility(View.VISIBLE);
            mTvErrorTip.setText(message);
        }
    }

    @Override
    public void setVertifyCodeBtEnabled(boolean isEnable) {
        mIsVertifyCodeEnalbe = isEnable;
        mBtSendVertifyCode.setEnabled(isEnable);
    }

    @Override
    public void setVertifyCodeLoading(boolean isEnable) {
        if (isEnable) {
            mIvVertifyLoading.setVisibility(View.VISIBLE);
            mBtSendVertifyCode.setVisibility(View.INVISIBLE);
            mVertifyAnimationDrawable.start();
        } else {
            mVertifyAnimationDrawable.stop();
            mIvVertifyLoading.setVisibility(View.INVISIBLE);
            mBtSendVertifyCode.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setVertifyCodeBtText(String text) {
        mBtSendVertifyCode.setText(text);
    }

    @Override
    public void finsh() {
        getActivity().finish();
    }

    @Override
    public void setSureBtEnabled(boolean isEnable) {
        mBtSure.handleAnimation(!isEnable);
        isSureLoading = !isEnable;
    }


    /**
     * 设置确定按钮是否可点击
     */
    private void setConfirmEnable() {
        if (isPhoneEdited && isCodeEdited && isPassEdited && !isSureLoading) {
            mBtSure.setEnabled(true);
        } else {
            mBtSure.setEnabled(false);
        }
    }
}

