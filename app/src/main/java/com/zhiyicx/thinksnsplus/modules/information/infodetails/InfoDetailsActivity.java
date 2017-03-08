package com.zhiyicx.thinksnsplus.modules.information.infodetails;

import android.support.v4.app.Fragment;

import com.zhiyicx.baseproject.base.TSActivity;

/**
 * @Author Jliuer
 * @Date 2017/03/07
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class InfoDetailsActivity extends TSActivity {
    @Override
    protected Fragment getFragment() {
        return new InfoDetailsFragment();
    }

    @Override
    protected void componentInject() {

    }
}
