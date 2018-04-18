package com.zhiyicx.thinksnsplus.modules.tb.wallet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
import com.zhiyicx.thinksnsplus.modules.tb.mechainism.MechainsmCenterContainerActivity;
import com.zhiyicx.thinksnsplus.modules.tb.tbmark_detail.TBMarkDetailActivity;
import com.zhiyicx.thinksnsplus.modules.tb.tbmark_detail.TBMarkDetailFragment;
import com.zhiyicx.thinksnsplus.widget.CountTimerView;

/**
 * @author Jliuer
 * @Date 2017/11/14/10:33
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class WalletHeader {
    private View mHeader;
    private TextView mTvAccountUnit;
    private TextView mTvMineMoney;
    private CountTimerView mCountTimerView;

    WalletHeader(Context context) {
        mHeader = LayoutInflater.from(context).inflate(R.layout
                .header_wallet_for_tb, null);
        mHeader.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        mTvAccountUnit = (TextView) mHeader.findViewById(R.id.tv_account_unit);
        mTvMineMoney = (TextView) mHeader.findViewById(R.id.tv_mine_money);
        mTvMineMoney.setOnClickListener(view -> {
            Intent intent = new Intent(context, TBMarkDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable(TBMarkDetailFragment.BILL_TYPE, null);
            intent.putExtras(bundle);
            context.startActivity(intent);
        });
        mCountTimerView = (CountTimerView) mHeader.findViewById(R.id.count_timer);
        mCountTimerView.setOnStopListener(new CountTimerView.OnStopListener() {
            @Override
            public void isStop() {
                //mCountTimerView.setTime(0);
            }
        });
        mCountTimerView.setTime(320000);
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
