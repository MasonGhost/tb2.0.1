package com.zhiyicx.thinksnsplus.modules.wallet.bill;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.zhiyicx.baseproject.base.TSListFragment;
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

    private int[] mBillTypes = new int[]{0, 1, 2};
    private int mBillType;

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
                boolean status_success = recharge.getStatus() == 1;
                int action = recharge.getAction();
                desc.setEnabled(status_success);

                desc.setText(status_success ? (action > 1 ? "- " + recharge.getAmount() : "+ " + recharge.getAmount()) : getString(R.string.bill_doing));
                account.setText(recharge.getSubject() + " " + recharge.getBody());
                time.setText(TimeUtils.string2_ToDya_Yesterday_Week(recharge.getCreated_at()));
            }
        };
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(getActivity(), BillDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(BILL_INFO, BillDetailBean.recharge2Bill(mListDatas.get(position)));
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
    public int getBillType() {
        return mBillType;
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
                .item1ClickListener(new ActionPopupWindow.ActionPopupWindowItem1ClickListener() {
                    @Override
                    public void onItemClicked() {
                        mToolbarCenter.setText(getString(R.string.withdraw_all));
                        mPresenter.selectAll();
                        mBillType = mBillTypes[0];
                        mActionPopupWindow.hide();
                    }
                })
                .item2ClickListener(new ActionPopupWindow.ActionPopupWindowItem2ClickListener() {
                    @Override
                    public void onItemClicked() {
                        mToolbarCenter.setText(getString(R.string.withdraw_out));
                        mPresenter.selectBillByAction(2);
                        mBillType = mBillTypes[2];
                        mActionPopupWindow.hide();
                    }
                })
                .item3ClickListener(new ActionPopupWindow.ActionPopupWindowItem3ClickListener() {
                    @Override
                    public void onItemClicked() {
                        mToolbarCenter.setText(getString(R.string.withdraw_in));
                        mPresenter.selectBillByAction(1);
                        mBillType = mBillTypes[1];
                        mActionPopupWindow.hide();
                    }
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
