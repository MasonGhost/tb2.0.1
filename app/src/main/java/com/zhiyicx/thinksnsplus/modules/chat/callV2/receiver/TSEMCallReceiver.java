package com.zhiyicx.thinksnsplus.modules.chat.callV2.receiver;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hyphenate.chat.EMClient;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.zhiyicx.baseproject.em.manager.TSEMCallStatus;
import com.zhiyicx.baseproject.em.manager.control.TSEMConstants;
import com.zhiyicx.thinksnsplus.modules.chat.callV2.BaseCallActivity;
import com.zhiyicx.thinksnsplus.modules.chat.callV2.TSEMHyphenate;


/**
 * @Author Jliuer
 * @Date 2018/2/1/21:08
 * @Email Jliuer@aliyun.com
 * @Description 监听其他账户对自己的呼叫
 */
public class TSEMCallReceiver extends BroadcastReceiver {

    /**
     * 视频通话
     */
    private String TYPE_VIDEO = "video";

    /**
     * 语音通话
     */
    private String TYPE_VOICE = "voice";

    private RxPermissions mRxPermissions;

    public TSEMCallReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        // 判断环信是否登录成功
        if (!TSEMHyphenate.getInstance().isLoginedInBefore()) {
            return;
        }

        // 呼叫方的 usernmae
        String callFrom = intent.getStringExtra(TSEMConstants.TS_EXTRA_FROM);

        // 呼叫类型，有语音和视频两种
        String callType = intent.getStringExtra(TSEMConstants.TS_EXTRA_TYPE);

        // 呼叫接收方
        String callTo = intent.getStringExtra(TSEMConstants.TS_EXTRA_TO);

        mRxPermissions = new RxPermissions(TSEMHyphenate.getInstance().getTopActivity());

        // 判断下当前被呼叫的为自己的时候才启动通话界面 TODO 这个当不同appkey下相同的username时就无效了
        if (callTo.equals(EMClient.getInstance().getCurrentUser())) {
            // 根据通话类型跳转到语音通话或视频通话界面
            if (callType.equals(TYPE_VIDEO)) {

                mRxPermissions
                        .requestEach(Manifest.permission.CAMERA)
                        .subscribe(permission -> {
                            if (permission.granted) {
                                // 权限被允许
                                BaseCallActivity.startVideoCallActivity(context, callFrom, true);
                                // 设置当前通话类型为视频通话
                                TSEMCallStatus.getInstance().setCallType(TSEMCallStatus.CALL_TYPE_VIDEO);
                            } else if (permission.shouldShowRequestPermissionRationale) {
                                // 权限没有被彻底禁止
                            } else {
                            }
                        });


            } else if (callType.equals(TYPE_VOICE)) {

                mRxPermissions
                        .requestEach(Manifest.permission.RECORD_AUDIO)
                        .subscribe(permission -> {
                            if (permission.granted) {
                                // 权限被允许
                                BaseCallActivity.startVoiceCallActivity(context, callFrom, true);
                                // 设置当前通话类型为语音通话
                                TSEMCallStatus.getInstance().setCallType(TSEMCallStatus.CALL_TYPE_VOICE);
                            } else if (permission.shouldShowRequestPermissionRationale) {
                                // 权限没有被彻底禁止
                            } else {
                                // 权限被彻底禁止
                            }
                        });
            }
        }
    }
}
