package com.zhiyicx.thinksnsplus.modules.wallet.rule;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.widget.popwindow.CenterInfoPopWindow;
import com.zhiyicx.thinksnsplus.R;

import butterknife.BindView;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/05/22
 * @Contact master.jungle68@gmail.com
 */
public class WalletRuleFragment extends TSFragment {
    public static final String BUNDLE_RULE = "RULE";
    public static final String BUNDLE_TITLE = "TITLE";


    @BindView(R.id.tv_content)
    TextView mTvContent;

    private String mRule = "";
    private String mTitle = "";

    public static WalletRuleFragment newInstance(Bundle extras) {

        WalletRuleFragment walletRuleFragment = new WalletRuleFragment();
        walletRuleFragment.setArguments(extras);
        return walletRuleFragment;
    }


    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_wallet_rule;
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.recharge_and_withdraw_rule);
    }


    @Override
    protected void initView(View rootView) {
        if (getArguments() != null) {
            mRule = getArguments().getString(BUNDLE_RULE);
            mTitle = getArguments().getString(BUNDLE_TITLE);
        }
        mTvContent.setText(mRule);
        if (!TextUtils.isEmpty(mTitle)) {
            setCenterText(mTitle);
        }
    }

    @Override
    protected void initData() {
    }


}
