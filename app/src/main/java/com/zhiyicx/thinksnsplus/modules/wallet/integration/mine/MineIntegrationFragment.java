package com.zhiyicx.thinksnsplus.modules.wallet.integration.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.widget.button.CombinationButton;
import com.zhiyicx.baseproject.widget.popwindow.CenterInfoPopWindow;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.UIUtils;
import com.zhiyicx.common.widget.popwindow.CustomPopupWindow;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.RealAdvertListBean;
import com.zhiyicx.thinksnsplus.data.beans.WalletConfigBean;
import com.zhiyicx.thinksnsplus.data.beans.integration.IntegrationConfigBean;
import com.zhiyicx.thinksnsplus.modules.develop.TSDevelopActivity;
import com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailAdvertHeader;
import com.zhiyicx.thinksnsplus.modules.settings.aboutus.CustomWEBActivity;
import com.zhiyicx.thinksnsplus.modules.wallet.WalletPresenter;
import com.zhiyicx.thinksnsplus.modules.wallet.bill.BillActivity;
import com.zhiyicx.thinksnsplus.modules.wallet.integration.recharge.IntegrationRechargeActivity;
import com.zhiyicx.thinksnsplus.modules.wallet.recharge.RechargeActivity;
import com.zhiyicx.thinksnsplus.modules.wallet.recharge.RechargeFragment;
import com.zhiyicx.thinksnsplus.modules.wallet.rule.WalletRuleActivity;
import com.zhiyicx.thinksnsplus.modules.wallet.rule.WalletRuleFragment;
import com.zhiyicx.thinksnsplus.modules.wallet.withdrawals.WithdrawalsActivity;
import com.zhiyicx.thinksnsplus.modules.wallet.withdrawals.WithdrawalsFragment;

import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
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
public class MineIntegrationFragment extends TSFragment<MineIntegrationContract.Presenter> implements MineIntegrationContract.View {

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


    /**
     * 充值提示规则选择弹框
     */
    private CenterInfoPopWindow mRulePop;
    private DynamicDetailAdvertHeader mDynamicDetailAdvertHeader;

    public static MineIntegrationFragment newInstance() {
        return new MineIntegrationFragment();
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
    protected boolean setStatusbarGrey() {
        return false;
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
    protected int setLeftImg() {
        return super.setLeftImg();
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_mine_integration;
    }

    @Override
    protected void initView(View rootView) {
        setStatusPlaceholderViewBackgroundColor(android.R.color.transparent);
        mIvRefresh = (ImageView) mRootView.findViewById(R.id.iv_refresh);
        mToolbar.setBackgroundResource(android.R.color.transparent);
        ((LinearLayout.LayoutParams) mToolbar.getLayoutParams()).setMargins(0, DeviceUtils.getStatuBarHeight(mActivity), 0, 0);
        mTvToolbarCenter.setTextColor(ContextCompat.getColor(mActivity, R.color.white));
        mTvToolbarCenter.setText(getString(R.string.mine_integration));
        mTvToolbarRight.setText(getString(R.string.detail));
        mTvToolbarLeft.setCompoundDrawables(UIUtils.getCompoundDrawables(getContext(), R.mipmap.topbar_back_white), null, null, null);

        initListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.updateUserInfo();
    }

    @Override
    protected void initData() {
        initAdvert(mActivity, new ArrayList<>());
    }

    private void initAdvert(Context context, List<RealAdvertListBean> adverts) {
        mDynamicDetailAdvertHeader = new DynamicDetailAdvertHeader(context, mRootView.findViewById(R.id.ll_advert));
        if (!com.zhiyicx.common.BuildConfig.USE_ADVERT || adverts == null || adverts != null && adverts.isEmpty()) {
            mDynamicDetailAdvertHeader.hideAdvert();
            return;
        }
        mDynamicDetailAdvertHeader.setAdverts(adverts);
        mDynamicDetailAdvertHeader.setOnItemClickListener((v, position1, url) ->
                toAdvert(context, adverts.get(position1).getAdvertFormat().getImage().getLink(), adverts.get(position1).getTitle())
        );
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mPresenter.checkIsNeedTipPop()) {
            getView().post(() -> mPresenter.checkIntegrationConfig(TAG_SHOWRULE_POP, false));
        }
    }

    @Override
    protected void setRightClick() {
        super.setRightClick();
        startActivity(new Intent(getActivity(), BillActivity.class));
    }

    private void initListener() {
        // 充值积分
        RxView.clicks(mBtReCharge)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> {
                    Intent intent = new Intent(mActivity, IntegrationRechargeActivity.class);
                    startActivity(intent);
                });
        // 提取积分
        RxView.clicks(mBtWithdraw)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> mPresenter.checkIntegrationConfig(WalletPresenter.TAG_WITHDRAW, true));     // 提现
        // 积分商城
        RxView.clicks(btMineIntegration)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> {
                    TSDevelopActivity.startDeveloperAcitvity(mActivity, getString(R.string.integration_shop)
                            , R.mipmap.pic_default_mall);
                });
        // 积分规则
        RxView.clicks(mTvReChargeAndWithdrawRule)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> {
                    mPresenter.checkIntegrationConfig(WalletPresenter.TAG_SHOWRULE_JUMP, true);
                });
        RxView.clicks(mTvToolbarLeft)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> {
                    mActivity.finish();
                });
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
                .titleStr(getString(R.string.integration_rule))
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
     * @param configBean integration config info
     * @param tag        action tag, 1 recharge 2 withdraw
     */
    @Override
    public void integrationConfigCallBack(IntegrationConfigBean configBean, int tag) {
        Bundle bundle = new Bundle();
        switch (tag) {
            case WalletPresenter.TAG_RECHARGE:
                bundle.putSerializable(RechargeFragment.BUNDLE_DATA, configBean);
                jumpActivity(bundle, RechargeActivity.class);
                break;
            case WalletPresenter.TAG_WITHDRAW:
                bundle.putSerializable(WithdrawalsFragment.BUNDLE_DATA, configBean);
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

    /**
     * jump  to  advert
     *
     * @param context
     * @param link
     * @param title
     */
    private void toAdvert(Context context, String link, String title) {
        CustomWEBActivity.startToWEBActivity(context, link, title);
    }

}
