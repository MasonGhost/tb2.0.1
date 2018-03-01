package com.zhiyicx.thinksnsplus.modules.tb.contribution;

import android.support.v4.app.Fragment;

import com.zhiyicx.baseproject.base.TSActivity;

/**
 * @Author Jliuer
 * @Date 2018/02/28/14:40
 * @Email Jliuer@aliyun.com
 * @Description 贡献
 */
public class ContributionActivity extends TSActivity {

    @Override
    protected Fragment getFragment() {
        return new ContributionContainerFragment();
    }

    @Override
    protected void componentInject() {

    }
}
