package com.zhiyicx.thinksnsplus.modules.information.infochannel;

import android.support.v4.app.Fragment;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.R;

/**
 * @Author Jliuer
 * @Date 2017/03/06
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class ChannelActivity extends TSActivity {

    @Override
    protected Fragment getFragment() {
        return new InfoChannelFragment();
    }

    @Override
    protected void componentInject() {

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_from_top_enter, R.anim.slide_from_top_quit);
    }
}
