package com.zhiyicx.thinksnsplus.modules.wallet.integration.detail.recharge_withdrawal;

import android.support.v4.app.Fragment;

import com.zhiyicx.baseproject.base.TSActivity;


/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/11/14
 * @Contact master.jungle68@gmail.com
 */
public class IntegrationRWDetailActivity extends TSActivity {

    @Override
    protected Fragment getFragment() {
        return new IntegrationRWDetailContainerFragment();
    }

    @Override
    protected void componentInject() {

    }
}
