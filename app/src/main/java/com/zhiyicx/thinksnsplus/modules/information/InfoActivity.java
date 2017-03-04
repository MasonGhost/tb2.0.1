package com.zhiyicx.thinksnsplus.modules.information;

import android.support.v4.app.Fragment;

import com.zhiyicx.baseproject.base.TSActivity;

/**
 * @Author Jliuer
 * @Date 2017/03/03
 * @Email Jliuer@aliyun.com
 * @Description 资讯
 */
public class InfoActivity extends TSActivity {

    @Override
    protected Fragment getFragment() {
        return new InfoContainerFragment();
    }

    @Override
    protected void componentInject() {

    }
}
