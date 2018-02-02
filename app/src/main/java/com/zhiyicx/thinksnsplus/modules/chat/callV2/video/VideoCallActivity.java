package com.zhiyicx.thinksnsplus.modules.chat.callV2.video;

import android.support.v4.app.Fragment;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.zhiyicx.baseproject.em.manager.TSEMCallStatus;
import com.zhiyicx.thinksnsplus.modules.chat.callV2.BaseCallActivity;

/**
 * @Author Jliuer
 * @Date 2018/02/01/14:59
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class VideoCallActivity extends BaseCallActivity {

    @Override
    protected Fragment getFragment() {
        return VideoCallFragment.getInstance(getIntent().getExtras());
    }

    @Override
    protected void onUserLeaveHint() {
        // 判断如果是通话中，暂停启动图像的传输
        if (TSEMCallStatus.getInstance().getCallState() == TSEMCallStatus.CALL_STATUS_ACCEPTED) {
            try {
                EMClient.getInstance().callManager().pauseVideoTransfer();
            } catch (HyphenateException e) {
                e.printStackTrace();
            }
        }
        VideoCallFragment videoCallFragment = (VideoCallFragment) mContanierFragment;
        if (videoCallFragment != null) {
            videoCallFragment.onUserLeaveHint();
        }
        super.onUserLeaveHint();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 判断如果是通话中，重新启动图像的传输
        if (TSEMCallStatus.getInstance().getCallState() == TSEMCallStatus.CALL_STATUS_ACCEPTED) {
            try {
                EMClient.getInstance().callManager().resumeVideoTransfer();
            } catch (HyphenateException e) {
                e.printStackTrace();
            }
        }
        VideoCallFragment videoCallFragment = (VideoCallFragment) mContanierFragment;
        if (videoCallFragment != null) {
            videoCallFragment.onActivityResume();
        }
    }
}
