package com.zhiyicx.thinksnsplus.modules.tb.wallet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;

/**
 * @author Jliuer
 * @Date 2017/11/14/10:33
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class WalletHeader {
    private View mHeader;
    TextView mTvAccountUnit;
    TextView mTvMineMoney;

    WalletHeader(Context context) {
        mHeader = LayoutInflater.from(context).inflate(R.layout
                .header_wallet_for_tb, null);
        mHeader.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        mTvAccountUnit = (TextView) mHeader.findViewById(R.id.tv_account_unit);
        mTvMineMoney = (TextView) mHeader.findViewById(R.id.tv_mine_money);
    }

    /**
     * @param userInfoBean
     * @param uint
     */
    public void updateWallet(UserInfoBean userInfoBean, String uint) {
        mTvAccountUnit.setText(uint);
        mTvMineMoney.setText(userInfoBean.getWallet() != null ? String.valueOf((int) userInfoBean.getWallet().getBalance()) : "0");
    }


    public View getHeader() {
        return mHeader;
    }
}
