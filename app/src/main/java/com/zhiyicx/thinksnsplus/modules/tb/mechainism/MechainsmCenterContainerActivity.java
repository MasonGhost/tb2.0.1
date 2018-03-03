package com.zhiyicx.thinksnsplus.modules.tb.mechainism;

import android.support.v4.app.Fragment;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;

/**
 * @Author Jliuer
 * @Date 2018/03/01/9:43
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class MechainsmCenterContainerActivity extends TSActivity {
    @Override
    protected Fragment getFragment() {
        return MechanismCenterContainerFragment.newInstance(getIntent().getExtras());
    }

    @Override
    protected void componentInject() {

    }
}
