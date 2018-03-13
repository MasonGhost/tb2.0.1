package com.zhiyicx.thinksnsplus.modules.wallet.withdrawals.list_detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.config.PayConfig;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.common.utils.recycleviewdecoration.CustomLinearDecoration;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.WithdrawalsListBean;
import com.zhiyicx.thinksnsplus.modules.wallet.PayType;
import com.zhiyicx.thinksnsplus.modules.wallet.bill_detail.BillDetailActivity;
import com.zhiyicx.thinksnsplus.modules.wallet.bill_detail.BillDetailBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import static com.zhiyicx.thinksnsplus.modules.wallet.bill.BillListFragment.BILL_INFO;

/**
 * @Author Jliuer
 * @Date 2017/05/23/10:56
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class WithdrawalsDetailFragment extends TSListFragment<WithdrawalsDetailConstract.Presenter, WithdrawalsListBean>
        implements WithdrawalsDetailConstract.View {

    public static WithdrawalsDetailFragment newInstance() {
        return new WithdrawalsDetailFragment();
    }

    @Override
    protected boolean isNeedRefreshDataWhenComeIn() {
        return true;
    }

    @Override
    protected boolean isNeedRefreshAnimation() {
        return false;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_withdrawals_detail;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.withdraw_details);
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        CommonAdapter adapter = new CommonAdapter<WithdrawalsListBean>(getActivity(), R.layout.item_withdrawals_detail, mListDatas) {
            @Override
            protected void convert(ViewHolder holder, WithdrawalsListBean withdrawal, int position) {
                TextView desc = holder.getView(R.id.withdrawals_desc);
                TextView time = holder.getView(R.id.withdrawals_time);
                TextView account = holder.getView(R.id.withdrawals_account);
                int status = withdrawal.getStatus();
                boolean status_success = status == 1;
                desc.setEnabled(status_success);
                String moneyStr = String.format(Locale.getDefault(), getString(R.string.money_format),
                        PayConfig.realCurrencyFen2Yuan(withdrawal.getValue()));
                desc.setText(status_success ? "- " + moneyStr : (getString(status == 0 ? R.string.bill_doing : R.string.transaction_fail)));
                account.setText(String.format(getString(R.string.withdraw_money_done),
                        PayType.getValue(withdrawal.getType().toLowerCase())));
                time.setText(TimeUtils.string2_ToDya_Yesterday_Week(withdrawal.getCreated_at()));

            }
        };
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(getActivity(), BillDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(BILL_INFO, BillDetailBean.withdrawals2Bill(mListDatas.get(position),mPresenter.getRatio()));
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
    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new CustomLinearDecoration(0, getResources().getDimensionPixelSize(R.dimen
                .divider_line), 0, 0, ContextCompat.getDrawable(getContext(), R.drawable
                .shape_recyclerview_grey_divider));
    }
}
