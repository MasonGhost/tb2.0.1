package com.zhiyicx.thinksnsplus.modules.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.widget.button.CombinationButton;
import com.zhiyicx.baseproject.widget.popwindow.CenterInfoPopWindow;
import com.zhiyicx.common.widget.popwindow.CustomPopupWindow;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.WalletConfigBean;
import com.zhiyicx.thinksnsplus.modules.settings.aboutus.CustomWEBActivity;
import com.zhiyicx.thinksnsplus.modules.wallet.bill.BillActivity;
import com.zhiyicx.thinksnsplus.modules.wallet.recharge.RechargeActivity;
import com.zhiyicx.thinksnsplus.modules.wallet.recharge.RechargeFragment;
import com.zhiyicx.thinksnsplus.modules.wallet.withdrawals.WithdrawalsActivity;
import com.zhiyicx.thinksnsplus.modules.wallet.withdrawals.WithdrawalsFragment;
import com.zhiyicx.thinksnsplus.modules.wallet.withdrawals.list_detail.WithdrawalsDetailActivity;

import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;
import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.EVENT_MUSIC_COMMENT_COUNT;
import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.EVENT_WALLET_RECHARGE;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/05/22
 * @Contact master.jungle68@gmail.com
 */
public class WalletFragment extends TSFragment<WalletContract.Presenter> implements WalletContract.View {

    @BindView(R.id.tv_mine_money)
    TextView mTvMineMoney;
    @BindView(R.id.bt_recharge)
    CombinationButton mBtReCharge;
    @BindView(R.id.bt_withdraw)
    CombinationButton mBtWithdraw;
    @BindView(R.id.tv_recharge_and_withdraw_rule)
    TextView mTvReChargeAndWithdrawRule;


    private CenterInfoPopWindow mRulePop;// 充值提示规则选择弹框

    public static WalletFragment newInstance() {
        return new WalletFragment();
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_wallet;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.wallet);
    }

    @Override
    protected int setLeftImg() {
        return R.mipmap.topbar_back_white;
    }

    @Override
    protected boolean setStatusbarGrey() {
        return false;
    }

    @Override
    protected int setToolBarBackgroud() {
        return R.color.themeColor;
    }

    @Override
    protected void initView(View rootView) {
        setCenterTextColor(R.color.white);
        setRightText(getString(R.string.detail));
        initListener();
    }

    @Override
    protected void initData() {
        mPresenter.updateUserInfo();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mPresenter.checkIsNeedTipPop()) {
            getView().post(() -> showRulePopupWindow());
        }
    }

    @Override
    protected void setRightClick() {
        super.setRightClick();
        startActivity(new Intent(getActivity(), BillActivity.class));
    }

    private void initListener() {
        // 充值
        RxView.clicks(mBtReCharge)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> mPresenter.checkWalletConfig(WalletPresenter.TAG_RECHARGE));
        // 提现
        RxView.clicks(mBtWithdraw)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> mPresenter.checkWalletConfig(WalletPresenter.TAG_WITHDRAW));
        // 充值提现规则
        RxView.clicks(mTvReChargeAndWithdrawRule)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> CustomWEBActivity.startToWEBActivity(getContext(), "http://www.baidu.com"));
    }

    /**
     * 初始化登录选择弹框
     */
    private void showRulePopupWindow() {
        if (mRulePop != null) {
            mRulePop.show();
            return;
        }
        mRulePop = CenterInfoPopWindow.builder()
                .titleStr(getString(R.string.recharge_and_withdraw_rule))
                .desStr(mPresenter.getTipPopRule())
                .item1Str(getString(R.string.get_it))
                .item1Color(R.color.themeColor)
                .isOutsideTouch(true)
                .isFocus(true)
                .animationStyle(R.style.style_actionPopupAnimation)
                .backgroundAlpha(CustomPopupWindow.POPUPWINDOW_ALPHA)
                .with(getActivity())
                .buildCenterPopWindowItem1ClickListener(() -> mRulePop.hide())
                .parentView(getView())
                .build();
        mRulePop.show();
    }

    @Override
    public void updateBalance(double balance) {
        mTvMineMoney.setText(getString(R.string.money_format, balance));
    }

    @Override
    public void handleLoading(boolean isShow) {
        if (isShow) {
            showLeftTopLoading();
        } else {
            hideLeftTopLoading();
        }
    }

    /**
     * the api walletconfig call back
     *
     * @param walletConfigBean wallet config info
     * @param tag              action tag, 1 recharge 2 withdraw
     */
    @Override
    public void walletConfigCallBack(WalletConfigBean walletConfigBean, int tag) {
        Bundle bundle = new Bundle();
        switch (tag) {
            case WalletPresenter.TAG_RECHARGE:
                bundle.putParcelable(RechargeFragment.BUNDLE_DATA, walletConfigBean);
                jumpActivity(bundle, RechargeActivity.class);
                break;
            case WalletPresenter.TAG_WITHDRAW:
                bundle.putParcelable(WithdrawalsFragment.BUNDLE_DATA, walletConfigBean);
                jumpActivity(bundle, WithdrawalsActivity.class);
                break;
            default:

        }
    }

    /**
     * activity jump
     *
     * @param bundle intent data
     * @param cls    target class
     */
    private void jumpActivity(Bundle bundle, Class<?> cls) {
        Intent to = new Intent(getActivity(), cls);
        to.putExtras(bundle);
        startActivity(to);
    }

    @Subscriber(tag = EVENT_WALLET_RECHARGE, mode = ThreadMode.MAIN)
    public void onRechargeSuccessUpdate(String result) {
        initData();
    }

}
