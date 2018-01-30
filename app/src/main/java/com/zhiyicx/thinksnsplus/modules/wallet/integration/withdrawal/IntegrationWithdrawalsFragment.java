package com.zhiyicx.thinksnsplus.modules.wallet.integration.withdrawal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.trycatch.mysnackbar.Prompt;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.config.PayConfig;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.config.ConstantConfig;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.UIUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.common.widget.popwindow.CustomPopupWindow;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.PayStrV2Bean;
import com.zhiyicx.thinksnsplus.data.beans.RechargeSuccessBean;
import com.zhiyicx.thinksnsplus.data.beans.integration.IntegrationConfigBean;
import com.zhiyicx.thinksnsplus.modules.wallet.bill.BillActivity;
import com.zhiyicx.thinksnsplus.modules.wallet.integration.detail.recharge_withdrawal.IntegrationRWDetailActivity;
import com.zhiyicx.thinksnsplus.modules.wallet.integration.detail.recharge_withdrawal.IntegrationRWDetailContainerFragment;
import com.zhiyicx.thinksnsplus.modules.wallet.rule.WalletRuleActivity;
import com.zhiyicx.thinksnsplus.modules.wallet.rule.WalletRuleFragment;
import com.zhiyicx.thinksnsplus.widget.chooseview.ChooseDataBean;
import com.zhiyicx.thinksnsplus.widget.chooseview.SingleChooseView;
import com.zhiyicx.tspay.TSPayClient;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/05/22
 * @Contact master.jungle68@gmail.com
 */
public class IntegrationWithdrawalsFragment extends TSFragment<IntegrationWithdrawalsContract.Presenter> implements IntegrationWithdrawalsContract
        .View {


    public static final String BUNDLE_DATA = "data";

    @BindView(R.id.tv_ratio)
    TextView mTvMineIntegration;
    @BindView(R.id.tv_recharge_rule)
    TextView mTvRechargeRule;
    @BindView(R.id.tv_toolbar_center)
    TextView mTvToolbarCenter;
    @BindView(R.id.tv_toolbar_left)
    TextView mTvToolbarLeft;
    @BindView(R.id.tv_toolbar_right)
    TextView mTvToolbarRight;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.et_input)
    EditText mEtInput;
    @BindView(R.id.bt_sure)
    TextView mBtSure;
    @BindView(R.id.tv_input_tip1)
    TextView mTvInputTip1;
    @BindView(R.id.tv_input_tip2)
    TextView mTvInputTip2;

    private int mBaseRatioNum = 100;

    private IntegrationConfigBean mIntegrationConfigBean;

    /**
     * 充值提示规则选择弹框
     */
    private ActionPopupWindow mRechargeInstructionsPopupWindow;// recharge instruction pop


    private double mRechargeMoney; // money choosed for recharge

    private String mGoldName;


    public static IntegrationWithdrawalsFragment newInstance(Bundle bundle) {
        IntegrationWithdrawalsFragment integrationRechargeFragment = new IntegrationWithdrawalsFragment();
        integrationRechargeFragment.setArguments(bundle);
        return integrationRechargeFragment;
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    protected boolean showToolBarDivider() {
        return false;
    }

    @Override
    protected boolean setUseSatusbar() {
        return true;
    }

    @Override
    protected boolean setUseStatusView() {
        return false;
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected boolean setStatusbarGrey() {
        return false;
    }


    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_integration_withdrawals;
    }

    @Override
    protected void initView(View rootView) {
        setStatusPlaceholderViewBackgroundColor(android.R.color.transparent);
        mIvRefresh = (ImageView) mRootView.findViewById(R.id.iv_refresh);
        mToolbar.setBackgroundResource(android.R.color.transparent);
        ((LinearLayout.LayoutParams) mToolbar.getLayoutParams()).setMargins(0, DeviceUtils.getStatuBarHeight(mActivity), 0, 0);
        mGoldName = mPresenter.getGoldName();
        mTvToolbarCenter.setTextColor(ContextCompat.getColor(mActivity, R.color.white));
        mTvToolbarCenter.setText(getString(R.string.integration_withdrawals_format, mGoldName));
        mTvToolbarRight.setText(getString(R.string.withdrawals_record));
        mTvToolbarLeft.setCompoundDrawables(UIUtils.getCompoundDrawables(getContext(), R.mipmap.topbar_back_white), null, null, null);
        initListener();
    }

    @Override
    protected void initData() {
        if (getArguments() != null) {
            mIntegrationConfigBean = (IntegrationConfigBean) getArguments().getSerializable(BUNDLE_DATA);
        }
        if (mIntegrationConfigBean == null) {
            return;
        }
        mEtInput.setHint(getString(R.string.et_input_withdrawals_integration_tip_format, mIntegrationConfigBean.getCashmin(), mGoldName));

        // 元对应的积分比例，服务器返回的是以分为单位的比例
        setDynamicRatio(mBaseRatioNum);
        mTvRechargeRule.setText(getResources().getString(R.string.integration_withdrawals_rule_format, mGoldName));
        mTvInputTip1.setText(getString(R.string.input_withdrawals_integration_format, mGoldName));
        mTvInputTip2.setText(getString(R.string.input_withdrawals_integration_tip_format, mGoldName));
    }

    /**
     * 动态显示提取金额
     *
     * @param currentIntegration
     */
    private void setDynamicRatio(int currentIntegration) {
        mTvMineIntegration.setText(getString(R.string.integration_2_money_ratio_formart, currentIntegration, mGoldName, PayConfig.realCurrencyFen2Yuan
                ((float) currentIntegration /
                        mIntegrationConfigBean.getRechargeratio())));
    }

    @Override
    protected void setRightClick() {
        super.setRightClick();
        startActivity(new Intent(getActivity(), BillActivity.class));
    }

    private void initListener() {
        // 积分规则
        RxView.clicks(mTvRechargeRule)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(aVoid ->
                        {
                            Intent intent = new Intent(mActivity, WalletRuleActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString(WalletRuleFragment.BUNDLE_RULE, mIntegrationConfigBean.getCashrule());
                            bundle.putString(WalletRuleFragment.BUNDLE_TITLE, getResources().getString(R.string
                                    .integration_withdrawals_rule_format, mGoldName));

                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                );
        // 充值记录
        RxView.clicks(mTvToolbarRight)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(aVoid ->
                        {
                            Intent intent = new Intent(mActivity, IntegrationRWDetailActivity.class);
                            intent.putExtra(IntegrationRWDetailContainerFragment.BUNDLE_DEFAULT_POSITION, IntegrationRWDetailContainerFragment
                                    .POSITION_WITHDRAWASL_RECORD);
                            startActivity(intent);
                        }
                );
        // 返回
        RxView.clicks(mTvToolbarLeft)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(aVoid ->
                        mActivity.finish()
                );


        // 确认
        RxView.clicks(mBtSure)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> {
                    if (mRechargeMoney < mIntegrationConfigBean.getCashmin()) {
                        showSnackErrorMessage(getString(R.string.please_more_than_min_withdrawals_formart, mIntegrationConfigBean.getCashmin()));
                        return;
                    }
                    if (mRechargeMoney > mIntegrationConfigBean.getCashmax()) {
                        showSnackErrorMessage(getString(R.string.please_less_max_withdrawals_formart, mIntegrationConfigBean.getCashmax()));
                        return;
                    }
                    setSureBtEnable(false);
                    mPresenter.integrationWithdrawals((int) mRechargeMoney);
                });// 传入的是真实货币分单位

        RxTextView.textChanges(mEtInput).subscribe(charSequence -> {
            String mRechargeMoneyStr = charSequence.toString();
            if (mRechargeMoneyStr.replaceAll(" ", "").length() > 0) {
                mRechargeMoney = Double.parseDouble(mRechargeMoneyStr);
            } else {
                mRechargeMoney = 0;
            }
            configSureButton();
        }, throwable -> {
            throwable.printStackTrace();
            setCustomMoneyDefault();
            mRechargeMoney = 0;
            configSureButton();
        });

    }

    @Override
    protected void snackViewDismissWhenTimeOut(Prompt prompt) {
        super.snackViewDismissWhenTimeOut(prompt);
        if (getActivity() != null && Prompt.SUCCESS == prompt) {
            getActivity().finish();
        }
    }

    /**
     * 设置自定义金额数量
     */
    private void setCustomMoneyDefault() {
        mEtInput.setText("");
    }

    /**
     * 检查确认按钮是否可点击
     */
    private void configSureButton() {
        setSureBtEnable(mRechargeMoney > 0);
    }

    @Override
    public void setSureBtEnable(boolean enable) {
        mBtSure.setEnabled(enable);
    }
}
