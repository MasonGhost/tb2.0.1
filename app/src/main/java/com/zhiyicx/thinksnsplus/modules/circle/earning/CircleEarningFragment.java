package com.zhiyicx.thinksnsplus.modules.circle.earning;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.widget.button.CombinationButton;
import com.zhiyicx.thinksnsplus.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @Author Jliuer
 * @Date 2017/12/12/11:31
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CircleEarningFragment extends TSFragment<CircleEarningContract.Presenter>
        implements CircleEarningContract.View {


    @BindView(R.id.tv_account_unit)
    TextView mTvAccountUnit;
    @BindView(R.id.tv_mine_money)
    TextView mTvMineMoney;
    @BindView(R.id.bt_member)
    CombinationButton mBtMember;
    @BindView(R.id.bt_top)
    CombinationButton mBtTop;

    @Override
    protected void initView(View rootView) {

    }

    @Override
    protected void initData() {

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
