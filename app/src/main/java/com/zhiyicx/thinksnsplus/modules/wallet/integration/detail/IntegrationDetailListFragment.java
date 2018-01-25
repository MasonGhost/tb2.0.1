package com.zhiyicx.thinksnsplus.modules.wallet.integration.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.common.utils.recycleviewdecoration.CustomLinearDecoration;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.RechargeSuccessV2Bean;
import com.zhiyicx.thinksnsplus.data.beans.integration.IntegrationConfigBean;
import com.zhiyicx.thinksnsplus.modules.wallet.rule.WalletRuleActivity;
import com.zhiyicx.thinksnsplus.modules.wallet.rule.WalletRuleFragment;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @Author Jliuer
 * @Date 2017/06/02/15:42
 * @Email Jliuer@aliyun.com
 * @Description 账单
 */
public class IntegrationDetailListFragment extends TSListFragment<IntegrationDetailContract.Presenter, RechargeSuccessV2Bean> implements
        IntegrationDetailContract.View {
    public static final String BUNDLE_INTEGRATION_CONFIG = "config";

    private static final String BUNDLE_CHOOSE_TYPE = "CHOOSE_TYPE";
    public static final String CHOOSE_TYPE_RECHARGE = "recharge";
    public static final String CHOOSE_TYPE_CASH = "cash";


    @BindView(R.id.v_shadow)
    View mVshadow;

    @BindView(R.id.tv_rule)
    View mTvRule;

    private ActionPopupWindow mActionPopupWindow;

    @Inject
    IntegrationDetailPresenter mIntegrationDetailPresenter;


    /**
     * 0 全部，1 收入，-1 支出
     */
    private int[] mBillTypes = new int[]{0, 1, -1};

    private int mBillType = mBillTypes[0];
    /**
     * 明细类型 	筛选类型 recharge - 充值记录 cash - 提现记录 默认为全部
     */
    private String mChooseType;

    /**
     * 积分配置
     */
    private IntegrationConfigBean mIntegrationConfigBean;

    /**
     * 构造方法
     *
     * @param chooseType
     * @param configBean
     * @return
     */
    public static IntegrationDetailListFragment newInstance(String chooseType, IntegrationConfigBean configBean) {
        IntegrationDetailListFragment integrationDetailListFragment = new IntegrationDetailListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_CHOOSE_TYPE, chooseType);
        bundle.putSerializable(BUNDLE_INTEGRATION_CONFIG, configBean);
        integrationDetailListFragment.setArguments(bundle);
        return integrationDetailListFragment;
    }

    /**
     * 构造方法
     *
     * @param chooseType
     * @return
     */
    public static IntegrationDetailListFragment newInstance(String chooseType) {
        IntegrationDetailListFragment integrationDetailListFragment = new IntegrationDetailListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_CHOOSE_TYPE, chooseType);
        integrationDetailListFragment.setArguments(bundle);
        return integrationDetailListFragment;
    }

    @Override
    protected boolean showToolbar() {
        return mChooseType == null;
    }

    @Override
    protected boolean showToolBarDivider() {
        return mChooseType == null;
    }

    @Override
    protected boolean isNeedRefreshDataWhenComeIn() {
        return true;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerIntegrationDetailComponent
                .builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .integrationDetailPresenterModule(new IntegrationDetailPresenterModule(IntegrationDetailListFragment.this))
                .build()
                .inject(IntegrationDetailListFragment.this);
        if (getArguments() != null) {
            mChooseType = getArguments().getString(BUNDLE_CHOOSE_TYPE);
            mIntegrationConfigBean = (IntegrationConfigBean) getArguments().getSerializable(BUNDLE_INTEGRATION_CONFIG);
        }
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        CommonAdapter adapter;
        // 充值与体现
        if (mChooseType != null) {
            adapter = new CommonAdapter<RechargeSuccessV2Bean>(getActivity(), R.layout.item_integration_withdrawals_detail_recharge_and_cash,
                    mListDatas) {
                @Override
                protected void convert(ViewHolder holder, RechargeSuccessV2Bean recharge, int position) {
                    TextView desc = holder.getView(R.id.withdrawals_desc);
                    TextView time = holder.getView(R.id.withdrawals_time);
                    TextView account = holder.getView(R.id.withdrawals_account);
                    boolean statusSuccess = recharge.getState() == 1;
                    int action = recharge.getType();
                    desc.setEnabled(statusSuccess);
                    String moneyStr = String.valueOf(recharge.getAmount());
                    desc.setText(statusSuccess ? (action < 0 ? "- " + moneyStr : "+ " + moneyStr) :
                            getString(recharge.getState() == 0 ? R.string.bill_doing : R.string.transaction_fail));
                    account.setText(getDes(recharge));
                    time.setText(TimeUtils.getTimeFriendlyForDetail(recharge.getCreated_at()));
                }
            };
        } else {
            // 收入支出，全部
            adapter = new CommonAdapter<RechargeSuccessV2Bean>(getActivity(), R.layout.item_integration_withdrawals_detail,
                    mListDatas) {
                @Override
                protected void convert(ViewHolder holder, RechargeSuccessV2Bean recharge, int position) {
                    TextView desc = holder.getView(R.id.withdrawals_desc);
                    TextView time = holder.getView(R.id.withdrawals_time);
                    TextView account = holder.getView(R.id.withdrawals_account);
                    boolean statusSuccess = recharge.getState() == 1;
                    int action = recharge.getType();
                    desc.setEnabled(statusSuccess);
                    String moneyStr = String.valueOf(recharge.getAmount());
                    desc.setText(statusSuccess ? (action < 0 ? "- " + moneyStr : "+ " + moneyStr) :
                            getString(recharge.getState() == 0 ? R.string.bill_doing : R.string.transaction_fail));
                    account.setText(getDes(recharge));
                    time.setText(TimeUtils.getTimeFriendlyForDetail(recharge.getCreated_at()));
                }
            };
            mTvRule.setVisibility(View.VISIBLE);
            RxView.clicks(mTvRule)
                    .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                    .compose(bindToLifecycle())
                    .subscribe(aVoid -> {
                        Intent intent = new Intent(mActivity, WalletRuleActivity.class);
                        if (mIntegrationConfigBean != null) {
                            Bundle bundle = new Bundle();
                            bundle.putString(WalletRuleFragment.BUNDLE_RULE, mIntegrationConfigBean.getRechargerule());
                            intent.putExtras(bundle);
                        }
                        startActivity(intent);
                    });

        }
        return adapter;
    }

    @NonNull
    private String getDes(RechargeSuccessV2Bean recharge) {

        String result = recharge.getTitle();
//        /**
//         * 操作类型 目前有： default - 默认操作、commodity - 购买积分商品、user - 用户到用户流程（如采纳、付费置顶等）、task - 积分任务、recharge - 充值、cash - 积分提取
//         */
//        switch (recharge.getTarget_type()) {
//            case DEFAULT:
//            case "alipay_wap":
//            case "alipay_pc_direct":
//            case "alipay_qr":
//                result = getString(R.string.alipay) + " " + recharge.getBody();
//                break;
//            case "wx":
//            case "wx_pub":
//            case "wx_pub_qr":
//            case "wx_wap":
//            case "wx_lite":
//                result = getString(R.string.wxpay) + " " + recharge.getBody();
//                break;
//            case "applepay_upacp":
//                result = getString(R.string.apple_pay_upacp) + " " + recharge.getBody();
//                break;
//            default:
//                result = recharge.getTitle();
//
//        }
        return result;
    }

    @Override
    public HeaderAndFooterWrapper getTSAdapter() {
        return mHeaderAndFooterWrapper;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_withdrawals_detail;
    }

    @Override
    protected String setCenterTitle() {
        mToolbarCenter.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
//        mToolbarCenter.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ico_detail_arrowdown, 0);
        return getString(R.string.integration_detail);
    }

    @Override
    protected void setCenterClick() {
//        mActionPopupWindow.showTop();
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
//        initTopPopWindow();
    }

    @Override
    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new CustomLinearDecoration(0, getResources().getDimensionPixelSize(R.dimen
                .divider_line), 0, 0, ContextCompat.getDrawable(getContext(), R.drawable
                .shape_recyclerview_grey_divider));
    }

    @Override
    public Integer getBillType() {
        return mBillType == mBillTypes[0] ? null : mBillType;

    }

    @Override
    public String getChooseType() {
        return mChooseType;
    }

    private void initTopPopWindow() {
        if (mActionPopupWindow != null) {
            return;
        }
        mActionPopupWindow = ActionPopupWindow.builder()
                .with(getActivity())
                .isFocus(true)
                .isOutsideTouch(true)
                .parentView(mDriver)
                .animationStyle(ActionPopupWindow.NO_ANIMATION)
                .item1Str(getString(R.string.withdraw_all))
                .item2Str(getString(R.string.withdraw_out))
                .item3Str(getString(R.string.withdraw_in))
                .item1ClickListener(() -> {
                    mToolbarCenter.setText(getString(R.string.withdraw_all));
                    mBillType = mBillTypes[0];
                    mActionPopupWindow.hide();
                    startRefrsh();
                })
                .item2ClickListener(() -> {
                    mToolbarCenter.setText(getString(R.string.withdraw_out));
                    mBillType = mBillTypes[2];
                    mActionPopupWindow.hide();
                    startRefrsh();

                })
                .item3ClickListener(() -> {
                    mToolbarCenter.setText(getString(R.string.withdraw_in));
                    mBillType = mBillTypes[1];
                    mActionPopupWindow.hide();
                    startRefrsh();
                })
                .dismissListener(new ActionPopupWindow.ActionPopupWindowShowOrDismissListener() {
                    @Override
                    public void onShow() {
                        mToolbarCenter.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ico_detail_arrowup, 0);
                        mVshadow.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onDismiss() {
                        mToolbarCenter.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ico_detail_arrowdown, 0);
                        mVshadow.setVisibility(View.GONE);
                    }
                })
                .build();
    }
}
