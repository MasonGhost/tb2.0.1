package com.zhiyicx.thinksnsplus.modules.wallet.integration.recharge;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.pingplusplus.android.Pingpp;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.widget.button.CombinationButton;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.baseproject.widget.popwindow.CenterInfoPopWindow;
import com.zhiyicx.common.config.ConstantConfig;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.UIUtils;
import com.zhiyicx.common.widget.popwindow.CustomPopupWindow;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.WalletConfigBean;
import com.zhiyicx.thinksnsplus.data.beans.integration.IntegrationConfigBean;
import com.zhiyicx.thinksnsplus.modules.develop.TSDevelopActivity;
import com.zhiyicx.thinksnsplus.modules.wallet.WalletPresenter;
import com.zhiyicx.thinksnsplus.modules.wallet.bill.BillActivity;
import com.zhiyicx.thinksnsplus.modules.wallet.recharge.RechargeActivity;
import com.zhiyicx.thinksnsplus.modules.wallet.recharge.RechargeFragment;
import com.zhiyicx.thinksnsplus.modules.wallet.rule.WalletRuleActivity;
import com.zhiyicx.thinksnsplus.modules.wallet.rule.WalletRuleFragment;
import com.zhiyicx.thinksnsplus.modules.wallet.withdrawals.WithdrawalsActivity;
import com.zhiyicx.thinksnsplus.modules.wallet.withdrawals.WithdrawalsFragment;
import com.zhiyicx.thinksnsplus.widget.chooseview.ChooseDataBean;
import com.zhiyicx.thinksnsplus.widget.chooseview.SingleChooseView;
import com.zhiyicx.tspay.TSPayClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;
import static com.zhiyicx.thinksnsplus.modules.wallet.WalletPresenter.TAG_SHOWRULE_POP;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/05/22
 * @Contact master.jungle68@gmail.com
 */
public class IntegrationRechargeFragment extends TSFragment<IntegrationRechargeContract.Presenter> implements IntegrationRechargeContract.View,
        SingleChooseView.OnItemChooseChangeListener {
    public static final String BUNDLE_DATA = "data";

    @BindView(R.id.tv_recharge_ratio)
    TextView mTvMineIntegration;
    @BindView(R.id.tv_recharge_rule)
    TextView mTvRechargeRule;
    @BindView(R.id.tv_toolbar_center)
    TextView mTvToolbarCenter;
    @BindView(R.id.tv_toolbar_left)
    TextView mTvToolbarLeft;
    @BindView(R.id.tv_toolbar_right_left)
    TextView mTvToolbarRightLeft;
    @BindView(R.id.tv_toolbar_right)
    TextView mTvToolbarRight;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.choose_view)
    SingleChooseView mChooseView;
    @BindView(R.id.v_line)
    View mVLine;
    @BindView(R.id.ll_recharge_choose_money_item)
    LinearLayout mLlRechargeChooseMoneyItem;
    @BindView(R.id.et_input)
    EditText mEtInput;
    @BindView(R.id.tv_custom_money)
    TextView mTvCustomMoney;
    @BindView(R.id.bt_recharge_style)
    CombinationButton mBtRechargeStyle;
    @BindView(R.id.bt_sure)
    TextView mBtSure;

    private String mPayType;     // type for recharge
    private IntegrationConfigBean mIntegrationConfigBean;

    /**
     * 充值提示规则选择弹框
     */
    private CenterInfoPopWindow mRulePop;
    private ActionPopupWindow mPayStylePopupWindow;// pay type choose pop

    private double mRechargeMoney; // money choosed for recharge


    public static IntegrationRechargeFragment newInstance(Bundle bundle) {
        IntegrationRechargeFragment integrationRechargeFragment = new IntegrationRechargeFragment();
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
        return R.layout.fragment_integration_recharge;
    }

    @Override
    protected void initView(View rootView) {
        setStatusPlaceholderViewBackgroundColor(android.R.color.transparent);
        mIvRefresh = (ImageView) mRootView.findViewById(R.id.iv_refresh);
        mToolbar.setBackgroundResource(android.R.color.transparent);
        ((LinearLayout.LayoutParams) mToolbar.getLayoutParams()).setMargins(0, DeviceUtils.getStatuBarHeight(mActivity), 0, 0);
        mTvToolbarCenter.setTextColor(ContextCompat.getColor(mActivity, R.color.white));
        mTvToolbarCenter.setText(getString(R.string.recharge_integration));
        mTvToolbarRight.setText(getString(R.string.recharge_record));
        mTvToolbarLeft.setCompoundDrawables(UIUtils.getCompoundDrawables(getContext(), R.mipmap.topbar_back_white), null, null, null);

        initListener();
        mLlRechargeChooseMoneyItem.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.updateUserInfo();
    }

    @Override
    protected void initData() {
        if (getArguments() != null) {
            mIntegrationConfigBean = (IntegrationConfigBean) getArguments().getSerializable(BUNDLE_DATA);
        }
        if (mIntegrationConfigBean == null) {
            return;
        }
        // 元对应的积分比例，服务器返回的是以分为单位的比例
        mTvMineIntegration.setText(getString(R.string.integration_ratio_formart, mIntegrationConfigBean.getRechargeratio() * 100));
        if (!TextUtils.isEmpty(mIntegrationConfigBean.getRechargeoptions())) {
            List<ChooseDataBean> datas = new ArrayList<>();
            String[] rechargeoptions = mIntegrationConfigBean.getRechargeoptions().split(ConstantConfig.SPLIT_SMBOL);
            for (String rechargeoption : rechargeoptions) {
                if (TextUtils.isEmpty(rechargeoption)) {
                    continue;
                }
                ChooseDataBean chooseDataBean = new ChooseDataBean();
                chooseDataBean.setText(rechargeoption);
                datas.add(chooseDataBean);
            }
            mChooseView.updateData(datas);
            mChooseView.setOnItemChooseChangeListener(this);
        } else {
            mChooseView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mPresenter.checkIsNeedTipPop()) {
            getView().post(() -> mPresenter.checkWalletConfig(TAG_SHOWRULE_POP, false));
        }
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
                        mPresenter.checkWalletConfig(WalletPresenter.TAG_SHOWRULE_JUMP, true)
                );
        RxView.clicks(mTvToolbarLeft)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(aVoid ->
                        mActivity.finish()
                );


        // 选择充值方式
        RxView.clicks(mBtRechargeStyle)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> {
                    DeviceUtils.hideSoftKeyboard(getContext(), mBtRechargeStyle);
                    initPayStylePop();
                });
        // 确认
        RxView.clicks(mBtSure)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> {
                    mBtSure.setEnabled(false);
//                    mPresenter.getPayStr(mPayType, PayConfig.gameCurrency2RealCurrency(mRechargeMoney, mPresenter.getRatio()));
                });// 传入的是真实货币分单位

        RxTextView.textChanges(mEtInput).subscribe(charSequence -> {
            String mRechargeMoneyStr = charSequence.toString();
            if (mRechargeMoneyStr.replaceAll(" ", "").length() > 0) {
                mRechargeMoney = Double.parseDouble(mRechargeMoneyStr);
                mChooseView.clearChoose();
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
        mBtSure.setEnabled(mRechargeMoney > 0 && !TextUtils.isEmpty(mBtRechargeStyle.getRightText()));
    }

    private void jumpWalletRuleActivity() {
        Intent intent = new Intent(getActivity(), WalletRuleActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(WalletRuleFragment.BUNDLE_RULE, mPresenter.getTipPopRule());
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

    /**
     * 充值方式选择弹框
     */
    private void initPayStylePop() {
        List<String> rechargeTypes = new ArrayList<>();
        if (mWalletConfigBean.getRecharge_type() != null) {
            rechargeTypes.addAll(Arrays.asList(mWalletConfigBean.getRecharge_type()));
        }

        WalletConfigBean mWalletConfigBean =mPresenter.getSystemConfigBean();
        if (mWalletConfigBean.getRecharge_type() != null) {
            rechargeTypes.addAll(Arrays.asList(mWalletConfigBean.getRecharge_type()));
        }
        if (mPayStylePopupWindow != null) {
            mPayStylePopupWindow.show();
            return;
        }
        mPayStylePopupWindow = ActionPopupWindow.builder()
                .item2Str(rechargeTypes.contains(TSPayClient.CHANNEL_ALIPAY) ? getString(R.string.choose_pay_style_formart, getString(R.string
                        .alipay)) : "")
                .item3Str(rechargeTypes.contains(TSPayClient.CHANNEL_WXPAY) ? getString(R.string.choose_pay_style_formart, getString(R.string
                        .wxpay)) : "")
                .item4Str(rechargeTypes.size() == 0 ? getString(R.string.recharge_disallow) : "")
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(CustomPopupWindow.POPUPWINDOW_ALPHA)
                .with(getActivity())
                .item2ClickListener(() -> {
                    mPayType = TSPayClient.CHANNEL_ALIPAY;
                    mBtRechargeStyle.setRightText(getString(R.string.choose_recharge_style_formart, getString(R.string.alipay)));
                    mPayStylePopupWindow.hide();
                    configSureButton();
                })
                .item3ClickListener(() -> {
                    mPayType = TSPayClient.CHANNEL_WXPAY;
                    mBtRechargeStyle.setRightText(getString(R.string.choose_recharge_style_formart, getString(R.string.wxpay)));
                    mPayStylePopupWindow.hide();
                    configSureButton();
                })
                .bottomClickListener(() -> mPayStylePopupWindow.hide())
                .build();
        mPayStylePopupWindow.show();
    }

    @Override
    public void updateBalance(double balance) {
        mTvMineIntegration.setText(getString(R.string.money_format, balance));
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


    @Override
    public void onItemChooseChanged(int position, ChooseDataBean dataBean) {
        if (position != -1) {
            mEtInput.setText("");
//            mRechargeMoney = dataBean.getText();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Pingpp.REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                mBtSure.setEnabled(true);
                String result = data.getExtras().getString("pay_result", "");
                /* 处理返回值
                 * "success" - 支付成功
                 * "fail"    - 支付失败
                 * "cancel"  - 取消支付
                 * "invalid" - 支付插件未安装（一般是微信客户端未安装的情况）
                 * "unknown" - app进程异常被杀死(一般是低内存状态下,app进程被杀死)
                 */
                String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
                String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息
                int id = UIUtils.getResourceByName("pay_" + result, "string", getContext());
                if (result.contains("success")) {
                    showSnackSuccessMessage(getString(id));
                } else {
                    showSnackErrorMessage(getString(id));
                }
                if (result.equals("success")) {
                    mPresenter.rechargeSuccess(mPayChargeId);
                }
            }
        }
    }

}
