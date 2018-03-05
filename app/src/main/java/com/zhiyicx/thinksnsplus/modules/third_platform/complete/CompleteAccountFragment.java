package com.zhiyicx.thinksnsplus.modules.third_platform.complete;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.widget.button.LoadingButton;
import com.zhiyicx.common.config.ConstantConfig;
import com.zhiyicx.common.utils.ActivityHandler;
import com.zhiyicx.imsdk.utils.common.DeviceUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.ThridInfoBean;
import com.zhiyicx.thinksnsplus.modules.register.rule.UserRuleActivity;
import com.zhiyicx.thinksnsplus.modules.third_platform.choose_bind.ChooseBindActivity;
import com.zhiyicx.thinksnsplus.modules.usertag.EditUserTagFragment;
import com.zhiyicx.thinksnsplus.modules.usertag.TagFrom;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/31
 * @contact email:648129313@qq.com
 */

public class CompleteAccountFragment extends TSFragment<CompleteAccountContract.Presenter>
        implements CompleteAccountContract.View {

    @BindView(R.id.et_login_phone)
    EditText mEtLoginPhone;
    @BindView(R.id.tv_error_tip)
    TextView mTvErrorTip;
    @BindView(R.id.tv_app_rule)
    TextView mAppRule;
    @BindView(R.id.bt_login_login)
    LoadingButton mBtLoginLogin;
    @BindView(R.id.iv_check)
    ImageView mIvCheck;


    private ThridInfoBean mThridInfoBean;

    public CompleteAccountFragment instance(Bundle bundle) {
        CompleteAccountFragment fragment = new CompleteAccountFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mThridInfoBean = getArguments().getParcelable(ChooseBindActivity.BUNDLE_THIRD_INFO);
        } else {
            throw new IllegalArgumentException("thrid info not be null");
        }
    }

    @Override
    protected void initView(View rootView) {
        //  应用名检查
        RxView.clicks(mIvCheck)
                .throttleFirst(ConstantConfig.JITTER_SPACING_TIME, TimeUnit.MILLISECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> {
                    if (mBtLoginLogin.isEnabled()) {

                    } else {
                        mEtLoginPhone.setText("");
                    }
                });
        // 下一步
        RxView.clicks(mBtLoginLogin)
                .throttleFirst(ConstantConfig.JITTER_SPACING_TIME, TimeUnit.MILLISECONDS)
                .compose(this.bindToLifecycle())
                .compose(mRxPermissions.ensure(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest
                        .permission.READ_PHONE_STATE))
                .subscribe(aBoolean -> {
                    if (aBoolean) {// 获取到了权限
                        mPresenter.thridRegister(mThridInfoBean, mEtLoginPhone.getText().toString());
                    } else {// 拒绝权限，但是可以再次请求
                        showErrorTips(getString(R.string.permisson_refused));
                    }
                });

        // 用户名输入框观察
        RxTextView.afterTextChangeEvents(mEtLoginPhone)
                .compose(this.bindToLifecycle())
                .subscribe(textViewAfterTextChangeEvent -> {
                    showErrorTips("");
                    setConfirmEnable(!TextUtils.isEmpty(textViewAfterTextChangeEvent.editable().toString().trim()));
                });
        try {
            mAppRule.setVisibility(mPresenter.getSystemConfigBean().getRegisterSettings().hasShowTerms() ? View.VISIBLE : View.GONE);
        } catch (NullPointerException e) {
            mAppRule.setVisibility(View.GONE);
        }
        mAppRule.setText(getString(R.string.app_rule, getString(R.string.app_name)));
        RxView.clicks(mAppRule)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> UserRuleActivity.startUserRuleActivity(getActivity(), getString(R.string.user_rule_register),
                        mPresenter.getSystemConfigBean().getRegisterSettings().getContent()));

    }

    @Override
    protected void initData() {
        mEtLoginPhone.setText(mThridInfoBean.getName());
        mEtLoginPhone.setSelection(mThridInfoBean.getName().length());
        mPresenter.checkName(mThridInfoBean, mThridInfoBean.getName());
        mBtLoginLogin.setEnabled(false);
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

    @Override
    public void checkNameSuccess(ThridInfoBean thridInfoBean, String name) {
        mIvCheck.setImageResource(R.mipmap.ico_edit_chosen_32);
        mIvCheck.setVisibility(View.VISIBLE);
        mBtLoginLogin.setEnabled(true);
    }

    @Override
    public void registerSuccess() {

        DeviceUtils.hideSoftKeyboard(getContext(), mEtLoginPhone);
        ActivityHandler.getInstance().finishAllActivityEcepteCurrent();// 清除 homeAcitivity 重新加载
        EditUserTagFragment.startToEditTagActivity(getActivity(), TagFrom.REGISTER, null);
        getActivity().finish();
    }

    /**
     * 设置登录按钮是否可点击
     */
    private void setConfirmEnable(boolean isEnable) {
        mBtLoginLogin.setEnabled(isEnable);
        if (isEnable) {
//            mIvCheck.setImageResource(R.mipmap.ico_edit_chosen_32);
            mIvCheck.setImageResource(R.mipmap.login_inputbox_clean);
            mIvCheck.setVisibility(View.VISIBLE);
        } else {
            mIvCheck.setVisibility(View.GONE);
        }
    }
}
