package com.zhiyicx.thinksnsplus.modules.chat.call.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.hyphenate.chat.EMClient;
import com.zhiyicx.baseproject.em.manager.control.TSEMConstants;
import com.zhiyicx.baseproject.em.manager.TSEMCallStatus;
import com.zhiyicx.thinksnsplus.modules.chat.call.TSEMHyphenate;
import com.zhiyicx.thinksnsplus.modules.chat.call.VideoCallActivity;
import com.zhiyicx.thinksnsplus.modules.chat.call.VoiceCallActivity;


/**
 * 通话呼叫监听广播实现，用来监听其他账户对自己的呼叫
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

        // 判断下当前被呼叫的为自己的时候才启动通话界面 TODO 这个当不同appkey下相同的username时就无效了
        if (callTo.equals(EMClient.getInstance().getCurrentUser())) {
            Intent callIntent = new Intent();
            // 根据通话类型跳转到语音通话或视频通话界面
            if (callType.equals(TYPE_VIDEO)) {
                callIntent.setClass(context, VideoCallActivity.class);
                // 设置当前通话类型为视频通话
                TSEMCallStatus.getInstance().setCallType(TSEMCallStatus.CALL_TYPE_VIDEO);
            } else if (callType.equals(TYPE_VOICE)) {
                callIntent.setClass(context, VoiceCallActivity.class);
                // 设置当前通话类型为语音通话
                TSEMCallStatus.getInstance().setCallType(TSEMCallStatus.CALL_TYPE_VOICE);
            }
            // 设置 activity 启动方式
            callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Bundle bundle=new Bundle();
            // 设置呼叫方 username 参数
            bundle.putString(TSEMConstants.TS_EXTRA_CHAT_ID, callFrom);
            // 设置通话为对方打来
            bundle.putBoolean(TSEMConstants.TS_EXTRA_CALL_IS_INCOMING, true);
            callIntent.putExtras(bundle);
            context.startActivity(callIntent);
        }
    }
}
