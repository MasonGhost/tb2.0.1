package com.zhiyicx.thinksnsplus.modules.circle.create.types;

import android.support.v4.app.Fragment;

import com.zhiyicx.baseproject.base.TSActivity;

/**
 * @author Jliuer
 * @Date 2017/11/28/14:31
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CircleTyepsActivity extends TSActivity {
    @Override
    protected Fragment getFragment() {
        return CircleTypesFragment.newInstance(getIntent().getExtras());
    }

    @Override
    protected void componentInject() {

    }
}
