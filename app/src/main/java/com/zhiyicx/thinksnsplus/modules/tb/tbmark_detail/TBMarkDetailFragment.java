package com.zhiyicx.thinksnsplus.modules.tb.tbmark_detail;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.CandyWalletOrderBean;
import com.zhiyicx.thinksnsplus.data.beans.RechargeSuccessBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.tbcandy.CandyCateBean;
import com.zhiyicx.thinksnsplus.modules.tb.wallet.WalletHeader;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * @Author MasonGhost
 * @Date 2018/04/17/14:55
 * @Email lx1993m@gmail.com
 * @Description
 */

public class TBMarkDetailFragment extends TSListFragment<TBMarkDetailContract.Presenter, CandyWalletOrderBean> implements TBMarkDetailContract.View{

    public static final String CANDY = "candy";
    private TBMarkDetailHeader mTBMarkDetailHeader;

    // 上一个页面传过来的用户信息
    private String mCandyCateId;
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
        mCandyCateId = getArguments().getString(CANDY);
        if(mCandyCateId != null){
            mTBMarkDetailHeader = new TBMarkDetailHeader(mActivity);
            mHeaderAndFooterWrapper.addHeaderView(mTBMarkDetailHeader.getHeader());
        }
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
    public CandyCateBean getCurrentCandy() {
        return null;
    }

    @Override
    protected int setEmptView() {
        return R.mipmap.def_wallet_prompt;
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        CommonAdapter adapter = new CommonAdapter<CandyWalletOrderBean>(mActivity, R.layout.item_wallet_for_tb, mListDatas) {
            @Override
            protected void convert(ViewHolder holder, CandyWalletOrderBean candyWalletOrderBean, int position) {

                // 收入or支出 图标不一样
                // 收入 ico_in ，支出 ico_out
                int type = candyWalletOrderBean.getType();
                ImageView imageView = holder.getImageViwe(R.id.iv_wallet_income);
                switch (type){
                    case 1: {
                        imageView.setImageResource(R.mipmap.ico_in);
                        // 收支金额
                        holder.setText(R.id.tv_wallet_count, "+" + candyWalletOrderBean.getCount());
                        break;
                    }
                    case 2: {
                        imageView.setImageResource(R.mipmap.ico_out);
                        // 收支金额
                        holder.setText(R.id.tv_wallet_count, "-" + candyWalletOrderBean.getCount());
                        break;
                    }
                    default:
                        break;
                }

                // 收支描述
                holder.setText(R.id.tv_wallet_dec, candyWalletOrderBean.getTitle());

                // 收支时间 TimeUtils.getYeayMonthDay()
                holder.setText(R.id.tv_wallet_time, TimeUtils.utc2LocalStr(candyWalletOrderBean.getCreated_at()));
            }
        };
        return adapter;
    }
}
