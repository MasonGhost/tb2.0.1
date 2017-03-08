package com.zhiyicx.thinksnsplus.modules.information.infomain;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.common.utils.ActivityUtils;
import com.zhiyicx.thinksnsplus.R;

/**
 * @Author Jliuer
 * @Date 2017/03/03
 * @Email Jliuer@aliyun.com
 * @Description 资讯
 */
public class InfoActivity extends TSActivity {
    InfoContainerFragment mInfoContainerFragment;

    @Override
    protected Fragment getFragment() {
        if (mInfoContainerFragment == null) {
            mInfoContainerFragment = new InfoContainerFragment();
        }
        return mInfoContainerFragment;
    }

    @Override
    protected void componentInject() {

    }

    @Override
    protected void initView() {
        mContanierFragment = getFragment();
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),mContanierFragment,
                R.id.fl_fragment_container);
    }

}
