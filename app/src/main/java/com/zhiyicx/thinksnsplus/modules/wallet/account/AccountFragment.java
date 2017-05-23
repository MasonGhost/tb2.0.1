package com.zhiyicx.thinksnsplus.modules.wallet.account;

import android.view.View;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.thinksnsplus.R;

/**
 * @Author Jliuer
 * @Date 2017/05/23/15:02
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class AccountFragment extends TSFragment {

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.account_detail);
    }

    @Override
    public void setPresenter(Object presenter) {

    }

    @Override
    protected void initView(View rootView) {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_account;
    }
}
