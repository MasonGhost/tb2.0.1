package com.zhiyicx.thinksnsplus.modules.settings.init_password;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.widget.button.LoadingButton;
import com.zhiyicx.baseproject.widget.edittext.PasswordEditText;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.settings.bind.AccountBindActivity;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;
import static com.zhiyicx.thinksnsplus.modules.settings.bind.AccountBindActivity.BUNDLE_BIND_DATA;
import static com.zhiyicx.thinksnsplus.modules.settings.bind.AccountBindActivity.BUNDLE_BIND_STATE;
import static com.zhiyicx.thinksnsplus.modules.settings.bind.AccountBindActivity.BUNDLE_BIND_TYPE;

/**
 * @author Catherine
 * @describe
 * @date 2017/9/18
 * @contact email:648129313@qq.com
 */

public class InitPasswordFragment extends TSFragment<InitPasswordContract.Presenter> implements InitPasswordContract.View {

    @BindView(R.id.tv_error_tip)
    TextView mTvErrorTip;
    @BindView(R.id.et_password)
    PasswordEditText mEtPassword;
    @BindView(R.id.ll_container_password)
    LinearLayout mLlContainerPassword;
    @BindView(R.id.et_sure_password)
    PasswordEditText mEtSurePassword;
    @BindView(R.id.ll_container_sure_password)
    LinearLayout mLlContainerSurePassword;
    @BindView(R.id.bt_sure)
    LoadingButton mBtSure;

    private boolean mIsPassEdited;
    private boolean mIsSurePassEdited;

    private int mBindType;
    private UserInfoBean mCurrentUser;
    private boolean mBindState;

    public InitPasswordFragment instance(Bundle bundle){
        InitPasswordFragment fragment = new InitPasswordFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initView(View rootView) {
        mBtSure.setText(getString(R.string.next));
        initListener();
    }

    @Override
    protected void initData() {
        mBindType = getArguments().getInt(BUNDLE_BIND_TYPE);
        mCurrentUser = getArguments().getParcelable(BUNDLE_BIND_DATA);
        mBindState = getArguments().getBoolean(BUNDLE_BIND_STATE);
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_init_password;
    }

    @Override
    protected boolean showToolbar() {
        return true;
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
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
    protected String setCenterTitle() {
        return getString(R.string.set_password);
    }

    private void initListener() {
        // 密码观察
        RxTextView.textChanges(mEtPassword)
                .compose(this.bindToLifecycle())
                .subscribe(charSequence -> {
                    mIsPassEdited = !TextUtils.isEmpty(charSequence.toString());
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
                });    // 密码观察
        RxTextView.textChanges(mEtSurePassword)
                .compose(this.bindToLifecycle())
                .subscribe(charSequence -> {
                    mIsSurePassEdited = !TextUtils.isEmpty(charSequence.toString());
                    setConfirmEnable();
                    Editable editable = mEtSurePassword.getText();
                    int len = editable.length();
                    if (len > getResources().getInteger(R.integer.password_maxlenght)) {
                        int selEndIndex = Selection.getSelectionEnd(editable);
                        String str = editable.toString();
                        //截取新字符串
                        String newStr = str.substring(0, getResources().getInteger(R.integer.password_maxlenght));
                        mEtSurePassword.setText(newStr);
                        editable = mEtSurePassword.getText();
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
        // 点击下一步
        RxView.clicks(mBtSure)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> {
                   // 设置密码，成功后进入绑定页面
                    mPresenter.initPassword(String.valueOf(mEtPassword.getText()), String.valueOf(mEtSurePassword.getText()));
                    mBtSure.setEnabled(false);
                });
    }

    /**
     * 设置确认按钮是否可点击
     */
    private void setConfirmEnable() {
        mBtSure.setEnabled(mIsPassEdited && mIsSurePassEdited);
    }

    @Override
    public void initPasswordResult(boolean isSuccess) {
        if (isSuccess){
            // 设置成功后，跳转到绑定页面
            Intent intent = new Intent(getActivity(), AccountBindActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt(BUNDLE_BIND_TYPE, mBindType);
            bundle.putBoolean(BUNDLE_BIND_STATE, mBindState);
            bundle.putParcelable(BUNDLE_BIND_DATA, mCurrentUser);
            intent.putExtras(bundle);
            startActivity(intent);
            // 关闭这个页面
            getActivity().finish();
        } else {
            mBtSure.setEnabled(true);
        }
    }

    @Override
    public UserInfoBean getCurrentUser() {
        return mCurrentUser;
    }
}
