package com.zhiyicx.thinksnsplus.modules.wallet.bill_detail;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.widget.textview.DrawableSizeTextView;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.RechargeSuccessBean;

import butterknife.BindView;

import static com.zhiyicx.thinksnsplus.modules.wallet.bill.BillListFragment.BILL_INFO;

/**
 * @Author Jliuer
 * @Date 2017/05/23/15:02
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class BillDetailFragment extends TSFragment {

    @BindView(R.id.bill_status)
    TextView mBillStatus;
    @BindView(R.id.tv_mine_money)
    TextView mTvMineMoney;
    @BindView(R.id.bill_user)
    TextView mBillUser;
    @BindView(R.id.bill_user_container)
    LinearLayout mBillUserContainer;
    @BindView(R.id.bill_user_head)
    DrawableSizeTextView mBillUserHead;
    @BindView(R.id.bill_account)
    TextView mBillAccount;
    @BindView(R.id.bill_account_container)
    LinearLayout mBillAccountContainer;
    @BindView(R.id.bill_desc)
    TextView mBillDesc;
    @BindView(R.id.bill_time)
    TextView mBillTime;

    private BillDetailBean mBillDetailBean;
    private int userId;

    public static BillDetailFragment getInstance(Bundle bundle) {
        BillDetailFragment billDetailFragment = new BillDetailFragment();
        billDetailFragment.setArguments(bundle);
        return billDetailFragment;
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.account_detail);
    }

    @Override
    protected void initView(View rootView) {

    }

    @Override
    protected void initData() {
        mBillDetailBean = getArguments().getParcelable(BILL_INFO);
        int action = mBillDetailBean.getAction();
        int status = mBillDetailBean.getStatus();
        boolean is_user = true;
        try {
            userId = Integer.valueOf(mBillDetailBean.getAccount());
        } catch (NumberFormatException e) {
            is_user = false;
            e.printStackTrace();
        }
        mBillStatus.setText(getString(status == 0 ? R.string.transaction_doing : (status == 1 ? R.string.transaction_success : R.string.transaction_fail)));
        String moneyStr = (status == 1 ? (action == 0 ? "- " : "+ ") : "") + String.valueOf(mBillDetailBean.getAmount());
        mTvMineMoney.setText(moneyStr);
        mBillUserContainer.setVisibility(is_user ? View.VISIBLE : View.GONE);
        mBillAccountContainer.setVisibility(is_user ? View.GONE : View.VISIBLE);
        mBillAccount.setText(TextUtils.isEmpty(mBillDetailBean.getAccount()) ? mBillDetailBean.getChannel() : mBillDetailBean.getAccount());
        mBillDesc.setText(mBillDetailBean.getBody());
        mBillTime.setText(TimeUtils.string2_Dya_Week_Time(mBillDetailBean.getCreated_at()));
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_account;
    }

}
