package com.zhiyicx.thinksnsplus.modules.tb.exchange;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.data.beans.RechargeSuccessBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.tb.wallet.WalletHeader;

/**
 * @Author MasonGhost
 * @Date 2018/04/17/14:55
 * @Email lx1993m@gmail.com
 * @Description
 */

public class ExchangeFragment extends TSListFragment<ExchangeContract.Presenter, RechargeSuccessBean> implements ExchangeContract.View{

    private ExchangeHeader mExchangeHeader;

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

    public static ExchangeFragment initFragment(Bundle bundle) {
        ExchangeFragment exchangeFragment = new ExchangeFragment();
        exchangeFragment.setArguments(bundle);
        return exchangeFragment;
    }

    @Override
    protected void initData() {
        mExchangeHeader = new ExchangeHeader(mActivity);
        mHeaderAndFooterWrapper.addHeaderView(mExchangeHeader.getHeader());
        super.initData();
    }

    @Override
    public void updateUserInfo(UserInfoBean data) {

    }

    @Override
    public String getBillType() {
        return null;
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return null;
    }
}
