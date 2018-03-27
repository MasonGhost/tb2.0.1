package com.zhiyicx.thinksnsplus.modules.tb.wallet;

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
import com.zhiyicx.thinksnsplus.data.beans.RechargeSuccessBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
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
        mWalletToolbar.setPadding(0, DeviceUtils.getStatuBarHeight(mActivity), 0, 0);
        mTvToolbarLeft.setOnClickListener(v -> getActivity().finish());
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
