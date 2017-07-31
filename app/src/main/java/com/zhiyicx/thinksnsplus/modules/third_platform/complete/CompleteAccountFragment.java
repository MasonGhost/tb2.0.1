package com.zhiyicx.thinksnsplus.modules.third_platform.complete;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.widget.button.LoadingButton;
import com.zhiyicx.baseproject.widget.edittext.DeleteEditText;
import com.zhiyicx.thinksnsplus.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/31
 * @contact email:648129313@qq.com
 */

public class CompleteAccountFragment extends TSFragment<CompleteAccountContract.Presenter>
        implements CompleteAccountContract.View {

    @BindView(R.id.et_login_phone)
    DeleteEditText mEtLoginPhone;
    @BindView(R.id.tv_error_tip)
    TextView mTvErrorTip;
    @BindView(R.id.bt_login_login)
    LoadingButton mBtLoginLogin;

    private boolean mIsPhoneEdited;

    public CompleteAccountFragment instance(Bundle bundle) {
        CompleteAccountFragment fragment = new CompleteAccountFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initView(View rootView) {

    }

    @Override
    protected void initData() {
        // 用户名输入框观察
        RxTextView.textChanges(mEtLoginPhone)
                .compose(this.<CharSequence>bindToLifecycle())
                .subscribe(charSequence -> {
                    mIsPhoneEdited = !TextUtils.isEmpty(charSequence.toString());
                    setConfirmEnable();
                });
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_complete_accouont;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.third_platform_complete_account);
    }

    @Override
    protected boolean usePermisson() {
        return true;
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
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
        mBtLoginLogin.setEnabled(mIsPhoneEdited);
    }
}
