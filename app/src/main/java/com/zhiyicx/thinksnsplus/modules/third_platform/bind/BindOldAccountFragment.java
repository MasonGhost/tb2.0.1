package com.zhiyicx.thinksnsplus.modules.third_platform.bind;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.widget.button.LoadingButton;
import com.zhiyicx.thinksnsplus.R;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @author Catherine
 * @describe  绑定已有账号失败的情况下，直接返回到完善资料页面
 * @date 2017/7/31
 * @contact email:648129313@qq.com
 */

public class BindOldAccountFragment extends TSFragment<BindOldAccountContract.Presenter>
        implements BindOldAccountContract.View{

    @BindView(R.id.et_login_phone)
    EditText mEtLoginPhone;
    @BindView(R.id.et_login_password)
    EditText mEtLoginPassword;
    @BindView(R.id.bt_login_login)
    LoadingButton mBtLoginLogin;
    @BindView(R.id.tv_error_tip)
    TextView mTvErrorTip;
    @BindView(R.id.et_complete_input)
    AppCompatAutoCompleteTextView mEtCompleteInput;

    private boolean mIsPhoneEdited;
    private boolean mIsPasswordEdited;

    public BindOldAccountFragment instance(Bundle bundle){
        BindOldAccountFragment fragment = new BindOldAccountFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initView(View rootView) {
        mEtCompleteInput.setVisibility(View.GONE);
        mEtLoginPhone.setVisibility(View.VISIBLE);
        mBtLoginLogin.setText(getString(R.string.third_platform_bind_confirm));
    }

    @Override
    protected void initData() {
        initListener();
    }

    private void initListener() {
        // 手机号码输入框观察
        RxTextView.textChanges(mEtLoginPhone)
                .compose(this.<CharSequence>bindToLifecycle())
                .subscribe(charSequence -> {
                    mIsPhoneEdited = !TextUtils.isEmpty(charSequence.toString());
                    setConfirmEnable();
                });
        // 密码输入框观察
        RxTextView.textChanges(mEtLoginPassword)
                .compose(this.<CharSequence>bindToLifecycle())
                .subscribe(charSequence -> {
                    mIsPasswordEdited = !TextUtils.isEmpty(charSequence.toString());
                    setConfirmEnable();
                });
        // 点击登录按钮
        RxView.clicks(mBtLoginLogin)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.<Void>bindToLifecycle())
                .compose(mRxPermissions.ensure(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE))
                .subscribe(aBoolean -> {
                    if (aBoolean) {// 获取到了权限

                    } else {// 拒绝权限，但是可以再次请求
                        showErrorTips(getString(R.string.permisson_refused));
                    }
                });
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_bind_old_account;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.third_platform_exist_account);
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

    @Override
    protected boolean usePermisson() {
        return true;
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
}
