package com.zhiyicx.thinksnsplus.modules.circle.manager.earning;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.widget.button.CombinationButton;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.CircleInfoDetail;

import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @Author Jliuer
 * @Date 2017/12/12/11:31
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CircleEarningFragment extends TSFragment<CircleEarningContract.Presenter>
        implements CircleEarningContract.View {

    public static final String DATA = "data";

    @BindView(R.id.tv_account_unit)
    TextView mTvAccountUnit;
    @BindView(R.id.tv_mine_money)
    TextView mTvMineMoney;
    @BindView(R.id.bt_member)
    CombinationButton mBtMember;
    @BindView(R.id.bt_top)
    CombinationButton mBtTop;

    private CircleInfoDetail mCircleInfoDetail;

    public static CircleEarningFragment newInstance(Bundle bundle) {
        CircleEarningFragment circleEarningFragment = new CircleEarningFragment();
        circleEarningFragment.setArguments(bundle);
        return circleEarningFragment;
    }

    @Override
    protected void initView(View rootView) {
        setCenterTextColor(R.color.white);
        setRightText(getString(R.string.detail));
        mTvAccountUnit.setText(String.format(Locale.getDefault(), getString(R.string.circle_earningn_total), mPresenter.getGoldName()));
        mCircleInfoDetail = getArguments().getParcelable(DATA);
    }

    @Override
    protected void initData() {
        mBtMember.setRightText(mCircleInfoDetail.getJoin_income_count() + "");
        mBtTop.setRightText(mCircleInfoDetail.getPinned_income_count() + "");
        mTvMineMoney.setText(String.valueOf(mCircleInfoDetail.getJoin_income_count() + mCircleInfoDetail.getPinned_income_count()));
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_circle_earning;
    }

    @OnClick({R.id.bt_member, R.id.bt_top})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_member:
                break;
            case R.id.bt_top:
                break;
            default:
        }
    }
}
