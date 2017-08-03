package com.zhiyicx.thinksnsplus.modules.wallet.rule;

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
import com.zhiyicx.thinksnsplus.modules.settings.aboutus.CustomWEBActivity;
import com.zhiyicx.thinksnsplus.modules.wallet.WalletContract;
import com.zhiyicx.thinksnsplus.modules.wallet.WalletPresenter;
import com.zhiyicx.thinksnsplus.modules.wallet.bill.BillActivity;
import com.zhiyicx.thinksnsplus.modules.wallet.recharge.RechargeActivity;
import com.zhiyicx.thinksnsplus.modules.wallet.recharge.RechargeFragment;
import com.zhiyicx.thinksnsplus.modules.wallet.withdrawals.WithdrawalsActivity;
import com.zhiyicx.thinksnsplus.modules.wallet.withdrawals.WithdrawalsFragment;

import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;
import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.EVENT_WALLET_RECHARGE;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/05/22
 * @Contact master.jungle68@gmail.com
 */
public class WalletRuleFragment extends TSFragment {
    public static final String BUNDLE_RULE = "RULE";


    @BindView(R.id.tv_content)
    TextView mTvContent;

    private String mRule = "";

    private CenterInfoPopWindow mRulePop;// 充值提示规则选择弹框

    public static WalletRuleFragment newInstance(Bundle extras) {

        WalletRuleFragment walletRuleFragment = new WalletRuleFragment();
        walletRuleFragment.setArguments(extras);
        return walletRuleFragment;
    }


    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_wallet_rule;
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.recharge_and_withdraw_rule);
    }


    @Override
    protected void initView(View rootView) {
        if (getArguments() != null) {
            mRule = getArguments().getString(BUNDLE_RULE);
        }
        mTvContent.setText(mRule);
    }

    @Override
    protected void initData() {
    }


}
