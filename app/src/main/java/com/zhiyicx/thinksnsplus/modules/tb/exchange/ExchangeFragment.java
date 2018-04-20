package com.zhiyicx.thinksnsplus.modules.tb.exchange;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.widget.UserAvatarView;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.InfoListDataBean;
import com.zhiyicx.thinksnsplus.data.beans.tbcandy.CandyBean;
import com.zhiyicx.thinksnsplus.modules.home.message.messagecomment.MessageCommentAdapter;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * @Author MasonGhost
 * @Date 2018/04/17/14:55
 * @Email lx1993m@gmail.com
 * @Description
 */

public class ExchangeFragment extends TSFragment<ExchangeContract.Presenter> implements ExchangeContract.View{

    public static final String BUNDLE_INFO = "candy";

    @Inject
    ExchangePresenter mExchangePresenter;

    @BindView(R.id.iv_headpic)
    UserAvatarView mIvHeadPic;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_content)
    TextView mTvContent;
    @BindView(R.id.tv_follow)
    TextView mTvFollow;
    @BindView(R.id.tv_subtraction)
    TextView mTvSubtraction;
    @BindView(R.id.tv_count)
    TextView mTvCount;
    @BindView(R.id.tv_plus)
    TextView mTvPlus;
    @BindView(R.id.tv_tip)
    TextView mTvTip;
    @BindView(R.id.bt_exchange)
    TextView mBtExchange;
    @BindView(R.id.tv_project_name)
    TextView mTvProjectName;
    @BindView(R.id.tv_project_intro)
    TextView mTvProjectIntro;

    private CandyBean mCandyBean;

    @Override
    protected boolean showToolbar() {
        return true;
    }

    @Override
    protected boolean showToolBarDivider() {
        return false;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_exchange;
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
    protected void initView(View rootView) {
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mCandyBean = (CandyBean) getArguments().getSerializable(BUNDLE_INFO);
            if(mPresenter != null){
                mPresenter.getCandy();
            }
        }
    }

    @Override
    public CandyBean getCurrentCandy() {
        return this.mCandyBean;
    }

    @Override
    public void getCandySuccess(CandyBean candyBean) {
        this.mCandyBean = candyBean;
    }
}
