package com.zhiyicx.thinksnsplus.jpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.JpushMessageBean;
import com.zhiyicx.thinksnsplus.modules.home.HomeActivity;
import com.zhiyicx.thinksnsplus.utils.NotificationUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.simple.eventbus.EventBus;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/3/28
 * @Contact master.jungle68@gmail.com
 */

public class JpushReceiver extends BroadcastReceiver {
    private static final String TAG = "JpushReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        LogUtils.d(TAG, "[JpushReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            LogUtils.d(TAG, "[JpushReceiver] 接收注册消息Registration Id : " + regId);

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            LogUtils.d(TAG, "[JpushReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            handleCustomMessage(context, bundle);
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            LogUtils.d(TAG, "[JpushReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            LogUtils.d(TAG, "[JpushReceiver] 接收到推送下来的通知的ID: " + notifactionId);
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            LogUtils.d(TAG, "[JpushReceiver] 用户点击打开了通知");
            hanldeNofityMessage(context, bundle);

        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            LogUtils.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            LogUtils.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else {
            LogUtils.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }


    //send msg to MainActivity
    private void handleCustomMessage(Context context, Bundle bundle) {
        if (AppApplication.getmCurrentLoginAuth() != null) {// 没有登录就丢弃掉
            JpushMessageBean jpushMessageBean = packgeJpushMessage(bundle, false);
            NotificationUtil.showNotifyMessage(context, jpushMessageBean);
        }

    }


    private void hanldeNofityMessage(Context context, Bundle bundle) {
        JpushMessageBean jpushMessageBean = packgeJpushMessage(bundle, true);
        if (AppApplication.getmCurrentLoginAuth() != null) {// 没有登录就丢弃掉
            //判断app进程是否存活
            if (DeviceUtils.isAppAlive(context, context.getPackageName())) {
                Intent mainIntent = new Intent(context, HomeActivity.class);
                Bundle msgBundle = new Bundle();
                msgBundle.putParcelable(HomeActivity.BUNDLE_JPUSH_MESSAGE, jpushMessageBean);
                mainIntent.putExtras(msgBundle);
                //将MainAtivity的launchMode设置成SingleTask, 或者在下面flag中加上Intent.FLAG_CLEAR_TOP,
                //如果Task栈中有MainActivity的实例，就会把它移到栈顶，把在它之上的Activity都清理出栈，
                //如果Task栈不存在MainActivity实例，则在栈顶创建
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(mainIntent);
            } else {
                //如果app进程已经被杀死，先重新启动app
                LogUtils.d(TAG, "the app process is dead");
                Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
                launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                Bundle msgBundle = new Bundle();
                msgBundle.putParcelable(HomeActivity.BUNDLE_JPUSH_MESSAGE, jpushMessageBean);
                launchIntent.putExtras(msgBundle);
                context.startActivity(launchIntent);
            }
        }
    }

    @NonNull
    private JpushMessageBean packgeJpushMessage(Bundle bundle, boolean isNofiy) {
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        JpushMessageBean jpushMessageBean = new Gson().fromJson(extras, JpushMessageBean.class);
        jpushMessageBean.setCreat_time(System.currentTimeMillis());
        jpushMessageBean.setNofity(isNofiy);
        jpushMessageBean.setMessage(bundle.getString(JPushInterface.EXTRA_MESSAGE));
        jpushMessageBean.setExtras(extras);
        EventBus.getDefault().post(jpushMessageBean, EventBusTagConfig.EVENT_JPUSH_RECIEVED_MESSAGE_UPDATE_MESSAGE_LIST);
        LogUtils.d(TAG, "-----------------extras = " + extras);
        return jpushMessageBean;
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    LogUtils.i(TAG, "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    LogUtils.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }


}
