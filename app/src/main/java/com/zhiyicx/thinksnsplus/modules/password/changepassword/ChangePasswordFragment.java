package com.zhiyicx.thinksnsplus.modules.password.changepassword;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.widget.edittext.PasswordEditText;
import com.zhiyicx.thinksnsplus.R;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/10
 * @Contact master.jungle68@gmail.com
 */
public class ChangePasswordFragment extends TSFragment<ChangePasswordContract.Presenter> implements ChangePasswordContract.View {


    @BindView(R.id.et_old_password)
    PasswordEditText mEtOldPassword;
    @BindView(R.id.et_new_password)
    PasswordEditText mEtNewPassword;
    @BindView(R.id.et_sure_new_password)
    PasswordEditText mEtSureNewPassword;
    @BindView(R.id.tv_error_tip)
    TextView mTvErrorTip;

    private boolean isOldPasswordEdited;
    private boolean isNewPasswordEdited;
    private boolean isSureNewPasswordEdited;

    public static ChangePasswordFragment newInstance() {
        ChangePasswordFragment fragment = new ChangePasswordFragment();
        return fragment;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_change_password;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.change_password);
    }

    @Override
    protected String setRightTitle() {
        return getString(R.string.change);
    }

    /**
     * toolbar右边点击
     */
    @Override
    protected void setRightClick() {
        mPresenter.changePassword(mEtOldPassword.getText().toString().trim()
                , mEtNewPassword.getText().toString().trim(), mEtSureNewPassword.getText().toString().trim());
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
        // 旧密码观察
        RxTextView.textChanges(mEtOldPassword)
                .compose(this.<CharSequence>bindToLifecycle())
                .subscribe(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence charSequence) {
                        isOldPasswordEdited = !TextUtils.isEmpty(charSequence.toString());
                        setConfirmEnable();
                    }
                });
        // 旧密码观察
        RxTextView.textChanges(mEtNewPassword)
                .compose(this.<CharSequence>bindToLifecycle())
                .subscribe(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence charSequence) {
                        isNewPasswordEdited = !TextUtils.isEmpty(charSequence.toString());
                        setConfirmEnable();
                    }
                });
        // 旧密码观察
        RxTextView.textChanges(mEtSureNewPassword)
                .compose(this.<CharSequence>bindToLifecycle())
                .subscribe(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence charSequence) {
                        isSureNewPasswordEdited = !TextUtils.isEmpty(charSequence.toString());
                        setConfirmEnable();
                    }
                });
    }

    @Override
    protected void initData() {
    }

    @Override
    public void setPresenter(ChangePasswordContract.Presenter presenter) {
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
        if (TextUtils.isEmpty(message)) {
            mTvErrorTip.setVisibility(View.INVISIBLE);
        } else {
            mTvErrorTip.setVisibility(View.VISIBLE);
            mTvErrorTip.setText(message);
        }
    }

    /**
     * 更改按钮是否可用
     */
    private void setConfirmEnable() {
        if (isOldPasswordEdited && isNewPasswordEdited && isSureNewPasswordEdited) {
            mToolbarRight.setEnabled(true);
        } else {
            mToolbarRight.setEnabled(false);
        }
    }


    @Override
    public void finsh() {
        getActivity().finish();
    }
}
