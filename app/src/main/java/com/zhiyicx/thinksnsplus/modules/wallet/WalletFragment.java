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
import com.zhiyicx.thinksnsplus.data.beans.WalletConfigBean;
import com.zhiyicx.thinksnsplus.modules.wallet.bill.BillActivity;
import com.zhiyicx.thinksnsplus.modules.wallet.integration.mine.MineIntegrationActivity;
import com.zhiyicx.thinksnsplus.modules.wallet.recharge.RechargeActivity;
import com.zhiyicx.thinksnsplus.modules.wallet.recharge.RechargeFragment;
import com.zhiyicx.thinksnsplus.modules.wallet.rule.WalletRuleActivity;
import com.zhiyicx.thinksnsplus.modules.wallet.rule.WalletRuleFragment;
import com.zhiyicx.thinksnsplus.modules.wallet.withdrawals.WithdrawalsActivity;
import com.zhiyicx.thinksnsplus.modules.wallet.withdrawals.WithdrawalsFragment;

import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;
import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.EVENT_WALLET_RECHARGE;
import static com.zhiyicx.thinksnsplus.modules.wallet.WalletPresenter.TAG_SHOWRULE_POP;

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
    @BindView(R.id.bt_mine_integration)
    CombinationButton btMineIntegration;
    @BindView(R.id.tv_recharge_and_withdraw_rule)
    TextView mTvReChargeAndWithdrawRule;
    @BindView(R.id.tv_account_unit)
    TextView getTvMineMoney;

    /**
     *  充值提示规则选择弹框
     */
    private CenterInfoPopWindow mRulePop;

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
        getTvMineMoney.setText(String.format(Locale.getDefault(),getString(R.string.account_balance),mPresenter.getGoldName()));
        mSystemConfigBean=mPresenter.getSystemConfigBean();
        if(mSystemConfigBean==null||mSystemConfigBean.getWalletRecharge()==null||!mSystemConfigBean.getWalletRecharge().isOpen()){
            mBtReCharge.setVisibility(View.GONE);
        }else {
            mBtReCharge.setVisibility(View.VISIBLE);
        }
        if(mSystemConfigBean==null||mSystemConfigBean.getWalletCash()==null||!mSystemConfigBean.getWalletCash().isOpen()){
            mBtWithdraw.setVisibility(View.GONE);
        }else {
            mBtWithdraw.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.updateUserInfo();
    }

    @Override
    protected void initData() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mPresenter.checkIsNeedTipPop()) {
            getView().post(() -> mPresenter.checkWalletConfig(TAG_SHOWRULE_POP,false));
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
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> mPresenter.checkWalletConfig(WalletPresenter.TAG_RECHARGE,true));
        // 提现
        RxView.clicks(mBtWithdraw)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> mPresenter.checkWalletConfig(WalletPresenter.TAG_WITHDRAW,true));     // 提现
        // 我的积分
        RxView.clicks(btMineIntegration)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(aVoid ->{
                    Intent intent = new Intent(mActivity, MineIntegrationActivity.class);
                    startActivity(intent);
                } );
        // 充值提现规则
        RxView.clicks(mTvReChargeAndWithdrawRule)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> {
                    mPresenter.checkWalletConfig(WalletPresenter.TAG_SHOWRULE_JUMP,true);
                });
    }

    private void jumpWalletRuleActivity() {
        Intent intent = new Intent(getActivity(), WalletRuleActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(WalletRuleFragment.BUNDLE_RULE, mPresenter.getTipPopRule());
        bundle.putString(WalletRuleFragment.BUNDLE_TITLE, getString(R.string.recharge_and_withdraw_rule));
        intent.putExtras(bundle);
        startActivity(intent);
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
            case WalletPresenter.TAG_SHOWRULE_POP:
                showRulePopupWindow();
                break;
            case WalletPresenter.TAG_SHOWRULE_JUMP:
                jumpWalletRuleActivity();
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
