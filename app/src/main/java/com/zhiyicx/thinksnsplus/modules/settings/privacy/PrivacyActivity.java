package com.zhiyicx.thinksnsplus.modules.settings.privacy;

import android.support.v4.app.Fragment;

import com.zhiyicx.baseproject.base.TSActivity;

/**
 * @Author Jliuer
 * @Date 2018/02/28/18:36
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class PrivacyActivity extends TSActivity {

    @Override
    protected Fragment getFragment() {
        return new PrivacyFragment();
    }

    @Override
    protected void componentInject() {

    }
}
