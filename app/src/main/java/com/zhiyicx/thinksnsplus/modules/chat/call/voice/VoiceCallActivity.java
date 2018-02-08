package com.zhiyicx.thinksnsplus.modules.chat.call.voice;

import android.support.v4.app.Fragment;

import com.zhiyicx.thinksnsplus.modules.chat.call.BaseCallActivity;

/**
 * @Author Jliuer
 * @Date 2018/02/06/14:26
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class VoiceCallActivity extends BaseCallActivity {
    @Override
    protected Fragment getFragment() {
        return VoiceCallFragment.getInstance(getIntent().getExtras());
    }
}
