package com.zhiyicx.thinksnsplus.modules.tb.mechainism;

import android.os.Bundle;
import android.view.View;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;

/**
 * @Author Jliuer
 * @Date 2018/03/01/9:24
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class MechanismCenterFragment extends TSFragment {
    private UserInfoBean mUserInfoBean;

    public static MechanismCenterFragment newInstance(Bundle bundle) {
        MechanismCenterFragment mechanismCenterFragment = new MechanismCenterFragment();
        mechanismCenterFragment.setArguments(bundle);
        return mechanismCenterFragment;
    }

    @Override
    protected boolean setUseSatusbar() {
        return true;
    }

    @Override
    protected boolean setUseStatusView() {
        return false;
    }

    @Override
    protected int getstatusbarAndToolbarHeight() {
        return 0;
    }

    @Override
    protected boolean showToolBarDivider() {
        return false;
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected void initView(View rootView) {
        mUserInfoBean = getArguments().getParcelable(PersonalCenterFragment.PERSONAL_CENTER_DATA);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_tb_mechainsm;
    }
}
