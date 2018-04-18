package com.zhiyicx.thinksnsplus.modules.tb.tbmark_detail;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.RechargeSuccessBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.tb.wallet.WalletHeader;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * @Author MasonGhost
 * @Date 2018/04/17/14:55
 * @Email lx1993m@gmail.com
 * @Description
 */

public class TBMarkDetailFragment extends TSListFragment<TBMarkDetailContract.Presenter, RechargeSuccessBean> implements TBMarkDetailContract.View{

    public static final String BILL_TYPE = "bill_type";
    private TBMarkDetailHeader mTBMarkDetailHeader;

    // 上一个页面传过来的用户信息
    private String mBillType;
    private UserInfoBean mUserInfoBean;

    @Override
    protected boolean showToolbar() {
        return true;
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
    protected void initView(View rootView) {
        super.initView(rootView);
        setCenterText(mPresenter.getWalletGoldName());
        mRvList.setBackgroundResource(R.color.bgColor);
    }

    @Override
    protected void initData() {
        super.initData();
        mTBMarkDetailHeader = new TBMarkDetailHeader(mActivity);
        mHeaderAndFooterWrapper.addHeaderView(mTBMarkDetailHeader.getHeader());
        mBillType = getArguments().getParcelable(BILL_TYPE);
        mPresenter.getUserInfo();
    }

    public static TBMarkDetailFragment initFragment(Bundle bundle) {
        TBMarkDetailFragment tbMarkDetailFragment = new TBMarkDetailFragment();
        tbMarkDetailFragment.setArguments(bundle);
        return tbMarkDetailFragment;
    }

    @Override
    public void updateUserInfo(UserInfoBean data) {
        mUserInfoBean = data;
    }

    @Override
    protected int setEmptView() {
        return R.mipmap.def_wallet_prompt;
    }

    @Override
    public String getBillType() {
        return mBillType/*{""--全部, "income"--收入, "expenses"--支出}*/;
    }


    @Override
    protected RecyclerView.Adapter getAdapter() {
        CommonAdapter adapter = new CommonAdapter<RechargeSuccessBean>(mActivity, R.layout.item_wallet_for_tb, mListDatas) {
            @Override
            protected void convert(ViewHolder holder, RechargeSuccessBean rechargeSuccessBean, int position) {

                // 收入or支出 图标不一样
                // 收入 ico_in ，支出 ico_out
                int action = rechargeSuccessBean.getAction();
                ImageView imageView = holder.getImageViwe(R.id.iv_wallet_income);
                if (action < -1) {
                    imageView.setImageResource(R.mipmap.ico_out);
                } else {
                    imageView.setImageResource(R.mipmap.ico_in);
                }

                // 收支描述
                holder.setText(R.id.tv_wallet_dec, rechargeSuccessBean.getSubject());

                // 收支时间 TimeUtils.getYeayMonthDay()
                holder.setText(R.id.tv_wallet_time, TimeUtils.utc2LocalStr(rechargeSuccessBean.getCreated_at()));

                // 收支金额
                holder.setText(R.id.tv_wallet_count, action < 0 ? "-" + rechargeSuccessBean.getAmount() : "+" + rechargeSuccessBean.getAmount());
            }
        };
        return adapter;
    }
}
