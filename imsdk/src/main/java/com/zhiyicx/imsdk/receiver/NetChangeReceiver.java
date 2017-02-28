package com.zhiyicx.imsdk.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.zhiyicx.imsdk.service.SocketService;
import com.zhiyicx.imsdk.utils.common.DeviceUtils;

import org.simple.eventbus.EventBus;

/**
 * Created by jungle on 16/5/21.
 * com.zhiyicx.zhibo.app.receiver
 * zhibo_android
 * email:335891510@qq.com
 */
public class NetChangeReceiver extends BroadcastReceiver {
    public static final String EVENT_NETWORK_CONNECTED = "event_network_connected";
    private static final String NETWORK_CHANGE_SATE = "android.net.conn.CONNECTIVITY_CHANGE";
    public static final long TIME_SPACING = 1000;
    private long last_time = System.currentTimeMillis();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (System.currentTimeMillis() - last_time > TIME_SPACING) {
            if (NETWORK_CHANGE_SATE.equals(intent.getAction()) && DeviceUtils.hasInternet(context)) {
                // socket重连
                Intent socketretry = new Intent(SocketService.SOCKET_RETRY_CONNECT);
                context.sendBroadcast(socketretry);
                EventBus.getDefault().post(EVENT_NETWORK_CONNECTED);
            }
        }
        last_time = System.currentTimeMillis();
    }
}
