package com.zhiyicx.thinksnsplus.modules.tb.wallet;

import android.support.v7.widget.RecyclerView;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.R;

/**
 * @Author Jliuer
 * @Date 2018/02/28/19:14
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class WalletFragment extends TSListFragment<WalletContract.Presenter,WalletData> {

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_wallet_for_tb;
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return null;
    }
}
