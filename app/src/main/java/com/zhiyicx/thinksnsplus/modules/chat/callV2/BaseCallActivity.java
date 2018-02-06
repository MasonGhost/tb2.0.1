package com.zhiyicx.thinksnsplus.modules.chat.callV2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.WindowManager;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.baseproject.em.manager.control.TSEMConstants;
import com.zhiyicx.thinksnsplus.modules.chat.callV2.voice.VoiceCallActivity;
import com.zhiyicx.thinksnsplus.modules.chat.callV2.video.VideoCallActivity;

/**
 * @Author Jliuer
 * @Date 2018/02/02/10:26
 * @Email Jliuer@aliyun.com
 * @Description
 */
public abstract class BaseCallActivity extends TSActivity {

    @Override
    protected void componentInject() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
    }

    /**
     * 视频
     * @param context
     * @param to
     * @param isInComming 来电
     */
    public static void startVideoCallActivity(Context context, String to, boolean isInComming) {
        Intent intent = new Intent(context, VideoCallActivity.class);
        Bundle bundle = new Bundle();
        // 设置呼叫方 username 参数
        bundle.putString(TSEMConstants.TS_EXTRA_CHAT_ID, to);
        // 设置通话为对方打来
        bundle.putBoolean(TSEMConstants.TS_EXTRA_CALL_IS_INCOMING, isInComming);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static void startVoiceCallActivity(Context context, String to, boolean isInComming) {
        Intent intent = new Intent(context, VoiceCallActivity.class);
        Bundle bundle = new Bundle();
        // 设置呼叫方 username 参数
        bundle.putString(TSEMConstants.TS_EXTRA_CHAT_ID, to);
        // 设置通话为对方打来
        bundle.putBoolean(TSEMConstants.TS_EXTRA_CALL_IS_INCOMING, isInComming);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
