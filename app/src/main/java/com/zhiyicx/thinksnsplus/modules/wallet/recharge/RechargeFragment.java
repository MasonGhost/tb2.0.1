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
import com.jakewharton.rxbinding.widget.TextViewAfterTextChangeEvent;
import com.pingplusplus.android.Pingpp;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.widget.button.CombinationButton;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.UIUtils;
import com.zhiyicx.common.widget.popwindow.CustomPopupWindow;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.PayStrBean;
import com.zhiyicx.thinksnsplus.data.beans.WalletConfigBean;
import com.zhiyicx.thinksnsplus.modules.wallet.PayType;
import com.zhiyicx.tspay.TSPayClient;

import org.simple.eventbus.EventBus;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/05/22
 * @Contact master.jungle68@gmail.com
 */
public class RechargeFragment extends TSFragment<RechargeContract.Presenter> implements RechargeContract.View {
    public static final String BUNDLE_DATA = "walletconfig";

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

    private ActionPopupWindow mPayStylePopupWindow;// pay type choose pop
    private ActionPopupWindow mRechargeInstructionsPopupWindow;// recharge instruction pop


    private WalletConfigBean mWalletConfigBean; // wallet config info

    private int mPayType;     // type for recharge

    private double mRechargeMoney; // money choosed for recharge

    private List<Integer> mSelectDays; // recharge lables

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
        initSelectDays();
    }

    @Override
    public void payCredentialsResult(PayStrBean payStrBean) {
        TSPayClient.pay(ConvertUtils.object2JsonStr(payStrBean.getCharge()), getActivity());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Pingpp.REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getExtras().getString("pay_result");
                /* 处理返回值
                 * "success" - 支付成功
                 * "fail"    - 支付失败
                 * "cancel"  - 取消支付
                 * "invalid" - 支付插件未安装（一般是微信客户端未安装的情况）
                 */
                String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
                String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息
                String pay_result;
                int id = UIUtils.getResourceByName("pay_" + result, "string", getContext());
                showMessage(getString(id));
                if (result.equals("success")) {
                    EventBus.getDefault().post(result, EventBusTagConfig.EVENT_WALLET_RECHARGE);
                }
            }
        }
    }

    private void initSelectDays() {
        if (getArguments() != null) {
            mWalletConfigBean = getArguments().getParcelable(BUNDLE_DATA);
        }
        mSelectDays = mWalletConfigBean.getLabels();

        if (mSelectDays == null) {
            return;
        }
        switch (mSelectDays.size()) {
            case 6:
            case 5:
            case 4:
            case 3:
                mRbThree.setVisibility(View.VISIBLE);
                mRbThree.setText(String.format(getString(R.string.select_day), mSelectDays.get(2)));
            case 2:
                mRbTwo.setVisibility(View.VISIBLE);
                mRbTwo.setText(String.format(getString(R.string.select_day), mSelectDays.get(1)));
            case 1:
                mRbOne.setVisibility(View.VISIBLE);
                mRbOne.setText(String.format(getString(R.string.select_day), mSelectDays.get(0)));
                mLlRechargeChooseMoneyItem.setVisibility(View.VISIBLE);
                break;
            case 0:
                mLlRechargeChooseMoneyItem.setVisibility(View.GONE);
                break;
            default:

        }
    }


    private void initListener() {

        // 选择充值方式
        RxView.clicks(mBtRechargeStyle)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .compose(this.<Void>bindToLifecycle())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        DeviceUtils.hideSoftKeyboard(getContext(), mBtRechargeStyle);
                        initPayStylePop();
                    }
                });
        // 确认
        RxView.clicks(mBtTop)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .compose(this.<Void>bindToLifecycle())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        mPresenter.getPayStr(TSPayClient.CHANNEL_ALIPAY, ((Double) mRechargeMoney).intValue() * 100);
                    }
                });
        // 
        RxTextView.afterTextChangeEvents(mEtInput)
                .subscribe(new Action1<TextViewAfterTextChangeEvent>() {
                    @Override
                    public void call(TextViewAfterTextChangeEvent textViewAfterTextChangeEvent) {
                        if (TextUtils.isEmpty(textViewAfterTextChangeEvent.editable().toString())) {
                            mRechargeMoney = 0;
                            configSureButton();
                            return;
                        }

                        if (textViewAfterTextChangeEvent.editable().toString().contains(".")) {
                            setCustomMoneyDefault();
                            DeviceUtils.hideSoftKeyboard(getContext(), mEtInput);
                            initmRechargeInstructionsPop();
                        } else {
                            mRbDaysGroup.clearCheck();
                            try {
                                mRechargeMoney = Double.parseDouble(textViewAfterTextChangeEvent.editable().toString());
                                configSureButton();
                            } catch (NumberFormatException ne) {

                            }
                        }
                        configSureButton();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        setCustomMoneyDefault();
                        mRechargeMoney = 0;
                    }
                });
        RxRadioGroup.checkedChanges(mRbDaysGroup)
                .compose(this.<Integer>bindToLifecycle())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer checkedId) {
                        switch (checkedId) {
                            case R.id.rb_one:
                                mRechargeMoney = mSelectDays.get(0);
                                break;
                            case R.id.rb_two:
                                mRechargeMoney = mSelectDays.get(1);
                                break;
                            case R.id.rb_three:
                                mRechargeMoney = mSelectDays.get(2);
                                break;
                        }
                        configSureButton();
                        setCustomMoneyDefault();
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
        if (mPayStylePopupWindow != null) {
            mPayStylePopupWindow.show();
            return;
        }
        mPayStylePopupWindow = ActionPopupWindow.builder()
//                .item2Str(mWalletConfigBean.getAlipay().isOpen()?getString(R.string.choose_pay_style_formart, getString(R.string.alipay)):"")
//                .item3Str(mWalletConfigBean.getWechat().isOpen()?getString(R.string.choose_pay_style_formart, getString(R.string.wxpay)):"")
                .item2Str(getString(R.string.choose_pay_style_formart, getString(R.string.alipay)))
                .item3Str(getString(R.string.choose_pay_style_formart, getString(R.string.wxpay)))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(CustomPopupWindow.POPUPWINDOW_ALPHA)
                .with(getActivity())
                .item2ClickListener(new ActionPopupWindow.ActionPopupWindowItem2ClickListener() {
                    @Override
                    public void onItemClicked() {
                        mPayType = PayType.ALIPAY.value;
                        mBtRechargeStyle.setRightText(getString(R.string.choose_recharge_style_formart, getString(R.string.alipay)));
                        mPayStylePopupWindow.hide();
                        configSureButton();
                    }
                })
                .item3ClickListener(new ActionPopupWindow.ActionPopupWindowItem3ClickListener() {
                    @Override
                    public void onItemClicked() {
                        mPayType = PayType.WX.value;
                        mBtRechargeStyle.setRightText(getString(R.string.choose_recharge_style_formart, getString(R.string.wxpay)));
                        mPayStylePopupWindow.hide();
                        configSureButton();
                    }
                })
                .bottomClickListener(new ActionPopupWindow.ActionPopupWindowBottomClickListener() {
                    @Override
                    public void onItemClicked() {
                        mPayStylePopupWindow.hide();
                    }
                })
                .build();
        mPayStylePopupWindow.show();
    }

    /**
     * 充值说明选择弹框
     */
    private void initmRechargeInstructionsPop() {
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
                .bottomClickListener(new ActionPopupWindow.ActionPopupWindowBottomClickListener() {
                    @Override
                    public void onItemClicked() {
                        mRechargeInstructionsPopupWindow.hide();
                    }
                })
                .build();
        mRechargeInstructionsPopupWindow.show();
    }

}
