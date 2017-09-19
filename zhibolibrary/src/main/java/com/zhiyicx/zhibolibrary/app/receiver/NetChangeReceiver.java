package com.zhiyicx.zhibolibrary.app.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.zhiyicx.zhibolibrary.util.DeviceUtils;

import org.simple.eventbus.EventBus;

/**
 * Created by jungle on 16/5/21.
 * com.zhiyicx.zhibo.app.receiver
 * zhibo_android
 * email:335891510@qq.com
 */
public class NetChangeReceiver extends BroadcastReceiver {
    private static final String NETWORK_CHANGE_SATE = "android.net.conn.CONNECTIVITY_CHANGE";
    public static final String RECONNECT_STREAM_OF_SHUTDOWN = "reconnect_stream_of_shutdown";
    public static final String NETWORK_DISCONNECT = "network_disconnect";
    public static final String NETWORK_NOT_WIFI = "net_change_not_wifi";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (NETWORK_CHANGE_SATE.equals(intent.getAction())) {
            //判断网络是否连接
            if (DeviceUtils.netIsConnected(context)) {//网络连接
                //如果主播直播间网络断开则重新连接
                EventBus.getDefault().post(true, RECONNECT_STREAM_OF_SHUTDOWN);
                //判断是否为wifi环境
                if (!DeviceUtils.isWifiOpen(context.getApplicationContext())) {//提示用户
                    EventBus.getDefault().post(true, NETWORK_NOT_WIFI);
                }

            }
            else {
                //网络断开
                EventBus.getDefault().post(true, NETWORK_DISCONNECT);
            }

        }
    }
}
