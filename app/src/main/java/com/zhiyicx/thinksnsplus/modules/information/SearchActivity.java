package com.zhiyicx.thinksnsplus.modules.information;

import android.support.v4.app.Fragment;

import com.zhiyicx.baseproject.base.TSActivity;

/**
 * @Author Jliuer
 * @Date 2017/03/06
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class SearchActivity extends TSActivity {
    @Override
    protected Fragment getFragment() {
        return new SearchFragment();
    }

    @Override
    protected void componentInject() {

    }
}
