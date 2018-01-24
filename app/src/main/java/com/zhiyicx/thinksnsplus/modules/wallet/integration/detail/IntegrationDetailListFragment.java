package com.zhiyicx.thinksnsplus.modules.wallet.integration.detail;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.common.utils.recycleviewdecoration.CustomLinearDecoration;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.RechargeSuccessV2Bean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import butterknife.BindView;

/**
 * @Author Jliuer
 * @Date 2017/06/02/15:42
 * @Email Jliuer@aliyun.com
 * @Description 账单
 */
public class IntegrationDetailListFragment extends TSListFragment<IntegrationDetailContract.Presenter, RechargeSuccessV2Bean> implements
        IntegrationDetailContract.View {


    @BindView(R.id.v_shadow)
    View mVshadow;

    private ActionPopupWindow mActionPopupWindow;

    /**
     * 0 全部，1 收入，-1 支出
     */
    private int[] mBillTypes = new int[]{0, 1, -1};

    private int mBillType = mBillTypes[0];

    public static IntegrationDetailListFragment newInstance() {
        return new IntegrationDetailListFragment();
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
        CommonAdapter adapter = new CommonAdapter<RechargeSuccessV2Bean>(getActivity(), R.layout.item_integration_withdrawals_detail, mListDatas) {
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
        return getString(R.string.detail);
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
