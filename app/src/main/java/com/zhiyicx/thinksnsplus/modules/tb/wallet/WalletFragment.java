package com.zhiyicx.thinksnsplus.modules.tb.wallet;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.thinksnsplus.R;
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
public class WalletFragment extends TSListFragment<WalletContract.Presenter, WalletData> implements WalletContract.View {

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
        mWalletToolbar.setPadding(0, DeviceUtils.getStatuBarHeight(mActivity),0,0);
        mTvToolbarLeft.setOnClickListener(v -> getActivity().finish());
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
    protected RecyclerView.Adapter getAdapter() {
        CommonAdapter adapter = new CommonAdapter<WalletData>(mActivity, R.layout.item_wallet_for_tb, mListDatas) {
            @Override
            protected void convert(ViewHolder holder, WalletData walletData, int position) {

                // 收入or支出 图标不一样
                // 收入 ico_in ，支出 ico_out
                ImageView imageView = holder.getImageViwe(R.id.iv_wallet_income);

                // 收支描述
                holder.setText(R.id.tv_wallet_dec, "sss");

                // 收支时间 TimeUtils.getYeayMonthDay()
                holder.setText(R.id.tv_wallet_time, "sss");

                // 收支金额
                holder.setText(R.id.tv_wallet_count, "sss");
            }
        };
        return adapter;
    }
}
