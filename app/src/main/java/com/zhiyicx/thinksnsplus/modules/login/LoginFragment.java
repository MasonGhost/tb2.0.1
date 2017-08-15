package com.zhiyicx.thinksnsplus.modules.login;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.umeng.qq.handler.UmengQQHandler;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.impl.share.UmengSharePolicyImpl;
import com.zhiyicx.baseproject.widget.button.LoadingButton;
import com.zhiyicx.common.utils.ActivityHandler;
import com.zhiyicx.common.utils.UIUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.imsdk.utils.common.DeviceUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.AccountBean;
import com.zhiyicx.thinksnsplus.modules.home.HomeActivity;
import com.zhiyicx.thinksnsplus.modules.password.findpassword.FindPasswordActivity;
import com.zhiyicx.thinksnsplus.modules.register.RegisterActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;
import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;
import static com.zhiyicx.thinksnsplus.modules.login.LoginActivity.BUNDLE_TOURIST_LOGIN;

/**
 * @author LiuChao
 * @describe
 * @date 2016/12/23
 * @contact email:450127106@qq.com
 */

public class LoginFragment extends TSFragment<LoginContract.Presenter> implements LoginContract.View, AccountAdapter.OnItemSelectListener {

    @BindView(R.id.et_login_phone)
    EditText mEtLoginPhone;
    @BindView(R.id.et_login_password)
    EditText mEtLoginPassword;
    @BindView(R.id.bt_login_login)
    LoadingButton mBtLoginLogin;
    @BindView(R.id.tv_error_tip)
    TextView mTvErrorTip;
    @BindView(R.id.tv_look_around)
    TextView mTvLookAround;
    @BindView(R.id.tv_forget_password)
    TextView mTvForgetPassword;
    @BindView(R.id.iv_clear)
    ImageView mIvClear;
    @BindView(R.id.et_complete_input)
    AppCompatAutoCompleteTextView mEtCompleteInput;
    @BindView(R.id.tv_login_by_qq)
    TextView mTvLoginByQq;

    private boolean mIsPhoneEdited;
    private boolean mIsPasswordEdited;

    private boolean mIsToourist;

    private List<AccountBean> mAccountList; // 历史的账号
    private AccountBean mAccountBean; // 当前登录的账号

    private ArrayAdapter mArrayAdapter;
    private AccountAdapter mAccountAdapter;

    UmengSharePolicyImpl mUmengSharePolicy;

    public static LoginFragment newInstance(boolean isTourist) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putBoolean(BUNDLE_TOURIST_LOGIN, isTourist);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            mIsToourist = getArguments().getBoolean(BUNDLE_TOURIST_LOGIN);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView(View rootView) {
        mEtCompleteInput.setDropDownWidth(UIUtils.getWindowWidth(getContext()));
        initListener();
        // 游客判断
        mTvLookAround.setVisibility((!mIsToourist && mPresenter.istourist()) ? View.VISIBLE : View.GONE);
        if (mIsToourist || !mPresenter.istourist()) {
            setLeftTextColor(R.color.themeColor);
        }
        mRxPermissions.setLogging(true); //是否需要日志
        mRxPermissions.request(android.Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe(aBoolean -> {
                    System.out.println("aBoolean = " + aBoolean);
                });
        mUmengSharePolicy =new UmengSharePolicyImpl(getContext());

    }

    private void initListener() {
        // 手机号码输入框观察
        RxTextView.textChanges(mEtLoginPhone)
                .compose(this.<CharSequence>bindToLifecycle())
                .subscribe(charSequence -> {
                    mIsPhoneEdited = !TextUtils.isEmpty(charSequence.toString());
                    setConfirmEnable();
                });
        RxTextView.textChanges(mEtCompleteInput)
                .compose(this.bindToLifecycle())
                .subscribe(charSequence -> {
                    mIsPhoneEdited = !TextUtils.isEmpty(charSequence.toString());
                    setConfirmEnable();
                    mIvClear.setVisibility(TextUtils.isEmpty(charSequence.toString()) ? View.GONE : View.VISIBLE);
                    if (mArrayAdapter != null) {
                        setAccountListPopHeight(mArrayAdapter.getCount());
                    }
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
                        mAccountBean.setId(new Date().getTime());
                        mAccountBean.setAccountName(mEtCompleteInput.getText().toString().trim());
                        mPresenter.login(mEtCompleteInput.getText().toString().trim(), mEtLoginPassword.getText().toString().trim());
                    } else {// 拒绝权限，但是可以再次请求
                        showErrorTips(getString(R.string.permisson_refused));
                    }
                });
        RxView.clicks(mIvClear)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.<Void>bindToLifecycle())
                .subscribe(aVoid -> mEtCompleteInput.setText(""));

        RxView.clicks(mTvLoginByQq)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.<Void>bindToLifecycle())
                .subscribe(aVoid -> {
//                    Intent intent = new Intent(getActivity(), ChooseBindActivity.class);
//                    startActivity(intent);
                    login();

                });
    }

    public void login() {
        UMShareAPI mShareAPI = UMShareAPI.get(getActivity());
        mShareAPI.getPlatformInfo(getActivity(), SHARE_MEDIA.QQ, authListener);

    }

    UMAuthListener authListener = new UMAuthListener() {
        /**
         * @desc 授权开始的回调
         * @param platform 平台名称
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @desc 授权成功的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param data 用户资料返回
         */
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            System.out.println("platform = " + data);

mTvErrorTip.setText(data.toString());
            mTvErrorTip.setVisibility(View.VISIBLE);
        }

        /**
         * @desc 授权失败的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {

            Toast.makeText(getActivity(), "失败：" + t.getMessage(), Toast.LENGTH_LONG).show();
        }

        /**
         * @desc 授权取消的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         */
        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText(getActivity(), "取消了", Toast.LENGTH_LONG).show();
        }
    };



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(getContext()).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void initData() {
        mAccountList = new ArrayList<>();
        mAccountBean = new AccountBean();
        initAccount();
    }

    @Override
    protected boolean usePermisson() {
        return true;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    protected int setToolBarBackgroud() {
        return R.color.white;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.bt_login);
    }

    @Override
    protected String setRightTitle() {
        return getString(R.string.regist);
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected void setRightClick() {
        super.setRightClick();
        Intent intent = new Intent(getActivity(), RegisterActivity.class);
        intent.putExtra(BUNDLE_TOURIST_LOGIN, mIsToourist);
        startActivity(intent);
    }

    @Override
    protected int setLeftImg() {
        return 0;
    }

    @Override
    protected String setLeftTitle() {
        return mIsToourist ? getString(R.string.cancel) : "";
    }


    @Override
    public void setLogining() {
        mBtLoginLogin.handleAnimation(true);
        mBtLoginLogin.setEnabled(false);
    }

    @Override
    public void setLoginState(boolean loginState) {
        mBtLoginLogin.handleAnimation(false);
        mBtLoginLogin.setEnabled(true);
        if (loginState) {
            mTvErrorTip.setVisibility(View.INVISIBLE);
            mTvErrorTip.setText("");
            mEtLoginPassword.setText("");
            mEtLoginPhone.setText("");
            mEtLoginPhone.requestFocus();
            DeviceUtils.hideSoftKeyboard(getContext(), mEtLoginPassword);
            if (mIsToourist) {
                getActivity().setResult(RESULT_OK);
                getActivity().finish();
            } else {
                goHome();
            }
        }
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
    public AccountBean getAccountBean() {
        return mAccountBean;
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

    @OnClick({R.id.tv_look_around, R.id.tv_forget_password})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_look_around:
                goHome();
                break;
            case R.id.tv_forget_password:
                startActivity(new Intent(getActivity(), FindPasswordActivity.class));
                break;
            default:
        }
    }

    private void goHome() {
        ActivityHandler.getInstance().finishAllActivityEcepteCurrent();// 清除 homeAcitivity 重新加载
        startActivity(new Intent(getActivity(), HomeActivity.class));
        getActivity().finish();
    }

    private void initAccount() {
        List<String> list = new ArrayList<>();
        Drawable mBackgroundDrawable = new ColorDrawable(Color.WHITE);// 默认为透明;
//        mEtCompleteInput.setDropDownBackgroundDrawable(mBackgroundDrawable);
        mAccountList.addAll(mPresenter.getAllAccountList());
        if (mAccountAdapter == null) {
            mAccountAdapter = new AccountAdapter(getContext(), mAccountList, this);
        } else {
            mAccountAdapter.notifyDataSetChanged();
        }

        for (AccountBean accountBean : mAccountList) {
            list.add(accountBean.getAccountName());
        }
        setAccountListPopHeight(mAccountList.size());
//        mArrayAdapter = new ArrayAdapter(getContext(), R.layout.item_account, R.id.tv_account_name, list);
        mEtCompleteInput.setAdapter(mAccountAdapter);
    }

    @Override
    public void onItemSelect(AccountBean accountBean) {
        // 设置填充数据，收起下拉框
        mEtCompleteInput.setText(accountBean.getAccountName());
        mEtCompleteInput.setSelection(accountBean.getAccountName().length());
        mEtCompleteInput.dismissDropDown();
    }

    @Override
    public void onDataChange(int size) {
        setAccountListPopHeight(size);
    }

    private void setAccountListPopHeight(int size) {
        if (size > 3) {
            mEtCompleteInput.setDropDownHeight((int) DeviceUtils.dpToPixel(getContext(), 140));
        } else {
            mEtCompleteInput.setDropDownHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
}
