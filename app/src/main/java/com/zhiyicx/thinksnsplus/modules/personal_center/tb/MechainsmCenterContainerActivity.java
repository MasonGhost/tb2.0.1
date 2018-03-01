package com.zhiyicx.thinksnsplus.modules.personal_center.tb;

import android.support.v4.app.Fragment;

import com.zhiyicx.baseproject.base.TSActivity;

/**
 * @Author Jliuer
 * @Date 2018/03/01/9:43
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class MechainsmCenterContainerActivity extends TSActivity {
    @Override
    protected Fragment getFragment() {
        return new MechanismCenterContainerFragment();
    }

    @Override
    protected void componentInject() {

    }
}
