package com.zhiyicx.thinksnsplus.modules.password.findpassword;

import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;
import static com.zhiyicx.common.config.ConstantConfig.MOBILE_PHONE_NUMBER_LENGHT;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/11
 * @Contact master.jungle68@gmail.com
 */
public class FindPasswordFragment extends TSFragment<FindPasswordContract.Presenter> implements FindPasswordContract.View {

    private static final int FIND_BY_PHONE = 1;
    private static final int FIND_BY_EMAIL = 2;

    @BindView(R.id.tv_error_tip)
    TextView mTvErrorTip;
    @BindView(R.id.et_phone)
    DeleteEditText mEtPhone;
    @BindView(R.id.bt_send_vertify_code)
    TextView mBtSendVertifyCode;
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
    @BindView(R.id.ll_find_by_phone)
    LinearLayout mLlFindByPhone;
    @BindView(R.id.et_emial)
    DeleteEditText mEtEmial;
    @BindView(R.id.ll_find_by_email)
    LinearLayout mLlFindByEmail;

    private boolean isPhoneEdited;
    private boolean isEmailEdited;
    private boolean isCodeEdited;
    private boolean isPassEdited;
    private boolean mIsVertifyCodeEnalbe = true;
    private Animatable mVertifyAnimationDrawable;
    private boolean isSureLoading;

    private int mCurrentType = FIND_BY_PHONE;

    public static FindPasswordFragment newInstance() {
        return new FindPasswordFragment();
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_find_password;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.forget_password);
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected void initView(View rootView) {
        setTitleRightByType();
        mVertifyAnimationDrawable = (Animatable) mIvVertifyLoading.getDrawable();
        // 电话号码观察
        RxTextView.textChanges(mEtPhone)
                .compose(this.bindToLifecycle())
                .subscribe(charSequence -> {
                    if (mIsVertifyCodeEnalbe) {
                        mBtSendVertifyCode.setEnabled(charSequence.length() == MOBILE_PHONE_NUMBER_LENGHT);
                    }
                    isPhoneEdited = !TextUtils.isEmpty(charSequence.toString());
                    setConfirmEnable();
                });
        RxTextView.textChanges(mEtEmial)
                .compose(this.bindToLifecycle())
                .subscribe(charSequence -> {
                    if (mIsVertifyCodeEnalbe) {
                        mBtSendVertifyCode.setEnabled(RegexUtils.isEmail(charSequence));
                    }
                    isEmailEdited = !TextUtils.isEmpty(charSequence.toString());
                    setConfirmEnable();
                });
        // 验证码观察
        RxTextView.textChanges(mEtVertifyCode)
                .compose(this.bindToLifecycle())
                .subscribe(charSequence -> {
                    isCodeEdited = !TextUtils.isEmpty(charSequence.toString());
                    setConfirmEnable();
                });
        // 密码观察
        RxTextView.textChanges(mEtPassword)
                .compose(this.bindToLifecycle())
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
        RxView.clicks(mBtSendVertifyCode)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> {
                    if (mCurrentType == FIND_BY_PHONE){
                        mPresenter.getVertifyCode(mEtPhone.getText().toString().trim());
                    } else {
                        mPresenter.getVerifyCodeByEmail(mEtEmial.getText().toString().trim());
                    }
                });
        // 点击注册按钮
        RxView.clicks(mBtSure)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> {
                    if (mCurrentType == FIND_BY_PHONE){
                        mPresenter.findPassword(mEtPhone.getText().toString().trim()
                                , mEtVertifyCode.getText().toString().trim()
                                , mEtPassword.getText().toString().trim());
                    } else {
                        mPresenter.findPasswordByEmail(mEtEmial.getText().toString().trim()
                                , mEtVertifyCode.getText().toString().trim()
                                , mEtPassword.getText().toString().trim());
                    }
                });
    }

    @Override
    protected void initData() {
        setConfirmEnable();
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

    @Override
    protected void setRightClick() {
        // 重置按钮状态
        resetUI();
        // 重置UI
        setRightText(mCurrentType == FIND_BY_PHONE ?
                getString(R.string.find_password_by_email) : getString(R.string.find_password_by_phone));
        // 清空数据
        clearAllData();
    }

    /**
     * 设置确定按钮是否可点击
     */
    private void setConfirmEnable() {
        if (isCodeEdited && isPassEdited && !isSureLoading) {
            if ((mCurrentType == FIND_BY_PHONE && isPhoneEdited)
                    || (mCurrentType == FIND_BY_EMAIL && isEmailEdited)){
                mBtSure.setEnabled(true);
            } else {
                mBtSure.setEnabled(false);
            }
        } else {
            mBtSure.setEnabled(false);
        }
    }

    /**
     * 清空界面数据
     */
    private void clearAllData() {
        mEtPhone.setText("");
        mEtEmial.setText("");
        mEtPassword.setText("");
        mEtVertifyCode.setText("");
        isPhoneEdited = false;
        isEmailEdited = false;
        isCodeEdited = false;
        isPassEdited = false;
        setConfirmEnable();
    }

    private void resetUI(){
        mCurrentType = (mCurrentType == FIND_BY_PHONE) ? FIND_BY_EMAIL : FIND_BY_PHONE;
        setTitleRightByType();
        setVertifyCodeLoading(false);
        setSureBtEnabled(true);
        setVertifyCodeBtEnabled(false);
        mIsVertifyCodeEnalbe = true;
        showMessage("");
        if (mCurrentType == FIND_BY_EMAIL){
            mEtEmial.requestFocus();
        }
    }

    private void setTitleRightByType(){
        mLlFindByPhone.setVisibility(mCurrentType == FIND_BY_PHONE ? View.VISIBLE : View.GONE);
        mLlFindByEmail.setVisibility(mCurrentType == FIND_BY_EMAIL ? View.VISIBLE : View.GONE);
    }
}
