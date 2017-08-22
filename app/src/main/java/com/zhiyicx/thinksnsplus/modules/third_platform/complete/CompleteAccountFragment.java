package com.zhiyicx.thinksnsplus.modules.third_platform.complete;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewAfterTextChangeEvent;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.widget.button.LoadingButton;
import com.zhiyicx.baseproject.widget.edittext.DeleteEditText;
import com.zhiyicx.common.config.ConstantConfig;
import com.zhiyicx.common.utils.ActivityHandler;
import com.zhiyicx.imsdk.utils.common.DeviceUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.ThridInfoBean;
import com.zhiyicx.thinksnsplus.modules.third_platform.choose_bind.ChooseBindActivity;
import com.zhiyicx.thinksnsplus.modules.usertag.EditUserTagFragment;
import com.zhiyicx.thinksnsplus.modules.usertag.TagFrom;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.functions.Action1;

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
        // 下一步
        RxView.clicks(mBtLoginLogin)
                .throttleFirst(ConstantConfig.JITTER_SPACING_TIME, TimeUnit.MILLISECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> {
                    if (mBtLoginLogin.isEnabled()) {

                    } else {
                      mEtLoginPhone.setText("");
                    }
                });
        // 应用名检查
        RxView.clicks(mIvCheck)
                .throttleFirst(ConstantConfig.JITTER_SPACING_TIME, TimeUnit.MILLISECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> {
                    mPresenter.thridRegister(mThridInfoBean, mEtLoginPhone.getText().toString());
                });
        // 用户名输入框观察
        RxTextView.afterTextChangeEvents(mEtLoginPhone)
                .compose(this.bindToLifecycle())
                .subscribe(textViewAfterTextChangeEvent -> {
                    setConfirmEnable(false);
                    if (!TextUtils.isEmpty(textViewAfterTextChangeEvent.editable().toString())) {
                        mPresenter.checkName(mThridInfoBean, textViewAfterTextChangeEvent.editable().toString());
                    }
                });

    }

    @Override
    protected void initData() {
        mEtLoginPhone.setText(mThridInfoBean.getName());
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
        showErrorTips("");
        setConfirmEnable(true);
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
            mIvCheck.setImageResource(R.mipmap.common_ico_bottom_home_normal);
        } else {
            mIvCheck.setImageResource(R.mipmap.login_inputbox_clean);

        }
    }
}
