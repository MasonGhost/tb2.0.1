package com.zhiyicx.thinksnsplus.modules.settings.account;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.widget.button.CombinationButton;
import com.zhiyicx.common.utils.SkinUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.settings.bind.AccountBindActivity;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;
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

    private UserInfoBean mCurrentUser;

    @Override
    protected void initView(View rootView) {

    }

    @Override
    protected void initData() {
        initListener();
        mPresenter.getBindSocialAcounts();
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.account_manager);
    }

    private void initListener() {
        RxView.clicks(mBtBindPhone)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> {
                    // 跳转绑定/解绑手机号
                    Intent intent = new Intent(getActivity(), AccountBindActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt(BUNDLE_BIND_TYPE, DEAL_TYPE_PHONE);
                    bundle.putBoolean(BUNDLE_BIND_STATE, false);
                    intent.putExtra(BUNDLE_BIND_TYPE, bundle);
                    startActivity(intent);
                });
        RxView.clicks(mBtBindEmail)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> {
                    // 跳转绑定/解绑邮箱
                    Intent intent = new Intent(getActivity(), AccountBindActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt(BUNDLE_BIND_TYPE, DEAL_TYPE_EMAIL);
                    bundle.putBoolean(BUNDLE_BIND_STATE, false);
                    intent.putExtra(BUNDLE_BIND_TYPE, bundle);
                    startActivity(intent);
                });
        RxView.clicks(mBtBindQq)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> {
                    // 跳转绑定/解绑QQ
                });
        RxView.clicks(mBtBindWechat)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> {
                    // 跳转绑定/解绑微信
                });
        RxView.clicks(mBtBindWeibo)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> {
                    // 跳转绑定/解绑微博
                });
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_account_management;
    }

    @Override
    public void updateBindStatus(List<String> data, UserInfoBean userInfoBean) {
        mCurrentUser=userInfoBean;
        setText(mBtBindPhone,TextUtils.isEmpty(userInfoBean.getPhone()));
        setText(mBtBindEmail,TextUtils.isEmpty(userInfoBean.getEmail()));
        setText(mBtBindQq,data.contains(ApiConfig.PROVIDER_QQ) );
        setText(mBtBindWechat,data.contains(ApiConfig.PROVIDER_WECHAT) );
        setText(mBtBindWeibo,data.contains(ApiConfig.PROVIDER_WEIBO) );

        setColor(mBtBindPhone,TextUtils.isEmpty(userInfoBean.getPhone()));
        setColor(mBtBindEmail,TextUtils.isEmpty(userInfoBean.getEmail()));
        setColor(mBtBindQq,data.contains(ApiConfig.PROVIDER_QQ));
        setColor(mBtBindWechat,data.contains(ApiConfig.PROVIDER_WECHAT));
        setColor(mBtBindWeibo,data.contains(ApiConfig.PROVIDER_WEIBO));

    }

    private void setColor(CombinationButton combinationButton, boolean b) {
        combinationButton.setRightTextColor(SkinUtils.getColor(b? R.color.dyanmic_top_flag : R.color.normal_for_assist_text));

    }

    private void setText(CombinationButton combinationButton,boolean b) {
        combinationButton.setRightText(getString(b? R.string.not_binding : R.string.had_binding));
    }
}
