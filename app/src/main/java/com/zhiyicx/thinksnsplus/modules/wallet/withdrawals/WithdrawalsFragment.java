package com.zhiyicx.thinksnsplus.modules.wallet.withdrawals;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.config.PayConfig;
import com.zhiyicx.baseproject.widget.button.CombinationButton;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.widget.popwindow.CustomPopupWindow;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.WalletConfigBean;
import com.zhiyicx.thinksnsplus.data.beans.WithdrawResultBean;
import com.zhiyicx.thinksnsplus.modules.wallet.withdrawals.list_detail.WithdrawalsDetailActivity;
import com.zhiyicx.tspay.TSPayClient;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.Observable;

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
    public WalletConfigBean getWalletConfigBean() {
        return mWalletConfigBean;
    }

    @Override
    public void withdrawResult(WithdrawResultBean withdrawResultBean) {

    }

    @Override
    public void minMoneyLimit() {
        initWithdrawalsInstructionsPop(R.string.min_withdraw_money);
    }

    @Override
    public void maxMoneyLimit() {
        initWithdrawalsInstructionsPop(R.string.limit_withdraw_money);
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_withdrawals;
    }

    @Override
    public void configSureBtn(boolean enable) {
        mBtSure.setEnabled(enable);
    }

    @Override
    public double getMoney() {
        return mWithdrawalsMoney;
    }

    private void setCustomMoneyDefault() {
        mEtWithdrawInput.setText("");
    }

    private void initListener() {
        RxView.clicks(mBtSure)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> {
                    DeviceUtils.hideSoftKeyboard(getContext(), mEtWithdrawInput);
                    mPresenter.withdraw(mWithdrawalsMoney
                            , mWithdrawalsType, mEtWithdrawAccountInput.getText().toString());
                });

        RxView.clicks(mBtWithdrawStyle).subscribe(aVoid -> {
            DeviceUtils.hideSoftKeyboard(getContext(), mEtWithdrawInput);
            initWithDrawalsStylePop();
        });

        Observable.combineLatest(RxTextView.textChanges(mEtWithdrawInput), RxTextView.textChanges(mEtWithdrawAccountInput),
                RxTextView.textChanges(mBtWithdrawStyle.getCombinedButtonRightTextView()),
                (charSequence, charSequence2, charSequence3) -> {
                    String mWithdrawalsMoneyStr = charSequence.toString();
                    if (mWithdrawalsMoneyStr.replaceAll(" ", "").length() > 0 && !mWithdrawalsMoneyStr.contains(".")) {
                        mWithdrawalsMoney = Double.parseDouble(charSequence.toString());
                    } else {
                        mWithdrawalsMoney = 0;
                    }
                    return mWithdrawalsMoney > 0 && !TextUtils.isEmpty(charSequence2) && !TextUtils.isEmpty(charSequence3);
                }).subscribe(aBoolean -> mBtSure.setEnabled(aBoolean));
    }

    @Override
    public void initWithdrawalsInstructionsPop(int resDesStr) {
        if (mWithdrawalsInstructionsPopupWindow != null) {
            mWithdrawalsInstructionsPopupWindow = mWithdrawalsInstructionsPopupWindow.newBuilder()
                    .desStr(getString(resDesStr))
                    .build();
            mWithdrawalsInstructionsPopupWindow.show();
            return;
        }
        mWithdrawalsInstructionsPopupWindow = ActionPopupWindow.builder()
                .item1Str(getString(R.string.withdrawal_instructions))
                .desStr(getString(resDesStr))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(CustomPopupWindow.POPUPWINDOW_ALPHA)
                .with(getActivity())
                .bottomClickListener(() -> mWithdrawalsInstructionsPopupWindow.hide())
                .build();
        mWithdrawalsInstructionsPopupWindow.show();
    }

    private void initWithDrawalsStylePop() {
        if (mActionPopupWindow != null) {
            mActionPopupWindow.show();
            return;
        }

        List<String> recharge_types = Arrays.asList(mWalletConfigBean.getRecharge_type());
        mActionPopupWindow = ActionPopupWindow.builder()
                .item2Str(recharge_types.contains(TSPayClient.CHANNEL_ALIPAY) ? getString(R.string.choose_withdrawals_style_formart, getString(R.string.alipay)) : "")
                .item3Str(recharge_types.contains(TSPayClient.CHANNEL_WXPAY) ? getString(R.string.choose_withdrawals_style_formart, getString(R.string.wxpay)) : "")
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(CustomPopupWindow.POPUPWINDOW_ALPHA)
                .with(getActivity())
                .item2ClickListener(() -> {
                    mWithdrawalsType = WithdrawType.ALIWITHDRAW.type;
                    mBtWithdrawStyle.setRightText(getString(R.string.choose_withdrawals_style_formart, getString(R.string.alipay)));
                    mActionPopupWindow.hide();
                })
                .item3ClickListener(() -> {
                    mWithdrawalsType = WithdrawType.WXWITHDRAW.type;
                    mBtWithdrawStyle.setRightText(getString(R.string.choose_withdrawals_style_formart, getString(R.string.wxpay)));
                    mActionPopupWindow.hide();
                })
                .bottomClickListener(() -> mActionPopupWindow.hide())
                .build();
        mActionPopupWindow.show();
    }

}
