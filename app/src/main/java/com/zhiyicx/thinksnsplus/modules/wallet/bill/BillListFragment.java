package com.zhiyicx.thinksnsplus.modules.wallet.bill;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.config.PayConfig;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.common.utils.recycleviewdecoration.CustomLinearDecoration;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.RechargeSuccessBean;
import com.zhiyicx.thinksnsplus.modules.wallet.bill_detail.BillDetailActivity;
import com.zhiyicx.thinksnsplus.modules.wallet.bill_detail.BillDetailBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;

/**
 * @Author Jliuer
 * @Date 2017/06/02/15:42
 * @Email Jliuer@aliyun.com
 * @Description 账单
 */
public class BillListFragment extends TSListFragment<BillContract.Presenter, RechargeSuccessBean> implements BillContract.View {

    public static final String BILL_INFO = "bill_info";

    @BindView(R.id.v_shadow)
    View mVshadow;

    private ActionPopupWindow mActionPopupWindow;

    /**
     * income - 收入 expenses - 支出 默认全部
     */
    private String[] mBillTypes = new String[]{"", "income", "expenses"};

    private String mBillType = mBillTypes[0];


    public static BillListFragment newInstance() {
        return new BillListFragment();
    }

    @Override
    protected boolean isNeedRefreshAnimation() {
        return false;
    }

    @Override
    protected boolean isNeedRefreshDataWhenComeIn() {
        return true;
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        CommonAdapter adapter = new CommonAdapter<RechargeSuccessBean>(getActivity(), R.layout.item_withdrawals_detail, mListDatas) {
            @Override
            protected void convert(ViewHolder holder, RechargeSuccessBean recharge, int position) {
                TextView desc = holder.getView(R.id.withdrawals_desc);
                TextView time = holder.getView(R.id.withdrawals_time);
                TextView account = holder.getView(R.id.withdrawals_account);
                boolean statusSuccess = recharge.getStatus() == 1;
                int action = recharge.getAction();
                desc.setEnabled(statusSuccess);
                String moneyStr = String.format(Locale.getDefault(), getString(R.string.money_format),
                        PayConfig.realCurrencyFen2Yuan(recharge.getAmount()));
                desc.setText(statusSuccess ? (action < 0 ? "-" + moneyStr : "+" + moneyStr) :
                        getString(recharge.getStatus() == 0 ? R.string.bill_doing : R.string.transaction_fail));
                account.setText(getDes(recharge));
                time.setText(TimeUtils.string2_ToDya_Yesterday_Week(recharge.getCreated_at()));
            }
        };
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (position < 0) {
                    return;
                }
                Intent intent = new Intent(getActivity(), BillDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(BILL_INFO, BillDetailBean.recharge2Bill(mListDatas.get(position), mPresenter.getRatio()));
                intent.putExtra(BILL_INFO, bundle);
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        return adapter;
    }

    @NonNull
    private String getDes(RechargeSuccessBean recharge) {

        String result = "";
        switch (recharge.getChannel()) {
            case "alipay":
            case "alipay_wap":
            case "alipay_pc_direct":
            case "alipay_qr":
                result = getString(R.string.alipay) + " " + recharge.getBody();
                break;
            case "wx":
            case "wx_pub":
            case "wx_pub_qr":
            case "wx_wap":
            case "wx_lite":
                result = getString(R.string.wxpay) + " " + recharge.getBody();
                break;
            case "applepay_upacp":
                result = getString(R.string.apple_pay_upacp) + " " + recharge.getBody();
                break;
            default:
                if (TextUtils.isEmpty(recharge.getBody())) {
                    result = recharge.getSubject();

                } else {
                    result = recharge.getBody();
                }


        }
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
        mToolbarCenter.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ico_detail_arrowdown, 0);
        return getString(R.string.detail);
    }

    @Override
    protected void setCenterClick() {
        mActionPopupWindow.showTop();
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        initTopPopWindow();
    }

    @Override
    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new CustomLinearDecoration(0, getResources().getDimensionPixelSize(R.dimen
                .divider_line), 0, 0, ContextCompat.getDrawable(getContext(), R.drawable
                .shape_recyclerview_grey_divider));
    }

    @Override
    public String getBillType() {
        return mBillTypes[0].equals(mBillType) ? null : mBillType;
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
                    startRefrsh();
                    mToolbarCenter.setText(getString(R.string.withdraw_all));
                    mBillType = mBillTypes[0];
                    mActionPopupWindow.hide();
                })
                .item2ClickListener(() -> {
                    startRefrsh();
                    mToolbarCenter.setText(getString(R.string.withdraw_out));
                    mBillType = mBillTypes[2];
                    mActionPopupWindow.hide();
                    startRefrsh();

                })
                .item3ClickListener(() -> {
                    startRefrsh();
                    mToolbarCenter.setText(getString(R.string.withdraw_in));
                    mBillType = mBillTypes[1];
                    mActionPopupWindow.hide();

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
