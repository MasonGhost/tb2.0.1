package com.zhiyicx.thinksnsplus.modules.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.baseproject.impl.share.UmengSharePolicyImpl;
import com.zhiyicx.common.utils.ActivityHandler;
import com.zhiyicx.common.utils.ActivityUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.config.JpushMessageTypeConfig;
import com.zhiyicx.thinksnsplus.data.beans.JpushMessageBean;


/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/22
 * @Contact master.jungle68@gmail.com
 */

public class HomeActivity extends TSActivity {
    public static final String BUNDLE_JPUSH_MESSAGE = "jpush_message";

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            JpushMessageBean jpushMessageBean = bundle.getParcelable(BUNDLE_JPUSH_MESSAGE);
            if (jpushMessageBean != null && jpushMessageBean.getType() != null) {
                switch (jpushMessageBean.getType()) {
                    case JpushMessageTypeConfig.JPUSH_MESSAGE_TYPE_SYSTEM:
                        ((HomeContract.View) mContanierFragment).checkBottomItem(HomeFragment.PAGE_MINE);
                        break;
                    default:
//                        ((HomeContract.View) mContanierFragment).checkBottomItem(HomeFragment.PAGE_MESSAGE);
                        ((HomeContract.View) mContanierFragment).checkBottomItem(HomeFragment.PAGE_MINE);
                }

            }
        }
    }

    @Override
    protected void componentInject() {
    }

    @Override
    protected Fragment getFragment() {
        return HomeFragment.newInstance(getIntent().getExtras());
    }

    @Override
    public void onBackPressed() {
        ActivityUtils.goHome(this);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UmengSharePolicyImpl.onActivityResult(requestCode, resultCode, data, this);
        mContanierFragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UmengSharePolicyImpl.onDestroy(this);
    }

}
