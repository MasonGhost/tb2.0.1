package com.zhiyicx.thinksnsplus.modules.settings.account;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.widget.button.CombinationButton;
import com.zhiyicx.common.utils.SkinUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.settings.bind.AccountBindActivity;
import com.zhiyicx.thinksnsplus.modules.settings.init_password.InitPasswordActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;

import static com.zhiyicx.baseproject.config.ApiConfig.PROVIDER_WECHAT;
import static com.zhiyicx.baseproject.config.ApiConfig.PROVIDER_WEIBO;
import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;
import static com.zhiyicx.thinksnsplus.modules.settings.bind.AccountBindActivity.BUNDLE_BIND_DATA;
import static com.zhiyicx.thinksnsplus.modules.settings.bind.AccountBindActivity.BUNDLE_BIND_STATE;
import static com.zhiyicx.thinksnsplus.modules.settings.bind.AccountBindActivity.BUNDLE_BIND_TYPE;
import static com.zhiyicx.thinksnsplus.modules.settings.bind.AccountBindFragment.DEAL_TYPE_EMAIL;
import static com.zhiyicx.thinksnsplus.modules.settings.bind.AccountBindFragment.DEAL_TYPE_PHONE;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/28
 * @contact email:648129313@qq.com
 */

public class AccountManagementFragment extends TSFragment<AccountManagementContract.Presenter>
        implements AccountManagementContract.View {

    @BindView(R.id.bt_bind_phone)
    CombinationButton mBtBindPhone;
    @BindView(R.id.bt_bind_email)
    CombinationButton mBtBindEmail;
    @BindView(R.id.bt_bind_qq)
    CombinationButton mBtBindQq;
    @BindView(R.id.bt_bind_wechat)
    CombinationButton mBtBindWechat;
    @BindView(R.id.bt_bind_weibo)
    CombinationButton mBtBindWeibo;

    private List<String> mBindAccounts = new ArrayList<>();
    private UserInfoBean mCurrentUser;

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_account_management;
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected void initView(View rootView) {
        mBtBindQq.setEnabled(false);
        mBtBindWechat.setEnabled(false);
        mBtBindWeibo.setEnabled(false);
        mBtBindPhone.setEnabled(false);
        mBtBindEmail.setEnabled(false);

        initUserListener();
        initThirdListener();
    }

    @Override
    protected void initData() {
        mPresenter.getBindSocialAcounts();
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.account_manager);
    }

    private void initUserListener() {
        RxView.clicks(mBtBindPhone)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> {
                    if (mCurrentUser != null) {
                        // 三方绑定,解绑逻辑.是否保留某个主信息无法解绑. [邮箱和手机必须保留一个]
                        if (!TextUtils.isEmpty(mCurrentUser.getPhone()) && TextUtils.isEmpty(mCurrentUser.getEmail())) {
                            showSnackErrorMessage(getString(R.string.you_must_bind_email_to_unbind_phone));
                            return;
                        }
                        // 跳转绑定/解绑手机号
                        Intent intent = new Intent(getActivity(), AccountBindActivity.class);
                        if (!AppApplication.getmCurrentLoginAuth().getUser().isInitial_password()) {
                            intent.setClass(getActivity(), InitPasswordActivity.class);
                        }
                        Bundle bundle = new Bundle();
                        bundle.putInt(BUNDLE_BIND_TYPE, DEAL_TYPE_PHONE);
                        bundle.putBoolean(BUNDLE_BIND_STATE, !TextUtils.isEmpty(mCurrentUser.getPhone()));
                        bundle.putParcelable(BUNDLE_BIND_DATA, mCurrentUser);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
        RxView.clicks(mBtBindEmail)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> {
                    if (mCurrentUser != null) {
                        // 三方绑定,解绑逻辑.是否保留某个主信息无法解绑. [邮箱和手机必须保留一个]
                        if (!TextUtils.isEmpty(mCurrentUser.getEmail()) && TextUtils.isEmpty(mCurrentUser.getPhone())) {
                            showSnackErrorMessage(getString(R.string.you_must_bind_phone_to_unbind_email));
                            return;
                        }
                        // 跳转绑定/解绑邮箱
                        Intent intent = new Intent(getActivity(), AccountBindActivity.class);
                        if (!AppApplication.getmCurrentLoginAuth().getUser().isInitial_password()) {
                            intent.setClass(getActivity(), InitPasswordActivity.class);
                        }
                        Bundle bundle = new Bundle();
                        bundle.putInt(BUNDLE_BIND_TYPE, DEAL_TYPE_EMAIL);

                        bundle.putBoolean(BUNDLE_BIND_STATE, !TextUtils.isEmpty(mCurrentUser.getEmail()));
                        bundle.putParcelable(BUNDLE_BIND_DATA, mCurrentUser);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });

    }

    private void initThirdListener() {

        RxView.clicks(mBtBindQq)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> {
                    handleThirdAccount(ApiConfig.PROVIDER_QQ);

                });
        RxView.clicks(mBtBindWechat)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> {
                    // 跳转绑定/解绑微信
                    handleThirdAccount(ApiConfig.PROVIDER_WECHAT);

                });
        RxView.clicks(mBtBindWeibo)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> {
                    // 跳转绑定/解绑微博
                    handleThirdAccount(ApiConfig.PROVIDER_WEIBO);
                });
    }

    private void handleThirdAccount(String provider) {
        // 跳转绑定/解绑QQ
        if (mBindAccounts.contains(provider)) { // 解绑
            if (TextUtils.isEmpty(mCurrentUser.getPhone())) {
                showSnackErrorMessage(getString(R.string.you_must_bind_phone));
            } else {
                mPresenter.bindOrUnbindThirdAccount(provider, null, false);
            }
        } else {
            if (TextUtils.isEmpty(mCurrentUser.getPhone())) {
                showSnackErrorMessage(getString(R.string.you_must_bind_phone2));
            } else {
                // 绑定
                switch (provider) {
                    case ApiConfig.PROVIDER_QQ:
                        // QQ 和微信 该版本不提供网页支持，故提示安装应用
                        if (UMShareAPI.get(getContext()).isInstall(getActivity(), SHARE_MEDIA.QQ)) {
                            thridLogin(SHARE_MEDIA.QQ);
                        } else {
                            showSnackErrorMessage(getString(R.string.please_install_app));
                        }
                        break;
                    case ApiConfig.PROVIDER_WEIBO:
//                        if (UMShareAPI.get(getContext()).isInstall(getActivity(), SHARE_MEDIA.SINA)) {

                        thridLogin(SHARE_MEDIA.SINA);
//                        } else {
//                            showSnackErrorMessage(getString(R.string.please_install_app));
//                        }
                        break;
                    case ApiConfig.PROVIDER_WECHAT:
                        if (UMShareAPI.get(getContext()).isInstall(getActivity(), SHARE_MEDIA.WEIXIN)) {

                            thridLogin(SHARE_MEDIA.WEIXIN);
                        } else {
                            showSnackErrorMessage(getString(R.string.please_install_app));
                        }
                        break;
                    default:
                        if (UMShareAPI.get(getContext()).isInstall(getActivity(), SHARE_MEDIA.QQ)) {

                            thridLogin(SHARE_MEDIA.QQ);
                        } else {
                            showSnackErrorMessage(getString(R.string.please_install_app));
                        }
                }

            }
        }
    }


    public void thridLogin(SHARE_MEDIA type) {
        showSnackLoadingMessage(getString(R.string.loading_state));
        UMShareAPI mShareAPI = UMShareAPI.get(getActivity());
        mShareAPI.getPlatformInfo(getActivity(), type, authListener);

    }


    private String mAccessToken;

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
            showSnackSuccessMessage(getString(R.string.loading_state));
            String provider = ApiConfig.PROVIDER_QQ;
            switch (platform) {
                case QQ:
                    provider = ApiConfig.PROVIDER_QQ;
                    break;
                case SINA:
                    provider = PROVIDER_WEIBO;
                    break;

                case WEIXIN:
                    provider = PROVIDER_WECHAT;
                    break;
                default:

            }
            mAccessToken = data.get("accessToken");
            mPresenter.bindOrUnbindThirdAccount(provider, mAccessToken, true);
        }

        /**
         * @desc 授权失败的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            showSnackErrorMessage(getString(R.string.login_fail));
        }

        /**
         * @desc 授权取消的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         */
        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            showSnackWarningMessage(getString(R.string.login_cancel));
        }
    };

    /**
     * @param provider
     */
    @Override
    public void bindThirdSuccess(String provider) {
        mBindAccounts.add(provider);
        updateText(mBindAccounts);
    }

    /**
     * @param provider
     */
    @Override
    public void unBindThirdSuccess(String provider) {
        mBindAccounts.remove(provider);
        updateText(mBindAccounts);
    }

    /**
     * @param data bind accounts
     */
    @Override
    public void updateBindStatus(List<String> data) {
        this.mBindAccounts.clear();
        this.mBindAccounts.addAll(data);
        updateText(mBindAccounts);

    }

    private void updateText(List<String> data) {
        setText(mBtBindQq, data.contains(ApiConfig.PROVIDER_QQ));
        setText(mBtBindWechat, data.contains(PROVIDER_WECHAT));
        setText(mBtBindWeibo, data.contains(PROVIDER_WEIBO));

        setColor(mBtBindQq, data.contains(ApiConfig.PROVIDER_QQ));
        setColor(mBtBindWechat, data.contains(PROVIDER_WECHAT));
        setColor(mBtBindWeibo, data.contains(PROVIDER_WEIBO));
        mBtBindQq.setEnabled(true);
        mBtBindWechat.setEnabled(true);
        mBtBindWeibo.setEnabled(true);

    }

    private void setColor(CombinationButton combinationButton, boolean b) {
        combinationButton.setRightTextColor(SkinUtils.getColor(b ? R.color.normal_for_assist_text : R.color.dyanmic_top_flag));

    }

    private void setText(CombinationButton combinationButton, boolean b) {
        combinationButton.setRightText(getString(b ? R.string.had_binding : R.string.not_binding));
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.updaeUserInfo();
    }

    @Override
    public void updateUserinfo(UserInfoBean userInfoBean) {
        if (userInfoBean != null) {
            mCurrentUser = userInfoBean;
            setText(mBtBindPhone, !TextUtils.isEmpty(userInfoBean.getPhone()));
            setText(mBtBindEmail, !TextUtils.isEmpty(userInfoBean.getEmail()));

            setColor(mBtBindPhone, !TextUtils.isEmpty(userInfoBean.getPhone()));
            setColor(mBtBindEmail, !TextUtils.isEmpty(userInfoBean.getEmail()));
            mBtBindPhone.setEnabled(true);
            mBtBindEmail.setEnabled(true);
        }
    }


}
