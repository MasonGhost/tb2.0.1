package com.zhiyicx.thinksnsplus.modules.wallet;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.widget.button.CombinationButton;
import com.zhiyicx.baseproject.widget.popwindow.CenterInfoPopWindow;
import com.zhiyicx.common.widget.popwindow.CustomPopupWindow;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.modules.wallet.recharge.RechargeActivity;
import com.zhiyicx.thinksnsplus.modules.wallet.withdrawals.WithdrawalsActivity;
import com.zhiyicx.thinksnsplus.modules.wallet.withdrawals.detail.WithdrawalsDetailActivity;

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
    protected void setRightClick() {
        super.setRightClick();
        startActivity(new Intent(getActivity(), WithdrawalsDetailActivity.class));
    }

    private void initListener() {
        // 充值
        RxView.clicks(mBtReCharge)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .compose(this.<Void>bindToLifecycle())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(getActivity(), RechargeActivity.class));
                    }
                });
        // 提现
        RxView.clicks(mBtWithdraw)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .compose(this.<Void>bindToLifecycle())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(getActivity(), WithdrawalsActivity.class));
                    }
                });
        // 充值提现规则
        RxView.clicks(mTvReChargeAndWithdrawRule)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .compose(this.<Void>bindToLifecycle())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        showRulePopupWindow();
                    }
                });
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
                .desStr(getString(R.string.recharge_and_withdraw_rule_detail))
                .item1Str(getString(R.string.get_it))
                .item1Color(R.color.themeColor)
                .isOutsideTouch(true)
                .isFocus(true)
                .animationStyle(R.style.style_actionPopupAnimation)
                .backgroundAlpha(CustomPopupWindow.POPUPWINDOW_ALPHA)
                .with(getActivity())
                .buildCenterPopWindowItem1ClickListener(new CenterInfoPopWindow.CenterPopWindowItem1ClickListener() {
                    @Override
                    public void onClicked() {
                        mRulePop.hide();
                    }
                })
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
}
