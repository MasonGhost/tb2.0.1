package com.zhiyicx.thinksnsplus.modules.wallet.withdrawals;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewAfterTextChangeEvent;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.widget.button.CombinationButton;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.widget.popwindow.CustomPopupWindow;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.WalletConfigBean;
import com.zhiyicx.thinksnsplus.data.beans.WithdrawResultBean;
import com.zhiyicx.thinksnsplus.modules.wallet.withdrawals.list_detail.WithdrawalsDetailActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import rx.functions.Action1;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @Author Jliuer
 * @Date 2017/05/23/10:24
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class WithdrawalsFragment extends TSFragment<WithDrawalsConstract.Presenter> implements WithDrawalsConstract.View {
    public static final String BUNDLE_DATA = "walletconfig";

    @BindView(R.id.et_withdraw_input)
    EditText mEtWithdrawInput;
    @BindView(R.id.bt_withdraw_style)
    CombinationButton mBtWithdrawStyle;
    @BindView(R.id.et_withdraw_account_input)
    EditText mEtWithdrawAccountInput;
    @BindView(R.id.bt_sure)
    TextView mBtSure;


    private ActionPopupWindow mWithdrawalsInstructionsPopupWindow;

    private ActionPopupWindow mActionPopupWindow;

    private String mWithdrawalsType;

    private double mWithdrawalsMoney;

    private WalletConfigBean mWalletConfigBean; // wallet config info


    public static WithdrawalsFragment newInstance(Bundle bundle) {
        WithdrawalsFragment withdrawalsFragment = new WithdrawalsFragment();
        withdrawalsFragment.setArguments(bundle);
        return withdrawalsFragment;
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.withdraw);
    }

    @Override
    protected String setRightTitle() {
        mToolbarRight.setTextColor(getColor(R.color.themeColor));
        return getString(R.string.withdraw_details);
    }

    @Override
    protected void setRightClick() {
        startActivity(new Intent(getActivity(), WithdrawalsDetailActivity.class));
    }

    @Override
    protected void initView(View rootView) {
        initListener();
    }

    @Override
    protected void initData() {
        if (getArguments() != null) {
            mWalletConfigBean = getArguments().getParcelable(BUNDLE_DATA);
        }
    }

    @Override
    public void withdrawResult(WithdrawResultBean withdrawResultBean) {

    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_withdrawals;
    }

    private void setCustomMoneyDefault() {
        mEtWithdrawInput.setText("");
    }

    private void initListener() {
        RxView.clicks(mBtSure)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        DeviceUtils.hideSoftKeyboard(getContext(), mEtWithdrawInput);
                        mPresenter.withdraw((int) mWithdrawalsMoney * 200, mWithdrawalsType, mEtWithdrawAccountInput.getText().toString());
                    }
                });

        RxView.clicks(mBtWithdrawStyle).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                DeviceUtils.hideSoftKeyboard(getContext(), mEtWithdrawInput);
                initWithDrawalsStylePop();
            }
        });
        RxTextView.afterTextChangeEvents(mEtWithdrawAccountInput).subscribe(new Action1<TextViewAfterTextChangeEvent>() {
            @Override
            public void call(TextViewAfterTextChangeEvent textViewAfterTextChangeEvent) {
                configSureButton();
            }
        });
        RxTextView.afterTextChangeEvents(mEtWithdrawInput).subscribe(new Action1<TextViewAfterTextChangeEvent>() {
            @Override
            public void call(TextViewAfterTextChangeEvent textViewAfterTextChangeEvent) {
                if (TextUtils.isEmpty(textViewAfterTextChangeEvent.editable().toString())) {
                    return;
                }

                if (textViewAfterTextChangeEvent.editable().toString().contains(".")) {
                    setCustomMoneyDefault();
                    DeviceUtils.hideSoftKeyboard(getContext(), mEtWithdrawInput);
                    initWithdrawalsInstructionsPop();

                } else {
                    try {
                        mWithdrawalsMoney = Double.parseDouble(textViewAfterTextChangeEvent.editable().toString());
                        configSureButton();
                    } catch (NumberFormatException ne) {

                    }
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                mWithdrawalsMoney = 0;
            }
        });
    }

    private void initWithdrawalsInstructionsPop() {
        if (mWithdrawalsInstructionsPopupWindow != null) {
            mWithdrawalsInstructionsPopupWindow.show();
            return;
        }
        mWithdrawalsInstructionsPopupWindow = ActionPopupWindow.builder()
                .item1Str(getString(R.string.withdrawal_instructions))
                .desStr(getString(R.string.withdrawal_instructions_detail))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(CustomPopupWindow.POPUPWINDOW_ALPHA)
                .with(getActivity())
                .bottomClickListener(new ActionPopupWindow.ActionPopupWindowBottomClickListener() {
                    @Override
                    public void onItemClicked() {
                        mWithdrawalsInstructionsPopupWindow.hide();
                    }
                })
                .build();
        mWithdrawalsInstructionsPopupWindow.show();
    }

    private void initWithDrawalsStylePop() {
        if (mActionPopupWindow != null) {
            mActionPopupWindow.show();
            return;
        }
        List<String> cashType = null;
        if (mWalletConfigBean != null && mWalletConfigBean.getCash() != null) {
            cashType = mWalletConfigBean.getCash().getTypes();
        }
        if (cashType == null) {
            cashType = new ArrayList<>();
        }
        mActionPopupWindow = ActionPopupWindow.builder()
                .item2Str(cashType.contains(WalletConfigBean.CashBean.TYPE_ALIPAY) ? getString(R.string.choose_withdrawals_style_formart, getString(R.string.alipay)) : "")
                .item3Str(cashType.contains(WalletConfigBean.CashBean.TYPE_WECHAT) ? getString(R.string.choose_withdrawals_style_formart, getString(R.string.wxpay)) : "")
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(CustomPopupWindow.POPUPWINDOW_ALPHA)
                .with(getActivity())
                .item2ClickListener(new ActionPopupWindow.ActionPopupWindowItem2ClickListener() {
                    @Override
                    public void onItemClicked() {
                        mWithdrawalsType = WithdrawType.ALIWITHDRAW.type;
                        mBtWithdrawStyle.setRightText(getString(R.string.choose_withdrawals_style_formart, getString(R.string.alipay)));
                        mActionPopupWindow.hide();
                        configSureButton();
                    }
                })
                .item3ClickListener(new ActionPopupWindow.ActionPopupWindowItem3ClickListener() {
                    @Override
                    public void onItemClicked() {
                        mWithdrawalsType = WithdrawType.WXWITHDRAW.type;
                        mBtWithdrawStyle.setRightText(getString(R.string.choose_withdrawals_style_formart, getString(R.string.wxpay)));
                        mActionPopupWindow.hide();
                        configSureButton();
                    }
                })
                .bottomClickListener(new ActionPopupWindow.ActionPopupWindowBottomClickListener() {
                    @Override
                    public void onItemClicked() {
                        mActionPopupWindow.hide();
                    }
                })
                .build();
        mActionPopupWindow.show();
    }

    @OnClick(R.id.bt_sure)
    public void onViewClicked() {
    }

    private void configSureButton() {
        mBtSure.setEnabled(mWithdrawalsMoney > 0 && !TextUtils.isEmpty(mBtWithdrawStyle.getRightText())
                && !TextUtils.isEmpty(mEtWithdrawAccountInput.getText()));
    }
}
