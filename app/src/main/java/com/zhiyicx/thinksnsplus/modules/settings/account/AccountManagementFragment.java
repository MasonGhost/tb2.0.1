package com.zhiyicx.thinksnsplus.modules.settings.account;

import android.view.View;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.widget.button.CombinationButton;
import com.zhiyicx.thinksnsplus.R;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

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

    @Override
    protected void initView(View rootView) {

    }

    @Override
    protected void initData() {
        initListener();
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.account_manager);
    }

    private void initListener(){
        RxView.clicks(mBtBindPhone)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> {
                    // 跳转绑定/解绑手机号
                });
        RxView.clicks(mBtBindEmail)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> {
                    // 跳转绑定/解绑邮箱
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
}
