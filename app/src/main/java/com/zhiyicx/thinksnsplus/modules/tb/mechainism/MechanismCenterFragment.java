package com.zhiyicx.thinksnsplus.modules.tb.mechainism;

import android.view.View;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.thinksnsplus.R;

/**
 * @Author Jliuer
 * @Date 2018/03/01/9:24
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class MechanismCenterFragment extends TSFragment {

    public static MechanismCenterFragment newInstance() {
        MechanismCenterFragment mechanismCenterFragment = new MechanismCenterFragment();
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

    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_tb_mechainsm;
    }
}
