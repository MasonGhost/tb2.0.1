package com.zhiyicx.thinksnsplus.modules.tb.privacy;

import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.thinksnsplus.R;

import butterknife.BindView;

/**
 * @Author Jliuer
 * @Date 2018/02/28/18:35
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class PrivacyFragment extends TSFragment<PrivacyContract.Presenter> implements PrivacyContract.View, CompoundButton.OnCheckedChangeListener {
    @BindView(R.id.ck_rank)
    CheckBox ckRank;

    @Override
    protected String setCenterTitle() {
        return getString(R.string.setting_rank);
    }

    @Override
    protected void initView(View rootView) {
        ckRank.setOnCheckedChangeListener(this);
    }

    @Override
    protected void initData() {
        mPresenter.getRankStatus();
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_privacy;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        mPresenter.changeRankStatus(b);
    }

    @Override
    public void onChangeRankStatus(boolean rank) {
        if (rank) {
            ToastUtils.showToast("设置成功");
        } else {
            ToastUtils.showToast("取消成功");
        }
    }

    @Override
    public void onGetRankStatus(int rankStatus) {
        ckRank.setChecked(rankStatus == 1);
    }
}
