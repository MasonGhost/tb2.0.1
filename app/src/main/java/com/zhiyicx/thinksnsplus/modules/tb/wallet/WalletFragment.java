package com.zhiyicx.thinksnsplus.modules.tb.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.CandyWalletBean;
import com.zhiyicx.thinksnsplus.data.beans.RechargeSuccessBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.tb.tbmark_detail.TBMarkDetailActivity;
import com.zhiyicx.thinksnsplus.modules.tb.tbmark_detail.TBMarkDetailFragment;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import butterknife.BindView;

/**
 * @Author Jliuer
 * @Date 2018/02/28/19:14
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class WalletFragment extends TSListFragment<WalletContract.Presenter, RechargeSuccessBean> implements WalletContract.View {

    @BindView(R.id.wallet_toolbar)
    Toolbar mWalletToolbar;
    @BindView(R.id.wallet_tv_toolbar_left)
    TextView mTvToolbarLeft;
    @BindView(R.id.wallet_tv_toolbar_right)
    TextView mTvToolbarRight;

    private WalletHeader mWalletHeader;
    private UserInfoBean mUserInfoBean;


    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_wallet_for_tb;
    }

    @Override
    protected boolean showToolbar() {
        return false;
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
        //mWalletToolbar.setPadding(0, DeviceUtils.getStatuBarHeight(mActivity), 0, 0);
        mTvToolbarLeft.setOnClickListener(v -> getActivity().finish());
        mTvToolbarRight.setVisibility(View.GONE);
        mTvToolbarRight.setText(getString(R.string.detail));
        mTvToolbarRight.setOnClickListener(v -> {
            /*Intent intent = new Intent(mActivity, TBMarkDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable(TBMarkDetailFragment.BILL_TYPE, null);
            intent.putExtras(bundle);
            mActivity.startActivity(intent);*/
        });
        mRvList.setBackgroundResource(R.color.bgColor);
    }

    @Override
    protected void initData() {
        mWalletHeader = new WalletHeader(mActivity);
        mHeaderAndFooterWrapper.addHeaderView(mWalletHeader.getHeader());
        super.initData();
        mPresenter.getUserInfo();
    }

    @Override
    public void updateUserInfo(UserInfoBean data) {
        mUserInfoBean = data;
        mWalletHeader.updateWallet(data, mPresenter.getWalletGoldName());
    }

    @Override
    protected int setEmptView() {
        return R.mipmap.def_wallet_prompt;
    }

    @Override
    public String getBillType() {
        return null/*{""--全部, "income"--收入, "expenses"--支出}*/;
    }


    @Override
    protected RecyclerView.Adapter getAdapter() {
        CommonAdapter adapter = new CommonAdapter<RechargeSuccessBean>(mActivity, R.layout.item_wallet_for_tb, mListDatas) {
            @Override
            protected void convert(ViewHolder holder, RechargeSuccessBean rechargeSuccessBean, int position) {
                // 收入or支出 图标不一样
                // 收入 ico_in ，支出 ico_out
                int type = rechargeSuccessBean.getAction();

                ImageView imageView = holder.getImageViwe(R.id.iv_wallet_income);
                if(type < 1){
                    imageView.setImageResource(R.mipmap.ico_out);
                } else {
                    imageView.setImageResource(R.mipmap.ico_in);
                }
                /*switch (type){
                    case 1: {
                        imageView.setImageResource(R.mipmap.ico_in);

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
                }*/

                // 收支描述
                holder.setText(R.id.tv_wallet_dec, rechargeSuccessBean.getSubject());

                // 收支时间 TimeUtils.getYeayMonthDay()
                holder.setText(R.id.tv_wallet_time, TimeUtils.utc2LocalStr(rechargeSuccessBean.getCreated_at()));

                // 收支金额
                holder.setText(R.id.tv_wallet_count, type < 1 ? "-" + rechargeSuccessBean.getAmount() : "+" + rechargeSuccessBean.getAmount());
            }
        };
        return adapter;
    }
}
