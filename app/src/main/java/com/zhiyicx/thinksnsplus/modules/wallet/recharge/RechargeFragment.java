package com.zhiyicx.thinksnsplus.modules.wallet.recharge;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxRadioGroup;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.pingplusplus.android.Pingpp;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.config.PayConfig;
import com.zhiyicx.baseproject.widget.button.CombinationButton;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.UIUtils;
import com.zhiyicx.common.widget.popwindow.CustomPopupWindow;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.PayStrV2Bean;
import com.zhiyicx.thinksnsplus.data.beans.RechargeSuccessBean;
import com.zhiyicx.thinksnsplus.data.beans.WalletConfigBean;
import com.zhiyicx.tspay.TSPayClient;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
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
public class RechargeFragment extends TSFragment<RechargeContract.Presenter> implements RechargeContract.View {
    public static final String BUNDLE_DATA = "integrationConfig";

    @BindView(R.id.ll_recharge_choose_money_item)
    LinearLayout mLlRechargeChooseMoneyItem;
    @BindView(R.id.rb_one)
    RadioButton mRbOne;
    @BindView(R.id.rb_two)
    RadioButton mRbTwo;
    @BindView(R.id.rb_three)
    RadioButton mRbThree;
    @BindView(R.id.rb_days_group)
    RadioGroup mRbDaysGroup;
    @BindView(R.id.tv_choose_tip)
    TextView mTvChooseTip;
    @BindView(R.id.et_input)
    EditText mEtInput;
    @BindView(R.id.bt_recharge_style)
    CombinationButton mBtRechargeStyle;
    @BindView(R.id.bt_top)
    TextView mBtTop;

    @BindView(R.id.tv_custom_money)
    TextView mCustomMoney;

    private ActionPopupWindow mPayStylePopupWindow;// pay type choose pop
    private ActionPopupWindow mRechargeInstructionsPopupWindow;// recharge instruction pop


    private WalletConfigBean mWalletConfigBean; // wallet config info

    private String mPayType;     // type for recharge

    private double mRechargeMoney; // money choosed for recharge

    private List<Float> mRechargeLables; // recharge lables

    private String mPayChargeId; // recharge lables

    public static RechargeFragment newInstance(Bundle bundle) {
        RechargeFragment rechargeFragment = new RechargeFragment();
        rechargeFragment.setArguments(bundle);
        return rechargeFragment;
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_recharge;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.recharge);
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected void initView(View rootView) {
        mTvChooseTip.setText(R.string.choose_recharge_money);
        initListener();

    }

    @Override
    protected void initData() {
        initRechargeLables();
        mCustomMoney.setText(getString(R.string.yuan));
    }

    @Override
    public void payCredentialsResult(PayStrV2Bean payStrBean) {
        mPayChargeId = payStrBean.getOrder().getId() + "";
        TSPayClient.pay(ConvertUtils.object2JsonStr(payStrBean.getPingpp_order()), getActivity());
    }

    @Override
    public void rechargeSuccess(RechargeSuccessBean rechargeSuccessBean) {
        EventBus.getDefault().post("", EventBusTagConfig.EVENT_WALLET_RECHARGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Pingpp.REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                configSureBtn(true);
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

    @Override
    public void configSureBtn(boolean enable) {
        mBtTop.setEnabled(enable);
    }

    @Override
    public double getMoney() {
        return mRechargeMoney;
    }

    private void initRechargeLables() {
        if (getArguments() != null) {
            mWalletConfigBean = getArguments().getParcelable(BUNDLE_DATA);
        }
        mRechargeLables = mWalletConfigBean.getLabels();

        if (mRechargeLables == null) {
            return;
        }
        switch (mRechargeLables.size()) {
            case 6:
            case 5:
            case 4:
            case 3:
                mRbThree.setVisibility(View.VISIBLE);
                mRbThree.setText(String.format(getString(R.string.money_format), mRechargeLables.get(2) / PayConfig.MONEY_UNIT));
            case 2:
                mRbTwo.setVisibility(View.VISIBLE);
                mRbTwo.setText(String.format(getString(R.string.money_format), mRechargeLables.get(1) / PayConfig.MONEY_UNIT));
            case 1:
                mRbOne.setVisibility(View.VISIBLE);
                mRbOne.setText(String.format(getString(R.string.money_format), mRechargeLables.get(0) / PayConfig.MONEY_UNIT));
                mLlRechargeChooseMoneyItem.setVisibility(View.VISIBLE);
                break;
            case 0:
                mLlRechargeChooseMoneyItem.setVisibility(View.GONE);
                break;
            default:

        }
    }

    @Override
    public void showSnackErrorMessage(String message) {
        super.showSnackErrorMessage(message);
        mBtTop.setEnabled(true);
    }

    @Override
    public boolean useInputMonye() {
        return !mEtInput.getText().toString().isEmpty();
    }

    private void initListener() {

        // 选择充值方式
        RxView.clicks(mBtRechargeStyle)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> {
                    DeviceUtils.hideSoftKeyboard(getContext(), mBtRechargeStyle);
                    initPayStylePop();
                });
        // 确认
        RxView.clicks(mBtTop)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> {
                    mBtTop.setEnabled(false);
                    mPresenter.getPayStr(mPayType, PayConfig.realCurrencyYuan2Fen(mRechargeMoney));
                });// 传入的是真实货币分单位

        RxTextView.textChanges(mEtInput).subscribe(charSequence -> {
            String mRechargeMoneyStr = charSequence.toString();
            if (mRechargeMoneyStr.replaceAll(" ", "").length() > 0) {
                mRechargeMoney = Double.parseDouble(mRechargeMoneyStr);
                if (mRbDaysGroup.getCheckedRadioButtonId() != -1) {
                    mRbDaysGroup.clearCheck();
                }
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

        RxRadioGroup.checkedChanges(mRbDaysGroup)
                .compose(this.bindToLifecycle())
                .subscribe(checkedId -> {
                    if (checkedId != -1) {
                        setCustomMoneyDefault();
                    }
                    switch (checkedId) {
                        case R.id.rb_one:
                            mRechargeMoney = mRechargeLables.get(0) / PayConfig.MONEY_UNIT;
                            break;
                        case R.id.rb_two:
                            mRechargeMoney = mRechargeLables.get(1) / PayConfig.MONEY_UNIT;
                            break;
                        case R.id.rb_three:
                            mRechargeMoney = mRechargeLables.get(2) / PayConfig.MONEY_UNIT;
                            break;
                        default:
                            break;
                    }
                    if (checkedId != -1) {
                        configSureButton();
                    }
                });

    }

    /**
     * 设置自定义金额数量
     */
    private void setCustomMoneyDefault() {
        mEtInput.setText("");
    }

    private void configSureButton() {
        mBtTop.setEnabled(mRechargeMoney > 0 && !TextUtils.isEmpty(mBtRechargeStyle.getRightText()));
    }

    /**
     * 充值方式选择弹框
     */
    private void initPayStylePop() {
        List<String> rechargeTypes = new ArrayList<>();
        if (mWalletConfigBean.getRecharge_type() != null) {
            rechargeTypes.addAll(Arrays.asList(mWalletConfigBean.getRecharge_type()));
        }
        if (mPayStylePopupWindow != null) {
            mPayStylePopupWindow.show();
            return;
        }
        mPayStylePopupWindow = ActionPopupWindow.builder()
                .item2Str(rechargeTypes.contains(TSPayClient.CHANNEL_ALIPAY) ? getString(R.string.choose_pay_style_formart, getString(R.string.alipay)) : "")
                .item3Str(rechargeTypes.contains(TSPayClient.CHANNEL_WXPAY)||rechargeTypes.contains(TSPayClient.CHANNEL_WX) ? getString(R.string.choose_pay_style_formart, getString(R.string.wxpay)) : "")
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
                    mPayType = TSPayClient.CHANNEL_WX;
                    mBtRechargeStyle.setRightText(getString(R.string.choose_recharge_style_formart, getString(R.string.wxpay)));
                    mPayStylePopupWindow.hide();
                    configSureButton();
                })
                .bottomClickListener(() -> mPayStylePopupWindow.hide())
                .build();
        mPayStylePopupWindow.show();
    }

    /**
     * 充值说明选择弹框
     */
    @Override
    public void initmRechargeInstructionsPop() {
        if (mRechargeInstructionsPopupWindow != null) {
            mRechargeInstructionsPopupWindow.show();
            return;
        }
        mRechargeInstructionsPopupWindow = ActionPopupWindow.builder()
                .item1Str(getString(R.string.recharge_instructions))
                .desStr(getString(R.string.recharge_instructions_detail))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(CustomPopupWindow.POPUPWINDOW_ALPHA)
                .with(getActivity())
                .bottomClickListener(() -> mRechargeInstructionsPopupWindow.hide())
                .build();
        mRechargeInstructionsPopupWindow.show();
    }

}
